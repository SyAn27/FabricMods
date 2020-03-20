package ninjaphenix.chainmail.impl;

import net.fabricmc.api.ModInitializer;

import java.io.IOException;

public class Main implements ModInitializer
{
    private static final boolean DEBUG = true;

    @Override
    public void onInitialize()
    {
        // todo: Consider making this a separate sub-project,
        //  whilst may require some initial effort will mean there's no pre-release steps.
        //  would also shrink mod size by an insignificant amount.
        //  also would simplify the build process.
        //  negatives: delay switching between tests and code
        //  clutters root folder
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
