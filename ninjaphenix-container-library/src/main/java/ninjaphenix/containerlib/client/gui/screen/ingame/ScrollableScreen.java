package ninjaphenix.containerlib.client.gui.screen.ingame;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.ScreenHandlerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import ninjaphenix.containerlib.client.gui.widget.SearchTextFieldWidget;
import ninjaphenix.containerlib.inventory.ScrollableScreenHandler;

import java.util.Optional;

@Environment(EnvType.CLIENT)
public class ScrollableScreen extends HandledScreen<ScrollableScreenHandler> implements ScreenHandlerProvider<ScrollableScreenHandler>
{
    private static final Identifier BASE_TEXTURE = new Identifier("textures/gui/container/generic_54.png");
    private static final Identifier WIDGETS_TEXTURE = new Identifier("ninjaphenix-container-lib", "textures/gui/container/widgets.png");
    private final int displayedRows;
    private final int totalRows;
    private int topRow;
    private double progress;
    private boolean dragging;
    private Optional<SearchTextFieldWidget> searchBox;
    private String searchBoxOldText;

    public ScrollableScreen(ScrollableScreenHandler container, PlayerInventory playerInventory, Text containerTitle)
    {
        super(container, playerInventory, containerTitle);
        totalRows = container.getRows();
        topRow = 0;
        displayedRows = hasScrollbar() ? 6 : totalRows;
        if (hasScrollbar() && !FabricLoader.getInstance().isModLoaded("roughlyenoughitems")) { backgroundWidth += 22; }
        backgroundHeight = 114 + displayedRows * 18;
        progress = 0;
        container.setSearchTerm("");
        searchBoxOldText = "";
    }

    @SuppressWarnings("ConstantConditions")
    public static ScrollableScreen createMenu(ScrollableScreenHandler container)
    {
        return new ScrollableScreen(container, MinecraftClient.getInstance().player.inventory, container.getDisplayName());
    }

    @Override
    public void init()
    {
        super.init();
        if (hasScrollbar())
        {
            searchBox = Optional.of(new SearchTextFieldWidget(textRenderer, x + 82, y + 127, 80, 8, new LiteralText("")));
            final SearchTextFieldWidget box = searchBox.get();
            box.setMaxLength(50);
            box.setHasBorder(false);
            box.setVisible(hasScrollbar());
            box.setEditableColor(16777215);
            box.setChangedListener(str ->
            {
                if (str.equals(searchBoxOldText)) { return; }
                handler.setSearchTerm(str);
                progress = 0;
                topRow = 0;
                searchBoxOldText = str;
            });
            this.children.add(box);
            this.setInitialFocus(box);
        }
        else { searchBox = Optional.empty(); }
    }

    @Override
    public void tick() { searchBox.ifPresent(SearchTextFieldWidget::tick); }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float delta)
    {
        renderBackground(matrixStack);
        drawBackground(matrixStack, delta, mouseX, mouseY);
        super.render(matrixStack, mouseX, mouseY, delta);
        drawMouseoverTooltip(matrixStack, mouseX, mouseY);
    }

    @Override
    protected void drawForeground(MatrixStack matrixStack, int mouseX, int mouseY)
    {
        textRenderer.draw(matrixStack, title, 8, 6, 4210752);
        textRenderer.draw(matrixStack, playerInventory.getDisplayName(), 8, backgroundHeight - 94, 4210752);
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    protected void drawBackground(MatrixStack matrixStack, float delta, int mouseX, int mouseY)
    {
        RenderSystem.color4f(1, 1, 1, 1);
        client.getTextureManager().bindTexture(BASE_TEXTURE);
        int left = (width - backgroundWidth) / 2;
        int top = (height - backgroundHeight) / 2;
        drawTexture(matrixStack, left, top, 0, 0, backgroundWidth, displayedRows * 18 + 17);
        drawTexture(matrixStack, left, top + displayedRows * 18 + 17, 0, 126, backgroundWidth, 96);
        if (hasScrollbar())
        {
            client.getTextureManager().bindTexture(WIDGETS_TEXTURE);
            drawTexture(matrixStack, left + 172, top, 0, 0, 22, 132);
            drawTexture(matrixStack, left + 174, (int) (top + 18 + 91 * progress), 22, 0, 12, 15);
            drawTexture(matrixStack, left + 79, top + 126, 34, 0, 90, 11);
        }
        searchBox.ifPresent(box -> box.render(matrixStack, mouseX, mouseY, delta));
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollDelta)
    {
        if (hasScrollbar())
        {
            setTopRow(topRow - (int) scrollDelta);
            progress = ((double) topRow) / ((double) (totalRows - 6));
            return true;
        }
        return false;
    }

    @Override
    protected boolean isClickOutsideBounds(double mouseX, double mouseY, int left, int top, int mouseButton)
    {
        final boolean left_up_down = mouseX < left || mouseY < top || mouseY > top + height;
        boolean right = mouseX > left + width;
        if (hasScrollbar()) { right = (right && mouseY > top + 132) || mouseX > left + width + 18; }
        return left_up_down || right;
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY)
    {
        if (!dragging) { return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY); }
        progress = MathHelper.clamp((mouseY - y - 25.5) / 90, 0, 1);
        setTopRow((int) (progress * (totalRows - 6)));
        return true;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button)
    {
        if (searchBox.isPresent())
        {
            final SearchTextFieldWidget box = searchBox.get();
            if (box.isFocused() && !box.mouseInBounds(mouseX, mouseY) && button == 0)
            {
                box.changeFocus(true);
                setFocused(null);
            }
        }
        if (button == 0 && x + 172 < mouseX && mouseX < x + 184 && y + 18 < mouseY && mouseY < y + 123)
        {
            dragging = true;
            return true;
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button)
    {
        if (dragging && button == 0) { dragging = false; }
        return super.mouseReleased(mouseX, mouseY, button);
    }

    private void setTopRow(int row)
    {
        topRow = MathHelper.clamp(row, 0, totalRows - 6);
        handler.updateSlotPositions(topRow, false);
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    public boolean keyPressed(int keyCode, int scanCode, int modifiers)
    {
        if (keyCode == 256)
        {
            client.player.closeHandledScreen();
            return true;
        }
        if (searchBox.isPresent())
        {
            final SearchTextFieldWidget box = searchBox.get();
            if (!box.isFocused())
            {
                if (client.options.keyChat.matchesKey(keyCode, scanCode))
                {
                    box.changeFocus(true);
                    setFocused(box);
                    box.ignoreNextChar();
                    return true;
                }
                return super.keyPressed(keyCode, scanCode, modifiers);
            }
            return box.keyPressed(keyCode, scanCode, modifiers);
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean charTyped(char character, int keyCode)
    {
        if (searchBox.isPresent())
        {
            final SearchTextFieldWidget box = searchBox.get();
            if (box.isFocused()) { return box.charTyped(character, keyCode); }
        }
        return super.charTyped(character, keyCode);
    }

    @Override
    public void resize(MinecraftClient client, int width, int height)
    {
        if (searchBox.isPresent())
        {
            final SearchTextFieldWidget box = searchBox.get();
            final String text = box.getText();
            final boolean focused = box.isFocused();
            super.resize(client, width, height);
            box.setText(text);
            if (focused)
            {
                box.changeFocus(true);
                setFocused(box);
            }
        }
        else { super.resize(client, width, height); }
    }

    public int getTop() { return y; }

    public int getLeft() { return x; }

    public boolean hasScrollbar() { return totalRows > 6; }
}

