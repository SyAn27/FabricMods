package ninjaphenix.expandedstorage.impl.block.entity;

import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;

import java.util.function.Predicate;
import java.util.function.Supplier;

/* todo: remove, use vanilla's own block entity type which takes a set of blocks. */
public final class CustomBlockEntityType<T extends BlockEntity> extends BlockEntityType<T>
{
    private final Predicate<Block> predicate;

    public CustomBlockEntityType(final Supplier<? extends T> supplier, final Predicate<Block> supportPredicate)
    {
        super(supplier, null, null);
        predicate = supportPredicate;
    }

    @Override
    public boolean supports(final Block block) { return predicate.test(block); }
}