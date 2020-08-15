package ninjaphenix.expandedstorage.api;

import net.minecraft.util.Identifier;
import ninjaphenix.expandedstorage.impl.ExpandedStorage;

public class Constants
{
    public static final Identifier SCREEN_SELECT = ExpandedStorage.id("screen_select");
    public static final Identifier OPEN_SCREEN_SELECT = ExpandedStorage.id("open_screen_select");

    public static final Identifier SINGLE_CONTAINER = ExpandedStorage.id("single");
    public static final Identifier SCROLLABLE_CONTAINER = ExpandedStorage.id("scrollable");
    public static final Identifier PAGED_CONTAINER = ExpandedStorage.id("paged");
}
