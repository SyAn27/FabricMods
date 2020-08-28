package ninjaphenix.container_library.impl.client;

import blue.endless.jankson.JsonPrimitive;
import io.netty.buffer.Unpooled;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import ninjaphenix.chainmail.api.config.JanksonConfigParser;
import ninjaphenix.container_library.api.client.Config;
import ninjaphenix.container_library.impl.BuiltinScreenTypes;
import ninjaphenix.container_library.impl.client.screen.SelectContainerScreen;
import ninjaphenix.container_library.impl.common.Const;
import ninjaphenix.container_library.impl.common.ContainerLibrary;
import org.apache.logging.log4j.MarkerManager;
import java.nio.file.Path;
import java.util.HashMap;

public class ContainerLibraryClient implements ClientModInitializer
{
    public static final ContainerLibraryClient INSTANCE = new ContainerLibraryClient();
    public static final Config CONFIG = getConfigParser().load(Config.class, Config::new, getConfigPath(), new MarkerManager.Log4jMarker(Const.MOD_ID));

    static
    {
        final String namespace = CONFIG.preferred_container_type.getNamespace();
        if ("ninjaphenix-container-lib".equals(namespace) || "expandedstorage".equals(namespace))
        {
            setPreference(Const.id(CONFIG.preferred_container_type.getPath()));
        }
    }

    private ContainerLibraryClient() { }

    @Override
    public void onInitializeClient()
    {
        ClientSidePacketRegistry.INSTANCE.register(Const.SCREEN_SELECT, (context, buffer) ->
        {
            final int count = buffer.readInt();
            final HashMap<Identifier, Pair<Identifier, Text>> allowed = new HashMap<>();
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

        BuiltinScreenTypes.registerScreens();
    }

    private static JanksonConfigParser getConfigParser()
    {
        return new JanksonConfigParser.Builder().deSerializer(
                JsonPrimitive.class, Identifier.class, (it, marshaller) -> new Identifier(it.asString()),
                ((identifier, marshaller) -> marshaller.serialize(identifier.toString()))).build();
    }

    private static Path getConfigPath() { return FabricLoader.getInstance().getConfigDir().resolve(Const.MOD_ID+".json"); }

    // todo move all these methods to their own class so porting between mod loaders is easier
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
}
