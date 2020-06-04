package ninjaphenix.noncorrelatedextras.features;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.registry.Registry;
import ninjaphenix.noncorrelatedextras.Main;
import ninjaphenix.noncorrelatedextras.config.Config;
import ninjaphenix.noncorrelatedextras.core.Feature;
import ninjaphenix.noncorrelatedextras.core.ItemAdder;
import ninjaphenix.noncorrelatedextras.items.StructureCompassItem;

import java.util.Locale;
import java.util.Set;

public class StructureCompassFeature extends Feature implements ItemAdder
{
    @Override
    public void registerItems()
    {
        final Set<String> structures = Config.INSTANCE.getEnabledStructureCompasses();
        final Item.Settings settings = new Item.Settings().maxCount(1).group(ItemGroup.TOOLS);
        for (String structure : structures)
        {
            structure = structure.toLowerCase(Locale.ROOT);
            StructureCompassItem item = new StructureCompassItem(settings, structure);
            Registry.register(Registry.ITEM, Main.getId(structure + "_compass"), item);
        }
    }
}
