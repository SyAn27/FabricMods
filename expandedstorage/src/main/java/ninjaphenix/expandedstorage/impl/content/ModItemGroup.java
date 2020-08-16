package ninjaphenix.expandedstorage.impl.content;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public final class ModItemGroup extends ItemGroup
{
    public ModItemGroup(final int index, final String id)
    {
        super(index, id);
    }

    @Override
    @Environment(EnvType.CLIENT)
    public ItemStack createIcon() { return new ItemStack(ModContent.DIAMOND_CHEST); }
}
