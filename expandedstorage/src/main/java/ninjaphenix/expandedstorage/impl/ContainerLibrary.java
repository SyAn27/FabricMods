package ninjaphenix.expandedstorage.impl;

import com.google.common.collect.ImmutableMap;
import io.netty.buffer.Unpooled;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.network.PacketContext;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import ninjaphenix.chainmail.api.events.PlayerDisconnectCallback;
import ninjaphenix.expandedstorage.impl.inventory.*;
import ninjaphenix.expandedstorage.impl.client.ScreenMiscSettings;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;


public final class ContainerLibrary implements ModInitializer
{
    public static final ContainerLibrary INSTANCE = new ContainerLibrary();

    private ContainerLibrary() {}

    @Environment(EnvType.CLIENT)
    private final HashMap<Identifier, ScreenMiscSettings> screenMiscSettings = new HashMap<>();
    private final HashSet<Identifier> declaredContainerTypes = new HashSet<>();
    private final HashMap<UUID, Consumer<Identifier>> preferenceCallbacks = new HashMap<>();
    private final HashMap<UUID, Identifier> playerPreferences = new HashMap<>();
    private final ImmutableMap<Identifier, ServerScreenHandlerFactory<?>> handlerFactories =
            new ImmutableMap.Builder<Identifier, ServerScreenHandlerFactory<?>>()
                    .put(Const.id("single"), SingleScreenHandler::new)
                    .put(Const.id("scrollable"), ScrollableScreenHandler::new)
                    .put(Const.id("paged"), PagedScreenHandler::new)
                    .build();

    public boolean isContainerTypeDeclared(final Identifier containerTypeId)
    {
        Objects.requireNonNull(containerTypeId, "ContainerTypeImpl#isContainerTypeDeclared received null instead of an Identifier. (Container Type ID)");
        return declaredContainerTypes.contains(containerTypeId);
    }

    public void setPlayerPreference(final PlayerEntity player, final Identifier containerTypeId)
    {
        Objects.requireNonNull(player, "ContainerLibraryImpl#setPlayerPreference received null instead of a PlayerEntity.");
        final UUID uuid = player.getUuid();
        if (declaredContainerTypes.contains(containerTypeId))
        {
            playerPreferences.put(uuid, containerTypeId);
            if (preferenceCallbacks.containsKey(uuid)) { preferenceCallbacks.get(uuid).accept(containerTypeId); }
        }
        else
        {
            if (containerTypeId == null || !containerTypeId.equals(Const.id("auto"))) { playerPreferences.remove(uuid); }
            preferenceCallbacks.remove(uuid);
        }
    }

    public void openContainer(final PlayerEntity player, final ExtendedScreenHandlerFactory handlerFactory)
    {
        final UUID uuid = player.getUuid();
        if (playerPreferences.containsKey(uuid) && handlerFactories.containsKey(playerPreferences.get(uuid)))
        {
            player.openHandledScreen(handlerFactory);
        }
        else
        {
            openSelectScreen(player, (type) -> openContainer(player, handlerFactory));
        }
    }

    public void openSelectScreen(final PlayerEntity player, final Consumer<Identifier> playerPreferenceCallback)
    {
        Objects.requireNonNull(player, "ContainerLibraryImpl#openSelectScreen received null instead of a PlayerEntity.");
        if (playerPreferenceCallback != null) { preferenceCallbacks.put(player.getUuid(), playerPreferenceCallback); }
        final PacketByteBuf buffer = new PacketByteBuf(Unpooled.buffer());
        buffer.writeInt(declaredContainerTypes.size());
        declaredContainerTypes.forEach(buffer::writeIdentifier);
        ServerSidePacketRegistry.INSTANCE.sendToPlayer(player, Const.SCREEN_SELECT, buffer);
    }

