package ninjaphenix.userdefinedadditions.constants;

import net.fabricmc.fabric.api.tools.FabricToolTags;
import net.minecraft.item.Item;
import net.minecraft.tag.Tag;

public class Tools
{
    public static final Tools INSTANCE = new Tools();
    // Maybe abstract tool types into a javascript friendly type with a .name or .id attribute
    // <editor-fold desc="Tool Types">
    public final Tag<Item> PICKAXE = FabricToolTags.PICKAXES;
    public final Tag<Item> AXE = FabricToolTags.AXES;
    public final Tag<Item> SHOVEL = FabricToolTags.SHOVELS;
    public final Tag<Item> HOE = FabricToolTags.HOES;
    public final Tag<Item> SWORD = FabricToolTags.SWORDS;
    // </editor-fold>
    // <editor-fold desc="Tool Tiers">
    public final int WOOD_TIER = 0;
    public final int STONE_TIER = 1;
    public final int IRON_TIER = 2;
    public final int DIAMOND_TIER = 3;
    // </editor-fold>
    //<editor-fold desc="Pickaxes">
    public final Tool WOOD_PICKAXE = new Tool(PICKAXE, WOOD_TIER);
    public final Tool STONE_PICKAXE = new Tool(PICKAXE, STONE_TIER);
    public final Tool IRON_PICKAXE = new Tool(PICKAXE, IRON_TIER);
    public final Tool DIAMOND_PICKAXE = new Tool(PICKAXE, DIAMOND_TIER);
    //</editor-fold>
    //<editor-fold desc="Swords">
    public final Tool WOOD_SWORD = new Tool(SWORD, WOOD_TIER);
    public final Tool STONE_SWORD = new Tool(SWORD, STONE_TIER);
    public final Tool IRON_SWORD = new Tool(SWORD, IRON_TIER);
    public final Tool DIAMOND_SWORD = new Tool(SWORD, DIAMOND_TIER);
    //</editor-fold>
    //<editor-fold desc="Shovels">
    public final Tool WOOD_SHOVEL = new Tool(SHOVEL, WOOD_TIER);
    public final Tool STONE_SHOVEL = new Tool(SHOVEL, STONE_TIER);
    public final Tool IRON_SHOVEL = new Tool(SHOVEL, IRON_TIER);
    public final Tool DIAMOND_SHOVEL = new Tool(SHOVEL, DIAMOND_TIER);
    //</editor-fold>
    //<editor-fold desc="Hoes">
    public final Tool WOOD_HOE = new Tool(HOE, WOOD_TIER);
    public final Tool STONE_HOE = new Tool(HOE, STONE_TIER);
    public final Tool IRON_HOE = new Tool(HOE, IRON_TIER);
    public final Tool DIAMOND_HOE = new Tool(HOE, DIAMOND_TIER);
    //</editor-fold>
    //<editor-fold desc="Axes">
    public final Tool WOOD_AXE = new Tool(AXE, WOOD_TIER);
    public final Tool STONE_AXE = new Tool(AXE, STONE_TIER);
    public final Tool IRON_AXE = new Tool(AXE, IRON_TIER);
    public final Tool DIAMOND_AXE = new Tool(AXE, DIAMOND_TIER);
    //</editor-fold>

    private Tools() {}

    public static class Tool
    {
        public final String type;
        public final int miningLevel;
        private final Tag<Item> _tag;

        private Tool(Tag<Item> type, int miningLevel)
        {
            this.type = type.getId().toString();
            _tag = type;
            this.miningLevel = miningLevel;
        }

        public Tag<Item> getTag()
        {
            return _tag;
        }
    }
}
