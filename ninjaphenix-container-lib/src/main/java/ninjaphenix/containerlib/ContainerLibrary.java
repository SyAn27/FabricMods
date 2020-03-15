package ninjaphenix.containerlib;

import net.fabricmc.api.*;
import net.fabricmc.fabric.api.client.screen.ScreenProviderRegistry;
import net.fabricmc.fabric.api.container.ContainerProviderRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.InventoryProvider;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import ninjaphenix.containerlib.client.gui.screen.ingame.ScrollableScreen;
import ninjaphenix.containerlib.inventory.ScrollableContainer;

@EnvironmentInterface(value = EnvType.CLIENT, itf = ClientModInitializer.class)
public final class ContainerLibrary implements ModInitializer, ClientModInitializer
{
	public static final Identifier CONTAINER_ID = new Identifier("ninjaphenix-container-lib", "container");

	/**
	 * Open's a modded container which block implements InventoryProvider.
	 *
	 * @param player The Player who attempted to open the container.
	 * @param pos The block pos of the container.
	 * @param containerName The text that should be displayed as the container name.
	 * @since 0.0.1
	 */
	public static void openContainer(PlayerEntity player, BlockPos pos, Text containerName)
	{
		ContainerProviderRegistry.INSTANCE.openContainer(CONTAINER_ID, player, (buffer) ->
		{
			buffer.writeBlockPos(pos);
			buffer.writeText(containerName);
		});
	}

	public static final ContainerLibrary INSTANCE = new ContainerLibrary();

	private ContainerLibrary() {}

	@Override
	public void onInitialize()
	{
		ContainerProviderRegistry.INSTANCE.registerFactory(CONTAINER_ID, (syncId, identifier, player, buffer) ->
		{
			final BlockPos pos = buffer.readBlockPos();
			final Text name = buffer.readText();
			final World world = player.getEntityWorld();
			final BlockState state = world.getBlockState(pos);
			final Block block = state.getBlock();
			if (block instanceof InventoryProvider)
			{
				return new ScrollableContainer(syncId, player.inventory, ((InventoryProvider) block).getInventory(state, world, pos), name);
			}
			return null;
		});
	}

	@Override
	@Environment(EnvType.CLIENT)
	public void onInitializeClient() { ScreenProviderRegistry.INSTANCE.registerFactory(CONTAINER_ID, ScrollableScreen::createScreen); }
}