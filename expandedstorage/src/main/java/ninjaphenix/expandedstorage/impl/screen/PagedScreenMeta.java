package ninjaphenix.expandedstorage.impl.screen;

import net.minecraft.util.Identifier;

public final class PagedScreenMeta extends ScreenMeta
{
    public final int BLANK_SLOTS;
    public final int PAGES;

    public PagedScreenMeta(int width, int height, int pages, int totalSlots, Identifier texture, int textureWidth, int textureHeight)
    {
        super(width, height, totalSlots, texture, textureWidth, textureHeight);
        PAGES = pages;
        BLANK_SLOTS = pages * width * height - totalSlots;
    }
}
