package ninjaphenix.expandedstorage.impl.client;

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
    private static final SingleChestModel singleChestModel = new SingleChestModel();
    private static final LeftChestModel leftChestModel = new LeftChestModel();
    private static final RightChestModel rightChestModel = new RightChestModel();
    private static final TopChestModel topChestModel = new TopChestModel();
    private static final BottomChestModel bottomChestModel = new BottomChestModel();
    private static final FrontChestModel frontChestModel = new FrontChestModel();
    private static final BackChestModel backChestModel = new BackChestModel();

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

    @Environment(EnvType.CLIENT)
    public SingleChestModel getModel(final CursedChestType type)
    {
       switch (type)
       {
           case SINGLE: return singleChestModel;
           case TOP: return topChestModel;
           case BACK: return backChestModel;
           case RIGHT: return rightChestModel;
           case BOTTOM: return bottomChestModel;
           case FRONT: return frontChestModel;
           case LEFT: return leftChestModel;
       }
       throw new RuntimeException("Unexpected enum value in CursedChestEntityRenderer#getModel");
    }
}