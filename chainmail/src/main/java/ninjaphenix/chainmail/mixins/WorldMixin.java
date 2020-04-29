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
    private void chainmail_addBlockEntity(BlockEntity be, CallbackInfoReturnable<Boolean> cir)
    {
        if (be instanceof ExpandedBlockEntity)
        {
            ((ExpandedBlockEntity) be).onLoad();
        }
    }
    //@Shadow @Final private Map<BlockPos, BlockEntity> blockEntities;
//
    //@Inject(method = "setLoadedToWorld", at = @At("HEAD"))
    //private void chainmail_setBlockEntity(boolean bl, CallbackInfo ci)
    //{
    //    if (bl)
    //    {
    //        blockEntities.values().forEach(be -> {
    //            if (be instanceof ExpandedBlockEntity)
    //            {
    //                ((ExpandedBlockEntity) be).onLoad();
    //            }
    //        });
    //    }
    //}
//
    ////@Inject(method = "tickBlockEntities", at = @At(value = "INVOKE", target = "Ljava/util/List;removeAll(Ljava/util/Collection;)Z", ordinal = 0))
    ////private void chainmail_tickBlockEntities(CallbackInfo ci)
    ////{
    ////    unloadedBlockEntities.forEach(be -> ((ExpandedBlockEntity) be).onUnload());
    ////}
}
