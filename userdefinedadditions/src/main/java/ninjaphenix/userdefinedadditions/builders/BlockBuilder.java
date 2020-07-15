package ninjaphenix.userdefinedadditions.builders;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.block.MaterialColor;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import ninjaphenix.userdefinedadditions.api.RegistrableBuilder;

import static ninjaphenix.userdefinedadditions.constants.Tools.Tool;

@SuppressWarnings("unused")
public class BlockBuilder implements RegistrableBuilder<Block>
{
    private final FabricBlockSettings settings;
    private Item.Settings itemSettings;

    public BlockBuilder(FabricBlockSettings settings) { this.settings = settings; }

    // <editor-fold desc="Builder Methods">
    public BlockBuilder hardness(float hardness)
    {
        settings.hardness(hardness);
        return this;
    }

    public BlockBuilder resistance(float resistance)
    {
        settings.resistance(resistance);
        return this;
    }

    public BlockBuilder strength(float hardness, float resistance)
    {
        settings.strength(hardness, resistance);
        return this;
    }

    public BlockBuilder breakByTool(Tool tool)
    {
        settings.breakByTool(tool.getTag(), tool.miningLevel);
        return this;
    }

    public BlockBuilder breakByHand()
    {
        settings.breakByHand(true);
        return this;
    }

    public BlockBuilder breakInstantly()
    {
        settings.breakInstantly();
        return this;
    }

    public BlockBuilder noCollision()
    {
        settings.noCollision();
        return this;
    }

    public BlockBuilder lightLevel(int level)
    {
        settings.lightLevel(level);
        return this;
    }

    public BlockBuilder slipperiness(float slipperiness)
    {
        settings.slipperiness(slipperiness);
        return this;
    }

    public BlockBuilder dropsNothing()
    {
        settings.dropsNothing();
        return this;
    }

    public BlockBuilder dropsLike(String block)
    {
        settings.dropsLike(Registry.BLOCK.get(new Identifier(block)));
        return this;
    }

    public BlockBuilder drops(String table)
    {
        settings.drops(new Identifier(table));
        return this;
    }

    public BlockBuilder dynamicBounds()
    {
        settings.dynamicBounds();
        return this;
    }

    public BlockBuilder item(ItemBuilder item)
    {
        itemSettings = item.getSettings();
        return this;
    }

    public BlockBuilder item()
    {
        itemSettings = new Item.Settings();
        return this;
    }
    // </editor-fold>

    @Override
    public Block register(String id)
    {
        Block b = Registry.register(Registry.BLOCK, id, build());
        if (itemSettings != null) { Registry.register(Registry.ITEM, id, new BlockItem(b, itemSettings)); }
        return b;
    }

    @Override
    public Block build() { return new Block(settings); }

    public static class Factory
    {
        public static final Factory INSTANCE = new Factory();

        private Factory() {}

        public BlockBuilder of(Material material) { return new BlockBuilder(FabricBlockSettings.of(material)); }

        public BlockBuilder of(Material material, MaterialColor color) { return new BlockBuilder(FabricBlockSettings.of(material, color)); }
    }
}
