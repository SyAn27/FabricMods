package ninjaphenix.expandedstorage.common.content.block;

import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.SimpleRegistry;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import ninjaphenix.expandedstorage.common.Registries;
import ninjaphenix.expandedstorage.common.content.block.entity.CursedChestBlockEntity;
import ninjaphenix.expandedstorage.common.content.misc.CursedChestType;
import ninjaphenix.expandedstorage.common.content.ModContent;

import static net.minecraft.state.property.Properties.HORIZONTAL_FACING;

public final class CursedChestBlock extends FluidLoggableChestBlock<CursedChestBlockEntity>
{
    private static final VoxelShape SINGLE_SHAPE = createCuboidShape(1, 0, 1, 15, 14, 15);
    private static final VoxelShape TOP_SHAPE = createCuboidShape(1, 0, 1, 15, 14, 15);
    private static final VoxelShape BOTTOM_SHAPE = createCuboidShape(1, 0, 1, 15, 16, 15);
    private static final VoxelShape[] HORIZONTAL_VALUES = {
            createCuboidShape(1, 0, 0, 15, 14, 15),
            createCuboidShape(1, 0, 1, 16, 14, 15),
            createCuboidShape(1, 0, 1, 15, 14, 16),
            createCuboidShape(0, 0, 1, 15, 14, 15)
    };

    public CursedChestBlock(final Settings settings)
    {
        super(settings, () -> ModContent.CHEST);
        setDefaultState(getDefaultState().with(HORIZONTAL_FACING, Direction.SOUTH));
    }

    @Override
    public BlockEntity createBlockEntity(final BlockView view) { return new CursedChestBlockEntity(Registry.BLOCK.getId(this)); }

    @Override
    @SuppressWarnings("deprecation")
    public VoxelShape getOutlineShape(final BlockState state, final BlockView view, final BlockPos pos, final ShapeContext context)
    {
        final CursedChestType type = state.get(TYPE);
        if (type == CursedChestType.TOP) { return TOP_SHAPE; }
        else if (type == CursedChestType.BOTTOM) { return BOTTOM_SHAPE; }
        else if (type == CursedChestType.SINGLE) {return SINGLE_SHAPE; }
        else { return HORIZONTAL_VALUES[(state.get(HORIZONTAL_FACING).getHorizontal() + type.getOffset()) % 4]; }
    }

    @Override
    public BlockRenderType getRenderType(final BlockState state) { return BlockRenderType.ENTITYBLOCK_ANIMATED; }

    @Override
    @SuppressWarnings({"unchecked"})
    public SimpleRegistry<Registries.ChestTierData> getDataRegistry() { return Registries.CHEST; }
}