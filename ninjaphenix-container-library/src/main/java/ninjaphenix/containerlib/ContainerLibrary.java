package ninjaphenix.containerlib;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.container.ContainerFactory;
import net.fabricmc.fabric.api.container.ContainerProviderRegistry;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.InventoryProvider;
import net.minecraft.container.Container;
import net.minecraft.container.ContainerType;
import net.minecraft.container.Slot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import ninjaphenix.containerlib.api.Constants;
import ninjaphenix.containerlib.api.ContainerLibraryAPI;
import ninjaphenix.containerlib.api.container.AbstractContainer;
import ninjaphenix.containerlib.api.inventory.AreaAwareSlotFactory;
import ninjaphenix.containerlib.impl.ContainerLibraryImpl;
import ninjaphenix.containerlib.impl.inventory.PagedContainer;
import ninjaphenix.containerlib.impl.inventory.SingleContainer;

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

    private <T extends Container> ContainerFactory<T> getContainerFactory(containerConstructor<T> newMethod)
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

    @Override
    public void onInitialize()
    {
        ContainerProviderRegistry.INSTANCE.registerFactory(Constants.SINGLE_CONTAINER, getContainerFactory(SingleContainer::new));
        ContainerProviderRegistry.INSTANCE.registerFactory(Constants.PAGED_CONTAINER, getContainerFactory(PagedContainer::new));

        ContainerLibraryAPI.INSTANCE.declareContainerType(Constants.SINGLE_CONTAINER,
                Constants.idOf("textures/gui/single_button.png"), new TranslatableText("screen.ninjaphenix-container-lib.single_screen_type"));
        ContainerLibraryAPI.INSTANCE.declareContainerType(Constants.SCROLLABLE_CONTAINER,
                Constants.idOf("textures/gui/scrollable_button.png"), new TranslatableText("screen.ninjaphenix-container-lib.scrollable_screen_type"));
        ContainerLibraryAPI.INSTANCE.declareContainerType(Constants.PAGED_CONTAINER,
                Constants.idOf("textures/gui/paged_button.png"), new TranslatableText("screen.ninjaphenix-container-lib.paged_screen_type"));
        ServerSidePacketRegistry.INSTANCE.register(Constants.OPEN_SCREEN_SELECT, (context, buffer) -> {
            final ServerPlayerEntity player = (ServerPlayerEntity) context.getPlayer();
            final Container container = player.container;
            if (container instanceof ninjaphenix.containerlib.api.container.AbstractContainer)
            {
                ContainerLibraryImpl.INSTANCE.openSelectScreen(player, (type) -> ContainerLibraryAPI.INSTANCE.openContainer(
                        player, ((AbstractContainer) container).ORIGIN, ((AbstractContainer) container).getDisplayName())
                );
            }
            else
            {
                ContainerLibraryImpl.INSTANCE.openSelectScreen(player, null);
            }
        });
        ServerSidePacketRegistry.INSTANCE.register(Constants.SCREEN_SELECT, (context, buffer) -> context.getTaskQueue().submitAndJoin(() -> {
            ContainerLibraryImpl.INSTANCE.setPlayerPreference(context.getPlayer(), buffer.readIdentifier());
        }));
    }

    private interface containerConstructor<T extends Container>
    {
        T create(ContainerType<T> type, int syncId, BlockPos pos, Inventory inventory,
                PlayerEntity player, Text containerName, AreaAwareSlotFactory slotFactory);
    }
}