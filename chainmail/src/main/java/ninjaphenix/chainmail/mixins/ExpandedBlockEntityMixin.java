package ninjaphenix.chainmail.mixins;

import net.minecraft.block.entity.BlockEntity;
import ninjaphenix.chainmail.api.blockentity.ExpandedBlockEntity;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(BlockEntity.class)
public class ExpandedBlockEntityMixin implements ExpandedBlockEntity {

    @Override
    public void onLoad() {
    }

    @Override
    public void onUnload() {
    }
}