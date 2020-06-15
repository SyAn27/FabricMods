package ninjaphenix.containerlib.impl.client.screen;


import net.minecraft.client.MinecraftClient;
import ninjaphenix.containerlib.api.screen.ScrollableScreenMeta;
import ninjaphenix.containerlib.api.client.screen.AbstractScreen;
import ninjaphenix.containerlib.api.client.screen.widget.ScreenTypeSelectionScreenButton;
import ninjaphenix.containerlib.impl.inventory.ScrollableContainer;

public class ScrollableScreen<T extends ScrollableContainer> extends AbstractScreen<T, ScrollableScreenMeta>
{
    private Rectangle blankArea = null;
    private final boolean hasScrollbar;
    private boolean isDragging;
    private int topRow;

    public ScrollableScreen(T container)
    {
        super(container, (screenMeta) -> (screenMeta.WIDTH * 18 + 14) / 2 - 80);
        containerWidth = 14 + 18 * SCREEN_META.WIDTH;
        containerHeight = 17 + 97 + 18 * SCREEN_META.HEIGHT;
        hasScrollbar = SCREEN_META.TOTAL_ROWS != SCREEN_META.HEIGHT;
    }

    @Override
    protected void init()
    {
        super.init();
        addButton(new ScreenTypeSelectionScreenButton(x + containerWidth - (hasScrollbar ? 1 : 19), y + 4));
        if (hasScrollbar)
        {
            isDragging = false;
            topRow = 0;
        }
    }

    @Override
    protected void drawBackground(float delta, int mouseX, int mouseY)
    {
        super.drawBackground(delta, mouseX, mouseY);
        if (hasScrollbar)
        {
            final int scrollbarHeight = SCREEN_META.HEIGHT * 18 + 24;
            blit(x + containerWidth - 4, y, containerWidth, 0, 22, scrollbarHeight, SCREEN_META.TEXTURE_WIDTH, SCREEN_META.TEXTURE_HEIGHT);
            int yOffset = (int) (((scrollbarHeight - 34) * (double) topRow) / (SCREEN_META.TOTAL_ROWS));
            blit(x + containerWidth - 2, y + yOffset + 18, containerWidth, scrollbarHeight, 12, 15, SCREEN_META.TEXTURE_WIDTH, SCREEN_META.TEXTURE_HEIGHT);
        }
        if (blankArea != null) { blankArea.render(); }
    }

    private boolean isMouseOverScrollbar(double mouseX, double mouseY)
    {
        final int top = y + 18;
        final int left = x + containerWidth - 2;
        return mouseX >= left && mouseY >= top && mouseX < left + 12 && mouseY < top + SCREEN_META.HEIGHT * 18;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button)
    {
        if (hasScrollbar && isMouseOverScrollbar(mouseX, mouseY) && button == 0)
        {
            isDragging = true;
            updateTopRow(mouseY);
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY)
    {
        if (hasScrollbar && isDragging)
        {
            updateTopRow(mouseY);
        }
        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

    private void updateTopRow(double mouseY)
    {
        MinecraftClient.getInstance().player.sendChatMessage(Double.toString(mouseY));
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button)
    {
        if (hasScrollbar && isDragging)
        {
            isDragging = false;
            return true;
        }
        return super.mouseReleased(mouseX, mouseY, button);
    }
}


