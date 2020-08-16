package ninjaphenix.expandedstorage.impl.inventory;

import com.google.common.collect.ImmutableMap;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import ninjaphenix.expandedstorage.impl.Const;
import ninjaphenix.expandedstorage.impl.ExpandedStorage;
import ninjaphenix.expandedstorage.impl.screen.ScreenMeta;

import java.util.HashMap;
import java.util.function.Consumer;

public abstract class AbstractContainer<T extends ScreenMeta> extends ScreenHandler
{
    public final BlockPos ORIGIN;
    public final PlayerInventory PLAYER_INVENTORY;
    public final T SCREEN_META;
    protected final Inventory INVENTORY;
    private final Text DISPLAY_NAME;

    public AbstractContainer(ScreenHandlerType<?> type, int syncId, BlockPos pos, Inventory inventory, PlayerEntity player, Text displayName, T meta)
    {
        super(type, syncId);
        ORIGIN = pos;
        INVENTORY = inventory;
        PLAYER_INVENTORY = player.inventory;
        DISPLAY_NAME = displayName;
        SCREEN_META = meta;
        inventory.onOpen(player);
    }

    @Override
    public boolean canUse(PlayerEntity player) { return INVENTORY.canPlayerUse(player); }

    public Text getDisplayName() { return DISPLAY_NAME.copy(); }

    @Override
    public ItemStack transferSlot(PlayerEntity player, int slotIndex)
    {
        ItemStack stack = ItemStack.EMPTY;
        final Slot slot = slots.get(slotIndex);
        if (slot != null && slot.hasStack())
        {
            final ItemStack slotStack = slot.getStack();
            stack = slotStack.copy();
            if (slotIndex < INVENTORY.size()) { if (!insertItem(slotStack, INVENTORY.size(), slots.size(), true)) { return ItemStack.EMPTY; } }
            else if (!insertItem(slotStack, 0, INVENTORY.size(), false)) { return ItemStack.EMPTY; }
            if (slotStack.isEmpty()) { slot.setStack(ItemStack.EMPTY); }
            else { slot.markDirty(); }
        }
        return stack;
    }

    @Override
    public void close(PlayerEntity player)
    {
        super.close(player);
        INVENTORY.onClose(player);
    }

    public Inventory getInventory() { return INVENTORY; }

    public static Identifier getTexture(final String type, final int width, final int height)
    {
        return new Identifier(Const.MOD_ID, String.format("textures/gui/container/%s_%d_%d.png", type, width, height));
    }

    public static <A, B> ImmutableMap<A, B> initializedMap(final Consumer<HashMap<A, B>> initializer) {
        HashMap<A, B> rv = new HashMap<A, B>();
        initializer.accept(rv);
        return ImmutableMap.copyOf(rv);
    }
}
