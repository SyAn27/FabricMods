package ninjaphenix.noncorrelatedextras.features;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import ninjaphenix.noncorrelatedextras.Main;
import ninjaphenix.noncorrelatedextras.config.Config;
import ninjaphenix.noncorrelatedextras.core.Feature;
import ninjaphenix.noncorrelatedextras.core.ItemAdder;
import ninjaphenix.noncorrelatedextras.items.StructureCompassItem;

import java.util.Set;

public class StructureCompassFeature extends Feature implements ItemAdder
{
    @Override
    public void registerItems()
    {
        final Set<Identifier> structures = Config.INSTANCE.getEnabledStructureCompasses();
        final Item.Settings settings = new Item.Settings().maxCount(1).group(ItemGroup.TOOLS);
        for (Identifier structure : structures)
        {
            StructureCompassItem item = new StructureCompassItem(settings, structure.getPath());
            Registry.register(Registry.ITEM, Main.getId(structure + "_compass"), item);
        }
    }
}
