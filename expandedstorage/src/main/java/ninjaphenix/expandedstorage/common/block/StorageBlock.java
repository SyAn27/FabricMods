package ninjaphenix.expandedstorage.common.block;

import javax.annotation.Nullable;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.InventoryProvider;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.PiglinBrain;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.SimpleRegistry;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import ninjaphenix.expandedstorage.common.ExpandedStorage;
import ninjaphenix.expandedstorage.common.Registries;
import ninjaphenix.expandedstorage.common.block.entity.StorageBlockEntity;

public abstract class StorageBlock extends BlockWithEntity implements InventoryProvider
{
    protected StorageBlock(final Settings settings) { super(settings); }

    protected abstract Identifier getOpenStat();

    public abstract <R extends Registries.TierData> SimpleRegistry<R> getDataRegistry();

    protected ExtendedScreenHandlerFactory createContainerFactory(final BlockState state, final WorldAccess world, final BlockPos pos)
    {
        final BlockEntity entity = world.getBlockEntity(pos);
        if(!(entity instanceof StorageBlockEntity)) { return null; }
        final StorageBlockEntity container = (StorageBlockEntity) entity;
        return new ExtendedScreenHandlerFactory()
        {
            @Override
            public void writeScreenOpeningData(final ServerPlayerEntity player, final PacketByteBuf buffer)
            {
                buffer.writeBlockPos(pos).writeInt(container.size());
            }

            @Override
            public Text getDisplayName()
            {
                return container.getDisplayName();
            }

            @Nullable
            @Override
            public ScreenHandler createMenu(final int syncId, final PlayerInventory inv, final PlayerEntity player)
            {
                if (container.canPlayerUse(player))
                {
                    container.checkLootInteraction(player);
                    return ExpandedStorage.INSTANCE.getScreenHandler(syncId, container.getPos(), container, player, getDisplayName());
                }
                return null;
            }
        };
    }

    @Override
    @SuppressWarnings("deprecation")
    public final boolean hasComparatorOutput(final BlockState state) { return true; }

    @Override
    public BlockRenderType getRenderType(final BlockState state) { return BlockRenderType.MODEL; }

    @Override
    @SuppressWarnings("deprecation")
    public void onStateReplaced(final BlockState state, final World world, final BlockPos pos, final BlockState newState,
                                final boolean moved)
    {
        if (state.getBlock() != newState.getBlock())
        {
            final BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof Inventory)
            {
                ItemScatterer.spawn(world, pos, (Inventory) blockEntity);
                world.updateNeighborsAlways(pos, this);
            }
            super.onStateReplaced(state, world, pos, newState, moved);
        }
    }

    @Nullable
    @Override
    public NamedScreenHandlerFactory createScreenHandlerFactory(final BlockState state, final World world, final BlockPos pos)
    {
        return null;
    }

    @Override
    public void onPlaced(final World world, final BlockPos pos, final BlockState state, @Nullable final LivingEntity placer,
                         final ItemStack stack)
    {
        if (stack.hasCustomName())
        {
            final BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof StorageBlockEntity)
            {
                ((StorageBlockEntity) blockEntity).setCustomName(stack.getName());
            }
        }
    }

    @Override
    @SuppressWarnings("deprecation")
    public int getComparatorOutput(final BlockState state, final World world, final BlockPos pos)
    {
        return ScreenHandler.calculateComparatorOutput(world.getBlockEntity(pos));
    }

    @Override
    @SuppressWarnings("deprecation")
    public ActionResult onUse(final BlockState state, final World world, final BlockPos pos, final PlayerEntity player, final Hand hand,
                              final BlockHitResult hit)
    {
        if (!world.isClient)
        {
            final ExtendedScreenHandlerFactory factory = createContainerFactory(state, world, pos);
            if (factory != null)
            {
                ExpandedStorage.INSTANCE.openContainer(player, factory);
                player.incrementStat(getOpenStat());
                PiglinBrain.onGuardedBlockInteracted(player, true);
            }
            return ActionResult.CONSUME;
        }
        return ActionResult.SUCCESS;
    }

    @Override
    public SidedInventory getInventory(final BlockState state, final WorldAccess world, final BlockPos pos)
    {
        final BlockEntity entity = world.getBlockEntity(pos);
        if (entity instanceof StorageBlockEntity) { return (StorageBlockEntity) entity; }
        return null;
    }


}