package ninjaphenix.containerlib.api.screen;

import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

public class ScrollableScreenMeta extends ScreenMeta
{
    public final int BLANK_SLOTS;
    public final int TOTAL_ROWS;

    public ScrollableScreenMeta(int width, int height, int totalSlots, Identifier texture, int textureWidth, int textureHeight)
    {
        super(width, height, totalSlots, texture, textureWidth, textureHeight);
        TOTAL_ROWS = MathHelper.ceil((double) totalSlots / width);
        BLANK_SLOTS = TOTAL_ROWS * width - totalSlots;
    }
}
