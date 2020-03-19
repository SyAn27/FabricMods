package ninjaphenix.chainmail.impl;

import com.google.common.reflect.ClassPath;
import net.fabricmc.api.ModInitializer;

import java.io.IOException;

public class Main implements ModInitializer
{
    private static final boolean DEBUG = true;

    @Override
    public void onInitialize()
    {
        if (DEBUG)
        {
            try
            {
                ClassPath.from(this.getClass().getClassLoader()).getTopLevelClasses("ninjaphenix.test").forEach(c -> {
                    try
                    {
                        Object inst = c.load().newInstance();
                        if (inst instanceof ModInitializer)
                        {
                            ((ModInitializer) inst).onInitialize();
                        }
                    }
                    catch (InstantiationException | IllegalAccessException e)
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
