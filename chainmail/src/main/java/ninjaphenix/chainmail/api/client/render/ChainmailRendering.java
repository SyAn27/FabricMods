package ninjaphenix.chainmail.api.client.render;

import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.ItemStack;
import ninjaphenix.chainmail.impl.client.renderer.ChainmailRenderingImpl;

import java.util.function.Predicate;


public interface ChainmailRendering
{
    /**
     * @since 0.0.2
     */
    ChainmailRendering INSTANCE = new ChainmailRenderingImpl();

    /**
     * @since 0.0.2
     */
    void registerBlockEntityItemStackRenderer(final BlockEntityType<?> type, final ItemStackRenderFunction renderFunction);
}