    public void declareContainerType(final Identifier containerTypeId, final Identifier selectTextureId, final Text narrationMessage)
    {
        Objects.requireNonNull(containerTypeId, "ContainerLibraryImpl#declareContainerType received null instead of an Identifier. (Container Type ID)");
        Objects.requireNonNull(selectTextureId, "ContainerLibraryImpl#declareContainerType received null instead of an Identifier. (Select Texture ID)");
        Objects.requireNonNull(narrationMessage, "ContainerLibraryImpl#declareContainerType received null instead of a Text. (Narration Message)");
        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) { screenMiscSettings.put(containerTypeId, new ScreenMiscSettings(selectTextureId, narrationMessage)); }
        declaredContainerTypes.add(containerTypeId);
    }

    @Environment(EnvType.CLIENT)
    public ScreenMiscSettings getScreenSettings(final Identifier containerTypeId)
    {
        Objects.requireNonNull(containerTypeId, "ContainerLibraryImpl#getScreenSettings received null instead of an Identifier. (Container Type ID)");
        return screenMiscSettings.get(containerTypeId);
    }

    @Override
    public void onInitialize()
    {
        final Function<String, TranslatableText> nameFunc = (name) -> new TranslatableText(String.format("screen.%s.%s", Const.MOD_ID, name));
        declareContainerType(Const.SINGLE_CONTAINER, Const.id("textures/gui/single_button.png"), nameFunc.apply("single_screen_type"));
        declareContainerType(Const.SCROLLABLE_CONTAINER, Const.id("textures/gui/scrollable_button.png"), nameFunc.apply("scrollable_screen_type"));
        declareContainerType(Const.PAGED_CONTAINER, Const.id("textures/gui/paged_button.png"), nameFunc.apply("paged_screen_type"));
        ServerSidePacketRegistry.INSTANCE.register(Const.OPEN_SCREEN_SELECT, this::onReceiveOpenSelectScreenPacket);
        ServerSidePacketRegistry.INSTANCE.register(Const.SCREEN_SELECT, this::onReceivePlayerPreference);
        PlayerDisconnectCallback.EVENT.register(player -> setPlayerPreference(player, null));
    }

    private void onReceivePlayerPreference(PacketContext context, PacketByteBuf buffer)
    {
        context.getTaskQueue().submitAndJoin(() -> ContainerLibrary.INSTANCE.setPlayerPreference(context.getPlayer(), buffer.readIdentifier()));
    }

    private void onReceiveOpenSelectScreenPacket(final PacketContext context, final PacketByteBuf rOpenBuffer)
    {
        final ServerPlayerEntity sender = (ServerPlayerEntity) context.getPlayer();
        final ScreenHandler currentScreenHandler = sender.currentScreenHandler;
        if (currentScreenHandler instanceof AbstractScreenHandler)
        {
            final AbstractScreenHandler<?> screenHandler = (AbstractScreenHandler<?>) currentScreenHandler;
            openSelectScreen(sender, (type) -> sender.openHandledScreen(new ExtendedScreenHandlerFactory()
            {
                @Nullable
                @Override
                public ScreenHandler createMenu(final int syncId, final PlayerInventory inv, final PlayerEntity player)
                {
                    return ContainerLibrary.INSTANCE.getScreenHandler(syncId, screenHandler.ORIGIN, screenHandler.getInventory(),
                                                                      player, screenHandler.getDisplayName());
                }

                @Override
                public Text getDisplayName()
                {
                    return screenHandler.getDisplayName();
                }

                @Override
                public void writeScreenOpeningData(final ServerPlayerEntity player, final PacketByteBuf wOpenBuffer)
                {
                    wOpenBuffer.writeBlockPos(screenHandler.ORIGIN).writeInt(screenHandler.getInventory().size());
                }
            }));
        }
        else
        {
            ContainerLibrary.INSTANCE.openSelectScreen(sender, null);
        }
    }

    public ScreenHandler getScreenHandler(final int syncId, final BlockPos pos, final Inventory inventory, final PlayerEntity player,
                                          final Text displayName)
    {
        final UUID uuid = player.getUuid();
        final Identifier playerPreference;
        if (playerPreferences.containsKey(uuid) && handlerFactories.containsKey(playerPreference = playerPreferences.get(uuid)))
        {
            return handlerFactories.get(playerPreference).create(syncId, pos, inventory, player, displayName);
        }
        return null;
    }
}
