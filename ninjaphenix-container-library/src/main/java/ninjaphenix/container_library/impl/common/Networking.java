package ninjaphenix.container_library.impl.common;

import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.fabricmc.fabric.api.network.PacketContext;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import ninjaphenix.container_library.api.common.inventory.AbstractScreenHandler;
import ninjaphenix.container_library.api.common.inventory.AbstractScreenHandler.ScreenMeta;

public class Networking
{
    public static final Networking INSTANCE = new Networking();

    private Networking() { }

    public void registerClientHandlers()
    {

    }

    public void registerServerHandlers()
    {
        ServerSidePacketRegistry.INSTANCE.register(Const.OPEN_CONTAINER, this::handleOpenContainerRequest);
    }

    private void handleOpenContainerRequest(final PacketContext context, final PacketByteBuf buffer)
    {
        final BlockPos pos = buffer.readBlockPos();
        final Identifier preference = buffer.readIdentifier();
        final Identifier screenMetaDeserializeId = buffer.readIdentifier();
        final ScreenMeta meta = NewContainerLibrary.INSTANCE.getScreenMetaDeserializer(screenMetaDeserializeId).apply(buffer);
        // todo: finish tomorrow.
    }

    public void requestContainerOpen(final BlockPos pos, final Identifier preference, final ScreenMeta meta)
    {
        final PacketByteBuf buffer = new PacketByteBuf(Unpooled.buffer());
        buffer.writeBlockPos(pos);
        meta.serialize(buffer);
        ClientSidePacketRegistry.INSTANCE.sendToServer(Const.OPEN_CONTAINER, buffer);
    }
}
