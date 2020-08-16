package ninjaphenix.expandedstorage.mixin;

import net.minecraft.client.MinecraftClientGame;
import ninjaphenix.expandedstorage.impl.client.ContainerLibraryClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClientGame.class)
public class ClientGameStart
{
    @Inject(method = "onStartGameSession", at = @At("TAIL"))
    private void expandedstorage_onStartGameSession(CallbackInfo ci) { ContainerLibraryClient.sendPreferencesToServer(); }
}