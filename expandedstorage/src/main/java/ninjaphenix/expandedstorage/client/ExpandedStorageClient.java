package ninjaphenix.expandedstorage.client;

import java.util.Arrays;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendereregistry.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.fabricmc.fabric.api.event.client.ClientSpriteRegistryCallback;
import net.minecraft.client.render.TexturedRenderLayers;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.util.registry.Registry;
import ninjaphenix.expandedstorage.Registries;
import ninjaphenix.expandedstorage.content.block.entity.CursedChestBlockEntity;
import ninjaphenix.expandedstorage.content.misc.CursedChestType;
import ninjaphenix.expandedstorage.content.ModContent;

public final class ExpandedStorageClient implements ClientModInitializer
{
    public static final ExpandedStorageClient INSTANCE = new ExpandedStorageClient();
    private static final CursedChestBlockEntity CURSED_CHEST_RENDER_ENTITY = new CursedChestBlockEntity(null);

    @Override
    public void onInitializeClient()
    {
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
        ModContent.registerScreenHandlerScreens();
    }
}