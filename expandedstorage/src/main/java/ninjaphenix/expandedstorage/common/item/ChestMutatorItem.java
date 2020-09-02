package ninjaphenix.expandedstorage.common.item;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.enums.ChestType;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import ninjaphenix.expandedstorage.common.Const;
import ninjaphenix.expandedstorage.common.Registries;
import ninjaphenix.expandedstorage.common.block.ChestBlock;
import ninjaphenix.expandedstorage.common.misc.CursedChestType;

import java.util.List;

import static net.minecraft.state.property.Properties.WATERLOGGED;
import static net.minecraft.util.BlockRotation.CLOCKWISE_180;
import static net.minecraft.util.BlockRotation.CLOCKWISE_90;

@SuppressWarnings("ConstantConditions")
public final class ChestMutatorItem extends ChestModifierItem
{
    private static final EnumProperty<CursedChestType> TYPE = ChestBlock.TYPE;

    public ChestMutatorItem(final Settings settings) { super(settings); }

    @Override
    protected ActionResult useModifierOnChestBlock(final ItemUsageContext context, final BlockState mainState, final BlockPos mainBlockPos,
                                                   final BlockState otherState, final BlockPos otherBlockPos)
    {
        final PlayerEntity player = context.getPlayer();
        final World world = context.getWorld();
        final ItemStack stack = context.getStack();
        switch (getMode(stack))
        {
            case MERGE:
                final CompoundTag tag = stack.getOrCreateTag();
                if (tag.contains("pos"))
                {
                    if (mainState.get(TYPE) == CursedChestType.SINGLE)
                    {
                        final BlockPos pos = NbtHelper.toBlockPos(tag.getCompound("pos"));
                        final BlockState realOtherState = world.getBlockState(pos);
                        if (realOtherState.getBlock() == mainState.getBlock() && realOtherState.get(Properties.HORIZONTAL_FACING) == mainState.get(Properties.HORIZONTAL_FACING) && realOtherState.get(TYPE) == CursedChestType.SINGLE)
                        {
                            if (!world.isClient)
                            {
                                final BlockPos vec = pos.subtract(mainBlockPos);
                                final int sum = vec.getX() + vec.getY() + vec.getZ();
                                if (sum == 1 || sum == -1)
                                {
                                    final CursedChestType mainChestType = ChestBlock.getChestType(mainState.get(Properties.HORIZONTAL_FACING), Direction.fromVector(vec.getX(), vec.getY(), vec.getZ()));
                                    world.setBlockState(mainBlockPos, mainState.with(TYPE, mainChestType));
                                    world.setBlockState(pos, world.getBlockState(pos).with(TYPE, mainChestType.getOpposite()));
                                    tag.remove("pos");
                                    player.sendMessage(new TranslatableText("tooltip.expandedstorage.chest_mutator.merge_end"), true);
                                    player.getItemCooldownManager().set(this, 5);
                                    return ActionResult.SUCCESS;
                                }

                            }
                        }
                        return ActionResult.FAIL;
                    }
                }
                else
                {
                    if (mainState.get(TYPE) == CursedChestType.SINGLE)
                    {
                        tag.put("pos", NbtHelper.fromBlockPos(mainBlockPos));
                        player.sendMessage(new TranslatableText("tooltip.expandedstorage.chest_mutator.merge_start"), true);
                        player.getItemCooldownManager().set(this, 5);
                        return ActionResult.SUCCESS;
                    }

                }
                break;
            case UNMERGE:
                if (otherState != null)
                {
                    if (!world.isClient)
                    {
                        world.setBlockState(mainBlockPos, world.getBlockState(mainBlockPos).with(TYPE, CursedChestType.SINGLE));
                        world.setBlockState(otherBlockPos, world.getBlockState(otherBlockPos).with(TYPE, CursedChestType.SINGLE));
                    }
                    player.getItemCooldownManager().set(this, 5);
                    return ActionResult.SUCCESS;
                }
                break;
            case ROTATE:
                switch (mainState.get(ChestBlock.TYPE))
                {
                    case SINGLE:
                        if (!world.isClient) { world.setBlockState(mainBlockPos, mainState.rotate(CLOCKWISE_90)); }
                        player.getItemCooldownManager().set(this, 5);
                        return ActionResult.SUCCESS;
                    case FRONT:
                    case BACK:
                    case LEFT:
                    case RIGHT:
                        if (!world.isClient)
                        {
                            world.setBlockState(mainBlockPos, mainState.rotate(CLOCKWISE_180).with(TYPE, mainState.get(TYPE).getOpposite()));
                            world.setBlockState(otherBlockPos, otherState.rotate(CLOCKWISE_180).with(TYPE, otherState.get(TYPE).getOpposite()));
                        }
                        player.getItemCooldownManager().set(this, 5);
                        return ActionResult.SUCCESS;
                    case TOP:
                    case BOTTOM:
                        if (!world.isClient)
                        {
                            world.setBlockState(mainBlockPos, mainState.rotate(CLOCKWISE_90));
                            world.setBlockState(otherBlockPos, otherState.rotate(CLOCKWISE_90));
                        }
                        player.getItemCooldownManager().set(this, 5);
                        return ActionResult.SUCCESS;
                }
        }
        return ActionResult.FAIL;
    }

