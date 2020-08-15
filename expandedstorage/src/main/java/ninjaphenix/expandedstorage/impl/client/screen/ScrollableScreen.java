package ninjaphenix.expandedstorage.impl.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;
import ninjaphenix.expandedstorage.impl.client.screen.widget.ScreenTypeSelectionScreenButton;
import ninjaphenix.expandedstorage.impl.screen.ScrollableScreenMeta;
import ninjaphenix.expandedstorage.impl.client.ContainerLibraryClient;
import ninjaphenix.expandedstorage.impl.inventory.ScrollableScreenHandler;

public final class ScrollableScreen<T extends ScrollableScreenHandler> extends AbstractScreen<T, ScrollableScreenMeta>
{
    protected final boolean hasScrollbar;
    private Rectangle blankArea = null;
    private boolean isDragging;
    private int topRow;
    private final int renderBackgroundWidth;
    public ScrollableScreen(T container)
    {
        super(container, (screenMeta) -> (screenMeta.WIDTH * 18 + 14) / 2 - 80);
        renderBackgroundWidth = 14 + 18 * SCREEN_META.WIDTH;
        hasScrollbar = SCREEN_META.TOTAL_ROWS != SCREEN_META.HEIGHT;
        backgroundWidth = renderBackgroundWidth + (hasScrollbar ? 18 : 0);
        backgroundHeight = 17 + 97 + 18 * SCREEN_META.HEIGHT;
    }

    @Override
    protected void init()
    {
        final FabricLoader instance = FabricLoader.getInstance();
        final boolean inventoryProfilesLoaded = instance.isModLoaded("inventoryprofiles");
        final boolean inventorySorterLoaded = instance.isModLoaded("inventorysorter");
        super.init();
        int settingsXOffset = -19;
        if (!hasScrollbar)
        {
            if (inventoryProfilesLoaded)
            {
                settingsXOffset -= 48;
            }
            else if (inventorySorterLoaded)
            {
                settingsXOffset -= 18;
            }
            final int blanked = SCREEN_META.BLANK_SLOTS;
            if (blanked > 0)
            {
                final int xOffset = 7 + (SCREEN_META.WIDTH - blanked) * 18;
                blankArea = new Rectangle(x + xOffset, y + backgroundHeight - 115, blanked * 18, 18,
                        xOffset, backgroundHeight, SCREEN_META.TEXTURE_WIDTH, SCREEN_META.TEXTURE_HEIGHT);
            }
        }
        else
        {
            settingsXOffset = ContainerLibraryClient.CONFIG.settings_button_center_on_scrollbar ? -2 : -1;
            isDragging = false;
            topRow = 0;
        }
        addButton(new ScreenTypeSelectionScreenButton(x + renderBackgroundWidth + settingsXOffset, y + 4,
                (button, matrices, mouseX, mouseY) -> renderTooltip(matrices, button.getMessage(), mouseX, mouseY)));
    }

    @Override
    protected void drawBackground(final MatrixStack matrices, final float delta, final int mouseX, final int mouseY)
    {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        //noinspection ConstantConditions
        client.getTextureManager().bindTexture(SCREEN_META.TEXTURE);
        drawTexture(matrices, x, y, 0, 0, renderBackgroundWidth, backgroundHeight, SCREEN_META.TEXTURE_WIDTH, SCREEN_META.TEXTURE_HEIGHT);
        if (hasScrollbar)
        {
            final int slotsHeight = SCREEN_META.HEIGHT * 18;
            final int scrollbarHeight = slotsHeight + (SCREEN_META.WIDTH > 9 ? 34 : 24);
            drawTexture(matrices, x + renderBackgroundWidth - 4, y, renderBackgroundWidth, 0, 22, scrollbarHeight, SCREEN_META.TEXTURE_WIDTH, SCREEN_META.TEXTURE_HEIGHT);
            int yOffset = MathHelper.floor((slotsHeight - 17) * (((double) topRow) / (SCREEN_META.TOTAL_ROWS - SCREEN_META.HEIGHT)));
            drawTexture(matrices, x + renderBackgroundWidth - 2, y + yOffset + 18, renderBackgroundWidth, scrollbarHeight, 12, 15, SCREEN_META.TEXTURE_WIDTH, SCREEN_META.TEXTURE_HEIGHT);
        }
        if (blankArea != null) { blankArea.render(matrices); }
    }

