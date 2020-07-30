package ninjaphenix.noncorrelatedextras.features.config;

import ninjaphenix.noncorrelatedextras.core.FeatureConfig;
import ninjaphenix.noncorrelatedextras.features.StructureCompassFeature;

import java.util.HashMap;

public class StructureCompassFeatureConfig extends FeatureConfig<StructureCompassFeature>
{
    public StructureCompassFeatureConfig(final Boolean enabled) { super(enabled); }

    @Override
    public StructureCompassFeature getFeature() { return new StructureCompassFeature(); }

    @Override
    public HashMap<String, Boolean> getMixins()
    {
        final HashMap<String, Boolean> map = super.getMixins();
        map.put("ItemStackEqualMixin", isEnabled());
        return map;
    }
}