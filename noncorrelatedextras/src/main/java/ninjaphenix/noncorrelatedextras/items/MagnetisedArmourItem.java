package ninjaphenix.noncorrelatedextras.items;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.ClampedEntityAttribute;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributeModifier.Operation;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import ninjaphenix.noncorrelatedextras.config.Config;

import java.util.UUID;

public class MagnetisedArmourItem extends ArmorItem
{
    public static final EntityAttribute MAGNET_RANGE = new ClampedEntityAttribute("generic.max_magnet_range", 0, 0, Double.MAX_VALUE);
    private static final ArmorMaterial MATERIAL = new MagnetisedArmorMaterial();
    private final Multimap<EntityAttribute, EntityAttributeModifier> attributeModifiers;

    public MagnetisedArmourItem(final EquipmentSlot slot, final Settings settings)
    {
        super(MATERIAL, slot, settings);
        final int magnetModifier = Config.INSTANCE.getAdditionalMagnetRange(slot);
        attributeModifiers = ImmutableMultimap.<EntityAttribute, EntityAttributeModifier> builder()
                .putAll(super.getAttributeModifiers(slot))
                .put(MAGNET_RANGE, new EntityAttributeModifier(UUID.randomUUID(), "Magnet modifier", magnetModifier, Operation.ADDITION))
                .build();
    }

    @Override
    public Multimap<EntityAttribute, EntityAttributeModifier> getAttributeModifiers(final EquipmentSlot slot)
    {
        return this.slot == slot ? attributeModifiers : super.getAttributeModifiers(slot);
    }
}