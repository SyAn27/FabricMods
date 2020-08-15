package ninjaphenix.expandedstorage.impl.inventory;

import com.google.common.collect.ImmutableMap;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import ninjaphenix.expandedstorage.impl.screen.SingleScreenMeta;

import java.util.Arrays;

public final class SingleScreenHandler extends AbstractContainer<SingleScreenMeta>
{
    // todo: replace with datapack extension
    private static final ImmutableMap<Integer, SingleScreenMeta> SIZES = initializedMap(map ->
    {
        map.put(27, new SingleScreenMeta(9, 3, 27, getTexture("shared", 9, 3), 208, 192)); // Wood
        map.put(54, new SingleScreenMeta(9, 6, 54, getTexture("shared", 9, 6), 208, 240)); // Iron / Large Wood
        map.put(81, new SingleScreenMeta(9, 9, 81, getTexture("shared", 9, 9), 208, 304)); // Gold
        map.put(108, new SingleScreenMeta(12, 9, 108, getTexture("shared", 12, 9), 256, 304)); // Diamond / Large Iron
        map.put(135, new SingleScreenMeta(15, 9, 135, getTexture("shared", 15, 9), 320, 304)); // Netherite
        map.put(162, new SingleScreenMeta(18, 9, 162, getTexture("shared", 18, 9), 368, 304)); // Large Gold
        map.put(216, new SingleScreenMeta(18, 12, 216, getTexture("shared", 18, 12), 368, 352)); // Large Diamond
        map.put(270, new SingleScreenMeta(18, 15, 270, getTexture("shared", 18, 15), 368, 416)); // Large Netherite
    });

    public SingleScreenHandler(ScreenHandlerType<?> type, int syncId, BlockPos pos, Inventory inventory,
            PlayerEntity player, Text displayName)
    {
        super(type, syncId, pos, inventory, player, displayName, getNearestSize(inventory.size()));
        for (int i = 0; i < inventory.size(); i++)
        {
            final int x = i % SCREEN_META.WIDTH;
            final int y = (i - x) / SCREEN_META.WIDTH;
            this.addSlot(new Slot(inventory, i, x * 18 + 8, y * 18 + 18));
        }
        final int left = (SCREEN_META.WIDTH * 18 + 14) / 2 - 80;
        final int top = 18 + 14 + (SCREEN_META.HEIGHT * 18);
        for (int x = 0; x < 9; x++)
        {
            for (int y = 0; y < 3; y++)
            {
                this.addSlot(new Slot(PLAYER_INVENTORY, y * 9 + x + 9, left + 18 * x, top + y * 18));
            }
        }
        for (int i = 0; i < 9; i++) { this.addSlot(new Slot(PLAYER_INVENTORY, i, left + 18 * i, top + 58)); }
    }

    private static SingleScreenMeta getNearestSize(int invSize)
    {
        SingleScreenMeta val = SIZES.get(invSize);
        if (val != null) { return val; }
        final Integer[] keys = SIZES.keySet().toArray(new Integer[]{});
        Arrays.sort(keys);
        final int largestKey = keys[Math.abs(Arrays.binarySearch(keys, invSize)) - 1];
        val = SIZES.get(largestKey);
        if (largestKey > invSize && largestKey - invSize <= val.WIDTH) { return SIZES.get(largestKey); }
        throw new RuntimeException("No screen can show an inventory of size " + invSize + "."); // make this more obvious?
    }


}
