package torcherino.mixin;

import net.minecraft.client.particle.FlameParticle;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import torcherino.Torcherino;

@Mixin(ParticleManager.class)
public abstract class ParticleMixin
{

    @Shadow
    protected abstract <T extends ParticleEffect> void registerFactory(ParticleType<T> particleType, ParticleManager.SpriteAwareFactory<T> spriteAwareFactory);

    @Inject(method = "registerDefaultFactories", at = @At("TAIL"))
    private void registerAdditionalFactories(CallbackInfo ci)
    {
        Torcherino.particles.forEach(pt -> registerFactory(pt, FlameParticle.Factory::new));
    }

}
