package ninjaphenix.containerlib.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.screen.ScreenProviderRegistry;
import net.fabricmc.loader.api.FabricLoader;
import ninjaphenix.chainmail.api.config.JanksonConfigParser;
import ninjaphenix.containerlib.ContainerLibrary;
import ninjaphenix.containerlib.client.config.Config;
import ninjaphenix.containerlib.client.gui.screen.ingame.ScrollableScreen;
import org.apache.logging.log4j.MarkerManager;

public final class ContainerLibraryClient implements ClientModInitializer
{
    public static final ContainerLibraryClient INSTANCE = new ContainerLibraryClient();
    private static final JanksonConfigParser PARSER = new JanksonConfigParser.Builder().build();
    public static final Config CONFIG = PARSER.load(Config.class,
            FabricLoader.getInstance().getConfigDirectory().toPath().resolve("ninjaphenix-container-library.json"),
            new MarkerManager.Log4jMarker("ninjaphenix-container-library"));

    private ContainerLibraryClient() {}

    @Override
    public void onInitializeClient()
    {
        ScreenProviderRegistry.INSTANCE.registerFactory(ContainerLibrary.CONTAINER_ID, ScrollableScreen::createScreen);
        System.out.println("AUTOFOCUS = " + CONFIG.auto_focus_searchbar);
    }
}
