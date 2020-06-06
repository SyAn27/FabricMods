package ninjaphenix.containerlib.api;

import net.minecraft.util.Identifier;

public class Constants
{
    public static final String LIBRARY_ID = "ninjaphenix-container-lib";
    public static final Identifier OPEN_SCREEN_SELECT = idOf("open_screen_select");

    public static Identifier idOf(String path) { return new Identifier(LIBRARY_ID, path); }
}
