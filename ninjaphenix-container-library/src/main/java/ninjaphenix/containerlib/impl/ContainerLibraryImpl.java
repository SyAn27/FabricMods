package ninjaphenix.containerlib.impl;

import io.netty.buffer.Unpooled;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.container.ContainerProviderRegistry;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.PacketByteBuf;
import net.minecraft.util.math.BlockPos;
import ninjaphenix.containerlib.api.ContainerLibraryAPI;
import ninjaphenix.containerlib.impl.client.ScreenMiscSettings;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Consumer;

import static ninjaphenix.containerlib.api.Constants.SCREEN_SELECT;

@ApiStatus.Internal
public class ContainerLibraryImpl implements ContainerLibraryAPI
{
    public static final ContainerLibraryImpl INSTANCE = new ContainerLibraryImpl();
    private final HashMap<UUID, Consumer<Identifier>> preferenceCallbacks = new HashMap<>();
    private final HashMap<UUID, Identifier> playerPreferences = new HashMap<>();
    private final HashSet<Identifier> declaredContainerTypes = new HashSet<>();

    @Environment(EnvType.CLIENT)
    private final HashMap<Identifier, ScreenMiscSettings> screenMiscSettings = new HashMap<>();

    public boolean isContainerTypeDeclared(Identifier type)
    {
        Objects.requireNonNull(type);
        return declaredContainerTypes.contains(type);
    }

    public void setPlayerPreference(final PlayerEntity player, final Identifier type)
    {
        final UUID uuid = player.getUuid();
        if (declaredContainerTypes.contains(type))
        {
            playerPreferences.put(uuid, type);
            if (preferenceCallbacks.containsKey(uuid))
            {
                preferenceCallbacks.get(uuid).accept(type);
                preferenceCallbacks.remove(uuid);
            }
        }
        else
        {
            playerPreferences.remove(uuid);
            preferenceCallbacks.remove(uuid);
        }
    }

    @Override
    public void openContainer(final PlayerEntity player, final BlockPos pos, final Text containerName)
    {
        final UUID uuid = player.getUuid();
        Identifier playerPreference;
        if (playerPreferences.containsKey(uuid) &&
                declaredContainerTypes.contains(playerPreference = playerPreferences.get(uuid)) /*&&
                ContainerProviderRegistry.INSTANCE.factoryExists(playerPreference)*/)
        {
            openContainer(player, playerPreference, pos, containerName);
        }
        else
        {
            openSelectScreen(player, (type) -> openContainer(player, type, pos, containerName));
        }
    }

    public void openSelectScreen(final PlayerEntity player, @Nullable Consumer<Identifier> playerPreferenceCallback)
    {
        if (playerPreferenceCallback != null)
        {
            preferenceCallbacks.put(player.getUuid(), playerPreferenceCallback);
        }
        final PacketByteBuf buffer = new PacketByteBuf(Unpooled.buffer());
        buffer.writeInt(declaredContainerTypes.size());
        declaredContainerTypes.forEach(buffer::writeIdentifier);
        ServerSidePacketRegistry.INSTANCE.sendToPlayer(player, SCREEN_SELECT, buffer);
    }

    @Override
    public boolean declareContainerType(final Identifier containerTypeId, final Identifier selectTextureId, final Text narrationMessage)
    {
        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT)
        { screenMiscSettings.put(containerTypeId, new ScreenMiscSettings(selectTextureId, narrationMessage)); }
        return declaredContainerTypes.add(containerTypeId);
    }

    @Environment(EnvType.CLIENT)
    public ScreenMiscSettings getScreenSettings(final Identifier containerTypeId) { return screenMiscSettings.get(containerTypeId); }

    private void openContainer(final PlayerEntity player, final Identifier type, final BlockPos pos, final Text containerName)
    {
        ContainerProviderRegistry.INSTANCE.openContainer(type, player, buf ->
        {
            buf.writeBlockPos(pos);
            buf.writeText(containerName);
        });
    }
}
