package ninjaphenix.expandedstorage.mixin;

import net.minecraft.client.render.TexturedRenderLayers;
import net.minecraft.client.util.SpriteIdentifier;
import ninjaphenix.expandedstorage.impl.client.ExpandedStorageClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;

@Mixin(TexturedRenderLayers.class)
public final class TexturedRenderLayersMixin
{
    @Inject(at = @At("HEAD"), method = "addDefaultTextures(Ljava/util/function/Consumer;)V")
    private static void expandedstorage_addSprites(Consumer<SpriteIdentifier> consumer, CallbackInfo ci)
    {
        ExpandedStorageClient.INSTANCE.makeAtlas(consumer);
    }
}