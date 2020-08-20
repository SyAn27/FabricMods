package ninjaphenix.expandedstorage;

import net.fabricmc.api.ModInitializer;
import ninjaphenix.expandedstorage.content.ModContent;

public final class ExpandedStorage implements ModInitializer
{
    @Override
    public void onInitialize() { ModContent.register(); }
}