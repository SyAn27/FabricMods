package ninjaphenix.containerlib.impl.client.screen;

import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.MathHelper;
import ninjaphenix.containerlib.api.screen.ScrollableScreenMeta;
import ninjaphenix.containerlib.api.client.screen.AbstractScreen;
import ninjaphenix.containerlib.api.client.screen.widget.ScreenTypeSelectionScreenButton;
import ninjaphenix.containerlib.impl.client.ContainerLibraryClient;
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
        addButton(new ScreenTypeSelectionScreenButton(x + containerWidth -
                (hasScrollbar ? (ContainerLibraryClient.CONFIG.settings_button_center_on_scrollbar ? 2 : 1) : 19), y + 4));
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
            final int slotsHeight = SCREEN_META.HEIGHT * 18;
            final int scrollbarHeight = slotsHeight + (SCREEN_META.WIDTH > 9 ? 34 : 24);
            blit(x + containerWidth - 4, y, containerWidth, 0, 22, scrollbarHeight, SCREEN_META.TEXTURE_WIDTH, SCREEN_META.TEXTURE_HEIGHT);
            int yOffset = MathHelper.floor((slotsHeight - 17) * (((double) topRow) / (SCREEN_META.TOTAL_ROWS - SCREEN_META.HEIGHT)));
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
        int top = y + 18;
        int height = SCREEN_META.HEIGHT * 18;
        int newTopRow = MathHelper.floor(MathHelper.clampedLerp(0, SCREEN_META.TOTAL_ROWS - SCREEN_META.HEIGHT, (mouseY - top) / height));
        setTopRow(topRow, newTopRow);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta)
    {
        if (hasScrollbar && (!ContainerLibraryClient.CONFIG.restrictive_scrolling || isMouseOverScrollbar(mouseX, mouseY)))
        {
            int newTop;
            if (delta < 0)
            {
                newTop = Math.min(topRow + (hasShiftDown() ? SCREEN_META.HEIGHT : 1), SCREEN_META.TOTAL_ROWS - SCREEN_META.HEIGHT);
            }
            else
            {
                newTop = Math.max(topRow - (hasShiftDown() ? SCREEN_META.HEIGHT : 1), 0);
            }
            setTopRow(topRow, newTop);
            return true;
        }
        return super.mouseScrolled(mouseX, mouseY, delta);
    }

    private void setTopRow(final int oldTopRow, final int newTopRow)
    {
        if (oldTopRow == newTopRow) { return; }
        topRow = newTopRow;
        final int delta = newTopRow - oldTopRow;
        final int rows = Math.abs(delta);
        MinecraftClient.getInstance().player.sendChatMessage("Delta: " + delta);
        if (rows < SCREEN_META.HEIGHT)
        {
            if (delta > 0)
            {
                final int setOutBegin = oldTopRow * SCREEN_META.WIDTH;
                final int setAmount = rows * SCREEN_META.WIDTH;
                final int movableBegin = newTopRow * SCREEN_META.WIDTH;
                final int movableAmount = (SCREEN_META.HEIGHT - rows) * SCREEN_META.WIDTH;
                final int setInBegin = movableBegin + movableAmount;
                container.setSlotRange(setOutBegin, setOutBegin + setAmount, index -> -2000);
                container.moveSlotRange(movableBegin, movableBegin + movableAmount, -18 * rows);
                container.setSlotRange(setInBegin, setInBegin + setAmount,
                        index -> 18 * MathHelper.floorDiv(index - movableBegin + SCREEN_META.WIDTH, SCREEN_META.WIDTH));
            }
            else {
                // todo: code upwards scrolling
            }
        }
        else
        {
            final int oldMin = oldTopRow * SCREEN_META.WIDTH;
            container.setSlotRange(oldMin, oldMin + SCREEN_META.WIDTH * SCREEN_META.HEIGHT, index -> -2000);
            final int newMin = newTopRow * SCREEN_META.WIDTH;
            container.setSlotRange(newMin, newMin + SCREEN_META.WIDTH * SCREEN_META.HEIGHT,
                    index -> 18 + 18 * MathHelper.floorDiv(index - newMin, SCREEN_META.WIDTH));
        }
        // if no slots are still visible, move all off screen, move new on screen
        // if scrolled up/down by 1, move SCREEN_META.HEIGHT - 1, up/down and move remaining 1 off screen, move 1 row on screen.
    }

    @Override
    public void resize(MinecraftClient client, int width, int height)
    {
        if (hasScrollbar)
        {
            int row = topRow;
            super.resize(client, width, height);
            setTopRow(topRow, row);
        }
        else
        {
            super.resize(client, width, height);
        }
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


