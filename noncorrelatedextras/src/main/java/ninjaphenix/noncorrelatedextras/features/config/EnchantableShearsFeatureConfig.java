package ninjaphenix.noncorrelatedextras.features.config;

import ninjaphenix.noncorrelatedextras.core.MixinOnlyFeatureConfig;

import java.util.HashMap;

public class EnchantableShearsFeatureConfig extends MixinOnlyFeatureConfig
{
    public EnchantableShearsFeatureConfig(final Boolean enabled) { super(enabled); }

    @Override
    public HashMap<String, Boolean> getMixins()
    {
        final HashMap<String, Boolean> map = super.getMixins();
        map.put("EnchantableShear", isEnabled());
        return map;
    }
}