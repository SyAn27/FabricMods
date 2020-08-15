package ninjaphenix.expandedstorage.impl.inventory;

import com.google.common.collect.ImmutableMap;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import ninjaphenix.expandedstorage.api.inventory.AbstractContainer;
import ninjaphenix.expandedstorage.api.inventory.AreaAwareSlotFactory;
import ninjaphenix.expandedstorage.impl.screen.ScrollableScreenMeta;

import java.util.Arrays;
import java.util.function.IntUnaryOperator;

public final class ScrollableScreenHandler extends AbstractContainer<ScrollableScreenMeta>
{
    private static final ImmutableMap<Integer, ScrollableScreenMeta> SIZES = initializedMap(map ->
    {
        map.put(27, new ScrollableScreenMeta(9, 3, 27, getTexture("shared", 9, 3), 208, 192)); // Wood
        map.put(54, new ScrollableScreenMeta(9, 6, 54, getTexture("shared", 9, 6), 208, 240)); // Iron / Large Wood
        map.put(81, new ScrollableScreenMeta(9, 9, 81, getTexture("shared", 9, 9), 208, 304)); // Gold
        map.put(108, new ScrollableScreenMeta(9, 9, 108, getTexture("shared", 9, 9), 208, 304)); // Diamond / Large Iron
        map.put(135, new ScrollableScreenMeta(9, 9, 135, getTexture("shared", 9, 9), 208, 304)); // Netherite
        map.put(162, new ScrollableScreenMeta(9, 9, 162, getTexture("shared", 9, 9), 208, 304)); // Large Gold
        map.put(216, new ScrollableScreenMeta(9, 9, 216, getTexture("shared", 9, 9), 208, 304)); // Large Diamond
        map.put(270, new ScrollableScreenMeta(9, 9, 270, getTexture("shared", 9, 9), 208, 304)); // Large Netherite
    });

    public ScrollableScreenHandler(ScreenHandlerType<?> type, int syncId, BlockPos pos, Inventory inventory,
            PlayerEntity player, Text displayName, AreaAwareSlotFactory slotFactory)
    {
        super(type, syncId, pos, inventory, player, displayName, getNearestSize(inventory.size()));
        resetSlotPositions(slotFactory);
        final int left = (SCREEN_META.WIDTH * 18 + 14) / 2 - 80;
        final int top = 18 + 14 + (SCREEN_META.HEIGHT * 18);
        for (int x = 0; x < 9; x++)
        {
            for (int y = 0; y < 3; y++)
            {
                this.addSlot(slotFactory.create(PLAYER_INVENTORY, "player_inventory", y * 9 + x + 9, left + 18 * x, top + y * 18));
            }
        }
        for (int i = 0; i < 9; i++) { this.addSlot(slotFactory.create(PLAYER_INVENTORY, "player_hotbar", i, left + 18 * i, top + 58)); }
    }

    public static void onScreenSizeRegistered(ScrollableScreenMeta meta) { SIZES.put(meta.TOTAL_SLOTS, meta); }

    private static ScrollableScreenMeta getNearestSize(int invSize)
    {
        ScrollableScreenMeta val = SIZES.get(invSize);
        if (val != null) { return val; }
        final Integer[] keys = SIZES.keySet().toArray(new Integer[]{});
        Arrays.sort(keys);
        final int largestKey = keys[Math.abs(Arrays.binarySearch(keys, invSize)) - 1];
        val = SIZES.get(largestKey);
        if (largestKey > invSize && largestKey - invSize <= val.WIDTH) { return SIZES.get(largestKey); }
        throw new RuntimeException("No screen can show an inventory of size " + invSize + "."); // make this more obvious?
    }

    private void resetSlotPositions(AreaAwareSlotFactory slotFactory)
    {
        for (int i = 0; i < INVENTORY.size(); i++)
        {
            final int x = i % SCREEN_META.WIDTH;
            int y = MathHelper.ceil((((double) (i - x)) / SCREEN_META.WIDTH));
            if (y >= SCREEN_META.HEIGHT) { y = -2000; }
            else {y = y * 18 + 18;}
            if (slotFactory != null) { this.addSlot(slotFactory.create(INVENTORY, "inventory", i, x * 18 + 8, y)); }
            else { slots.get(i).y = y; }
        }
    }

    public void moveSlotRange(int min, int max, int yChange) { for (int i = min; i < max; i++) { slots.get(i).y += yChange; } }

    public void setSlotRange(int min, int max, IntUnaryOperator yPos) { for (int i = min; i < max; i++) { slots.get(i).y = yPos.applyAsInt(i); } }
}
