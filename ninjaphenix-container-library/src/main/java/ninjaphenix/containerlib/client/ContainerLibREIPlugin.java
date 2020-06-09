package ninjaphenix.containerlib.client;

import me.shedaniel.rei.api.BaseBoundsHandler;
import me.shedaniel.rei.api.DisplayHelper;
import me.shedaniel.rei.api.plugins.REIPluginV0;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.SemanticVersion;
import net.fabricmc.loader.util.version.VersionParsingException;
import net.minecraft.util.Identifier;
import ninjaphenix.containerlib.client.gui.screen.ingame.ScrollableScreen;

import java.util.ArrayList;

@Environment(EnvType.CLIENT)
public final class ContainerLibREIPlugin implements REIPluginV0
{
    @Override
    public SemanticVersion getMinimumVersion() throws VersionParsingException { return SemanticVersion.parse("3.0-pre"); }

    @Override
    public Identifier getPluginIdentifier() { return new Identifier("ninjaphenix-container-lib", "reiplugin"); }

    @Override
    public void registerBounds(DisplayHelper displayHelper)
    {
        BaseBoundsHandler.getInstance().registerExclusionZones(ScrollableScreen.class, () -> new ArrayList<>());
    }
}
