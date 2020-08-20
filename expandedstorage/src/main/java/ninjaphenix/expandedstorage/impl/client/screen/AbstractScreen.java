package ninjaphenix.expandedstorage.impl.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import java.util.function.Function;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import ninjaphenix.expandedstorage.impl.client.ContainerLibraryClient;
import ninjaphenix.expandedstorage.impl.inventory.AbstractScreenHandler;
import ninjaphenix.expandedstorage.impl.screen.ScreenMeta;

public abstract class AbstractScreen<T extends AbstractScreenHandler<R>, R extends ScreenMeta> extends HandledScreen<T>
{
    protected final R SCREEN_META;
    private final Integer INVENTORY_LABEL_LEFT;

    protected AbstractScreen(final T container, final PlayerInventory playerInventory, final Text title,
                             final Function<R, Integer> inventoryLabelLeftFunction)
    {
        super(container, playerInventory, title);
        SCREEN_META = container.SCREEN_META;
        INVENTORY_LABEL_LEFT = inventoryLabelLeftFunction.apply(SCREEN_META);
    }

    @Override
    @SuppressWarnings({"ConstantConditions", "deprecation"})
    protected void drawBackground(final MatrixStack matrices, final float delta, final int mouseX, final int mouseY)
    {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        client.getTextureManager().bindTexture(SCREEN_META.TEXTURE);
        drawTexture(matrices, x, y, 0, 0, backgroundWidth, backgroundHeight, SCREEN_META.TEXTURE_WIDTH, SCREEN_META.TEXTURE_HEIGHT);
    }

    @Override
    public void render(final MatrixStack matrices, final int mouseX, final int mouseY, final float delta)
    {
        renderBackground(matrices);
        super.render(matrices, mouseX, mouseY, delta);
        drawMouseoverTooltip(matrices, mouseX, mouseY);
    }

    @Override
    protected void drawForeground(final MatrixStack matrices, final int mouseX, final int mouseY)
    {
        textRenderer.draw(matrices, title, 8, 6, 4210752);
        textRenderer.draw(matrices, playerInventory.getDisplayName(), INVENTORY_LABEL_LEFT, backgroundHeight - 96 + 2, 4210752);
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    public boolean keyPressed(final int keyCode, final int scanCode, final int modifiers)
    {
        if (keyCode == 256 || client.options.keyInventory.matchesKey(keyCode, scanCode))
        {
            ContainerLibraryClient.sendCallbackRemoveToServer();
            client.player.closeHandledScreen();
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    protected static class Rectangle
    {
        public final int X, Y, WIDTH, HEIGHT, TEXTURE_X, TEXTURE_Y, TEXTURE_WIDTH, TEXTURE_HEIGHT;

        public Rectangle(final int x, final int y, final int width, final int height, final int textureX, final int textureY,
                         final int textureWidth, final int textureHeight)
        {
            X = x;
            Y = y;
            WIDTH = width;
            HEIGHT = height;
            TEXTURE_X = textureX;
            TEXTURE_Y = textureY;
            TEXTURE_WIDTH = textureWidth;
            TEXTURE_HEIGHT = textureHeight;
        }

        public void render(final MatrixStack matrices)
        {
            drawTexture(matrices, X, Y, TEXTURE_X, TEXTURE_Y, WIDTH, HEIGHT, TEXTURE_WIDTH, TEXTURE_HEIGHT);
        }
    }
}