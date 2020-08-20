package ninjaphenix.expandedstorage.impl.client.screen;

import java.util.HashMap;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.AbstractButtonWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import ninjaphenix.expandedstorage.impl.client.ContainerLibraryClient;
import ninjaphenix.expandedstorage.impl.client.ScreenMiscSettings;

public final class SelectContainerScreen extends Screen
{
    private final HashMap<Identifier, ScreenMiscSettings> OPTIONS;
    private final int PADDING = 24;
    private int TOP;

    public SelectContainerScreen(final HashMap<Identifier, ScreenMiscSettings> options)
    {
        super(new TranslatableText("screen.expandedstorage.screen_picker_title"));
        OPTIONS = options;
    }

    @Override
    public boolean isPauseScreen() { return false; }

    @Override
    protected void init()
    {
        super.init();
        final int choices = OPTIONS.size();
        final int maxColumns = Math.min(MathHelper.floorDiv(width - PADDING, 96 + PADDING), choices);
        final int totalRows = MathHelper.ceil((double) choices / maxColumns);
        int x = 0;
        int y = 0;
        int leftPadding = MathHelper.ceil((width - 96 * maxColumns - PADDING * (maxColumns - 1)) / 2D);
        final int topPadding = MathHelper.ceil((height - 96 * totalRows - PADDING * (totalRows - 1)) / 2D);
        TOP = topPadding;
        for (final HashMap.Entry<Identifier, ScreenMiscSettings> entry : OPTIONS.entrySet())
        {
            final Identifier id = entry.getKey();
            final ScreenMiscSettings settings = entry.getValue();
            addButton(new ScreenTypeButton(leftPadding + (PADDING + 96) * x, topPadding + (PADDING + 96) * y, 96, 96,
                                           settings.SELECT_TEXTURE_ID, settings.NARRATION_MESSAGE, button -> updatePlayerPreference(id),
                                           (button, matrices, tX, tY) -> renderTooltip(matrices, button.getMessage(), tX, tY)));
            x++;
            if (x == maxColumns)
            {
                x = 0;
                y++;
                if (y == totalRows - 1)
                {
                    final int remaining = choices - (maxColumns * (totalRows - 1));
                    leftPadding = MathHelper.ceil((width - 96 * remaining - PADDING * (remaining - 1)) / 2D);
                }
            }
        }
    }

    @Override
    public void onClose()
    {
        ContainerLibraryClient.sendCallbackRemoveToServer();
        super.onClose();
    }

    private void updatePlayerPreference(final Identifier selection)
    {
        ContainerLibraryClient.setPreference(selection);
        ContainerLibraryClient.sendPreferencesToServer();
        MinecraftClient.getInstance().openScreen(null);
    }

    @Override
    public void render(final MatrixStack matrices, final int mouseX, final int mouseY, final float delta)
    {
        setZOffset(0);
        renderBackground(matrices);
        for (final AbstractButtonWidget button : buttons) { button.render(matrices, mouseX, mouseY, delta); }
        for (final AbstractButtonWidget button : buttons)
        {
            if (button instanceof ScreenTypeButton) { ((ScreenTypeButton) button).renderTooltip(matrices, mouseX, mouseY); }
        }
        drawCenteredText(matrices, textRenderer, title, width / 2, Math.max(TOP - 2 * PADDING, 0), 0xFFFFFFFF);
    }

    private static class ScreenTypeButton extends ButtonWidget
    {
        private final Identifier TEXTURE;

        public ScreenTypeButton(final int x, final int y, final int width, final int height, final Identifier texture, final Text message,
                                final PressAction pressAction, final TooltipSupplier tooltipSupplier)
        {
            super(x, y, width, height, message, pressAction, tooltipSupplier);
            TEXTURE = texture;
        }

        @Override
        public void renderButton(final MatrixStack matrices, final int mouseX, final int mouseY, final float delta)
        {
            MinecraftClient.getInstance().getTextureManager().bindTexture(TEXTURE);
            drawTexture(matrices, x, y, 0, isHovered() ? height : 0, width, height, width, height * 2);
        }

        public void renderTooltip(final MatrixStack matrices, final int mouseX, final int mouseY)
        {
            if (active)
            {
                if (hovered) { renderToolTip(matrices, mouseX, mouseY); }
                else if (isHovered()) { renderToolTip(matrices, x, y); }
            }
        }
    }
}