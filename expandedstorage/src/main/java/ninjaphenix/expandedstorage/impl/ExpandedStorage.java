package ninjaphenix.expandedstorage.impl;

import net.fabricmc.api.ModInitializer;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import ninjaphenix.chainmail.api.ChainmailCommonApi;
import ninjaphenix.expandedstorage.impl.block.CursedChestBlock;
import ninjaphenix.expandedstorage.impl.block.OldChestBlock;
import ninjaphenix.expandedstorage.impl.block.entity.CursedChestBlockEntity;
import ninjaphenix.expandedstorage.impl.block.entity.CustomBlockEntityType;
import ninjaphenix.expandedstorage.impl.block.entity.OldChestBlockEntity;
import ninjaphenix.expandedstorage.impl.content.ModBlocks;
import ninjaphenix.expandedstorage.impl.content.ModItemGroup;
import ninjaphenix.expandedstorage.impl.content.ModItems;

public class ExpandedStorage implements ModInitializer
{
    public static final ExpandedStorage INSTANCE = new ExpandedStorage();
    public static final String MOD_ID = "expandedstorage";
    public static final ItemGroup group = ChainmailCommonApi.INSTANCE.registerItemGroup((index) -> new ModItemGroup(index, MOD_ID));
    public static final CustomBlockEntityType<CursedChestBlockEntity> CHEST =
            new CustomBlockEntityType<>(() -> new CursedChestBlockEntity(null), b -> b instanceof CursedChestBlock);
    public static final CustomBlockEntityType<OldChestBlockEntity> OLD_CHEST =
            new CustomBlockEntityType<>(() -> new OldChestBlockEntity(null), b -> b instanceof OldChestBlock);

    public static Identifier id(String path) { return new Identifier(MOD_ID, path); }

    @Override
    public void onInitialize()
    {
        Registry.register(Registry.BLOCK_ENTITY_TYPE, id("cursed_chest"), CHEST);
        Registry.register(Registry.BLOCK_ENTITY_TYPE, id("old_cursed_chest"), OLD_CHEST);
        ModBlocks.init();
        ModItems.init();
    }
}