    @Override
    protected ActionResult useModifierOnBlock(final ItemUsageContext context, final BlockState state)
    {
        final PlayerEntity player = context.getPlayer();
        final ItemStack stack = context.getStack();
        final World world = context.getWorld();
        final BlockPos mainPos = context.getBlockPos();
        final MutatorMode mode = getMode(stack);
        if (state.getBlock() instanceof net.minecraft.block.ChestBlock)
        {
            if (mode == MutatorMode.MERGE)
            {
                final CompoundTag tag = stack.getOrCreateTag();
                if (tag.contains("pos"))
                {
                    if (state.get(net.minecraft.block.ChestBlock.CHEST_TYPE) == ChestType.SINGLE)
                    {
                        final BlockPos otherPos = NbtHelper.toBlockPos(tag.getCompound("pos"));
                        final BlockState realOtherState = world.getBlockState(otherPos);
                        if (realOtherState.getBlock() == state.getBlock() && realOtherState.get(Properties.HORIZONTAL_FACING) == state.get(Properties.HORIZONTAL_FACING) && realOtherState.get(net.minecraft.block.ChestBlock.CHEST_TYPE) == ChestType.SINGLE)
                        {
                            final BlockPos vec = otherPos.subtract(mainPos);
                            final int sum = vec.getX() + vec.getY() + vec.getZ();
                            if (sum == 1 || sum == -1)
                            {
                                if (!world.isClient)
                                {
                                    final Registries.TierData entry = Registries.CHEST.get(Const.id("wood_chest"));
                                    final BlockState defState = Registry.BLOCK.get(entry.getBlockId()).getDefaultState().with(Properties.HORIZONTAL_FACING, state.get(Properties.HORIZONTAL_FACING));
                                    final CursedChestType mainChestType = ChestBlock.getChestType(state.get(Properties.HORIZONTAL_FACING), Direction.fromVector(vec.getX(), vec.getY(), vec.getZ()));
                                    // todo: refactor into method.
                                    BlockEntity blockEntity = world.getBlockEntity(mainPos);
                                    DefaultedList<ItemStack> invData = DefaultedList.ofSize(entry.getSlotCount(), ItemStack.EMPTY);
                                    Inventories.fromTag(blockEntity.toTag(new CompoundTag()), invData);
                                    world.removeBlockEntity(mainPos);
                                    world.setBlockState(mainPos, defState.with(WATERLOGGED, state.get(WATERLOGGED)).with(TYPE, mainChestType));
                                    blockEntity = world.getBlockEntity(mainPos);
                                    blockEntity.fromTag(world.getBlockState(mainPos), Inventories.toTag(blockEntity.toTag(new CompoundTag()), invData));

                                    blockEntity = world.getBlockEntity(otherPos);
                                    invData = DefaultedList.ofSize(entry.getSlotCount(), ItemStack.EMPTY);
                                    Inventories.fromTag(blockEntity.toTag(new CompoundTag()), invData);
                                    world.removeBlockEntity(otherPos);
                                    world.setBlockState(otherPos, defState.with(WATERLOGGED, state.get(WATERLOGGED)).with(TYPE, mainChestType.getOpposite()));
                                    blockEntity = world.getBlockEntity(otherPos);
                                    blockEntity.fromTag(world.getBlockState(otherPos), Inventories.toTag(blockEntity.toTag(new CompoundTag()), invData));

                                    tag.remove("pos");
                                    player.sendMessage(new TranslatableText("tooltip.expandedstorage.chest_mutator.merge_end"), true);
                                    player.getItemCooldownManager().set(this, 5);
                                }
                                return ActionResult.SUCCESS;
                            }
                        }
                        return ActionResult.FAIL;
                    }
                }
                else
                {
                    if (state.get(net.minecraft.block.ChestBlock.CHEST_TYPE) == ChestType.SINGLE)
                    {
                        tag.put("pos", NbtHelper.fromBlockPos(mainPos));
                        player.sendMessage(new TranslatableText("tooltip.expandedstorage.chest_mutator.merge_start"), true);
                        player.getItemCooldownManager().set(this, 5);
                        return ActionResult.SUCCESS;
                    }

                }
            }
            else if (mode == MutatorMode.UNMERGE)
            {
                final BlockPos otherPos;
                switch (state.get(net.minecraft.block.ChestBlock.CHEST_TYPE))
                {
                    case LEFT: otherPos = mainPos.offset(state.get(net.minecraft.block.ChestBlock.FACING).rotateYClockwise());
                        break;
                    case RIGHT: otherPos = mainPos.offset(state.get(net.minecraft.block.ChestBlock.FACING).rotateYCounterclockwise());
                        break;
                    default:
                        return ActionResult.FAIL;
                }
                if (!world.isClient)
                {
                    world.setBlockState(mainPos, state.with(net.minecraft.block.ChestBlock.CHEST_TYPE, ChestType.SINGLE));
                    world.setBlockState(otherPos, world.getBlockState(otherPos).with(net.minecraft.block.ChestBlock.CHEST_TYPE, ChestType.SINGLE));
                }
                player.getItemCooldownManager().set(this, 5);
                return ActionResult.SUCCESS;
            }
            else if (mode == MutatorMode.ROTATE)
            {
                final BlockPos otherPos;
                switch (state.get(net.minecraft.block.ChestBlock.CHEST_TYPE))
                {
                    case LEFT: otherPos = mainPos.offset(state.get(net.minecraft.block.ChestBlock.FACING).rotateYClockwise());
                        break;
                    case RIGHT: otherPos = mainPos.offset(state.get(net.minecraft.block.ChestBlock.FACING).rotateYCounterclockwise());
                        break;
                    case SINGLE:
                        if (!world.isClient) { world.setBlockState(mainPos, state.rotate(CLOCKWISE_90)); }
                        player.getItemCooldownManager().set(this, 5);
                        return ActionResult.SUCCESS;
                    default:
                        return ActionResult.FAIL;
                }
                if (!world.isClient)
                {
                    final BlockState otherState = world.getBlockState(otherPos);
                    world.setBlockState(mainPos, state.rotate(CLOCKWISE_180).with(net.minecraft.block.ChestBlock.CHEST_TYPE, state.get(net.minecraft.block.ChestBlock.CHEST_TYPE).getOpposite()));
                    world.setBlockState(otherPos, otherState.rotate(CLOCKWISE_180).with(net.minecraft.block.ChestBlock.CHEST_TYPE, otherState.get(net.minecraft.block.ChestBlock.CHEST_TYPE).getOpposite()));
                }
                player.getItemCooldownManager().set(this, 5);
                return ActionResult.SUCCESS;
            }
        }
        else if (state.getBlock() == Blocks.ENDER_CHEST)
        {
            if (mode == MutatorMode.ROTATE)
            {
                if (!world.isClient) { world.setBlockState(mainPos, state.rotate(CLOCKWISE_90)); }
                player.getItemCooldownManager().set(this, 5);
                return ActionResult.SUCCESS;
            }
            return ActionResult.FAIL;
        }
        return super.useModifierOnBlock(context, state);
    }

