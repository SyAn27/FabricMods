package ninjaphenix.expandedstorage.common.inventory;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import ninjaphenix.expandedstorage.common.Const;
import ninjaphenix.expandedstorage.common.inventory.screen.ScreenMeta;

public abstract class AbstractScreenHandler<T extends ScreenMeta> extends ScreenHandler
{
    public final BlockPos ORIGIN;
    public final T SCREEN_META;
    protected final Inventory INVENTORY;
    private final Text DISPLAY_NAME;

    public AbstractScreenHandler(final ScreenHandlerType<?> type, final int syncId, final BlockPos pos, final Inventory inventory,
                                 final PlayerEntity player, final Text displayName, final T meta)
    {
        super(type, syncId);
        ORIGIN = pos;
        INVENTORY = inventory;
        DISPLAY_NAME = displayName;
        SCREEN_META = meta;
        inventory.onOpen(player);
    }

    @Override
    public boolean canUse(final PlayerEntity player) { return INVENTORY.canPlayerUse(player); }

    public Text getDisplayName() { return DISPLAY_NAME.copy(); }

    @Override
    public ItemStack transferSlot(final PlayerEntity player, final int slotIndex)
    {
        ItemStack stack = ItemStack.EMPTY;
        final Slot slot = slots.get(slotIndex);
        if (slot != null && slot.hasStack())
        {
            final ItemStack slotStack = slot.getStack();
            stack = slotStack.copy();
            if (slotIndex < INVENTORY.size())
            {
                if (!insertItem(slotStack, INVENTORY.size(), slots.size(), true)) { return ItemStack.EMPTY; }
            }
            else if (!insertItem(slotStack, 0, INVENTORY.size(), false)) { return ItemStack.EMPTY; }
            if (slotStack.isEmpty()) { slot.setStack(ItemStack.EMPTY); }
            else { slot.markDirty(); }
        }
        return stack;
    }

    @Override
    public void close(final PlayerEntity player)
    {
        super.close(player);
        INVENTORY.onClose(player);
    }

    public Inventory getInventory() { return INVENTORY; }

    public static Identifier getTexture(final String type, final int width, final int height)
    {
        return new Identifier(Const.MOD_ID, String.format("textures/gui/container/%s_%d_%d.png", type, width, height));
    }
}
