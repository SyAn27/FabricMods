package ninjaphenix.noncorrelatedextras.client.screen;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.text.LiteralText;

@Environment(EnvType.CLIENT)
public abstract class CustomSliderWidget extends SliderWidget
{
    public CustomSliderWidget(final int x, final int y, final int width, final int height, final double progress)
    {
        super(x, y, width, height, LiteralText.EMPTY, progress);
        this.updateMessage();
    }
}