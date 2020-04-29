package ninjaphenix.userdefinedadditions.mixins;

import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import ninjaphenix.userdefinedadditions.constants.ItemGroups;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FabricItemGroupBuilder.class)
public class FabricItemGroupMixin
{
    @Shadow(remap = false) private Identifier identifier;

    @Inject(method = "build()Lnet/minecraft/item/ItemGroup;", at = @At("RETURN"), remap = false)
    private void build(CallbackInfoReturnable<ItemGroup> cir) { ItemGroups.INSTANCE.put(identifier, cir.getReturnValue()); }
}
