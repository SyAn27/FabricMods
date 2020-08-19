package ninjaphenix.expandedstorage.impl.client;

import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public final class ScreenMiscSettings
{
    public final Identifier SELECT_TEXTURE_ID;
    public final Text NARRATION_MESSAGE;

    public ScreenMiscSettings(Identifier selectTextureId, Text narrationMessage)
    {
        SELECT_TEXTURE_ID = selectTextureId;
        NARRATION_MESSAGE = narrationMessage;
    }
}
