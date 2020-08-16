package ninjaphenix.expandedstorage.impl.screen;

import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

public final class ScrollableScreenMeta extends ScreenMeta
{
    public final int BLANK_SLOTS, TOTAL_ROWS;

    public ScrollableScreenMeta(final int width, final int height, final int totalSlots, final Identifier texture, final int textureWidth,
                                final int textureHeight)
    {
        super(width, height, totalSlots, texture, textureWidth, textureHeight);
        TOTAL_ROWS = MathHelper.ceil((double) totalSlots / width);
        BLANK_SLOTS = TOTAL_ROWS * width - totalSlots;
    }
}