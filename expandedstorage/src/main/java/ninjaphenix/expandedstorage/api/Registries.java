package ninjaphenix.expandedstorage.api;

import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.SimpleRegistry;
import ninjaphenix.expandedstorage.block.misc.CursedChestType;

import java.util.function.Function;

/**
 * This class provides data registries for adding new chests to already defined chest types. This will likely be refactored in the future as new features and
 * chest types are added.
 *
 * @author NinjaPhenix
 * @since 4.0.0
 */
public final class Registries
{
    /**
     * This registry for CursedChestBlock data storage.
     */
    public static final SimpleRegistry<ChestTierData> CHEST = new SimpleRegistry<>();

    /**
     * This registry is for OldChestBlock data storage.
     */
    public static final SimpleRegistry<TierData> OLD_CHEST = new SimpleRegistry<>();

    public static class ChestTierData extends TierData
    {
        private final Identifier singleTexture;
        private final Identifier topTexture;
        private final Identifier backTexture;
        private final Identifier rightTexture;
        private final Identifier bottomTexture;
        private final Identifier frontTexture;
        private final Identifier leftTexture;

        /**
         * Data representing a vanilla looking chest block.
         *
         * @param slots The amount of itemstacks this chest tier can hold.
         * @param containerName The default container name for this chest tier.
         * @param blockId The block id that represents this data.
         * @param textureFunction The function which returns the chest texture for a supplied type.
         */
        public ChestTierData(int slots, Text containerName, Identifier blockId, Function<CursedChestType, Identifier> textureFunction)
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

        /**
         * @param type The chest type to receive the texture for.
         * @return The texture relevant to the type.
         */
        public Identifier getChestTexture(CursedChestType type)
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

        /**
         * Data representing minimalist chest blocks such as OldChestBlock's.
         *
         * @param slots The amount of itemstacks this chest tier can hold.
         * @param containerName The default container name for this chest tier.
         * @param blockId The block id that represents this data.
         */
        public TierData(int slots, Text containerName, Identifier blockId)
        {
            this.slots = slots;
            this.containerName = containerName;
            this.blockId = blockId;
        }

        /**
         * @return The amount of slots the chest tier contains.
         */
        public int getSlotCount() { return slots; }

        /**
         * @return The default container name for the chest tier.
         */
        public Text getContainerName() { return containerName; }

        /**
         * @return The block that represents this data instance.
         */
        public Identifier getBlockId() { return blockId; }
    }
}
