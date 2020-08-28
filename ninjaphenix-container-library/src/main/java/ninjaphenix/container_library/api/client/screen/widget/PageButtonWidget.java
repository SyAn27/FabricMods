package ninjaphenix.container_library.api.client.screen.widget;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import ninjaphenix.container_library.impl.common.Const;

public final class PageButtonWidget extends ButtonWidget
{
    private static final Identifier TEXTURE = Const.id("textures/gui/page_buttons.png");
    private final int TEXTURE_OFFSET;

    public PageButtonWidget(final int x, final int y, final int textureOffset, final Text text, final PressAction onPress,
                            final TooltipSupplier tooltipSupplier)
    {
        super(x, y, 12, 12, text, onPress, tooltipSupplier);
        TEXTURE_OFFSET = textureOffset;
    }

    public void setActive(final boolean active)
    {
        this.active = active;
        if (!active) { setFocused(false); }
    }

    @Override
    @SuppressWarnings("deprecation")
    public void renderButton(final MatrixStack matrices, final int mouseX, final int mouseY, final float delta)
    {
        final MinecraftClient minecraftClient = MinecraftClient.getInstance();
        minecraftClient.getTextureManager().bindTexture(TEXTURE);
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, alpha);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.blendFunc(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA);
        drawTexture(matrices, x, y, TEXTURE_OFFSET * 12, getYImage(isHovered()) * 12, width, height, 32, 48);
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