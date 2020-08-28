package ninjaphenix.container_library.impl.common;

import com.mojang.datafixers.types.Func;
import jdk.nashorn.api.scripting.ScriptObjectMirror;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import ninjaphenix.container_library.api.ContainerLibraryAPI;
import ninjaphenix.container_library.api.common.inventory.AbstractScreenHandler.ScreenMeta;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class NewContainerLibrary implements ContainerLibraryAPI
{
    private final Map<Identifier, Pair<Identifier, TranslatableText>> containerTypes = new HashMap<>();
    private final Map<Identifier, Function<PacketByteBuf, ScreenMeta>> deserializers = new HashMap<>();

    @Override
    public void declareContainerType(final Identifier id, final Identifier texture, final TranslatableText name)
    {
        containerTypes.putIfAbsent(id, new Pair<>(texture, name));
    }

    @Override
    public void registerScreenMetaDeserializer(final Identifier id, final Function<PacketByteBuf, ScreenMeta> deserializer)
    {
        deserializers.putIfAbsent(id, deserializer);
    }

    @Override
    public Function<PacketByteBuf, ScreenMeta> getScreenMetaDeserializer(final Identifier id)
    {
        return deserializers.get(id);
    }
}