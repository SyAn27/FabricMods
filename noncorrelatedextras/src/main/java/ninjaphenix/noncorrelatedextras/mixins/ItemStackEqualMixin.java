package ninjaphenix.noncorrelatedextras.mixins;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtHelper;
import ninjaphenix.noncorrelatedextras.items.StructureCompassItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemStack.class)
public class ItemStackEqualMixin
{
    /*
    If there's a better way to stop the player's active item from being cleared when its nbt is updated let me know.
     */
    @Inject(method = "isEqualIgnoreDamage", at = @At("HEAD"), cancellable = true)
    private void noncorrelatedextras_areStacksEqual(ItemStack stack, CallbackInfoReturnable<Boolean> cir)
    {
        ItemStack self = (ItemStack) (Object) this;
        if (self.getItem() == stack.getItem() && self.getItem() instanceof StructureCompassItem)
        {
            if (self.hasTag() && stack.hasTag())
            {
                final CompoundTag selfTag = self.getTag();
                final CompoundTag stackTag = stack.getTag();
                if (selfTag.contains("pos", 10) && stackTag.contains("pos", 10))
                {
                    cir.setReturnValue(NbtHelper.toBlockPos(selfTag.getCompound("pos")).equals(NbtHelper.toBlockPos(stackTag.getCompound("pos"))));
                }
            }
        }
    }
}
