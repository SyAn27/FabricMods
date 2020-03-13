package ninjaphenix.chainmail.api.client.render;

import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.ItemStack;
import ninjaphenix.chainmail.impl.client.renderer.ChainmainRenderingImpl;

import java.util.function.Predicate;


public interface ChainmailRendering {
    /**
     * @since 0.0.2
     */
    ChainmailRendering INSTANCE = new ChainmainRenderingImpl();

    /**
     * @since 0.0.2
     */
    void registerBlockEntityItemStackRenderer(BlockEntityType<?> type, ItemStackRenderFunction renderFunction);

    /**
     * @since 0.0.2
     */
    void registerItemStackRenderer(Predicate<ItemStack> stackPredicate, ItemStackRenderFunction renderFunction);
}
