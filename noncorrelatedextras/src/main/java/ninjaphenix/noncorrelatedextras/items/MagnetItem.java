package ninjaphenix.noncorrelatedextras.items;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import ninjaphenix.noncorrelatedextras.config.Config;
import ninjaphenix.noncorrelatedextras.features.MagnetFeature;

import java.util.List;

public class MagnetItem extends Item
{
    private static final int MAX_RANGE = Config.INSTANCE.getMagnetMaxRange();
    private static final double SPEED = Config.INSTANCE.getMagnetSpeed();
    private static final EquipmentSlot[] EQUIPMENT_SLOTS = new EquipmentSlot[]
            { EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET };

    public MagnetItem(final Settings settings) { super(settings); }

    public static void magnetTick(final PlayerEntity player, final ItemStack magnetStack)
    {
        if (player.isSneaking()) { return; }
        ensureValidMagnetRange(player, magnetStack);
        final int range = getMagnetRange(magnetStack);
        final Vec3d finePos = player.getPos().add(0, 0.25, 0);
        final List<ItemEntity> entities = player.getEntityWorld().getEntitiesByType(EntityType.ITEM, new Box(finePos.subtract(range, range, range),
                finePos.add(range, range, range)), EntityPredicates.EXCEPT_SPECTATOR);
        if (getMagnetMode(magnetStack)) { for (ItemEntity item : entities) { if (!item.cannotPickup()) { item.onPlayerCollision(player); } } }
        else { for (ItemEntity item : entities) { item.setVelocity(finePos.subtract(item.getPos()).multiply(SPEED)); } }
    }

    private static void ensureValidMagnetRange(final PlayerEntity player, final ItemStack stack)
    {
        final int range = getMagnetRange(stack);
        if (range > MAX_RANGE)
        {
            final int max = getMagnetMaxRange(player);
            if (range > max) { setMagnetRange(stack, max); }
        }
    }

    public static int getMagnetMaxRange(final PlayerEntity player)
    {
        int range = MAX_RANGE;
        if (player != null)
        {
            for (EquipmentSlot type : EQUIPMENT_SLOTS)
            {
                range += player.getEquippedStack(type).getItem() instanceof MagnetisedArmourItem ? Config.INSTANCE.getAdditionalMagnetRange(type) : 0;
            }
        }
        return range;
    }

    public static int getMagnetRange(final ItemStack stack)
    {
        final CompoundTag tag = stack.getOrCreateTag();
        if (!tag.contains("range")) { tag.putInt("range", MAX_RANGE); }
        return tag.getInt("range");
    }

    public static void setMagnetRange(final ItemStack stack, final int range)
    {
        final CompoundTag tag = stack.getOrCreateTag();
        tag.putInt("range", range);
    }

    public static void setMagnetMode(final ItemStack stack, final boolean mode)
    {
        final CompoundTag tag = stack.getOrCreateTag();
        tag.putBoolean("mode", mode);
    }

    public static boolean getMagnetMode(final ItemStack stack)
    {
        final CompoundTag tag = stack.getOrCreateTag();
        if (!tag.contains("mode")) { tag.putBoolean("mode", false); }
        return tag.getBoolean("mode");
    }

    @Override
    public void inventoryTick(final ItemStack stack, final World world, final Entity entity, final int slot, final boolean selected)
    {
        if (slot < 9 && entity instanceof PlayerEntity) { magnetTick((PlayerEntity) entity, stack); }
    }

    @Override
    public TypedActionResult<ItemStack> use(final World world, final PlayerEntity user, final Hand hand)
    {
        ItemStack stack = user.getStackInHand(hand);
        if (!world.isClient) { if (user.isSneaking()) { MagnetFeature.openMagnetScreen(user, stack); } }
        return TypedActionResult.success(stack);
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void appendTooltip(final ItemStack stack, final World world, final List<Text> tooltip, final TooltipContext context)
    {
        final int magnet_range = getMagnetMaxRange(MinecraftClient.getInstance().player);
        Text rangeText = new TranslatableText("noncorrelatedextras.tooltip.magnet.range",
                new LiteralText(String.valueOf(getMagnetRange(stack))).formatted(Formatting.DARK_GRAY),
                new LiteralText(String.valueOf(magnet_range)).formatted(Formatting.DARK_GRAY)).formatted(Formatting.GRAY);
        if (magnet_range > MAX_RANGE)
        { rangeText = new TranslatableText("noncorrelatedextras.tooltip.magnet.extra_range", rangeText, magnet_range - MAX_RANGE).formatted(Formatting.BLUE); }
        tooltip.add(rangeText);
    }
}