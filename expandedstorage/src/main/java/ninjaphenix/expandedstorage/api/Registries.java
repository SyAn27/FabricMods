package ninjaphenix.expandedstorage.api;

import com.mojang.serialization.Lifecycle;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.util.registry.SimpleRegistry;
import ninjaphenix.expandedstorage.impl.Const;
import ninjaphenix.expandedstorage.impl.ExpandedStorage;
import ninjaphenix.expandedstorage.impl.block.misc.CursedChestType;

import java.util.function.Function;

/* To be deprecated when refinement is complete. */
@Deprecated
public final class Registries
{
    private static final RegistryKey<Registry<ChestTierData>> CHEST_KEY = RegistryKey.of(Const.id("root"), Const.id("chest"));
    public static final SimpleRegistry<ChestTierData> CHEST = new SimpleRegistry<>(CHEST_KEY, Lifecycle.experimental());
    private static final RegistryKey<Registry<TierData>> OLD_CHEST_KEY = RegistryKey.of(Const.id("root"), Const.id("old_chest"));
    public static final SimpleRegistry<TierData> OLD_CHEST = new SimpleRegistry<>(OLD_CHEST_KEY, Lifecycle.experimental());

    public static class ChestTierData extends TierData
    {
        private final Identifier singleTexture, topTexture, backTexture, rightTexture, bottomTexture, frontTexture, leftTexture;

        public ChestTierData(final int slots, final Text containerName, final Identifier blockId, final Function<CursedChestType, Identifier> textureFunction)
        {
            super(slots, containerName, blockId);
            singleTexture = textureFunction.apply(CursedChestType.SINGLE);
            topTexture = textureFunction.apply(CursedChestType.TOP);
            backTexture = textureFunction.apply(CursedChestType.BACK);
            rightTexture = textureFunction.apply(CursedChestType.RIGHT);
            bottomTexture = textureFunction.apply(CursedChestType.BOTTOM);
            frontTexture = textureFunction.apply(CursedChestType.FRONT);
            leftTexture = textureFunction.apply(CursedChestType.LEFT);
        }

        public Identifier getChestTexture(final CursedChestType type)
        {
            switch(type) {

                case TOP: return topTexture;
                case BACK: return backTexture;
                case RIGHT: return rightTexture;
                case BOTTOM: return bottomTexture;
                case FRONT: return frontTexture;
                case LEFT: return leftTexture;
                default: return singleTexture;
            }
        }
    }

    public static class TierData
    {
        private final int slots;
        private final Text containerName;
        private final Identifier blockId;

        public TierData(final int slots, final Text containerName, final Identifier blockId)
        {
            this.slots = slots;
            this.containerName = containerName;
            this.blockId = blockId;
        }

        public int getSlotCount() { return slots; }

        public Text getContainerName() { return containerName; }

        public Identifier getBlockId() { return blockId; }
    }
}