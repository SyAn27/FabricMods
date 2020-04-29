package ninjaphenix.expandedstorage.api.mixins;

import ninjaphenix.expandedstorage.api.block.FluidLoggableChestBlock;
import org.spongepowered.asm.mixin.Mixin;
import virtuoel.towelette.api.Fluidloggable;

@Mixin(FluidLoggableChestBlock.class)
public final class ToweletteSupportMixin implements Fluidloggable {}