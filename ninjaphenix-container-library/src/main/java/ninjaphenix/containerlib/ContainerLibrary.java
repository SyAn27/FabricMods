package ninjaphenix.containerlib;

import net.fabricmc.api.ModInitializer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import ninjaphenix.containerlib.api.ContainerLibraryAPI;

public final class ContainerLibrary implements ModInitializer
{
    public static final Identifier CONTAINER_ID = new Identifier("ninjaphenix-container-lib", "container");
    public static final ContainerLibrary INSTANCE = new ContainerLibrary();

    private ContainerLibrary() {}

    /**
     * Open's a modded container which block implements InventoryProvider.
     *
     * @param player The Player who attempted to open the container.
     * @param pos The block pos of the container.
     * @param containerName The text that should be displayed as the container name.
     * @since 0.0.1
     * @deprecated {@link ContainerLibraryAPI#openContainer}
     */
    @Deprecated
    public static void openContainer(PlayerEntity player, BlockPos pos, Text containerName)
    {
        ContainerLibraryAPI.INSTANCE.openContainer(player, pos, containerName);
    }

    @Override
    public void onInitialize()
    {
    }
}