    private boolean isMouseOverScrollbar(double mouseX, double mouseY)
    {
        final int top = y + 18;
        final int left = x + renderBackgroundWidth - 2;
        return mouseX >= left && mouseY >= top && mouseX < left + 12 && mouseY < top + SCREEN_META.HEIGHT * 18;
    }

    @Override
    protected boolean isClickOutsideBounds(double mouseX, double mouseY, int left, int top, int button)
    {
        return super.isClickOutsideBounds(mouseX, mouseY, left, top, button) && !isMouseOverScrollbar(mouseX, mouseY);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers)
    {
        if (hasScrollbar)
        {
            if (keyCode == 264 || keyCode == 267) // Down Arrow, Page Down
            {
                if (topRow != SCREEN_META.TOTAL_ROWS - SCREEN_META.HEIGHT)
                {
                    if (hasShiftDown()) { setTopRow(topRow, Math.min(topRow + SCREEN_META.HEIGHT, SCREEN_META.TOTAL_ROWS - SCREEN_META.HEIGHT)); }
                    else {setTopRow(topRow, topRow + 1);}
                }
                return true;
            }
            else if (keyCode == 265 || keyCode == 266) // Up Arrow, Page Up
            {
                if (topRow != 0)
                {
                    if (hasShiftDown()) { setTopRow(topRow, Math.max(topRow - SCREEN_META.HEIGHT, 0)); }
                    else {setTopRow(topRow, topRow - 1);}
                }
                return true;
            }
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
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
        if (rows < SCREEN_META.HEIGHT)
        {
            final int setAmount = rows * SCREEN_META.WIDTH;
            final int movableAmount = (SCREEN_META.HEIGHT - rows) * SCREEN_META.WIDTH;
            if (delta > 0)
            {
                final int setOutBegin = oldTopRow * SCREEN_META.WIDTH;
                final int movableBegin = newTopRow * SCREEN_META.WIDTH;
                final int setInBegin = movableBegin + movableAmount;
                handler.setSlotRange(setOutBegin, setOutBegin + setAmount, index -> -2000);
                handler.moveSlotRange(movableBegin, setInBegin, -18 * rows);
                handler.setSlotRange(setInBegin, Math.min(setInBegin + setAmount, SCREEN_META.TOTAL_SLOTS),
                        index -> 18 * MathHelper.floorDiv(index - movableBegin + SCREEN_META.WIDTH, SCREEN_META.WIDTH));
            }
            else
            {
                final int setInBegin = newTopRow * SCREEN_META.WIDTH;
                final int movableBegin = oldTopRow * SCREEN_META.WIDTH;
                final int setOutBegin = movableBegin + movableAmount;
                handler.setSlotRange(setInBegin, setInBegin + setAmount,
                        index -> 18 * MathHelper.floorDiv(index - setInBegin + SCREEN_META.WIDTH, SCREEN_META.WIDTH));
                handler.moveSlotRange(movableBegin, setOutBegin, 18 * rows);
                handler.setSlotRange(setOutBegin, Math.min(setOutBegin + setAmount, SCREEN_META.TOTAL_SLOTS), index -> -2000);
            }
        }
        else
        {
            final int oldMin = oldTopRow * SCREEN_META.WIDTH;
            handler.setSlotRange(oldMin, Math.min(oldMin + SCREEN_META.WIDTH * SCREEN_META.HEIGHT, SCREEN_META.TOTAL_SLOTS), index -> -2000);
            final int newMin = newTopRow * SCREEN_META.WIDTH;
            handler.setSlotRange(newMin, newMin + SCREEN_META.WIDTH * SCREEN_META.HEIGHT,
                    index -> 18 + 18 * MathHelper.floorDiv(index - newMin, SCREEN_META.WIDTH));
        }

        if (newTopRow == SCREEN_META.TOTAL_ROWS - SCREEN_META.HEIGHT)
        {
            int blanked = SCREEN_META.BLANK_SLOTS;
            if (blanked > 0)
            {
                final int xOffset = 7 + (SCREEN_META.WIDTH - blanked) * 18;
                blankArea = new Rectangle(x + xOffset, y + backgroundHeight - 115, blanked * 18, 18,
                        xOffset, backgroundHeight, SCREEN_META.TEXTURE_WIDTH, SCREEN_META.TEXTURE_HEIGHT);
            }
        }
        else
        {
            blankArea = null;
        }
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


