package ninjaphenix.expandedstorage.api;

import net.minecraft.util.Identifier;
import ninjaphenix.expandedstorage.ExpandedStorage;

public class Constants
{
    public static final String ENTRY_POINT_ID = "ninjaphenix-container-library-screens";
    public static final Identifier SCREEN_SELECT = ExpandedStorage.getId("screen_select");
    public static final Identifier OPEN_SCREEN_SELECT = ExpandedStorage.getId("open_screen_select");

    public static final Identifier SINGLE_CONTAINER = ExpandedStorage.getId("single");
    public static final Identifier SCROLLABLE_CONTAINER = ExpandedStorage.getId("scrollable");
    public static final Identifier PAGED_CONTAINER = ExpandedStorage.getId("paged");
}
