package ninjaphenix.expandedstorage.impl.block.misc;

import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.InventoryProvider;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.inventory.Inventory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

@SuppressWarnings("deprecation")
public abstract class BasicStorageBlock extends Block implements BlockEntityProvider, InventoryProvider
{
    protected BasicStorageBlock(final Settings settings) { super(settings); }

    @Override
    public boolean hasComparatorOutput(final BlockState state) { return true; }

    @Override
    public int getComparatorOutput(final BlockState state, final World world, final BlockPos pos)
    {
        return ScreenHandler.calculateComparatorOutput(getInventory(state, world, pos));
    }

    @Override
    public void onStateReplaced(final BlockState state, final World world, final BlockPos pos, final BlockState newState, final boolean moved)
    {
        if (state.getBlock() != newState.getBlock())
        {
            final BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof Inventory)
            {
                ItemScatterer.spawn(world, pos, (Inventory) blockEntity);
                world.updateComparators(pos, this);
            }
            super.onStateReplaced(state, world, pos, newState, moved);
        }
    }

    @Override
    public PistonBehavior getPistonBehavior(final BlockState state) { return PistonBehavior.IGNORE; }
}
