package ninjaphenix.expandedstorage.common.block;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.passive.CatEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.registry.SimpleRegistry;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldAccess;
import ninjaphenix.expandedstorage.common.Registries;
import ninjaphenix.expandedstorage.common.block.entity.OldChestBlockEntity;
import ninjaphenix.expandedstorage.common.ModContent;

public final class OldChestBlock extends ChestBlock<OldChestBlockEntity>
{
    public OldChestBlock(final Settings settings, final Identifier tierId) { super(settings, tierId, () -> ModContent.OLD_CHEST); }

    @Override
    public BlockEntity createBlockEntity(final BlockView view) { return new OldChestBlockEntity(TIER_ID); }

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
    @SuppressWarnings({"unchecked"})
    public SimpleRegistry<Registries.TierData> getDataRegistry() { return Registries.OLD_CHEST; }
}
