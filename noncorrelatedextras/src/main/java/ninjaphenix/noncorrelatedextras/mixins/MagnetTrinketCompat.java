package ninjaphenix.noncorrelatedextras.mixins;

import dev.emi.trinkets.api.Slots;
import dev.emi.trinkets.api.Trinket;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import ninjaphenix.noncorrelatedextras.items.MagnetItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MagnetItem.class)
public class MagnetTrinketCompat implements Trinket
{
    @Override
    public boolean canWearInSlot(final String group, final String slot) { return slot.equals(Slots.RING); }

    @Override
    public void tick(final PlayerEntity player, final ItemStack stack) { MagnetItem.magnetTick(player, stack); }

    @Inject(at = @At("HEAD"), method = "use(Lnet/minecraft/world/World;Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/util/Hand;)Lnet/minecraft/util/TypedActionResult;", cancellable = true)
    private void use(final World world, final PlayerEntity user, final Hand hand, final CallbackInfoReturnable<TypedActionResult<ItemStack>> cir)
    {
        if (!user.isSneaking()) { cir.setReturnValue(Trinket.equipTrinket(user, hand)); }
    }
}