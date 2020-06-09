package ninjaphenix.containerlib.impl.client.screen.widget;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.util.Identifier;

public class ScreenTypeButton extends ButtonWidget
{
    private final Identifier TEXTURE;

    public ScreenTypeButton(int x, int y, int width, int height, Identifier texture, String message, ButtonWidget.PressAction pressAction)
    {
        super(x, y, width, height, message, pressAction);
        TEXTURE = texture;
    }

    @Override
    public void renderButton(int mouseX, int mouseY, float delta)
    {
        MinecraftClient.getInstance().getTextureManager().bindTexture(TEXTURE);
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, this.alpha);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.blendFunc(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA);
        blit(x, y, 0, isHovered() ? height : 0, width, height, width, height * 2);
    }
}
