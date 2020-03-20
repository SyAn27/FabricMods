package ninjaphenix.test;

import net.fabricmc.api.ModInitializer;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import ninjaphenix.test.a.block.TestBlock;
import ninjaphenix.test.a.block.entity.TestBlockEntity;

public class A implements ModInitializer
{
    @Override
    public void onInitialize()
    {
        System.out.println("Now were making test mods :tiny_potato:");
        final Block TEST_BLOCK = Registry.register(Registry.BLOCK, new Identifier("test_a", "test_block"), new TestBlock(Block.Settings.of(Material.CLAY)));
        Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier("test_a", "test_block_entity"),
                BlockEntityType.Builder.create(TestBlockEntity::new, TEST_BLOCK).build(null));
        Registry.register(Registry.ITEM, new Identifier("test_a", "test_block"), new BlockItem(TEST_BLOCK, new Item.Settings()));
    }
}
