package ninjaphenix.chainmail.api.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.server.network.ServerPlayerEntity;
import java.util.Arrays;

/**
 * @since 0.0.1
 */
@FunctionalInterface
public interface PlayerConnectCallback
{
    Event<PlayerConnectCallback> EVENT = EventFactory.createArrayBacked(PlayerConnectCallback.class, listeners ->
            (player) -> Arrays.stream(listeners).forEach(listener -> listener.onPlayerConnected(player)));

    void onPlayerConnected(ServerPlayerEntity player);
}