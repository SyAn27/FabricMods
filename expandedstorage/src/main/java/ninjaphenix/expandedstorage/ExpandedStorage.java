package ninjaphenix.expandedstorage;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.impl.item.group.ItemGroupExtensions;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import ninjaphenix.expandedstorage.block.CursedChestBlock;
import ninjaphenix.expandedstorage.block.OldChestBlock;
import ninjaphenix.expandedstorage.block.entity.CursedChestBlockEntity;
import ninjaphenix.expandedstorage.block.entity.CustomBlockEntityType;
import ninjaphenix.expandedstorage.block.entity.OldChestBlockEntity;
import ninjaphenix.expandedstorage.content.ModBlocks;
import ninjaphenix.expandedstorage.content.ModItemGroup;
import ninjaphenix.expandedstorage.content.ModItems;

public class ExpandedStorage implements ModInitializer
{
    public static final ExpandedStorage INSTANCE = new ExpandedStorage();
    public static final String MOD_ID = "expandedstorage";
    public static final ItemGroup group;
    public static final CustomBlockEntityType<CursedChestBlockEntity> CHEST =
            new CustomBlockEntityType<>(() -> new CursedChestBlockEntity(null), b -> b instanceof CursedChestBlock);
    public static final CustomBlockEntityType<OldChestBlockEntity> OLD_CHEST =
            new CustomBlockEntityType<>(() -> new OldChestBlockEntity(null), b -> b instanceof OldChestBlock);

    static
    {
        ((ItemGroupExtensions) ItemGroup.BUILDING_BLOCKS).fabric_expandArray();
        group = new ModItemGroup(ItemGroup.GROUPS.length - 1, MOD_ID + "." + MOD_ID);
    }

    public static Identifier getId(String path) { return new Identifier(MOD_ID, path); }

    @Override
    public void onInitialize()
    {
        Registry.register(Registry.BLOCK_ENTITY_TYPE, getId("cursed_chest"), CHEST);
        Registry.register(Registry.BLOCK_ENTITY_TYPE, getId("old_cursed_chest"), OLD_CHEST);
        ModBlocks.init();
        ModItems.init();
    }
}
