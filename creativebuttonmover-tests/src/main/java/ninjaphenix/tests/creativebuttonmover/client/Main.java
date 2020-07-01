package ninjaphenix.tests.creativebuttonmover.client;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.registry.Registry;
import ninjaphenix.chainmail.api.ChainmailCommonApi;

import java.util.Random;

public class Main implements ClientModInitializer
{
    @Override
    public void onInitializeClient()
    {
        for (int i = 0; i < 20; i++)
        {
            ChainmailCommonApi.INSTANCE.registerItemGroup((index) -> new ItemGroup(index, "creativebuttonmover.tab_" + index)
            {
                @Override
                public ItemStack createIcon() { return new ItemStack(Registry.ITEM.getRandom(new Random())); }

                @Override
                public String getTranslationKey() { return "itemgroup."+getId(); }
            });
        }
    }
}
