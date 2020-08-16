package ninjaphenix.expandedstorage.impl.item;

import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import ninjaphenix.expandedstorage.impl.block.AbstractChestBlock;
import ninjaphenix.expandedstorage.impl.block.misc.CursedChestType;

public abstract class ChestModifierItem extends Item
{
    private static final DirectionProperty FACING = AbstractChestBlock.FACING;
    private static final EnumProperty<CursedChestType> TYPE = AbstractChestBlock.TYPE;

    public ChestModifierItem(final Settings settings) { super(settings); }

    @Override
    public ActionResult useOnBlock(final ItemUsageContext context)
    {
        final World world = context.getWorld();
        final BlockPos pos = context.getBlockPos();
        final BlockState state = world.getBlockState(pos);
        if (state.getBlock() instanceof AbstractChestBlock)
        {
            ActionResult result = ActionResult.FAIL;
            final CursedChestType type = state.get(TYPE);
            final Direction facing = state.get(FACING);
            if (type == CursedChestType.SINGLE) { result = useModifierOnChestBlock(context, state, pos, null, null); }
            else if (type == CursedChestType.BOTTOM)
            {
                final BlockPos otherPos = pos.offset(Direction.UP);
                result = useModifierOnChestBlock(context, state, pos, world.getBlockState(otherPos), otherPos);
            }
            else if (type == CursedChestType.TOP)
            {
                final BlockPos otherPos = pos.offset(Direction.DOWN);
                result = useModifierOnChestBlock(context, world.getBlockState(otherPos), otherPos, state, pos);
            }
            else if (type == CursedChestType.LEFT)
            {
                final BlockPos otherPos = pos.offset(facing.rotateYCounterclockwise());
                result = useModifierOnChestBlock(context, state, pos, world.getBlockState(otherPos), otherPos);
            }
            else if (type == CursedChestType.RIGHT)
            {
                final BlockPos otherPos = pos.offset(facing.rotateYClockwise());
                result = useModifierOnChestBlock(context, world.getBlockState(otherPos), otherPos, state, pos);
            }
            else if (type == CursedChestType.FRONT)
            {
                final BlockPos otherPos = pos.offset(facing.getOpposite());
                result = useModifierOnChestBlock(context, state, pos, world.getBlockState(otherPos), otherPos);
            }
            else if (type == CursedChestType.BACK)
            {
                final BlockPos otherPos = pos.offset(facing);
                result = useModifierOnChestBlock(context, world.getBlockState(otherPos), otherPos, state, pos);
            }
            return result;
        }
        else { return useModifierOnBlock(context, state); }
    }

    @Override
    public ActionResult useOnEntity(final ItemStack stack, final PlayerEntity player, final LivingEntity entity, final Hand hand)
    {
        return useModifierOnEntity(stack, player, entity, hand);
    }

    @Override
    public TypedActionResult<ItemStack> use(final World world, final PlayerEntity player, final Hand hand)
    {
        TypedActionResult<ItemStack> result = useModifierInAir(world, player, hand);
        if (result.getResult() == ActionResult.SUCCESS) { player.getItemCooldownManager().set(this, 5); }
        return result;
    }

    protected ActionResult useModifierOnChestBlock(final ItemUsageContext context, final BlockState mainState, final BlockPos mainBlockPos, final BlockState otherState, final BlockPos otherBlockPos) { return ActionResult.PASS; }

    protected ActionResult useModifierOnBlock(final ItemUsageContext context, final BlockState state) { return ActionResult.PASS; }

    protected ActionResult useModifierOnEntity(final ItemStack stack, final PlayerEntity player, final LivingEntity entity, final Hand hand) { return ActionResult.PASS; }

    protected TypedActionResult<ItemStack> useModifierInAir(final World world, final PlayerEntity player, final Hand hand)
    {
        return TypedActionResult.pass(player.getStackInHand(hand));
    }
}