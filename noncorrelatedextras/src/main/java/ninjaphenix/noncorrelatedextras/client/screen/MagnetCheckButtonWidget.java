package ninjaphenix.noncorrelatedextras.client.screen;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.AbstractPressableButtonWidget;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;

public abstract class MagnetCheckButtonWidget extends AbstractPressableButtonWidget
{
    private final Screen screen;
    private boolean selected;

    public MagnetCheckButtonWidget(int x, int y, int width, int height, boolean selected, Screen parent)
    {
        super(x, y, width, height, LiteralText.EMPTY);
        this.selected = selected;
        this.screen = parent;
    }

    @Override
    public void onPress()
    {
        selected = !selected;
        onValueChanged(selected);
    }

    @Override
    public void renderButton(final MatrixStack matrices, int mouseX, int mouseY, float delta)
    {
        super.renderButton(matrices, mouseX, mouseY, delta);
        MinecraftClient.getInstance().getTextureManager().bindTexture(SpriteAtlasTexture.BLOCK_ATLAS_TEX);
        ButtonModeProvider mode = selected ? MagnetModes.TELEPORT : MagnetModes.PULL;
        drawSprite(matrices, x + 2, y + 2, getZOffset() + 200, 16, 16, mode.getSprite());
        if (isMouseOver(mouseX, mouseY)) { screen.renderTooltip(matrices, mode.getText(), mouseX, mouseY); }
    }

    abstract void onValueChanged(boolean value);
}
