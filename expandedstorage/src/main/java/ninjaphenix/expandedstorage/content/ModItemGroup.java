package ninjaphenix.expandedstorage.content;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class ModItemGroup extends ItemGroup
{
    public ModItemGroup(int index, String id) { super(index, id); }

    @Override
    @Environment(EnvType.CLIENT)
    public ItemStack createIcon() { return new ItemStack(ModBlocks.diamond_chest); }
}
