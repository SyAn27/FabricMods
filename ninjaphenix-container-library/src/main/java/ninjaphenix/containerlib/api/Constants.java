package ninjaphenix.containerlib.api;

import net.minecraft.util.Identifier;

public class Constants
{
    public static final String LIBRARY_ID = "ninjaphenix-container-lib";
    public static final Identifier SCREEN_SELECT = idOf("screen_select");
    public static final Identifier OPEN_SCREEN_SELECT = idOf("open_screen_select");

    public static final Identifier SINGLE_CONTAINER = idOf("single");
    public static final Identifier SCROLLABLE_CONTAINER = idOf("scrollable");
    public static final Identifier PAGED_CONTAINER = idOf("paged");

    public static Identifier idOf(String path) { return new Identifier(LIBRARY_ID, path); }
}
