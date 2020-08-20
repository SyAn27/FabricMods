package ninjaphenix.expandedstorage.impl.inventory;

import com.google.common.collect.ImmutableMap;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.IntUnaryOperator;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import ninjaphenix.expandedstorage.impl.content.ModContent;
import ninjaphenix.expandedstorage.impl.screen.PagedScreenMeta;
import ninjaphenix.expandedstorage.impl.screen.ScrollableScreenMeta;

public final class ScrollableScreenHandler extends AbstractScreenHandler<ScrollableScreenMeta>
{
    private static final ImmutableMap<Integer, ScrollableScreenMeta> SIZES = ImmutableMap.<Integer, ScrollableScreenMeta>builder()
            .put(27, new ScrollableScreenMeta(9, 3, 27, getTexture("shared", 9, 3), 208, 192)) // Wood
            .put(54, new ScrollableScreenMeta(9, 6, 54, getTexture("shared", 9, 6), 208, 240)) // Iron / Large Wood
            .put(81, new ScrollableScreenMeta(9, 9, 81, getTexture("shared", 9, 9), 208, 304)) // Gold
            .put(108, new ScrollableScreenMeta(9, 9, 108, getTexture("shared", 9, 9), 208, 304)) // Diamond / Large Iron
            .put(135, new ScrollableScreenMeta(9, 9, 135, getTexture("shared", 9, 9), 208, 304)) // Netherite
            .put(162, new ScrollableScreenMeta(9, 9, 162, getTexture("shared", 9, 9), 208, 304)) // Large Gold
            .put(216, new ScrollableScreenMeta(9, 9, 216, getTexture("shared", 9, 9), 208, 304)) // Large Diamond
            .put(270, new ScrollableScreenMeta(9, 9, 270, getTexture("shared", 9, 9), 208, 304)) // Large Netherite
            .build();


    public ScrollableScreenHandler(final int syncId, final BlockPos pos, final Inventory inventory, final PlayerEntity player,
                                   final Text displayName)
    {
        super(ModContent.SCROLLABLE_HANDLER_TYPE, syncId, pos, inventory, player, displayName, getNearestSize(inventory.size()));
        for (int i = 0; i < INVENTORY.size(); i++)
        {
            final int x = i % SCREEN_META.WIDTH;
            int y = MathHelper.ceil((((double) (i - x)) / SCREEN_META.WIDTH));
            if (y >= SCREEN_META.HEIGHT) { y = -2000; }
            else {y = y * 18 + 18;}
            addSlot(new Slot(INVENTORY, i, x * 18 + 8, y));
        }
        final Inventory playerInventory = player.inventory;
        final int left = (SCREEN_META.WIDTH * 18 + 14) / 2 - 80;
        final int top = 18 + 14 + (SCREEN_META.HEIGHT * 18);
        for (int x = 0; x < 9; x++)
        {
            for (int y = 0; y < 3; y++) { addSlot(new Slot(playerInventory, y * 9 + x + 9, left + 18 * x, top + y * 18)); }
        }
        for (int i = 0; i < 9; i++) { addSlot(new Slot(playerInventory, i, left + 18 * i, top + 58)); }
    }

    private static ScrollableScreenMeta getNearestSize(final int invSize)
    {
        final ScrollableScreenMeta exactMeta = SIZES.get(invSize);
        if (exactMeta != null) { return exactMeta; }
        final List<Integer> keys = SIZES.keySet().asList();
        final int index = Collections.binarySearch(keys, invSize);
        final int largestKey = keys.get(Math.abs(index) - 1);
        final ScrollableScreenMeta nearestMeta = SIZES.get(largestKey);
        if (nearestMeta != null && largestKey > invSize && largestKey - invSize <= nearestMeta.WIDTH) { return nearestMeta; }
        throw new RuntimeException("No screen can show an inventory of size " + invSize + ".");
    }

    public void moveSlotRange(final int min, final int max, final int yChange)
    {
        for (int i = min; i < max; i++) { slots.get(i).y += yChange; }
    }

    public void setSlotRange(final int min, final int max, final IntUnaryOperator yPos)
    {
        for (int i = min; i < max; i++) { slots.get(i).y = yPos.applyAsInt(i); }
    }

    public static final class Factory implements ScreenHandlerRegistry.ExtendedClientHandlerFactory<ScrollableScreenHandler>
    {
        @Override
        public ScrollableScreenHandler create(final int syncId, final PlayerInventory playerInventory, final PacketByteBuf buffer)
        {
            if (buffer == null) { return null; }
            return new ScrollableScreenHandler(syncId, buffer.readBlockPos(), new SimpleInventory(buffer.readInt()), playerInventory.player,
                                               null);
        }
    }
}
