package ninjaphenix.containerlib.client;

import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.BaseBoundsHandler;
import me.shedaniel.rei.api.DisplayHelper;
import me.shedaniel.rei.api.plugins.REIPluginV0;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;
import ninjaphenix.containerlib.client.gui.screen.ingame.ScrollableScreen;

import java.util.Collections;

@Environment(EnvType.CLIENT)
public final class ContainerLibREIPlugin implements REIPluginV0
{
    @Override
    public Identifier getPluginIdentifier() { return new Identifier("ninjaphenix-container-lib", "reiplugin"); }

    @Override
    @SuppressWarnings("ConstantConditions")
    public void registerBounds(DisplayHelper displayHelper)
    {
        BaseBoundsHandler.getInstance().registerExclusionZones(ScrollableScreen.class, () ->
        {
            final ScrollableScreen screen = (ScrollableScreen) MinecraftClient.getInstance().currentScreen;
            // todo: invert this.
            if (!screen.hasScrollbar()) { return Collections.emptyList(); }
            return Collections.singletonList(new Rectangle(screen.getLeft() + 172, screen.getTop(), 22, 132));
        });
    }
}
