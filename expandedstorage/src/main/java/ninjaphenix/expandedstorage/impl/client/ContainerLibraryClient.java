package ninjaphenix.expandedstorage.impl.client;

import blue.endless.jankson.JsonPrimitive;
import io.netty.buffer.Unpooled;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.screen.ContainerScreenFactory;
import net.fabricmc.fabric.api.client.screen.ScreenProviderRegistry;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import ninjaphenix.chainmail.api.config.JanksonConfigParser;
import ninjaphenix.expandedstorage.impl.ExpandedStorage;
import ninjaphenix.expandedstorage.impl.ContainerLibrary;
import ninjaphenix.expandedstorage.impl.client.config.ContainerConfig;
import ninjaphenix.expandedstorage.impl.client.screen.PagedScreen;
import ninjaphenix.expandedstorage.impl.client.screen.ScrollableScreen;
import ninjaphenix.expandedstorage.impl.client.screen.SelectContainerScreen;
import ninjaphenix.expandedstorage.impl.client.screen.SingleScreen;
import ninjaphenix.expandedstorage.impl.inventory.PagedScreenHandler;
import ninjaphenix.expandedstorage.impl.inventory.ScrollableScreenHandler;
import ninjaphenix.expandedstorage.impl.inventory.SingleScreenHandler;
import org.apache.logging.log4j.MarkerManager;

import java.nio.file.Path;
import java.util.HashMap;

public final class ContainerLibraryClient implements ClientModInitializer
{
    public static final ContainerConfig CONFIG = getConfigParser().load(ContainerConfig.class, ContainerConfig::new, getConfigPath(), new MarkerManager.Log4jMarker(ExpandedStorage.MOD_ID));
    public static final ContainerLibraryClient INSTANCE = new ContainerLibraryClient();

    private ContainerLibraryClient()
    {
        if(CONFIG.preferred_container_type.getNamespace().equals("ninjaphenix-container-lib")) {
            setPreference(new Identifier(ExpandedStorage.MOD_ID, CONFIG.preferred_container_type.getPath()));
        }
    }

    private static JanksonConfigParser getConfigParser()
    {
        return new JanksonConfigParser.Builder()
                .deSerializer(JsonPrimitive.class, Identifier.class, (it, marshaller) -> new Identifier(it.asString()),
                        ((identifier, marshaller) -> marshaller.serialize(identifier.toString())))
                .build();
    }

    private static Path getConfigPath() { return FabricLoader.getInstance().getConfigDir().resolve("ninjaphenix-container-library.json"); }

    public static void sendPreferencesToServer()
    {
        PacketByteBuf buffer = new PacketByteBuf(Unpooled.buffer());
        buffer.writeIdentifier(CONFIG.preferred_container_type);
        ClientSidePacketRegistry.INSTANCE.sendToServer(ContainerLibrary.SCREEN_SELECT, buffer);
    }

    public static void sendCallbackRemoveToServer()
    {
        PacketByteBuf buffer = new PacketByteBuf(Unpooled.buffer());
        buffer.writeIdentifier(ExpandedStorage.id("auto"));
        ClientSidePacketRegistry.INSTANCE.sendToServer(ContainerLibrary.SCREEN_SELECT, buffer);
    }

    public static void setPreference(Identifier container_type)
    {
        CONFIG.preferred_container_type = container_type;
        getConfigParser().save(CONFIG, getConfigPath(), new MarkerManager.Log4jMarker(ExpandedStorage.MOD_ID));
    }

    @Override
    public void onInitializeClient()
    {
        ScreenProviderRegistry.INSTANCE.registerFactory(ContainerLibrary.SINGLE_CONTAINER, (ContainerScreenFactory<SingleScreenHandler>) SingleScreen::new);
        ScreenProviderRegistry.INSTANCE.registerFactory(ContainerLibrary.PAGED_CONTAINER, (ContainerScreenFactory<PagedScreenHandler>) PagedScreen::new);
        ScreenProviderRegistry.INSTANCE
                .registerFactory(ContainerLibrary.SCROLLABLE_CONTAINER, (ContainerScreenFactory<ScrollableScreenHandler>) ScrollableScreen::new);

        ClientSidePacketRegistry.INSTANCE.register(ContainerLibrary.SCREEN_SELECT, (context, buffer) ->
        {
            final int count = buffer.readInt();
            final HashMap<Identifier, ScreenMiscSettings> allowed = new HashMap<>();
            for (int i = 0; i < count; i++)
            {
                final Identifier containerFactoryId = buffer.readIdentifier();
                if (ContainerLibrary.INSTANCE.isContainerTypeDeclared(containerFactoryId) /*&&
                        ContainerProviderRegistry.INSTANCE.factoryExists(containerFactoryId)*/)
                {
                    allowed.put(containerFactoryId, ContainerLibrary.INSTANCE.getScreenSettings(containerFactoryId));
                }
            }
            MinecraftClient.getInstance().openScreen(new SelectContainerScreen(allowed));
        });
    }
}
