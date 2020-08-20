package ninjaphenix.expandedstorage.impl.inventory;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Direction;

public final class DoubleSidedInventory implements SidedInventory
{
    private final SidedInventory first;
    private final SidedInventory second;

    public DoubleSidedInventory(final SidedInventory first, final SidedInventory second)
    {
        this.first = first;
        this.second = second;
    }

    @Override
    public int[] getAvailableSlots(final Direction direction)
    {
        final int firstSize = first.size();
        final int[] firstSlots = first.getAvailableSlots(direction);
        final int[] secondSlots = second.getAvailableSlots(direction);
        final int[] combined = new int[firstSlots.length + secondSlots.length];
        int index = 0;
        for (final int slot : firstSlots) { combined[index++] = slot; }
        for (final int slot : secondSlots) { combined[index++] = slot + firstSize; }
        return combined;
    }

    @Override
    public boolean canInsert(final int slot, final ItemStack stack, final Direction direction)
    {
        if (slot >= first.size()) { return second.canInsert(slot - first.size(), stack, direction); }
        return first.canInsert(slot, stack, direction);
    }

    @Override
    public boolean canExtract(final int slot, final ItemStack stack, final Direction direction)
    {
        if (slot >= first.size()) { return second.canExtract(slot - first.size(), stack, direction); }
        return first.canExtract(slot, stack, direction);
    }

    @Override
    public int size() { return first.size() + second.size(); }

    @Override
    public boolean isEmpty() { return first.isEmpty() && second.isEmpty(); }

    @Override
    public boolean canPlayerUse(final PlayerEntity player) { return first.canPlayerUse(player) && second.canPlayerUse(player); }

    @Override
    public void clear()
    {
        first.clear();
        second.clear();
    }

    @Override
    public void markDirty()
    {
        first.markDirty();
        second.markDirty();
    }

    @Override
    public void onOpen(final PlayerEntity player)
    {
        first.onOpen(player);
        second.onOpen(player);
    }

    @Override
    public void onClose(final PlayerEntity player)
    {
        first.onClose(player);
        second.onClose(player);
    }

    public boolean isPart(final SidedInventory inventory) { return first == inventory || second == inventory; }

    public int getMaxCountPerStack() { return first.getMaxCountPerStack(); }

    @Override
    public ItemStack getStack(final int slot)
    {
        if (slot >= first.size()) { return second.getStack(slot - first.size()); }
        return first.getStack(slot);
    }

    @Override
    public ItemStack removeStack(final int slot, final int amount)
    {
        if (slot >= first.size()) { return second.removeStack(slot - first.size(), amount); }
        return first.removeStack(slot, amount);
    }

    @Override
    public ItemStack removeStack(final int slot)
    {
        if (slot >= first.size()) { return second.removeStack(slot - first.size()); }
        return first.removeStack(slot);
    }

    @Override
    public void setStack(final int slot, final ItemStack stack)
    {
        if (slot >= first.size()) { second.setStack(slot - first.size(), stack); }
        else { first.setStack(slot, stack); }
    }

    @Override
    public boolean isValid(final int slot, final ItemStack stack)
    {
        if (slot >= first.size()) { return second.isValid(slot - first.size(), stack); }
        return first.isValid(slot, stack);
    }
}
