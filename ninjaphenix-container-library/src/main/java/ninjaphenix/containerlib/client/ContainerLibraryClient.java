package ninjaphenix.containerlib.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;
import ninjaphenix.chainmail.api.config.JanksonConfigParser;
import ninjaphenix.containerlib.api.ContainerLibraryAPI;
import ninjaphenix.containerlib.client.config.Config;
import ninjaphenix.containerlib.impl.client.ScreenMiscSettings;
import ninjaphenix.containerlib.impl.client.screen.SelectContainerScreen;
import org.apache.logging.log4j.MarkerManager;

import java.util.HashMap;

import static ninjaphenix.containerlib.api.Constants.LIBRARY_ID;
import static ninjaphenix.containerlib.api.Constants.SCREEN_SELECT;

public final class ContainerLibraryClient implements ClientModInitializer
{
    public static final ContainerLibraryClient INSTANCE = new ContainerLibraryClient();
    public static final Config CONFIG = new JanksonConfigParser.Builder().build().load(Config.class,
            FabricLoader.getInstance().getConfigDirectory().toPath().resolve("ninjaphenix-container-library.json"),
            new MarkerManager.Log4jMarker(LIBRARY_ID));

    private ContainerLibraryClient() {}

    @Override
    public void onInitializeClient()
    {
        ClientSidePacketRegistry.INSTANCE.register(SCREEN_SELECT, (context, buffer) ->
        {
            final int count = buffer.readInt();
            final HashMap<Identifier, ScreenMiscSettings> allowed = new HashMap<>();
            for (int i = 0; i < count; i++)
            {
                final Identifier containerFactoryId = buffer.readIdentifier();
                //if(ContainerProviderRegistry.INSTANCE.factoryExists(containerFactoryId))
                //{
                allowed.put(containerFactoryId, ContainerLibraryAPI.INSTANCE.getScreenSettings(containerFactoryId));
                //}
            }
            MinecraftClient.getInstance().openScreen(new SelectContainerScreen(allowed));
        });
    }
}
