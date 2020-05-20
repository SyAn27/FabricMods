package ninjaphenix.expandedstorage.api.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendereregistry.v1.BlockEntityRendererRegistry;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.item.BlockItem;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import ninjaphenix.chainmail.api.client.render.ChainmailRendering;
import ninjaphenix.expandedstorage.api.ExpandedStorageAPI;
import ninjaphenix.expandedstorage.api.Registries;
import ninjaphenix.expandedstorage.api.block.entity.CursedChestBlockEntity;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Random;
import java.util.function.Consumer;

@Environment(EnvType.CLIENT)
public final class ExpandedStorageAPIClient
{
    public static final Identifier CHEST_TEXTURE_ATLAS = new Identifier("expandedstorage", "chest_textures");
    private static final CursedChestBlockEntity CURSED_CHEST_RENDER_ENTITY = new CursedChestBlockEntity(null);

    public static void makeAtlas(@NonNull Consumer<SpriteIdentifier> consumer)
    {
        ExpandedStorageAPI.forEachPlugin(plugin ->
                plugin.appendTexturesToAtlas((identifier ->
                        consumer.accept(new SpriteIdentifier(CHEST_TEXTURE_ATLAS, identifier)))));
    }

    public static void onInitializeClient()
    {
        if (Registries.CHEST.getRandom(new Random()) != null)
        {
            BlockEntityRendererRegistry.INSTANCE.register(ExpandedStorageAPI.CHEST, CursedChestBlockEntityRenderer::new);
        }
        ChainmailRendering.INSTANCE.registerBlockEntityItemStackRenderer(ExpandedStorageAPI.CHEST, (itemStack, matrixStack, consumerProvider, light, overlay) ->
        {
            CURSED_CHEST_RENDER_ENTITY.setBlock(Registry.BLOCK.getId(((BlockItem) itemStack.getItem()).getBlock())); // maybe supply the block
            BlockEntityRenderDispatcher.INSTANCE.renderEntity(CURSED_CHEST_RENDER_ENTITY, matrixStack, consumerProvider, light, overlay);
        });
    }
}
