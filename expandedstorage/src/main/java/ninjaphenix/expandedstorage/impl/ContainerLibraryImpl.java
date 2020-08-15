package ninjaphenix.expandedstorage.impl;

import io.netty.buffer.Unpooled;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.container.ContainerFactory;
import net.fabricmc.fabric.api.container.ContainerProviderRegistry;
import net.fabricmc.fabric.api.network.PacketContext;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.InventoryProvider;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import ninjaphenix.chainmail.api.events.PlayerDisconnectCallback;
import ninjaphenix.expandedstorage.api.Constants;
import ninjaphenix.expandedstorage.api.inventory.AbstractContainer;
import ninjaphenix.expandedstorage.api.inventory.AreaAwareSlotFactory;
import ninjaphenix.expandedstorage.impl.client.ScreenMiscSettings;
import ninjaphenix.expandedstorage.impl.inventory.PagedScreenHandler;
import ninjaphenix.expandedstorage.impl.inventory.ScrollableScreenHandler;
import ninjaphenix.expandedstorage.impl.inventory.SingleScreenHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;

import static ninjaphenix.expandedstorage.api.Constants.SCREEN_SELECT;
import static ninjaphenix.expandedstorage.api.Constants.SINGLE_CONTAINER;

public class ContainerLibraryImpl implements ModInitializer
{
    public static final ContainerLibraryImpl INSTANCE = new ContainerLibraryImpl();

