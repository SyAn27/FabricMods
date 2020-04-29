package ninjaphenix.userdefinedadditions.builders;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.FoodComponent;
import ninjaphenix.userdefinedadditions.api.Builder;

public class FoodComponentBuilder implements Builder<FoodComponent>
{
    private final FoodComponent.Builder builder = new FoodComponent.Builder();

    public FoodComponentBuilder hunger(int hunger) { builder.hunger(hunger); return this; }

    public FoodComponentBuilder saturation(float saturation) { builder.saturationModifier(saturation); return this; }

    public FoodComponentBuilder meat() { builder.meat(); return this; }

    public FoodComponentBuilder alwaysEdible() { builder.alwaysEdible(); return this; }

    public FoodComponentBuilder snack() { builder.snack(); return this; }

    public FoodComponentBuilder effect(StatusEffect effect, int duration, int amplifier, boolean hidden, float chance)
    {
        builder.statusEffect(new StatusEffectInstance(effect, duration, amplifier, false, !hidden), chance); return this;
    }

    @Override
    public FoodComponent build() { return builder.build(); }
}
