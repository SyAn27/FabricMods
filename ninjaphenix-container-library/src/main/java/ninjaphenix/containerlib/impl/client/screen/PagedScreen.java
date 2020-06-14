package ninjaphenix.containerlib.impl.client.screen;


import ninjaphenix.containerlib.api.screen.PagedScreenMeta;
import ninjaphenix.containerlib.api.client.screen.AbstractScreen;
import ninjaphenix.containerlib.api.client.screen.widget.ScreenTypeSelectionScreenButton;
import ninjaphenix.containerlib.impl.inventory.PagedContainer;

public class PagedScreen<T extends PagedContainer> extends AbstractScreen<T, PagedScreenMeta>
{
    private Rectangle blankArea = null;

    public PagedScreen(T container)
    {
        super(container, (screenMeta) -> (screenMeta.WIDTH * 18 + 14) / 2 - 80);
        containerWidth = 14 + 18 * SCREEN_META.WIDTH;
        containerHeight = 17 + 97 + 18 * SCREEN_META.HEIGHT;
    }

    @Override
    protected void init()
    {
        super.init();
        addButton(new ScreenTypeSelectionScreenButton(0, 0));
        //final int totalSlots = container.slots.size() - 36;
        //final int diff = SCREEN_META.TOTAL_SLOTS - totalSlots;
        //if (diff > 0)
        //{
        //    final int xOffset = 7 + (SCREEN_META.WIDTH - diff) * 18;
        //    blankArea = new Rectangle(x + xOffset, y + containerHeight - 115, diff * 18, 18,
        //            xOffset, containerHeight, SCREEN_META.TEXTURE_WIDTH, SCREEN_META.TEXTURE_HEIGHT);
        //}
        /*
         * TODO:
         *  Create PageButton Widget.
         *   Should take a consumer for onPageChangeCallback
         *  On Page change, move slots up/down and if on last page, check if the blank area needs to be set
         *  (also needs to be set in init in case of resize / only 1 page).
         */

    }

    @Override
    protected void drawBackground(float delta, int mouseX, int mouseY)
    {
        super.drawBackground(delta, mouseX, mouseY);
        if (blankArea != null) { blankArea.render(); }
    }
}


