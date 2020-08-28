package ninjaphenix.container_library.api;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import ninjaphenix.container_library.impl.common.NewContainerLibrary;
import java.util.function.Function;

import static ninjaphenix.container_library.api.common.inventory.AbstractScreenHandler.ScreenMeta;

public interface ContainerLibraryAPI
{
    ContainerLibraryAPI INSTANCE = new NewContainerLibrary();

    /**
     * Declares a new container type that can be picked by clients.
     */
    void declareContainerType(final Identifier id, final Identifier texture, final TranslatableText name);

    void registerScreenMetaDeserializer(final Identifier id, Function<PacketByteBuf, ScreenMeta> deserializer);

    Function<PacketByteBuf, ScreenMeta> getScreenMetaDeserializer(final Identifier deserializerId);
}
