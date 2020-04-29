package ninjaphenix.userdefinedadditions.builders;

import net.minecraft.item.FoodComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.minecraft.util.registry.Registry;
import ninjaphenix.userdefinedadditions.api.RegistrableBuilder;

public class ItemBuilder implements RegistrableBuilder<Item>
{
    private final Item.Settings settings = new Item.Settings();

    // <editor-fold desc="Builder Methods>
    public ItemBuilder maxCount(int count) { settings.maxCount(count); return this; }

    public ItemBuilder maxDamage(int damage) { settings.maxDamage(damage); return this; }

    public ItemBuilder remainder(String remainderId) { settings.recipeRemainder(Registry.ITEM.get(new Identifier(remainderId))); return this; }

    public ItemBuilder rarity(String rarity)
    {
        Rarity parsed = Rarity.COMMON;
        switch (rarity.toUpperCase())
        {
            case "COMMON":
                parsed = Rarity.COMMON; break;
            case "RARE":
                parsed = Rarity.RARE; break;
            case "UNCOMMON":
                parsed = Rarity.UNCOMMON; break;
            case "EPIC":
                parsed = Rarity.EPIC; break;
        }
        settings.rarity(parsed);
        return this;
    }

    public ItemBuilder group(ItemGroup group)
    {
        settings.group(group);
        return this;
    }

    public ItemBuilder foodOf(String of)
    {
        //TODO: handle if not food
        Item item = Registry.ITEM.get(new Identifier(of));
        if (item.isFood())
        {
            settings.food(item.getFoodComponent());
        }
        return this;
    }

    public ItemBuilder food(FoodComponent food)
    {
        settings.food(food);
        return this;
    }
    //</editor-fold>

    @Override
    public Item build() { return new Item(settings); }

    @Override
    public Item register(String id) { return Registry.register(Registry.ITEM, id, build()); }

    public Item.Settings getSettings() { return settings; }
}
