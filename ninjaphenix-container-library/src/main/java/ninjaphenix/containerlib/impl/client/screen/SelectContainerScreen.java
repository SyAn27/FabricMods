package ninjaphenix.containerlib.impl.client.screen;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import ninjaphenix.containerlib.client.ContainerLibraryClient;
import ninjaphenix.containerlib.impl.client.ScreenMiscSettings;
import ninjaphenix.containerlib.impl.client.screen.widget.ScreenTypeButton;

import java.util.HashMap;

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
        for (HashMap.Entry<Identifier, ScreenMiscSettings> entry : OPTIONS.entrySet())
        {
            final Identifier id = entry.getKey();
            final ScreenMiscSettings settings = entry.getValue();
            addButton(new ScreenTypeButton(x * 96 + (x - 1) * 5, 0, 96, 96,
                    settings.SELECT_TEXTURE_ID, settings.NARRATION_MESSAGE.asString(), button -> updatePlayerPreference(id)));
            x++;
        }
    }

    private void updatePlayerPreference(Identifier selection)
    {
        ContainerLibraryClient.setPreference(selection);
        ContainerLibraryClient.sendPreferencesToServer();
        MinecraftClient.getInstance().openScreen(null);
    }

    @Override
    public void render(int mouseX, int mouseY, float delta)
    {
        super.render(mouseX, mouseY, delta);
    }
}
