package ninjaphenix.chainmail.impl.mixinhelpers;

import net.minecraft.item.ItemStack;
import ninjaphenix.chainmail.api.client.render.ItemStackRenderFunction;

import java.util.function.Predicate;

public interface BuiltinModelItemRendererExtensions {
    void chainmail_addRenderer(Predicate<ItemStack> stackPredicate, ItemStackRenderFunction renderFunction);
}
