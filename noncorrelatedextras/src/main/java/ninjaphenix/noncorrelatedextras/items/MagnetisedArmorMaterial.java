package ninjaphenix.noncorrelatedextras.items;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.ArmorMaterials;
import net.minecraft.recipe.Ingredient;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.registry.Registry;
import ninjaphenix.noncorrelatedextras.Main;

public class MagnetisedArmorMaterial implements ArmorMaterial
{
    private static final ArmorMaterial BASE = ArmorMaterials.CHAIN;

    @Override
    public int getDurability(final EquipmentSlot slot) { return BASE.getDurability(slot); }

    @Override
    public int getProtectionAmount(final EquipmentSlot slot) { return BASE.getProtectionAmount(slot); }

    @Override
    public int getEnchantability() { return BASE.getEnchantability(); }

    @Override
    public SoundEvent getEquipSound() { return BASE.getEquipSound(); }

    @Override
    public Ingredient getRepairIngredient() { return Ingredient.ofItems(Registry.ITEM.get(Main.getId("polarized_iron_ingot"))); }

    @Override
    public String getName() { return "polarized_iron"; }

    @Override
    public float getToughness() { return BASE.getToughness(); }

    @Override
    public float getKnockbackResistance() { return BASE.getKnockbackResistance(); }
}