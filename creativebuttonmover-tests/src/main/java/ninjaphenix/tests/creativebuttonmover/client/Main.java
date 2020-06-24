package ninjaphenix.tests.creativebuttonmover.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.Random;

public class Main implements ClientModInitializer
{
    @Override
    public void onInitializeClient()
    {
        for (int i = 0; i < 20; i++)
        {
            FabricItemGroupBuilder.build(new Identifier("creativebuttonmover", "tab_" + i),
                    () -> new ItemStack(Registry.ITEM.getRandom(new Random()), 1));
        }
    }
}
