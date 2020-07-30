package ninjaphenix.chainmail.api;

import net.minecraft.item.ItemGroup;
import ninjaphenix.chainmail.impl.ChainmailCommonImpl;

import java.util.function.IntFunction;

public interface ChainmailCommonApi
{
    ChainmailCommonApi INSTANCE = ChainmailCommonImpl.INSTANCE;
    <T extends ItemGroup> T registerItemGroup(final IntFunction<T> itemGroup);
}