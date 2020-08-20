package ninjaphenix.chainmail.mixins;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.world.World;
import ninjaphenix.chainmail.api.blockentity.ExpandedBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(World.class)
public abstract class WorldMixin
{
    @Inject(method = "addBlockEntity", at = @At("TAIL"))
    private void chainmail_addBlockEntity(final BlockEntity be, final CallbackInfoReturnable<Boolean> cir)
    {
        if (be instanceof ExpandedBlockEntity) { ((ExpandedBlockEntity) be).onLoad(); }
    }
}