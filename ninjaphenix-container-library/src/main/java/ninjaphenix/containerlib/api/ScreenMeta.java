package ninjaphenix.containerlib.api;

import net.minecraft.util.Identifier;

public class ScreenMeta
{
    public final int WIDTH;
    public final int HEIGHT;
    public final int TOTAL_SLOTS;
    public final Identifier TEXTURE;
    public final int TEXTURE_WIDTH;
    public final int TEXTURE_HEIGHT;

    private ScreenMeta(int width, int height, int totalSlots, Identifier texture, int textureWidth, int textureHeight)
    {
        WIDTH = width;
        HEIGHT = height;
        TOTAL_SLOTS = totalSlots;
        TEXTURE = texture;
        TEXTURE_WIDTH = textureWidth;
        TEXTURE_HEIGHT = textureHeight;
    }

    public static ScreenMeta of(int width, int height, int totalSlots, Identifier texture) { return of(width, height, totalSlots, texture, 256, 256); }

    public static ScreenMeta of(int width, int height, int totalSlots, Identifier texture, int textureWidth, int textureHeight)
    { return new ScreenMeta(width, height, totalSlots, texture, textureWidth, textureHeight); }
}