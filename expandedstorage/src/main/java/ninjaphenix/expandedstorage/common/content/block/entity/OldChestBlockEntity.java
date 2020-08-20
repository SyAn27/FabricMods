package ninjaphenix.expandedstorage.common.content.block.entity;

import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import ninjaphenix.expandedstorage.common.Registries;
import ninjaphenix.expandedstorage.common.content.ModContent;

public final class OldChestBlockEntity extends AbstractChestBlockEntity
{
    public OldChestBlockEntity(final Identifier block) { super(ModContent.OLD_CHEST, block); }

    @Override
    @SuppressWarnings("ConstantConditions")
    protected void initialize(final Identifier block)
    {
        this.block = block;
        defaultContainerName = Registries.OLD_CHEST.get(block).getContainerName();
        inventorySize = Registries.OLD_CHEST.get(block).getSlotCount();
        inventory = DefaultedList.ofSize(inventorySize, ItemStack.EMPTY);
        SLOTS = new int[inventorySize];
        for (int i = 0; i < inventorySize; i++) { SLOTS[i] = i; }
    }
}