    @Environment(EnvType.CLIENT)
    private final HashMap<Identifier, ScreenMiscSettings> screenMiscSettings = new HashMap<>();
    private final HashSet<Identifier> declaredContainerTypes = new HashSet<>();
    private final HashMap<UUID, Consumer<Identifier>> preferenceCallbacks = new HashMap<>();
    private final HashMap<UUID, Identifier> playerPreferences = new HashMap<>();
    private final Logger LOGGER = LogManager.getLogger(ExpandedStorage.MOD_ID);

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
            if (preferenceCallbacks.containsKey(uuid))
            {
                preferenceCallbacks.get(uuid).accept(containerTypeId);
            }
        }
        else
        {
            if (containerTypeId == null || !containerTypeId.equals(ExpandedStorage.id("auto"))) { playerPreferences.remove(uuid); }
            preferenceCallbacks.remove(uuid);
        }
    }

    public void openContainer(final PlayerEntity player, final BlockPos pos, final Text containerName)
    {
        Objects.requireNonNull(player, "ContainerLibraryImpl#openContainer received null instead of a PlayerEntity.");
        Objects.requireNonNull(pos, "ContainerLibraryImpl#openContainer received null instead of a BlockPos.");
        Objects.requireNonNull(containerName, "ContainerLibraryImpl#declareContainerType received null instead of a Text. (Container Name)");
        final UUID uuid = player.getUuid();
        Identifier playerPreference;
        if (playerPreferences.containsKey(uuid) &&
                declaredContainerTypes.contains(playerPreference = playerPreferences.get(uuid)) /*&&
                ContainerProviderRegistry.INSTANCE.factoryExists(playerPreference)*/)
        {
            openContainer(player, playerPreference, pos, containerName);
            preferenceCallbacks.put(player.getUuid(), (type) -> openContainer(player, type, pos, containerName));
        }
        else
        {
            openSelectScreen(player, (type) -> openContainer(player, type, pos, containerName));
        }
    }

    public void openSelectScreen(final PlayerEntity player, final Consumer<Identifier> playerPreferenceCallback)
    {
        Objects.requireNonNull(player, "ContainerLibraryImpl#openSelectScreen received null instead of a PlayerEntity.");
        if (playerPreferenceCallback != null) { preferenceCallbacks.put(player.getUuid(), playerPreferenceCallback); }
        final PacketByteBuf buffer = new PacketByteBuf(Unpooled.buffer());
        buffer.writeInt(declaredContainerTypes.size());
        declaredContainerTypes.forEach(buffer::writeIdentifier);
        ServerSidePacketRegistry.INSTANCE.sendToPlayer(player, SCREEN_SELECT, buffer);
    }

    public void declareContainerType(final Identifier containerTypeId, final Identifier selectTextureId, final Text narrationMessage)
    {
        Objects.requireNonNull(containerTypeId, "ContainerLibraryImpl#declareContainerType received null instead of an Identifier. (Container Type ID)");
        Objects.requireNonNull(selectTextureId, "ContainerLibraryImpl#declareContainerType received null instead of an Identifier. (Select Texture ID)");
        Objects.requireNonNull(narrationMessage, "ContainerLibraryImpl#declareContainerType received null instead of a Text. (Narration Message)");
        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT)
        { screenMiscSettings.put(containerTypeId, new ScreenMiscSettings(selectTextureId, narrationMessage)); }
        declaredContainerTypes.add(containerTypeId);
    }

    @Environment(EnvType.CLIENT)
    public ScreenMiscSettings getScreenSettings(final Identifier containerTypeId)
    {
        Objects.requireNonNull(containerTypeId, "ContainerLibraryImpl#getScreenSettings received null instead of an Identifier. (Container Type ID)");
        return screenMiscSettings.get(containerTypeId);
    }

    private void openContainer(final PlayerEntity player, final Identifier type, final BlockPos pos, final Text containerName)
    {
        ContainerProviderRegistry.INSTANCE.openContainer(type, player, buf ->
        {
            buf.writeBlockPos(pos);
            buf.writeText(containerName);
        });
    }

    @Override
    public void onInitialize()
    {
        ContainerProviderRegistry.INSTANCE.registerFactory(SINGLE_CONTAINER, getContainerFactory(SingleScreenHandler::new));
        ContainerProviderRegistry.INSTANCE.registerFactory(Constants.PAGED_CONTAINER, getContainerFactory(PagedScreenHandler::new));
        ContainerProviderRegistry.INSTANCE.registerFactory(Constants.SCROLLABLE_CONTAINER, getContainerFactory(ScrollableScreenHandler::new));
        final Function<String, TranslatableText> nameFunc = (name) -> new TranslatableText(String.format("screen.%s.%s", ExpandedStorage.MOD_ID, name));
        declareContainerType(SINGLE_CONTAINER, ExpandedStorage.id("textures/gui/single_button.png"), nameFunc.apply("single_screen_type"));
        declareContainerType(Constants.SCROLLABLE_CONTAINER, ExpandedStorage.id("textures/gui/scrollable_button.png"), nameFunc.apply("scrollable_screen_type"));
        declareContainerType(Constants.PAGED_CONTAINER, ExpandedStorage.id("textures/gui/paged_button.png"), nameFunc.apply("paged_screen_type"));
        ServerSidePacketRegistry.INSTANCE.register(Constants.OPEN_SCREEN_SELECT, this::onReceiveOpenSelectScreenPacket);
        ServerSidePacketRegistry.INSTANCE.register(Constants.SCREEN_SELECT, this::onReceivePlayerPreference);
        PlayerDisconnectCallback.EVENT.register(player -> setPlayerPreference(player, null));
    }

    private void onReceivePlayerPreference(PacketContext context, PacketByteBuf buffer)
    {
        context.getTaskQueue().submitAndJoin(() -> ContainerLibraryImpl.INSTANCE.setPlayerPreference(context.getPlayer(), buffer.readIdentifier()));
    }

    private void onReceiveOpenSelectScreenPacket(final PacketContext context, final PacketByteBuf buffer)
    {
        final ServerPlayerEntity player = (ServerPlayerEntity) context.getPlayer();
        final ScreenHandler container = player.currentScreenHandler;
        if (container instanceof AbstractContainer)
        {
            final AbstractContainer<?> abstractContainer = (AbstractContainer<?>) container;
            openSelectScreen(player, (type) -> openContainer(player, abstractContainer.ORIGIN, abstractContainer.getDisplayName())
            );
        }
        else
        {
            ContainerLibraryImpl.INSTANCE.openSelectScreen(player, null);
        }
    }

    private <T extends ScreenHandler> ContainerFactory<T> getContainerFactory(containerConstructor<T> newMethod)
    {
        return (syncId, identifier, player, buffer) -> {
            final BlockPos pos = buffer.readBlockPos();
            final Text name = buffer.readText();
            final World world = player.getEntityWorld();
            final BlockState state = world.getBlockState(pos);
            final Block block = state.getBlock();
            if (block instanceof InventoryProvider)
            {
                return newMethod.create(null, syncId, pos, ((InventoryProvider) block).getInventory(state, world, pos), player, name,
                        (inventory, area, index, x, y) -> new Slot(inventory, index, x, y));
            }
            return null;
        };
    }

    private interface containerConstructor<T extends ScreenHandler>
    {
        T create(ScreenHandlerType<T> type, int syncId, BlockPos pos, Inventory inventory,
                PlayerEntity player, Text containerName, AreaAwareSlotFactory slotFactory);
    }
}
