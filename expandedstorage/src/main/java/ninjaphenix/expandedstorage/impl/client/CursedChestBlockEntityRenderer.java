package ninjaphenix.expandedstorage.impl.client;

import com.google.common.collect.ImmutableMap;
import net.minecraft.block.BlockState;
import net.minecraft.block.DoubleBlockProperties;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.TexturedRenderLayers;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.LightmapCoordinatesRetriever;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.state.property.Properties;
import ninjaphenix.expandedstorage.impl.Registries;
import ninjaphenix.expandedstorage.impl.block.CursedChestBlock;
import ninjaphenix.expandedstorage.impl.block.entity.CursedChestBlockEntity;
import ninjaphenix.expandedstorage.impl.block.misc.CursedChestType;
import ninjaphenix.expandedstorage.impl.client.models.*;
import ninjaphenix.expandedstorage.impl.content.ModContent;

public final class CursedChestBlockEntityRenderer extends BlockEntityRenderer<CursedChestBlockEntity>
{
    private static final BlockState defaultState = ModContent.DIAMOND_CHEST.getDefaultState();

    private static final ImmutableMap<CursedChestType, SingleChestModel> MODELS = new ImmutableMap.Builder<CursedChestType, SingleChestModel>()
            .put(CursedChestType.SINGLE, new SingleChestModel())
            .put(CursedChestType.FRONT, new FrontChestModel())
            .put(CursedChestType.BACK, new BackChestModel())
            .put(CursedChestType.TOP, new TopChestModel())
            .put(CursedChestType.BOTTOM, new BottomChestModel())
            .put(CursedChestType.LEFT, new LeftChestModel())
            .put(CursedChestType.RIGHT, new RightChestModel())
            .build();

    public CursedChestBlockEntityRenderer(final BlockEntityRenderDispatcher dispatcher) { super(dispatcher); }

    @Override
    @SuppressWarnings("ConstantConditions")
    public void render(final CursedChestBlockEntity blockEntity, final float tickDelta, final MatrixStack stack,
                       final VertexConsumerProvider vertexConsumerProvider, final int light, final int overlay)
    {
        final BlockState state = blockEntity.hasWorld() ? blockEntity.getCachedState() : defaultState;
        final CursedChestType chestType = state.get(CursedChestBlock.TYPE);
        final SingleChestModel model = getModel(chestType);
        stack.push();
        stack.translate(0.5D, 0.5D, 0.5D);
        stack.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(-state.get(Properties.HORIZONTAL_FACING).asRotation()));
        stack.translate(-0.5D, -0.5D, -0.5D);
        model.setLidPitch(blockEntity.getAnimationProgress(tickDelta));

        final DoubleBlockProperties.PropertySource<? extends CursedChestBlockEntity> wrapper = blockEntity.hasWorld() ?
                ((CursedChestBlock) state.getBlock()).combine(state, blockEntity.getWorld(), blockEntity.getPos(), true) :
                DoubleBlockProperties.PropertyRetriever::getFallback;
        model.render(stack, new SpriteIdentifier(TexturedRenderLayers.CHEST_ATLAS_TEXTURE,
                                                 Registries.CHEST.get(blockEntity.getBlock()).getChestTexture(chestType))
                             .getVertexConsumer(vertexConsumerProvider, RenderLayer::getEntityCutout),
                     wrapper.apply(new LightmapCoordinatesRetriever<>()).applyAsInt(light), overlay);
        stack.pop();
    }

    public SingleChestModel getModel(final CursedChestType type) { return MODELS.get(type); }
}