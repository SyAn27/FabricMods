package ninjaphenix.expandedstorage.api.block.entity;

import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import ninjaphenix.expandedstorage.api.ExpandedStorageAPI;
import ninjaphenix.expandedstorage.api.Registries;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

public class OldChestBlockEntity extends AbstractChestBlockEntity
{
    public OldChestBlockEntity(@Nullable Identifier block) { super(ExpandedStorageAPI.OLD_CHEST, block); }

    @Override
    @SuppressWarnings("ConstantConditions")
    protected void initialize(@NonNull Identifier block)
    {
        this.block = block;
        defaultContainerName = Registries.OLD_CHEST.get(block).getContainerName();
        inventorySize = Registries.OLD_CHEST.get(block).getSlotCount();
        inventory = DefaultedList.ofSize(inventorySize, ItemStack.EMPTY);
        SLOTS = new int[inventorySize];
        for (int i = 0; i < inventorySize; i++) { SLOTS[i] = i; }
    }
}
