package ninjaphenix.expandedstorage.impl.block.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.api.EnvironmentInterface;
import net.fabricmc.api.EnvironmentInterfaces;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.DoubleBlockProperties;
import net.minecraft.client.block.ChestAnimationProgress;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.Tickable;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.*;
import net.minecraft.world.World;
import ninjaphenix.expandedstorage.impl.block.BaseChestBlock;
import ninjaphenix.expandedstorage.impl.content.ModContent;
import ninjaphenix.expandedstorage.impl.inventory.AbstractContainer;
import ninjaphenix.expandedstorage.impl.inventory.DoubleSidedInventory;
import ninjaphenix.expandedstorage.api.Registries;
import ninjaphenix.expandedstorage.impl.block.CursedChestBlock;
import ninjaphenix.expandedstorage.impl.block.misc.CursedChestType;

import java.util.List;

@EnvironmentInterfaces({ @EnvironmentInterface(value = EnvType.CLIENT, itf = ChestAnimationProgress.class) })
public final class CursedChestBlockEntity extends AbstractChestBlockEntity implements ChestAnimationProgress, Tickable
{
    private float animationAngle, lastAnimationAngle;
    private int viewerCount, ticksOpen;

    public CursedChestBlockEntity(final Identifier block) { super(ModContent.CHEST, block); }

    private static int tickViewerCount(final World world, final CursedChestBlockEntity instance, final int ticksOpen, final int x,
                                       final int y, final int z, final int viewCount)
    {
        if (!world.isClient && viewCount != 0 && (ticksOpen + x + y + z) % 200 == 0)
        {
            return world.getNonSpectatingEntities(PlayerEntity.class, new Box(x - 5, y - 5, z - 5, x + 6, y + 6, z + 6)).stream()
                    .filter(player -> player.currentScreenHandler instanceof AbstractContainer)
                    .map(player -> ((AbstractContainer<?>) player.currentScreenHandler).getInventory())
                    .filter(inventory -> inventory == instance ||
                            inventory instanceof DoubleSidedInventory && ((DoubleSidedInventory) inventory).isPart(instance))
                    .mapToInt(inv -> 1).sum();
        }
        return viewCount;
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    protected void initialize(final Identifier block)
    {
        this.block = block;
        defaultContainerName = Registries.CHEST.get(block).getContainerName();
        inventorySize = Registries.CHEST.get(block).getSlotCount();
        inventory = DefaultedList.ofSize(inventorySize, ItemStack.EMPTY);
        SLOTS = new int[inventorySize];
        for (int i = 0; i < inventorySize; i++) { SLOTS[i] = i; }
    }

    @Override
    public boolean onSyncedBlockEvent(final int actionId, final int value)
    {
        if (actionId == 1)
        {
            viewerCount = value;
            return true;
        }
        else { return super.onSyncedBlockEvent(actionId, value); }
    }

    @Override
    public float getAnimationProgress(final float f) { return MathHelper.lerp(f, lastAnimationAngle, animationAngle); }

    @Override
    @SuppressWarnings("ConstantConditions")
    public void tick()
    {
        viewerCount = tickViewerCount(world, this, ++ticksOpen, pos.getX(), pos.getY(), pos.getZ(), viewerCount);
        lastAnimationAngle = animationAngle;
        if (viewerCount > 0 && animationAngle == 0.0F) { playSound(SoundEvents.BLOCK_CHEST_OPEN); }
        if (viewerCount == 0 && animationAngle > 0.0F || viewerCount > 0 && animationAngle < 1.0F)
        {
            animationAngle = MathHelper.clamp(animationAngle + (viewerCount > 0 ? 0.1F : -0.1F), 0, 1);
            if (animationAngle < 0.5F && lastAnimationAngle >= 0.5F) { playSound(SoundEvents.BLOCK_CHEST_CLOSE); }
        }
    }

    @SuppressWarnings("ConstantConditions")
    private void playSound(final SoundEvent soundEvent)
    {
        final BlockState state = getCachedState();
        if (BaseChestBlock.getMergeType(state) == DoubleBlockProperties.Type.SECOND) { return; }
        final Vec3i offset = BaseChestBlock.getDirectionToAttached(state).getVector();
        final Vec3d soundPos = Vec3d.ofCenter(pos).add(offset.getX() * 0.5D, offset.getY() * 0.5D, offset.getZ() * 0.5D);
        world.playSound(null, soundPos.getX(), soundPos.getY(), soundPos.getZ(), soundEvent, SoundCategory.BLOCKS, 0.5F,
                        world.random.nextFloat() * 0.1F + 0.9F);
    }

    @Override
    public void onOpen(final PlayerEntity player)
    {
        if (player.isSpectator()) { return; }
        if (viewerCount < 0) { viewerCount = 0; }
        viewerCount++;
        onInvOpenOrClose();
    }

    @Override
    public void onClose(final PlayerEntity player)
    {
        if (player.isSpectator()) { return; }
        viewerCount--;
        onInvOpenOrClose();
    }

    @SuppressWarnings("ConstantConditions")
    private void onInvOpenOrClose()
    {
        final Block block = getCachedState().getBlock();
        if (block instanceof CursedChestBlock)
        {
            world.addSyncedBlockEvent(pos, block, 1, viewerCount);
            world.updateNeighborsAlways(pos, block);
        }
    }
}
