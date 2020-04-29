package ninjaphenix.tests.chainmail.block.entity;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import ninjaphenix.chainmail.api.blockentity.ExpandedBlockEntity;

public class TestBlockEntity extends BlockEntity implements ExpandedBlockEntity
{
    public TestBlockEntity()
    {
        super(Registry.BLOCK_ENTITY_TYPE.get(new Identifier("test_a", "test_block_entity")));
    }

    @Override
    public void onLoad() { System.out.println("Test block entity instance loaded."); }

    @Override
    public void onUnload() { System.out.println("Test block entity instance unloaded."); }
}
