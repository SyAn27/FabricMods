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
    private final AngleRandomizer unknownTargetAngleSmoother = new AngleRandomizer();
    private final AngleRandomizer knownTargetAngleSmoother = new AngleRandomizer();
    private static final double TAU = 6.2831854820251465;
    public StructureCompassModelPredicateProvider() {}

    @Override
    public float call(ItemStack stack, @Nullable ClientWorld world, @Nullable LivingEntity user)
    {
        Entity entity = user != null ? user : stack.getHolder();
        if (entity == null) { return 2; }
        if(world == null && entity.getEntityWorld() instanceof ClientWorld) { world = (ClientWorld) entity.getEntityWorld(); }
        else if(world == null){ return 2; }
        final BlockPos pos = StructureCompassItem.hasTarget(stack) ? StructureCompassItem.getTargetPos(stack.getTag()) : null;
        if(pos == null) { return 2; }
        final Optional<RegistryKey<World>> targetDimension = StructureCompassItem.getTargetDimension(stack.getTag());
        long currentTime = world.getTime();
        if(targetDimension.isPresent() && world.getRegistryKey().equals(targetDimension.get())) {
            double yaw;
            final boolean entityIsMainPlayer = entity instanceof ClientPlayerEntity && ((ClientPlayerEntity) entity).isMainPlayer();
            if(entityIsMainPlayer) {
                yaw = entity.yaw;
            } else if(entity instanceof ItemFrameEntity) {
                yaw = getItemFrameAngleOffset((ItemFrameEntity) entity);
            } else if (entity instanceof ItemEntity) {
                yaw = 180D - ((ItemEntity) entity).method_27314(0.5F) / TAU * 360D;
            } else {
                yaw = ((LivingEntity) entity).bodyYaw;
            }
            yaw = MathHelper.floorMod(yaw / 360D, 1D);
            double angle = getAngleToPos(Vec3d.ofCenter(pos), entity) / TAU;
            double returnedAngle;
            if(entityIsMainPlayer) {
                if(knownTargetAngleSmoother.shouldUpdate(currentTime)) {
                    knownTargetAngleSmoother.update(currentTime, 0.75D - yaw );
                }
                returnedAngle = angle + knownTargetAngleSmoother.value;
            } else {
                returnedAngle = 0.75D - yaw + angle;
            }
            return (float) MathHelper.floorMod(returnedAngle, 1);
        } else {
            if (unknownTargetAngleSmoother.shouldUpdate(currentTime)) {
                unknownTargetAngleSmoother.update(currentTime, Math.random());
            }
            double returnedAngle = unknownTargetAngleSmoother.value + stack.hashCode() / 2.14748365E9F;
            return (float) MathHelper.floorMod(returnedAngle, 1);
        }
    }

    private double getItemFrameAngleOffset(ItemFrameEntity entity)
    {
        Direction direction = entity.getHorizontalFacing();
        int i = direction.getAxis().isVertical() ? 90 * direction.getDirection().offset() : 0;
        return MathHelper.wrapDegrees(180 + direction.getHorizontal() * 90 + entity.getRotation() * 45 + i);
    }

    private double getAngleToPos(Vec3d pos, Entity entity) {
        return Math.atan2(pos.getZ() - entity.getZ(), pos.getX() - entity.getX());
    }
}