    @Override
    protected TypedActionResult<ItemStack> useModifierInAir(final World world, final PlayerEntity player, final Hand hand)
    {
        if (player.isSneaking())
        {
            final ItemStack stack = player.getStackInHand(hand);
            final CompoundTag tag = stack.getOrCreateTag();
            tag.putByte("mode", getMode(stack).next);
            if (tag.contains("pos")) { tag.remove("pos"); }
            if (!world.isClient) { player.sendMessage(getMode(stack).title, true); }
            return TypedActionResult.success(stack);
        }
        return super.useModifierInAir(world, player, hand);
    }

    @Override
    public void onCraft(final ItemStack stack, final World world, final PlayerEntity player)
    {
        super.onCraft(stack, world, player);
        getMode(stack);
    }

    @Override
    public ItemStack getStackForRender()
    {
        final ItemStack stack = super.getStackForRender();
        getMode(stack);
        return stack;
    }

    @Override
    public void appendStacks(final ItemGroup group, final DefaultedList<ItemStack> stacks) { if (isIn(group)) { stacks.add(getStackForRender()); } }

    private MutatorMode getMode(final ItemStack stack)
    {
        final CompoundTag tag = stack.getOrCreateTag();
        if (!tag.contains("mode", 1)) { tag.putByte("mode", (byte) 0); }
        return MutatorMode.values()[tag.getByte("mode")];
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void appendTooltip(final ItemStack stack, final World world, final List<Text> tooltip, final TooltipContext context)
    {
        final MutatorMode mode = getMode(stack);
        tooltip.add(new TranslatableText("tooltip.expandedstorage.tool_mode", mode.title).formatted(Formatting.GRAY));
        tooltip.add(mode.description);
        super.appendTooltip(stack, world, tooltip, context);
    }
}