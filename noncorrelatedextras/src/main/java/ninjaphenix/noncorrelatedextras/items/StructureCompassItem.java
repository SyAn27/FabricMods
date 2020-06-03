package ninjaphenix.noncorrelatedextras.items;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.decoration.ItemFrameEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPropertyGetter;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import java.util.Objects;

public class StructureCompassItem extends Item
{
    private final String STRUCTURE;

    public StructureCompassItem(Settings settings, String structureName)
    {
        super(settings);
        STRUCTURE = Objects.requireNonNull(structureName);
        this.addPropertyGetter(new Identifier("angle"), new ItemPropertyGetter()
        {
            @Environment(EnvType.CLIENT)
            private double angle;
            @Environment(EnvType.CLIENT)
            private double step;
            @Environment(EnvType.CLIENT)
            private long lastTick;

            @Environment(EnvType.CLIENT)
            @Override
            public float call(ItemStack stack, World world, LivingEntity user)
            {
                if (user == null && !stack.isInFrame()) { return 0; }
                final boolean userNotNull = user != null;
                final Entity entity = userNotNull ? user : Objects.requireNonNull(stack.getFrame());
                if (world == null) { world = entity.world; }
                double angToFeature = getAngleToFeature(stack, entity);
                if (angToFeature == Double.MIN_VALUE) { return 2; }
                double pointerAngle = 0.75 +
                        (angToFeature / 6.2831854820251465D) -
                        (MathHelper.floorMod((userNotNull ? entity.yaw : getYaw((ItemFrameEntity) entity)) / 360, 1));
                if (userNotNull) { pointerAngle = this.getAngle(world, pointerAngle); }
                return MathHelper.floorMod((float) pointerAngle, 1.0F);
            }

            @Environment(EnvType.CLIENT)
            private double getAngle(final World world, final double entityYaw)
            {
                if (world.getTime() != lastTick)
                {
                    lastTick = world.getTime();
                    step += (MathHelper.floorMod(entityYaw - angle + 0.5D, 1.0D) - 0.5D) * 0.1D;
                    step *= 0.8D;
                    angle = MathHelper.floorMod(angle + step, 1.0D);
                }
                return angle;
            }

            @Environment(EnvType.CLIENT)
            private double getYaw(final ItemFrameEntity entity) { return MathHelper.wrapDegrees(180 + entity.getHorizontalFacing().getHorizontal() * 90); }

            @Environment(EnvType.CLIENT)
            private double getAngleToFeature(final ItemStack stack, final Entity entity)
            {
                final CompoundTag tag = stack.getOrCreateTag();
                if (tag.contains("pos"))
                {
                    final BlockPos pos = NbtHelper.toBlockPos(tag.getCompound("pos"));
                    return Math.atan2(pos.getZ() - entity.getZ(), pos.getX() - entity.getX());
                }
                return Double.MIN_VALUE;
            }
        });
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected)
    {
        super.inventoryTick(stack, world, entity, slot, selected);
        if (world instanceof ServerWorld)
        {
            CompoundTag tag = stack.getOrCreateTag();
            long lastTime = tag.getLong("lastSearch");
            long curTime = world.getTime();
            if (curTime - lastTime > 200)
            {
                tag.putLong("lastSearch", curTime);
                final BlockPos structurePos = ((ServerWorld) world).locateStructure(STRUCTURE, entity.getBlockPos(), 64, false);
                if (structurePos != null) { tag.put("pos", NbtHelper.fromBlockPos(structurePos)); }
            }

        }
    }
}
