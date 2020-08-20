package ninjaphenix.expandedstorage.common.inventory.screen;

import net.minecraft.util.Identifier;

public final class PagedScreenMeta extends ScreenMeta
{
    public final int BLANK_SLOTS, PAGES;

    public PagedScreenMeta(final int width, final int height, final int pages, final int totalSlots, final Identifier texture,
                           final int textureWidth, final int textureHeight)
    {
        super(width, height, totalSlots, texture, textureWidth, textureHeight);
        PAGES = pages;
        BLANK_SLOTS = pages * width * height - totalSlots;
    }
}