package ninjaphenix.expandedstorage.impl;

import net.fabricmc.api.ModInitializer;

public final class ExpandedStorage implements ModInitializer
{
    @Override
    public void onInitialize()
    {
        ModContent.registerBlockEntities();
        ModContent.registerBlocks();
        ModContent.registerItems();
    }
}