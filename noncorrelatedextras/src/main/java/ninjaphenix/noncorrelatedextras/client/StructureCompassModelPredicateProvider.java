package ninjaphenix.noncorrelatedextras.client;

import net.minecraft.client.item.ModelPredicateProvider;
import net.minecraft.client.item.ModelPredicateProviderRegistry.AngleRandomizer;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.decoration.ItemFrameEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import ninjaphenix.noncorrelatedextras.items.StructureCompassItem;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class StructureCompassModelPredicateProvider implements ModelPredicateProvider
{
    private static final double TAU = 6.2831854820251465;
    private final AngleRandomizer unknownTargetAngleSmoother = new AngleRandomizer();
    private final AngleRandomizer knownTargetAngleSmoother = new AngleRandomizer();

    public StructureCompassModelPredicateProvider() {}

    @Override
    public float call(ItemStack stack, @Nullable ClientWorld world, @Nullable LivingEntity user)
    {
        final CompoundTag stackTag = stack.getOrCreateTag();
        final BlockPos pos = StructureCompassItem.getTargetPos(stackTag);
        if (pos == null) { return 2; }
        final Entity entity = user != null ? user : stack.getHolder();
        if (entity == null) { return getRandomAngle(stack, world != null ? world.getTime() : -1); }
        if (world == null)
        {
            if (entity.getEntityWorld() instanceof ClientWorld) { world = (ClientWorld) entity.getEntityWorld(); }
            else { return getRandomAngle(stack, -1); }
        }
        final long currentTime = world.getTime();
        final Optional<RegistryKey<World>> targetDimension = StructureCompassItem.getTargetDimension(stackTag);
        if (targetDimension.isPresent() && world.getRegistryKey().equals(targetDimension.get()))
        {
            double yaw;
            final boolean entityIsMainPlayer = entity instanceof ClientPlayerEntity && ((ClientPlayerEntity) entity).isMainPlayer();
            if (entityIsMainPlayer) { yaw = entity.yaw; }
            else if (entity instanceof ItemFrameEntity)
            {
                final ItemFrameEntity frame = (ItemFrameEntity) entity;
                final Direction dir = frame.getHorizontalFacing();
                yaw = MathHelper.wrapDegrees(180 + dir.getHorizontal() * 90 + frame.getRotation() * 45 +
                        (dir.getAxis().isVertical() ? 90 * dir.getDirection().offset() : 0));
            }
            else if (entity instanceof ItemEntity) { yaw = 180D - ((ItemEntity) entity).method_27314(0.5F) / TAU * 360D; }
            else { yaw = ((LivingEntity) entity).bodyYaw; }
            yaw = MathHelper.floorMod(yaw / 360D, 1D);
            double returnedAngle = getAngle(Vec3d.ofCenter(pos), entity) / TAU;
            if (entityIsMainPlayer)
            {
                if (knownTargetAngleSmoother.shouldUpdate(currentTime)) { knownTargetAngleSmoother.update(currentTime, 0.75D - yaw); }
                returnedAngle += knownTargetAngleSmoother.value;
            }
            else { returnedAngle += 0.75D - yaw; }
            return (float) MathHelper.floorMod(returnedAngle, 1);
        }
        else
        {
            return getRandomAngle(stack, currentTime);
        }
    }

    private double getAngle(final Vec3d pos, final Entity entity) { return Math.atan2(pos.getZ() - entity.getZ(), pos.getX() - entity.getX()); }

    private float getRandomAngle(final ItemStack stack, final long time)
    {
        if (unknownTargetAngleSmoother.shouldUpdate(time)) { unknownTargetAngleSmoother.update(time, Math.random()); }
        double returnedAngle = unknownTargetAngleSmoother.value + stack.hashCode() / 2.14748365E9F;
        return (float) MathHelper.floorMod(returnedAngle, 1);
    }
}