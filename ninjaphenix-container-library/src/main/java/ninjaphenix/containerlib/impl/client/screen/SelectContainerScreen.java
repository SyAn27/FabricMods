package ninjaphenix.containerlib.impl.client.screen;

import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.PacketByteBuf;
import ninjaphenix.containerlib.api.Constants;
import ninjaphenix.containerlib.impl.client.ScreenMiscSettings;
import ninjaphenix.containerlib.impl.client.screen.widget.ScreenTypeButton;

import java.util.HashMap;
import java.util.Map;

public class SelectContainerScreen extends Screen
{
    private final HashMap<Identifier, ScreenMiscSettings> OPTIONS;

    public SelectContainerScreen(HashMap<Identifier, ScreenMiscSettings> options)
    {
        super(new TranslatableText("screen.ninjaphenix-container-lib.screen_picker_title"));
        OPTIONS = options;
    }

    @Override
    protected void init()
    {
        super.init();
        int x = 0;
        for (Map.Entry<Identifier, ScreenMiscSettings> entry : OPTIONS.entrySet())
        {
            final Identifier id = entry.getKey();
            final ScreenMiscSettings settings = entry.getValue();
            SelectContainerScreen.this.addButton(new ScreenTypeButton(x * 50 + (x - 1) * 5, 0, 50, 50,
                    settings.SELECT_TEXTURE_ID, settings.NARRATION_MESSAGE.asString(), button -> sendToServer(id)));
            x++;
        }
    }

    private void sendToServer(Identifier selection)
    {
        PacketByteBuf buffer = new PacketByteBuf(Unpooled.buffer());
        buffer.writeIdentifier(selection);
        ClientSidePacketRegistry.INSTANCE.sendToServer(Constants.SCREEN_SELECT, buffer);
        MinecraftClient.getInstance().openScreen(null);
    }

    @Override
    public void render(int mouseX, int mouseY, float delta)
    {
        super.render(mouseX, mouseY, delta);
    }
}
