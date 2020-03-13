package ninjaphenix.chainmail.impl.client.renderer;

import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.render.item.BuiltinModelItemRenderer;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import ninjaphenix.chainmail.api.client.render.ChainmailRendering;
import ninjaphenix.chainmail.api.client.render.ItemStackRenderFunction;
import ninjaphenix.chainmail.impl.mixinhelpers.BuiltinModelItemRendererExtensions;

import java.util.function.Predicate;

public class ChainmainRenderingImpl implements ChainmailRendering {

    public static final BuiltinModelItemRendererExtensions ext = (BuiltinModelItemRendererExtensions) BuiltinModelItemRenderer.INSTANCE;

    @Override
    public void registerBlockEntityItemStackRenderer(BlockEntityType<?> type, ItemStackRenderFunction renderFunction) {
        ext.chainmail_addRenderer((stack) -> {
            final Item item = stack.getItem();
            return item instanceof BlockItem && type.supports(((BlockItem) item).getBlock());
        }, renderFunction);
    }

    @Override
    public void registerItemStackRenderer(Predicate<ItemStack> stackPredicate, ItemStackRenderFunction renderFunction) {
        ext.chainmail_addRenderer(stackPredicate, renderFunction);
    }
}
