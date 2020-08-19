package ninjaphenix.expandedstorage.impl.client;

import java.util.function.Consumer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendereregistry.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.fabricmc.fabric.api.event.client.ClientSpriteRegistryCallback;
import net.minecraft.client.render.TexturedRenderLayers;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.SimpleRegistry;
import ninjaphenix.expandedstorage.api.Registries;
import ninjaphenix.expandedstorage.impl.Const;
import ninjaphenix.expandedstorage.impl.block.entity.CursedChestBlockEntity;
import ninjaphenix.expandedstorage.impl.block.misc.CursedChestType;
import ninjaphenix.expandedstorage.impl.content.ModContent;

public final class ExpandedStorageClient implements ClientModInitializer
{
    public static final ExpandedStorageClient INSTANCE = new ExpandedStorageClient();
    private static final CursedChestBlockEntity CURSED_CHEST_RENDER_ENTITY = new CursedChestBlockEntity(null);

    private static void iterateOurTiers(Consumer<Registries.ChestTierData> consumer)
    {
        Registries.CHEST.getIds().stream().filter(id -> id.getNamespace().equals(Const.MOD_ID)).forEach(
                id -> consumer.accept(Registries.CHEST.get(id)));
    }

    @Override
    public void onInitializeClient()
    {
        ClientSpriteRegistryCallback.event(TexturedRenderLayers.CHEST_ATLAS_TEXTURE).register(
                (atlas, registry) -> iterateOurTiers((data) ->
                {
                    for (final CursedChestType type : CursedChestType.values())
                    {
                        registry.register(data.getChestTexture(type));
                    }
                })
        );
        BlockEntityRendererRegistry.INSTANCE.register(ModContent.CHEST, CursedChestBlockEntityRenderer::new);
        ModContent.CHEST.blocks.forEach(block -> BuiltinItemRendererRegistry.INSTANCE.register(
                block.asItem(), (stack, matrices, vertexConsumers, light, overlay) ->
                {
                    CURSED_CHEST_RENDER_ENTITY.setBlock(Registry.ITEM.getId(stack.getItem()));
                    BlockEntityRenderDispatcher.INSTANCE.renderEntity(CURSED_CHEST_RENDER_ENTITY, matrices, vertexConsumers, light,
                                                                      overlay);
                }));
        ModContent.registerScreenHandlerScreens();
    }
}
