package ninjaphenix.expandedstorage.impl;

import net.fabricmc.api.ModInitializer;
import ninjaphenix.expandedstorage.impl.content.ModContent;

public final class ExpandedStorage implements ModInitializer
{
    @Override
    public void onInitialize() { ModContent.register(); }
}