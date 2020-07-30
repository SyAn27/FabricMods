package ninjaphenix.noncorrelatedextras.features.config;

import net.fabricmc.loader.api.FabricLoader;
import ninjaphenix.noncorrelatedextras.core.FeatureConfig;
import ninjaphenix.noncorrelatedextras.features.MagnetFeature;

import java.util.HashMap;

public class MagnetFeatureConfig extends FeatureConfig<MagnetFeature>
{
    public static final Boolean isTrinketLoaded = FabricLoader.getInstance().isModLoaded("trinkets");

    public MagnetFeatureConfig(final Boolean enabled) { super(enabled); }

    @Override
    public HashMap<String, Boolean> getMixins()
    {
        final HashMap<String, Boolean> map = super.getMixins();
        map.put("MagnetTrinketCompat", isEnabled() && isTrinketLoaded);
        return map;
    }

    @Override
    public MagnetFeature getFeature() { return new MagnetFeature(); }
}