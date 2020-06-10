package ninjaphenix.containerlib.impl.inventory;

import net.minecraft.container.ContainerType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import ninjaphenix.containerlib.api.ScreenMeta;
import ninjaphenix.containerlib.api.inventory.AreaAwareSlotFactory;

import java.util.Arrays;
import java.util.HashMap;
import java.util.function.BiFunction;

public class PagedContainer extends ninjaphenix.containerlib.api.container.AbstractContainer
{
    private static final HashMap<Integer, ScreenMeta> SIZES;

    static
    {
        BiFunction<Integer, Integer, Identifier> id = (w, h) -> new Identifier("ninjaphenix-container-lib",
                "textures/gui/container/fixed_" + w + "_" + h + ".png");
        SIZES = new HashMap<>();
        SIZES.put(27, ScreenMeta.of(9, 3, 27, id.apply(9, 3), 208, 192)); // Wood
        SIZES.put(54, ScreenMeta.of(9, 6, 54, id.apply(9, 6), 208, 240)); // Iron / Large Wood
        SIZES.put(81, ScreenMeta.of(9, 9, 81, id.apply(9, 9), 208, 304)); // Gold
        SIZES.put(108, ScreenMeta.of(12, 9, 108, id.apply(12, 9), 256, 304)); // Diamond / Large Iron
        SIZES.put(162, ScreenMeta.of(18, 9, 162, id.apply(18, 9), 368, 304)); // Large Gold
        SIZES.put(216, ScreenMeta.of(18, 12, 216, id.apply(18, 12), 368, 352)); // Large Diamond
    }

    public PagedContainer(ContainerType<?> type, int syncId, BlockPos pos, Inventory inventory,
            PlayerEntity player, Text displayName, AreaAwareSlotFactory slotFactory)
    {
        super(type, syncId, pos, inventory, player, displayName, getNearestSize(inventory.getInvSize()));
        for (int i = 0; i < inventory.getInvSize(); i++)
        {
            final int x = i % SCREEN_META.WIDTH;
            final int y = (i - x) / SCREEN_META.WIDTH;
            this.addSlot(slotFactory.create(inventory, "inventory", i, x * 18 + 8, y * 18 + 18));
        }
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

    private static ScreenMeta getNearestSize(int invSize)
    {
        ScreenMeta val = SIZES.get(invSize);
        if (val != null) { return val; }
        final Integer[] keys = SIZES.keySet().toArray(new Integer[]{});
        Arrays.sort(keys);
        final int largestKey = keys[Math.abs(Arrays.binarySearch(keys, invSize)) - 1];
        val = SIZES.get(largestKey);
        if (largestKey > invSize && largestKey - invSize <= val.WIDTH) { return SIZES.get(largestKey); }
        throw new RuntimeException("No screen can show an inventory of size " + invSize + "."); // make this more obvious?
    }
}
