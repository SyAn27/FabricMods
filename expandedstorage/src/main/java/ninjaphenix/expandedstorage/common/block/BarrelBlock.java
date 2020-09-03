package ninjaphenix.expandedstorage.common.block;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.stat.Stats;
import net.minecraft.state.StateManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.SimpleRegistry;
import net.minecraft.world.BlockView;
import ninjaphenix.expandedstorage.common.Const;
import ninjaphenix.expandedstorage.common.Registries;
import ninjaphenix.expandedstorage.common.block.entity.BarrelBlockEntity;
import org.jetbrains.annotations.Nullable;

import static net.minecraft.state.property.Properties.FACING;
import static net.minecraft.state.property.Properties.OPEN;

public class BarrelBlock extends StorageBlock
{
    public BarrelBlock(final Settings builder)
    {
        super(builder);
        setDefaultState(getDefaultState().with(FACING, Direction.NORTH).with(OPEN, false));
    }

    @Override
    protected void appendProperties(final StateManager.Builder<Block, BlockState> builder)
    {
        super.appendProperties(builder);
        builder.add(FACING, OPEN);
    }

    @Override
    @SuppressWarnings("deprecation")
    public void scheduledTick(final BlockState state, final ServerWorld world, final BlockPos pos, final Random random)
    {
        final BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof BarrelBlockEntity)
        {
            ((BarrelBlockEntity) blockEntity).tick();
        }
    }

    @Nullable
    @Override
    public BlockState getPlacementState(final ItemPlacementContext context)
    {
        return getDefaultState().with(FACING, context.getPlayerLookDirection().getOpposite());
    }

    @Override
    @SuppressWarnings("unchecked")
    public SimpleRegistry<Registries.TierData> getDataRegistry() { return Registries.BARREL; }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(final BlockView world)
    {
        final String path = Registry.BLOCK.getId(this).getPath();
        return new BarrelBlockEntity(Const.id(path.replace("_barrel", "_chest")));
    }

    @Override
    protected Identifier getOpenStat() { return Stats.OPEN_BARREL; }
}