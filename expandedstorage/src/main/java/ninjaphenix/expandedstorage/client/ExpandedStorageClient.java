package ninjaphenix.expandedstorage.client;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import blue.endless.jankson.JsonPrimitive;
import io.netty.buffer.Unpooled;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendereregistry.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import net.fabricmc.fabric.api.event.client.ClientSpriteRegistryCallback;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.TexturedRenderLayers;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.minecraft.util.registry.Registry;
import ninjaphenix.chainmail.api.config.JanksonConfigParser;
import ninjaphenix.container_library.api.client.Config;
import ninjaphenix.expandedstorage.common.Const;
import ninjaphenix.expandedstorage.common.ExpandedStorage;
import ninjaphenix.expandedstorage.common.Registries;
import ninjaphenix.expandedstorage.common.block.entity.CursedChestBlockEntity;
import ninjaphenix.expandedstorage.common.misc.CursedChestType;
import ninjaphenix.expandedstorage.common.ModContent;
import org.apache.logging.log4j.MarkerManager;

import static ninjaphenix.expandedstorage.common.ModContent.*;

// todo: move into container library
public final class ExpandedStorageClient implements ClientModInitializer
{
    public static final ExpandedStorageClient INSTANCE = new ExpandedStorageClient();
    private static final CursedChestBlockEntity CURSED_CHEST_RENDER_ENTITY = new CursedChestBlockEntity(null);
    public static final Config CONFIG = getConfigParser().load(Config.class, Config::new, getConfigPath(), new MarkerManager.Log4jMarker(Const.MOD_ID));

    static
    {
        if (CONFIG.preferred_container_type.getNamespace().equals("ninjaphenix-container-lib"))
        {
            setPreference(new Identifier(Const.MOD_ID, CONFIG.preferred_container_type.getPath()));
        }
    }

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
                if (ExpandedStorage.INSTANCE.isContainerTypeDeclared(containerFactoryId))
                {
                    allowed.put(containerFactoryId, ExpandedStorage.INSTANCE.getScreenSettings(containerFactoryId));
                }
            }
            MinecraftClient.getInstance().openScreen(new SelectContainerScreen(allowed));
        });
        ClientSpriteRegistryCallback.event(TexturedRenderLayers.CHEST_ATLAS_TEXTURE).register(
                (atlas, registry) -> Registries.CHEST.stream().forEach(data -> Arrays.stream(CursedChestType.values())
                        .map(data::getChestTexture).forEach(registry::register)));
        BlockEntityRendererRegistry.INSTANCE.register(ModContent.CHEST, CursedChestBlockEntityRenderer::new);
        ModContent.CHEST.blocks.forEach(block -> BuiltinItemRendererRegistry.INSTANCE.register(
                block.asItem(), (stack, matrices, vertexConsumers, light, overlay) ->
                {
                    CURSED_CHEST_RENDER_ENTITY.setBlock(Registry.ITEM.getId(stack.getItem()));
                    BlockEntityRenderDispatcher.INSTANCE.renderEntity(CURSED_CHEST_RENDER_ENTITY, matrices, vertexConsumers, light, overlay);
                }));
        ScreenRegistry.register(SCROLLABLE_HANDLER_TYPE, ScrollableScreen::new);
        ScreenRegistry.register(PAGED_HANDLER_TYPE, PagedScreen::new);
        ScreenRegistry.register(SINGLE_HANDLER_TYPE, SingleScreen::new);
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
}