package ninjaphenix.expandedstorage.common.content.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Waterloggable;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.WorldAccess;
import ninjaphenix.expandedstorage.common.content.block.entity.AbstractChestBlockEntity;

import java.util.function.Supplier;

import static net.minecraft.state.property.Properties.WATERLOGGED;

@SuppressWarnings("deprecation")
public abstract class FluidLoggableChestBlock<T extends AbstractChestBlockEntity> extends BaseChestBlock<T> implements Waterloggable
{
    protected FluidLoggableChestBlock(final Settings settings, final Supplier<BlockEntityType<T>> blockEntityType)
    {
        super(settings, blockEntityType);
        setDefaultState(getDefaultState().with(WATERLOGGED, false));
    }

    @Override
    public FluidState getFluidState(final BlockState state)
    {
        return state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
    }

    @Override
    protected void appendProperties(final StateManager.Builder<Block, BlockState> builder)
    {
        super.appendProperties(builder);
        builder.add(WATERLOGGED);
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    public BlockState getPlacementState(final ItemPlacementContext context)
    {
        return super.getPlacementState(context)
                .with(WATERLOGGED, context.getWorld().getFluidState(context.getBlockPos()) == Fluids.WATER.getDefaultState());
    }

    @Override
    public BlockState getStateForNeighborUpdate(final BlockState state, final Direction direction, final BlockState neighborState,
                                                final WorldAccess world, final BlockPos pos, final BlockPos neighborPos)
    {
        if (state.get(WATERLOGGED)) { world.getFluidTickScheduler().schedule(pos, Fluids.WATER, Fluids.WATER.getTickRate(world)); }
        return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
    }
}
