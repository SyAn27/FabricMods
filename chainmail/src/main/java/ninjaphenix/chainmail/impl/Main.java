package ninjaphenix.chainmail.impl;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;

public class Main implements ModInitializer
{
    private static final boolean DEBUG = true;

    @Override
    public void onInitialize()
    {
        if (DEBUG)
        {
            final ClassLoader loader = this.getClass().getClassLoader();
            try
            {
                com.google.common.reflect.ClassPath.from(loader).getTopLevelClasses("ninjaphenix.test").forEach(c -> {
                    try
                    {

                        Object inst = loader.loadClass(c.getName()).newInstance();
                        if (inst instanceof ModInitializer)
                        {
                            ((ModInitializer) inst).onInitialize();
                        }
                    }
                    catch (InstantiationException | IllegalAccessException | ClassNotFoundException e)
                    {
                        System.out.println("Error loading class: " + c.getName());
                    }
                });
            }
            catch (IOException e)
            {
                e.printStackTrace(System.out);
            }
        }
    }
}
