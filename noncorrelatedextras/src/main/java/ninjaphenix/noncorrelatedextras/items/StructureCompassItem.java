package ninjaphenix.noncorrelatedextras.items;

import net.minecraft.datafixer.NbtOps;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Vanishable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.StructureFeature;

import java.util.Optional;

public class StructureCompassItem extends Item implements Vanishable
{
    private final StructureFeature<?> STRUCTURE;

    public StructureCompassItem(final Settings settings, final StructureFeature<?> structure)
    {
        super(settings);
        STRUCTURE = structure;
    }

    public static Optional<RegistryKey<World>> getTargetDimension(final CompoundTag tag)
    {
        return World.CODEC.parse(NbtOps.INSTANCE, tag.get("dimension")).result();
    }

    public static BlockPos getTargetPos(final CompoundTag tag)
    {
        return tag != null && tag.contains("dimension") && tag.contains("pos") ? NbtHelper.toBlockPos(tag.getCompound("pos")) : null;
    }

    public void inventoryTick(final ItemStack stack, final World world, final Entity entity, final int slot, final boolean selected)
    {
        super.inventoryTick(stack, world, entity, slot, selected);
        if (world instanceof ServerWorld)
        {
            final CompoundTag tag = stack.getOrCreateTag();
            final long lastTime = tag.getLong("lastSearch");
            final long curTime = world.getTime();
            if (curTime - lastTime > 200)
            {
                tag.putLong("lastSearch", curTime);
                final BlockPos structurePos = ((ServerWorld) world).locateStructure(STRUCTURE, entity.getBlockPos(), 64, false);
                if (structurePos != null)
                {
                    World.CODEC.encodeStart(NbtOps.INSTANCE, world.getRegistryKey()).result().ifPresent(t -> {
                        tag.put("dimension", t);
                        tag.put("pos", NbtHelper.fromBlockPos(structurePos));
                    });
                }
            }
        }
    }
}