package ninjaphenix.expandedstorage.impl.client;

import com.google.common.collect.ImmutableMap;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.state.property.Properties;
import ninjaphenix.expandedstorage.api.Registries;
import ninjaphenix.expandedstorage.impl.block.CursedChestBlock;
import ninjaphenix.expandedstorage.impl.block.entity.CursedChestBlockEntity;
import ninjaphenix.expandedstorage.impl.block.misc.CursedChestType;
import ninjaphenix.expandedstorage.impl.client.models.*;
import ninjaphenix.expandedstorage.impl.content.ModBlocks;

@Environment(EnvType.CLIENT)
public final class CursedChestBlockEntityRenderer extends BlockEntityRenderer<CursedChestBlockEntity>
{
    private static final BlockState defaultState = ModBlocks.wood_chest.getDefaultState();

    private static final ImmutableMap<CursedChestType, SingleChestModel> MODELS = new ImmutableMap.Builder<CursedChestType, SingleChestModel>().put(CursedChestType.SINGLE, new SingleChestModel()).put(CursedChestType.FRONT, new FrontChestModel()).put(CursedChestType.BACK, new BackChestModel()).put(CursedChestType.TOP, new TopChestModel()).put(CursedChestType.BOTTOM, new BottomChestModel()).put(CursedChestType.LEFT, new LeftChestModel()).put(CursedChestType.RIGHT, new RightChestModel()).build();

    public CursedChestBlockEntityRenderer(BlockEntityRenderDispatcher dispatcher) { super(dispatcher); }

    @Override
    public void render(CursedChestBlockEntity be, float tickDelta, MatrixStack stack, VertexConsumerProvider vcp, int x, int y)
    {
        final BlockState state = be.hasWorld() ? be.getCachedState() : defaultState;
        final CursedChestType chestType = state.get(CursedChestBlock.TYPE);
        final SingleChestModel model = getModel(chestType);
        stack.push();
        stack.translate(0.5D, 0.5D, 0.5D);
        stack.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(-state.get(Properties.HORIZONTAL_FACING).asRotation()));
        stack.translate(-0.5D, -0.5D, -0.5D);
        model.setLidPitch(be.getAnimationProgress(tickDelta));
        //noinspection ConstantConditions
        model.render(stack, new SpriteIdentifier(ExpandedStorageClient.CHEST_TEXTURE_ATLAS, Registries.CHEST.get(be.getBlock()).getChestTexture(chestType)).getVertexConsumer(vcp, RenderLayer::getEntityCutout), x, y);
        stack.pop();
    }

    public SingleChestModel getModel(final CursedChestType type) { return MODELS.get(type); }
}