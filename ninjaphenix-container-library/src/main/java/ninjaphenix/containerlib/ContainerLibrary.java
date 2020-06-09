package ninjaphenix.containerlib;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import ninjaphenix.containerlib.api.Constants;
import ninjaphenix.containerlib.api.ContainerLibraryAPI;

public final class ContainerLibrary implements ModInitializer
{
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
    { ContainerLibraryAPI.INSTANCE.openContainer(player, pos, containerName); }

    @Override
    public void onInitialize()
    {
        ContainerLibraryAPI.INSTANCE
                .declareContainerType(Constants.idOf("single"), Constants.idOf("textures/gui/single_button.png"), new LiteralText("Single Page Screen"));
        ContainerLibraryAPI.INSTANCE
                .declareContainerType(Constants.idOf("scrollable"), Constants.idOf("textures/gui/scrollable_button.png"), new LiteralText("Scrollable Screen"));
        ContainerLibraryAPI.INSTANCE
                .declareContainerType(Constants.idOf("paged"), Constants.idOf("textures/gui/paged_button.png"), new LiteralText("Paginated Screen"));
        ServerSidePacketRegistry.INSTANCE.register(Constants.SCREEN_SELECT, (context, buffer) ->
                ContainerLibraryAPI.INSTANCE.setPlayerPreference(context.getPlayer(), buffer.readIdentifier()));
    }
}