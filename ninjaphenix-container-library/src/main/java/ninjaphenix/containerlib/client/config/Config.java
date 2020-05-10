package ninjaphenix.containerlib.client.config;

import blue.endless.jankson.Comment;

public class Config
{
    @Comment("\nEnables auto focus of the search bar as soon as screen is opened.")
    public final Boolean auto_focus_searchbar = Boolean.FALSE; // Not sure why primitives aren't working, but eh.
}
