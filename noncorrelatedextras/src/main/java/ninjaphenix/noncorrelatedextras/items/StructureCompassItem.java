package ninjaphenix.noncorrelatedextras.items;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.datafixer.NbtOps;
import net.minecraft.entity.Entity;
import net.minecraft.item.CompassItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Vanishable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.StructureFeature;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

public class StructureCompassItem extends Item implements Vanishable
{
    private final StructureFeature<?> STRUCTURE;
    private CompassItem _; /* todo: remove */

    public static boolean hasTarget(final ItemStack stack)
    {
        final CompoundTag compoundTag = stack.getTag();
        return compoundTag != null && compoundTag.contains("dimension") && compoundTag.contains("pos");
    }

    public static Optional<RegistryKey<World>> getTargetDimension(final CompoundTag tag)
    {
        return World.CODEC.parse(NbtOps.INSTANCE, tag.get("dimension")).result();
    }

    public static BlockPos getTargetPos(final CompoundTag tag) {
        final CompoundTag pos = tag.getCompound("pos");
        return NbtHelper.toBlockPos(pos);
    }

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
                if (structurePos != null)
                {
                    World.CODEC.encodeStart(NbtOps.INSTANCE, world.getRegistryKey()).result().ifPresent(t -> {
                        tag.put("dimension", t);
                        tag.put("pos", NbtHelper.fromBlockPos(structurePos));
                        System.out.println("Put Dimension ("+t.asString()+")");
                        System.out.println("Put Pos:" + structurePos.toShortString());
                    });
                }
            }
        }
    }

    public StructureCompassItem(final Settings settings, final @NotNull StructureFeature<?> structure)
    {
        super(settings);
        STRUCTURE = structure;
    }

    @Override
    public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context)
    {
        super.appendTooltip(stack, world, tooltip, context);
        tooltip.add(new LiteralText("Disabled for now."));
    }
}
