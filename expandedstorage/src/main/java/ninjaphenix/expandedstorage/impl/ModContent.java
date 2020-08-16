package ninjaphenix.expandedstorage.impl;

import net.minecraft.item.ItemGroup;
import net.minecraft.util.registry.Registry;
import ninjaphenix.chainmail.api.ChainmailCommonApi;
import ninjaphenix.expandedstorage.impl.block.CursedChestBlock;
import ninjaphenix.expandedstorage.impl.block.OldChestBlock;
import ninjaphenix.expandedstorage.impl.block.entity.CursedChestBlockEntity;
import ninjaphenix.expandedstorage.impl.block.entity.CustomBlockEntityType;
import ninjaphenix.expandedstorage.impl.block.entity.OldChestBlockEntity;
import ninjaphenix.expandedstorage.impl.content.ModItemGroup;

public final class ModContent
{
    public static final ItemGroup GROUP = ChainmailCommonApi.INSTANCE.registerItemGroup((index) -> new ModItemGroup(index, Const.MOD_ID));

    public static final CustomBlockEntityType<CursedChestBlockEntity> CHEST = new CustomBlockEntityType<>(() -> new CursedChestBlockEntity(null), b -> b instanceof CursedChestBlock);
    public static final CustomBlockEntityType<OldChestBlockEntity> OLD_CHEST = new CustomBlockEntityType<>(() -> new OldChestBlockEntity(null), b -> b instanceof OldChestBlock);


    public static void registerBlockEntities()
    {
        Registry.register(Registry.BLOCK_ENTITY_TYPE, Const.id("cursed_chest"), CHEST);
        Registry.register(Registry.BLOCK_ENTITY_TYPE, Const.id("old_cursed_chest"), OLD_CHEST);
    }

    public static void registerBlocks()
    {

    }

    public static void registerItems()
    {

    }
}
