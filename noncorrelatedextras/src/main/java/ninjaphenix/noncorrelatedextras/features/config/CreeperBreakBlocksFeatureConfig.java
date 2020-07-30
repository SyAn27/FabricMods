package ninjaphenix.noncorrelatedextras.features.config;

import ninjaphenix.noncorrelatedextras.core.MixinOnlyFeatureConfig;

import java.util.HashMap;

public class CreeperBreakBlocksFeatureConfig extends MixinOnlyFeatureConfig
{
    public CreeperBreakBlocksFeatureConfig(final Boolean enabled) { super(enabled); }

    @Override
    public HashMap<String, Boolean> getMixins()
    {
        final HashMap<String, Boolean> map = super.getMixins();
        map.put("CreeperBreakBlocks", isEnabled());
        return map;
    }
}