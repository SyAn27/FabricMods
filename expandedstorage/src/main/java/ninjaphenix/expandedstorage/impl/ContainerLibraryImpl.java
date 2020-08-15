package ninjaphenix.expandedstorage.impl;

import io.netty.buffer.Unpooled;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.container.ContainerProviderRegistry;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import ninjaphenix.expandedstorage.ExpandedStorage;
import ninjaphenix.expandedstorage.impl.client.ScreenMiscSettings;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.function.Consumer;

import static ninjaphenix.expandedstorage.api.Constants.SCREEN_SELECT;

public class ContainerLibraryImpl
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
            if (containerTypeId == null || !containerTypeId.equals(ExpandedStorage.getId("auto"))) { playerPreferences.remove(uuid); }
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
}
