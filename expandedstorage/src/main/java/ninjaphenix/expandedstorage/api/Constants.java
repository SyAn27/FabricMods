package ninjaphenix.expandedstorage.api;

import net.minecraft.util.Identifier;

public class Constants
{
    public static final String LIBRARY_ID = "expandedstorage";
    public static final String ENTRY_POINT_ID = "ninjaphenix-container-library-screens";
    public static final Identifier SCREEN_SELECT = id("screen_select");
    public static final Identifier OPEN_SCREEN_SELECT = id("open_screen_select");

    public static final Identifier SINGLE_CONTAINER = id("single");
    public static final Identifier SCROLLABLE_CONTAINER = id("scrollable");
    public static final Identifier PAGED_CONTAINER = id("paged");

    public static Identifier id(String path) { return new Identifier(LIBRARY_ID, path); }
}
