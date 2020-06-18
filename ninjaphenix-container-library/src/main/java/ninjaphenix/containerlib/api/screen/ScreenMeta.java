package ninjaphenix.containerlib.api.screen;

import net.minecraft.util.Identifier;

public abstract class ScreenMeta
{
    public final int WIDTH;
    public final int HEIGHT;
    public final int TOTAL_SLOTS;
    public final Identifier TEXTURE;
    public final int TEXTURE_WIDTH;
    public final int TEXTURE_HEIGHT;

    protected ScreenMeta(int width, int height, int totalSlots, Identifier texture, int textureWidth, int textureHeight)
    {
        WIDTH = width;
        HEIGHT = height;
        TOTAL_SLOTS = totalSlots;
        TEXTURE = texture;
        TEXTURE_WIDTH = textureWidth;
        TEXTURE_HEIGHT = textureHeight;
    }
}