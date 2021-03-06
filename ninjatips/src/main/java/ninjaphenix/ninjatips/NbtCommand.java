package ninjaphenix.ninjatips;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.command.argument.BlockPosArgumentType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.MessageType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.*;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;

import static com.mojang.brigadier.Command.SINGLE_SUCCESS;
import static net.minecraft.server.command.CommandManager.literal;

public class NbtCommand
{
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, boolean isDedicated)
    {
        dispatcher.register(literal("nbt")
                .then(literal("block").then(CommandManager.argument("pos", BlockPosArgumentType.blockPos()).executes(context ->
                {
                    final BlockPos pos = BlockPosArgumentType.getLoadedBlockPos(context, "pos");
                    final ServerPlayerEntity player = context.getSource().getPlayer();
                    return dumpBlockNBT(pos, player);
                })))
                .then(literal("hand").executes(context ->
                {
                    final ServerPlayerEntity player = context.getSource().getPlayer();
                    final ItemStack hand = player.getStackInHand(Hand.MAIN_HAND);
                    final ItemStack off_hand = player.getStackInHand(Hand.OFF_HAND);
                    if (hand.isEmpty() && !off_hand.isEmpty()) { return dumpItemStackNbt(off_hand, player); }
                    return dumpItemStackNbt(hand, player);
                }).then(literal("main").executes(context ->
                {
                    final ServerPlayerEntity player = context.getSource().getPlayer();
                    final ItemStack hand = player.getStackInHand(Hand.MAIN_HAND);
                    return dumpItemStackNbt(hand, player);
                })).then(literal("off").executes(context ->
                {
                    final ServerPlayerEntity player = context.getSource().getPlayer();
                    final ItemStack hand = player.getStackInHand(Hand.OFF_HAND);
                    return dumpItemStackNbt(hand, player);
                }))));
    }

    private static int dumpItemStackNbt(ItemStack stack, ServerPlayerEntity player)
    {
        final MutableText ninjaTips = new LiteralText("").append(new LiteralText("[Ninja Tips] ").formatted(Formatting.BLUE));
        if (stack.isEmpty())
        {
            player.sendMessage(ninjaTips.append(new TranslatableText("ninjatips.text.handempty")), MessageType.CHAT, Util.NIL_UUID);
            return SINGLE_SUCCESS;
        }
        if (!stack.hasTag())
        {
            player.sendMessage(ninjaTips.append(new TranslatableText("ninjatips.text.handnonbt")), MessageType.CHAT, Util.NIL_UUID);
            return SINGLE_SUCCESS;
        }
        player.sendMessage(ninjaTips.append(getItemText(stack, true)), MessageType.CHAT, Util.NIL_UUID);
        return SINGLE_SUCCESS;
    }

    private static Text getItemText(ItemStack stack, boolean copyText)
    {
        MutableText text = new LiteralText("").append(stack.toHoverableText());
        if (copyText && !stack.getTag().isEmpty())
        {
            text.append(new LiteralText(" ")
                    .append(new TranslatableText("ninjatips.text.clicktocopy").formatted(Formatting.UNDERLINE, Formatting.ITALIC).styled(
                            style -> style.withClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, stack.getTag().asString())))));
        }
        return text;
    }

    private static int dumpBlockNBT(BlockPos pos, ServerPlayerEntity player)
    {
        final ServerWorld world = player.getServerWorld();
        final ItemStack stack = new ItemStack(world.getBlockState(pos).getBlock(), 1);
        final BlockEntity entity = world.getBlockEntity(pos);
        stack.setTag(entity != null ? entity.toTag(new CompoundTag()) : new CompoundTag());
        player.sendMessage(new LiteralText("").append(new LiteralText("[Ninja Tips] ").formatted(Formatting.BLUE)).
                append(getItemText(stack, true)), MessageType.CHAT, Util.NIL_UUID);
        return SINGLE_SUCCESS;
    }
}
