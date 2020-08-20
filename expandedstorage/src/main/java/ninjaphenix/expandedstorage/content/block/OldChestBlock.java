package ninjaphenix.expandedstorage.content.block;

import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.passive.CatEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.SimpleRegistry;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldAccess;
import ninjaphenix.expandedstorage.Registries;
import ninjaphenix.expandedstorage.content.block.entity.OldChestBlockEntity;
import ninjaphenix.expandedstorage.content.ModContent;

public final class OldChestBlock extends BaseChestBlock<OldChestBlockEntity>
{
    public OldChestBlock(final Settings settings) { super(settings, () -> ModContent.OLD_CHEST); }

    @Override
    public BlockEntity createBlockEntity(final BlockView view)
    {
        final Identifier blockId = Registry.BLOCK.getId(this);
        return new OldChestBlockEntity(new Identifier(blockId.getNamespace(), blockId.getPath().substring(4)));
    }

    @Override
    protected boolean isBlocked(final WorldAccess world, final BlockPos pos)
    {
        final BlockPos upPos = pos.up();
        final BlockState upState = world.getBlockState(upPos);
        return (upState.isSolidBlock(world, upPos) && upState.getBlock() != this) ||
                world.getNonSpectatingEntities(CatEntity.class, new Box(pos.getX(), pos.getY() + 1, pos.getZ(), pos.getX() + 1,
                                                                        pos.getY() + 2, pos.getZ() + 1))
                        .stream().anyMatch(CatEntity::isInSittingPose);
    }

    @Override
    public BlockRenderType getRenderType(final BlockState state) { return BlockRenderType.MODEL; }

    @Override
    @SuppressWarnings({"unchecked"})
    public SimpleRegistry<Registries.TierData> getDataRegistry() { return Registries.OLD_CHEST; }
}
