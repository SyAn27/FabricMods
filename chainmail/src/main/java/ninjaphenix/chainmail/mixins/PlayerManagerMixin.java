package ninjaphenix.chainmail.mixins;

import net.minecraft.network.ClientConnection;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import ninjaphenix.chainmail.api.events.PlayerConnectCallback;
import ninjaphenix.chainmail.api.events.PlayerDisconnectCallback;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerManager.class)
public abstract class PlayerManagerMixin
{
    @Inject(method = "onPlayerConnect", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/server/world/ServerWorld;onPlayerConnected(Lnet/minecraft/server/network/ServerPlayerEntity;)V"))
    private void chainmail_onPlayerConnected(final ClientConnection connection, final ServerPlayerEntity player, final CallbackInfo ci)
    {
        PlayerConnectCallback.EVENT.invoker().onPlayerConnected(player);
    }

    @Inject(method = "remove", at = @At("HEAD"))
    private void chainmail_onPlayerDisconnected(final ServerPlayerEntity player, final CallbackInfo ci)
    {
        PlayerDisconnectCallback.EVENT.invoker().onPlayerDisconnected(player);
    }
}