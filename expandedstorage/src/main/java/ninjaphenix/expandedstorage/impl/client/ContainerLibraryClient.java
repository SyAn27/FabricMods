package ninjaphenix.expandedstorage.impl.client;

import blue.endless.jankson.JsonPrimitive;
import io.netty.buffer.Unpooled;
import java.nio.file.Path;
import java.util.HashMap;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import ninjaphenix.chainmail.api.config.JanksonConfigParser;
import ninjaphenix.expandedstorage.impl.Const;
import ninjaphenix.expandedstorage.impl.ContainerLibrary;
import ninjaphenix.expandedstorage.impl.client.config.ContainerConfig;
import ninjaphenix.expandedstorage.impl.client.screen.SelectContainerScreen;
import org.apache.logging.log4j.MarkerManager;

public final class ContainerLibraryClient implements ClientModInitializer
{
    public static final ContainerConfig CONFIG = getConfigParser().load(ContainerConfig.class, ContainerConfig::new, getConfigPath(), new MarkerManager.Log4jMarker(Const.MOD_ID));
    public static final ContainerLibraryClient INSTANCE = new ContainerLibraryClient();

    private ContainerLibraryClient()
    {
        if (CONFIG.preferred_container_type.getNamespace().equals("ninjaphenix-container-lib"))
        {
            setPreference(new Identifier(Const.MOD_ID, CONFIG.preferred_container_type.getPath()));
        }
    }

    private static JanksonConfigParser getConfigParser()
    {
        return new JanksonConfigParser.Builder().deSerializer(
                JsonPrimitive.class, Identifier.class, (it, marshaller) -> new Identifier(it.asString()),
                ((identifier, marshaller) -> marshaller.serialize(identifier.toString()))).build();
    }

    private static Path getConfigPath() { return FabricLoader.getInstance().getConfigDir().resolve("ninjaphenix-container-library.json"); }

    public static void sendPreferencesToServer()
    {
        ClientSidePacketRegistry.INSTANCE.sendToServer(Const.SCREEN_SELECT, new PacketByteBuf(Unpooled.buffer())
                .writeIdentifier(CONFIG.preferred_container_type));
    }

    public static void sendCallbackRemoveToServer()
    {
        ClientSidePacketRegistry.INSTANCE.sendToServer(Const.SCREEN_SELECT, new PacketByteBuf(Unpooled.buffer())
                .writeIdentifier(Const.id("auto")));
    }

    public static void setPreference(final Identifier handlerType)
    {
        CONFIG.preferred_container_type = handlerType;
        getConfigParser().save(CONFIG, getConfigPath(), new MarkerManager.Log4jMarker(Const.MOD_ID));
    }

    @Override
    public void onInitializeClient()
    {
        ClientSidePacketRegistry.INSTANCE.register(Const.SCREEN_SELECT, (context, buffer) ->
        {
            final int count = buffer.readInt();
            final HashMap<Identifier, ScreenMiscSettings> allowed = new HashMap<>();
            for (int i = 0; i < count; i++)
            {
                final Identifier containerFactoryId = buffer.readIdentifier();
                if (ContainerLibrary.INSTANCE.isContainerTypeDeclared(containerFactoryId))
                {
                    allowed.put(containerFactoryId, ContainerLibrary.INSTANCE.getScreenSettings(containerFactoryId));
                }
            }
            MinecraftClient.getInstance().openScreen(new SelectContainerScreen(allowed));
        });
    }
}