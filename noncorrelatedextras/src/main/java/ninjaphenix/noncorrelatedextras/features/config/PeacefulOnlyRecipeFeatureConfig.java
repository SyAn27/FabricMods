package ninjaphenix.noncorrelatedextras.features.config;

import ninjaphenix.noncorrelatedextras.core.FeatureConfig;
import ninjaphenix.noncorrelatedextras.features.PeacefulOnlyRecipeFeature;

public class PeacefulOnlyRecipeFeatureConfig extends FeatureConfig<PeacefulOnlyRecipeFeature>
{
    public PeacefulOnlyRecipeFeatureConfig(final Boolean enabled) { super(enabled); }

    @Override
    public PeacefulOnlyRecipeFeature getFeature() { return new PeacefulOnlyRecipeFeature(); }
}
