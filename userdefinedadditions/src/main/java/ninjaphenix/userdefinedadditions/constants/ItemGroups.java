package ninjaphenix.userdefinedadditions.constants;

import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;

import java.util.HashMap;

public class ItemGroups
{
    public static final ItemGroups INSTANCE = new ItemGroups();
    public final ItemGroup BREWING = ItemGroup.BREWING;
    public final ItemGroup BUILDING_BLOCKS = ItemGroup.BUILDING_BLOCKS;
    public final ItemGroup COMBAT = ItemGroup.COMBAT;
    public final ItemGroup DECORATIONS = ItemGroup.DECORATIONS;
    public final ItemGroup FOOD = ItemGroup.FOOD;
    public final ItemGroup MATERIALS = ItemGroup.MATERIALS;
    public final ItemGroup MISC = ItemGroup.MISC;
    public final ItemGroup REDSTONE = ItemGroup.REDSTONE;
    public final ItemGroup TRANSPORTATION = ItemGroup.TRANSPORTATION;
    public final ItemGroup TOOLS = ItemGroup.TOOLS;
    private final HashMap<Identifier, ItemGroup> itemGroups;

    private ItemGroups()
    {
        itemGroups = new HashMap<>();
        itemGroups.put(new Identifier("brewing"), BREWING);
        itemGroups.put(new Identifier("building_blocks"), BUILDING_BLOCKS);
        itemGroups.put(new Identifier("combat"), COMBAT);
        itemGroups.put(new Identifier("decorations"), DECORATIONS);
        itemGroups.put(new Identifier("food"), FOOD);
        itemGroups.put(new Identifier("materials"), MATERIALS);
        itemGroups.put(new Identifier("misc"), MISC);
        itemGroups.put(new Identifier("redstone"), REDSTONE);
        itemGroups.put(new Identifier("transportation"), TRANSPORTATION);
        itemGroups.put(new Identifier("tools"), TOOLS);
    }

    public ItemGroup get(String id) { return itemGroups.getOrDefault(new Identifier(id), null); }

    public void put(Identifier id, ItemGroup group) { itemGroups.putIfAbsent(id, group); }
}
