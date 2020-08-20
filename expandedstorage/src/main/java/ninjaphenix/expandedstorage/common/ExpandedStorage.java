package ninjaphenix.expandedstorage.common;

import net.fabricmc.api.ModInitializer;
import ninjaphenix.expandedstorage.common.content.ModContent;

public final class ExpandedStorage implements ModInitializer
{
    @Override
    public void onInitialize() { ModContent.register(); }
}