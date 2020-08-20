package ninjaphenix.expandedstorage.impl.item;

import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.Waterloggable;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.enums.ChestType;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.state.property.Properties;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.SimpleRegistry;
import net.minecraft.world.World;
import ninjaphenix.expandedstorage.impl.Registries;
import ninjaphenix.expandedstorage.impl.Const;
import ninjaphenix.expandedstorage.impl.block.BaseChestBlock;
import ninjaphenix.expandedstorage.impl.block.CursedChestBlock;
import ninjaphenix.expandedstorage.impl.block.entity.AbstractChestBlockEntity;
import ninjaphenix.expandedstorage.impl.block.misc.CursedChestType;

public final class ChestConversionItem extends ChestModifierItem
{
    private final Text TOOLTIP;
    private final Identifier FROM, TO;
    private static final MutableText DOUBLE_REQUIRES_2 = new TranslatableText("tooltip.expandedstorage.conversion_kit_double_requires_2")
            .formatted(Formatting.GRAY);

    public ChestConversionItem(final Item.Settings settings, final Pair<Identifier, String> from, final Pair<Identifier, String> to)
    {
        super(settings);
        FROM = from.getLeft();
        TO = to.getLeft();
        TOOLTIP = new TranslatableText(String.format("tooltip.expandedstorage.conversion_kit_%s_%s", from.getRight(), to.getRight()),
                                       Const.leftShiftRightClick).formatted(Formatting.GRAY);
    }

    @SuppressWarnings({"ConstantConditions", "unchecked"})
    private void upgradeCursedChest(final World world, final BlockPos pos, final BlockState state)
    {
        AbstractChestBlockEntity blockEntity = (AbstractChestBlockEntity) world.getBlockEntity(pos);
        final SimpleRegistry<Registries.TierData> registry = ((BaseChestBlock<AbstractChestBlockEntity>) state.getBlock()).getDataRegistry();
        final DefaultedList<ItemStack> inventoryData = DefaultedList.ofSize(registry.get(TO).getSlotCount(), ItemStack.EMPTY);
        Inventories.fromTag(blockEntity.toTag(new CompoundTag()), inventoryData);
        world.removeBlockEntity(pos);
        BlockState newState = Registry.BLOCK.get(registry.get(TO).getBlockId()).getDefaultState();
        if (newState.getBlock() instanceof Waterloggable)
        {
            newState = newState.with(Properties.WATERLOGGED, state.get(Properties.WATERLOGGED));
        }
        world.setBlockState(pos, newState.with(Properties.HORIZONTAL_FACING, state.get(Properties.HORIZONTAL_FACING))
                .with(CursedChestBlock.TYPE, state.get(CursedChestBlock.TYPE)));
        blockEntity = (AbstractChestBlockEntity) world.getBlockEntity(pos);
        blockEntity.fromTag(world.getBlockState(pos), Inventories.toTag(blockEntity.toTag(new CompoundTag()), inventoryData));
    }

    @SuppressWarnings({"ConstantConditions"})
    private void upgradeChest(final World world, final BlockPos pos, final BlockState state)
    {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        final DefaultedList<ItemStack> inventoryData = DefaultedList.ofSize(Registries.CHEST.get(FROM).getSlotCount(), ItemStack.EMPTY);
        Inventories.fromTag(blockEntity.toTag(new CompoundTag()), inventoryData);
        world.removeBlockEntity(pos);
        final BlockState newState = Registry.BLOCK.get(Registries.CHEST.get(TO).getBlockId()).getDefaultState();
        world.setBlockState(pos, newState.with(Properties.HORIZONTAL_FACING, state.get(Properties.HORIZONTAL_FACING))
                .with(Properties.WATERLOGGED, state.get(Properties.WATERLOGGED))
                .with(CursedChestBlock.TYPE, CursedChestType.valueOf(state.get(Properties.CHEST_TYPE))));
        blockEntity = world.getBlockEntity(pos);
        blockEntity.fromTag(world.getBlockState(pos), Inventories.toTag(blockEntity.toTag(new CompoundTag()), inventoryData));
    }

    @Override
    @SuppressWarnings({"ConstantConditions", "unchecked"})
    protected ActionResult useModifierOnChestBlock(final ItemUsageContext context, final BlockState mainState, final BlockPos mainBlockPos, final BlockState otherState,
                                                   final BlockPos otherBlockPos)
    {
        final World world = context.getWorld();
        final PlayerEntity player = context.getPlayer();
        final BaseChestBlock<AbstractChestBlockEntity> chestBlock = (BaseChestBlock<AbstractChestBlockEntity>) mainState.getBlock();
        if (Registry.BLOCK.getId(chestBlock) != chestBlock.getDataRegistry().get(FROM).getBlockId()) { return ActionResult.FAIL; }
        final ItemStack handStack = player.getStackInHand(context.getHand());
        if (otherBlockPos == null)
        {
            if (!world.isClient)
            {
                upgradeCursedChest(world, mainBlockPos, mainState);
                handStack.decrement(1);
            }
            return ActionResult.SUCCESS;
        }
        else if (handStack.getCount() > 1 || player.isCreative())
        {
            if (!world.isClient)
            {
                upgradeCursedChest(world, otherBlockPos, world.getBlockState(otherBlockPos));
                upgradeCursedChest(world, mainBlockPos, mainState);
                handStack.decrement(2);
            }
            return ActionResult.SUCCESS;
        }
        return ActionResult.FAIL;
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    protected ActionResult useModifierOnBlock(final ItemUsageContext context, final BlockState state)
    {
        // todo: fix this for other mods.
        //  Perhaps allow mods to define equivalents or use tags somehow e.g. Tag<Identifier>("expandedstorage:wood")
        if (state.getBlock() == Blocks.CHEST && FROM.equals(Const.id("wood_chest")))
        {
            final World world = context.getWorld();
            final BlockPos mainPos = context.getBlockPos();
            final PlayerEntity player = context.getPlayer();
            final ItemStack handStack = player.getStackInHand(context.getHand());
            if (state.get(Properties.CHEST_TYPE) == ChestType.SINGLE)
            {
                if (!world.isClient)
                {
                    upgradeChest(world, mainPos, state);
                    handStack.decrement(1);
                }
                return ActionResult.SUCCESS;
            }
            else if (handStack.getCount() > 1 || player.isCreative())
            {
                final BlockPos otherPos;
                if (state.get(Properties.CHEST_TYPE) == ChestType.RIGHT)
                {
                    otherPos = mainPos.offset(state.get(Properties.HORIZONTAL_FACING).rotateYCounterclockwise());
                }
                else if (state.get(Properties.CHEST_TYPE) == ChestType.LEFT)
                {
                    otherPos = mainPos.offset(state.get(Properties.HORIZONTAL_FACING).rotateYClockwise());
                }
                else { return ActionResult.FAIL; }
                if (!world.isClient)
                {
                    upgradeChest(world, otherPos, world.getBlockState(otherPos));
                    upgradeChest(world, mainPos, state);
                    handStack.decrement(2);
                }
                return ActionResult.SUCCESS;
            }
        }
        return ActionResult.FAIL;
    }

    @Override
    public void appendTooltip(final ItemStack stack, @Nullable final World world, final List<Text> tooltip, final TooltipContext context)
    {
        super.appendTooltip(stack, world, tooltip, context);
        tooltip.add(TOOLTIP);
        tooltip.add(DOUBLE_REQUIRES_2);
    }
}