package ninjaphenix.expandedstorage;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import ninjaphenix.expandedstorage.block.CursedChestBlock;
import ninjaphenix.expandedstorage.block.ModBlocks;
import ninjaphenix.expandedstorage.block.OldChestBlock;
import ninjaphenix.expandedstorage.block.entity.CursedChestBlockEntity;
import ninjaphenix.expandedstorage.block.entity.CustomBlockEntityType;
import ninjaphenix.expandedstorage.block.entity.OldChestBlockEntity;
import ninjaphenix.expandedstorage.item.ModItems;

public class ExpandedStorage implements ModInitializer
{
    public static final ExpandedStorage INSTANCE = new ExpandedStorage();
    public static final String MOD_ID = "expandedstorage";
    public static final ItemGroup group = FabricItemGroupBuilder.build(getId(MOD_ID), () -> new ItemStack(ModBlocks.diamond_chest));

    public static final CustomBlockEntityType<CursedChestBlockEntity> CHEST;
    public static final CustomBlockEntityType<OldChestBlockEntity> OLD_CHEST;

    static
    {
        CHEST = new CustomBlockEntityType<>(() -> new CursedChestBlockEntity(null), b -> b instanceof CursedChestBlock);
        OLD_CHEST = new CustomBlockEntityType<>(() -> new OldChestBlockEntity(null), b -> b instanceof OldChestBlock);
    }

    public static Identifier getId(String path) { return new Identifier(MOD_ID, path); }

    @Override
    public void onInitialize()
    {
        Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier("expandedstorage", "cursed_chest"), CHEST);
        Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier("expandedstorage", "old_cursed_chest"), OLD_CHEST);
        ModBlocks.init();
        ModItems.init();
    }
}
