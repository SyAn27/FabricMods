package ninjaphenix.expandedstorage.common.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.Vec3i;
import ninjaphenix.expandedstorage.common.ModContent;
import ninjaphenix.expandedstorage.common.Registries;
import ninjaphenix.expandedstorage.common.block.BarrelBlock;

public class BarrelBlockEntity extends StorageBlockEntity
{
    private int viewerCount;

    public BarrelBlockEntity(final Identifier block) { super(ModContent.BARREL, block); }

    @Override
    @SuppressWarnings("ConstantConditions")
    protected void initialize(final Identifier block)
    {
        this.block = block;
        defaultContainerName = Registries.BARREL.get(block).getContainerName();
        inventorySize = Registries.BARREL.get(block).getSlotCount();
        inventory = DefaultedList.ofSize(inventorySize, ItemStack.EMPTY);
        SLOTS = new int[inventorySize];
        for (int i = 0; i < inventorySize; i++) { SLOTS[i] = i; }
    }

    public void onOpen(final PlayerEntity player)
    {
        if (!player.isSpectator())
        {
            if (viewerCount < 0) { viewerCount = 0; }
            ++viewerCount;
            final BlockState state = getCachedState();
            if (!state.get(Properties.OPEN))
            {
                playSound(state, SoundEvents.BLOCK_BARREL_OPEN);
                setOpen(state, true);
            }
            scheduleBlockUpdate();
        }
    }

    private void scheduleBlockUpdate() { world.getBlockTickScheduler().schedule(getPos(), getCachedState().getBlock(), 5); }

    public void tick()
    {
        viewerCount = CursedChestBlockEntity.countViewers(world, this, pos.getX(), pos.getY(), pos.getZ());
        if (viewerCount > 0) { scheduleBlockUpdate(); }
        else
        {
            final BlockState state = getCachedState();
            if (!(state.getBlock() instanceof BarrelBlock))
            {
                markRemoved();
                return;
            }
            if (state.get(Properties.OPEN))
            {
                playSound(state, SoundEvents.BLOCK_BARREL_CLOSE);
                setOpen(state, false);
            }
        }
    }

    public void onClose(final PlayerEntity player)
    {
        if (!player.isSpectator()) { --viewerCount; }
    }

    private void setOpen(final BlockState state, final boolean open)
    {
        world.setBlockState(getPos(), state.with(Properties.OPEN, open), 3);
    }

    private void playSound(final BlockState state, final SoundEvent sound)
    {
        final Vec3i facingVector = state.get(Properties.FACING).getVector();
        final double x = pos.getX() + 0.5D + facingVector.getX() / 2.0D;
        final double y = pos.getY() + 0.5D + facingVector.getY() / 2.0D;
        final double z = pos.getZ() + 0.5D + facingVector.getZ() / 2.0D;
        world.playSound(null, x, y, z, sound, SoundCategory.BLOCKS, 0.5F, world.random.nextFloat() * 0.1F + 0.9F);
    }
}