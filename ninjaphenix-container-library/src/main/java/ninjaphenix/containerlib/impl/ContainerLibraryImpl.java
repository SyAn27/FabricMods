package ninjaphenix.containerlib.impl;

import net.fabricmc.fabric.api.container.ContainerProviderRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import ninjaphenix.containerlib.api.ContainerLibraryAPI;

import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;
import java.util.function.Consumer;

public class ContainerLibraryImpl implements ContainerLibraryAPI
{
    public static final ContainerLibraryAPI INSTANCE = new ContainerLibraryImpl();
    private final HashMap<UUID, Consumer<Identifier>> preferenceCallbacks = new HashMap<>();
    private final HashMap<UUID, Identifier> playerPreferences = new HashMap<>();
    private final HashSet<Identifier> declaredContainerTypes = new HashSet<>();

    @Override
    public void setPlayerPreference(PlayerEntity player, Identifier type)
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
    }

    @Override
    public void openContainer(PlayerEntity player, BlockPos pos, Text containerName)
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
            preferenceCallbacks.put(player.getUuid(), (type) -> openContainer(player, type, pos, containerName));
            // Send packet to client to open container type picker.
        }
    }

    @Override
    public boolean declareContainerType(Identifier containerTypeId) { return declaredContainerTypes.add(containerTypeId); }

    private void openContainer(PlayerEntity player, Identifier type, BlockPos pos, Text containerName)
    {
        ContainerProviderRegistry.INSTANCE.openContainer(type, player, buf ->
        {
            buf.writeBlockPos(pos);
            buf.writeText(containerName);
        });
    }
}
