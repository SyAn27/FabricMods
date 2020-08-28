package ninjaphenix.expandedstorage.common;

import net.fabricmc.api.ModInitializer;

public final class ExpandedStorage implements ModInitializer
{
    public static final ExpandedStorage INSTANCE = new ExpandedStorage();

    private ExpandedStorage() { }

    @Override
    public void onInitialize() { ModContent.register(); }
}