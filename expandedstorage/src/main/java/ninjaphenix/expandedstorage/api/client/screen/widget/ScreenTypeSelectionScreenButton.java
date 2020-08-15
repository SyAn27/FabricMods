package ninjaphenix.expandedstorage.api.client.screen.widget;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import ninjaphenix.expandedstorage.impl.ExpandedStorage;
import ninjaphenix.expandedstorage.api.Constants;

public final class ScreenTypeSelectionScreenButton extends ButtonWidget
{
    private final Identifier TEXTURE;

    public ScreenTypeSelectionScreenButton(final int x, final int y, final TooltipSupplier tooltipSupplier)
    {
        super(x, y, 12, 12, new TranslatableText("screen.expandedstorage.change_screen_button"), button ->
        {
            MinecraftClient.getInstance().player.closeHandledScreen();
            ClientSidePacketRegistry.INSTANCE.sendToServer(Constants.OPEN_SCREEN_SELECT, new PacketByteBuf(Unpooled.buffer()));
        }, tooltipSupplier);
        TEXTURE = ExpandedStorage.id("textures/gui/select_screen_button.png");
    }

    @Override
    public void renderButton(final MatrixStack matrices, final int mouseX, final int mouseY, final float delta)
    {
        MinecraftClient.getInstance().getTextureManager().bindTexture(TEXTURE);
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, this.alpha);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.blendFunc(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA);
        drawTexture(matrices, x, y, 0, isHovered() ? height : 0, width, height, 16, 32);
        if (isHovered()) { renderToolTip(matrices, mouseX, mouseY); }
    }
}
