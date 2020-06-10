package ninjaphenix.containerlib.api.container;

import net.minecraft.container.Container;
import net.minecraft.container.ContainerType;
import net.minecraft.container.Slot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import ninjaphenix.containerlib.api.ScreenMeta;

public abstract class AbstractContainer extends Container
{
    public final PlayerInventory PLAYER_INVENTORY;
    public final ScreenMeta SCREEN_META;
    protected final Inventory INVENTORY;
    private final Text DISPLAY_NAME;

    public AbstractContainer(ContainerType<?> type, int syncId, Inventory inventory, PlayerEntity player, Text displayName, ScreenMeta meta)
    {
        super(type, syncId);
        INVENTORY = inventory;
        PLAYER_INVENTORY = player.inventory;
        DISPLAY_NAME = displayName;
        SCREEN_META = meta;
    }

    @Override
    public boolean canUse(PlayerEntity player) { return INVENTORY.canPlayerUseInv(player); }

    public Text getDisplayName() { return DISPLAY_NAME.deepCopy(); }

    @Override
    public ItemStack transferSlot(PlayerEntity player, int slotIndex)
    {
        ItemStack stack = ItemStack.EMPTY;
        final Slot slot = slots.get(slotIndex);
        if (slot != null && slot.hasStack())
        {
            final ItemStack slotStack = slot.getStack();
            stack = slotStack.copy();
            if (slotIndex < INVENTORY.getInvSize()) { if (!insertItem(slotStack, INVENTORY.getInvSize(), slots.size(), true)) { return ItemStack.EMPTY; } }
            else if (!insertItem(slotStack, 0, INVENTORY.getInvSize(), false)) { return ItemStack.EMPTY; }
            if (slotStack.isEmpty()) { slot.setStack(ItemStack.EMPTY); }
            else { slot.markDirty(); }
        }
        return stack;
    }
}
