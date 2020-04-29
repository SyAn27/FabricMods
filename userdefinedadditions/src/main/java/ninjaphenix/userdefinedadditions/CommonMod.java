package ninjaphenix.userdefinedadditions;

import com.google.common.collect.ImmutableSet;
import io.github.cottonmc.staticdata.StaticData;
import io.github.cottonmc.staticdata.StaticDataItem;
import net.fabricmc.api.ModInitializer;
import ninjaphenix.userdefinedadditions.builders.BlockBuilder;
import ninjaphenix.userdefinedadditions.builders.FoodComponentBuilder;
import ninjaphenix.userdefinedadditions.builders.ItemBuilder;
import ninjaphenix.userdefinedadditions.constants.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.function.Supplier;

public class CommonMod implements ModInitializer
{
    private final Logger logger = LogManager.getLogger();

    @Override
    public void onInitialize()
    {
        ImmutableSet<StaticDataItem> scriptFiles = StaticData.getAllInDirectory("uda_scripts");
        if (scriptFiles.size() > 0)
        {
            ScriptEngineManager manager = new ScriptEngineManager();
            exposeContent(manager);
            for (StaticDataItem scriptFile : scriptFiles)
            {
                String filePath = scriptFile.getIdentifier().getPath();
                int dotIndex = filePath.lastIndexOf('.');
                if (dotIndex != -1 && dotIndex < filePath.length() - 1) // Is there a . and is it not the last character.
                {
                    String fileExtension = filePath.substring(dotIndex + 1);
                    ScriptEngine engine = manager.getEngineByExtension(fileExtension);
                    if (engine == null)
                    {
                        logger.error("Error loading content for {}, {} files aren't supported.", scriptFile.getIdentifier(), fileExtension);
                    }
                    else
                    {
                        try
                        {
                            engine.eval(new InputStreamReader(scriptFile.createInputStream()));
                        }
                        catch (IOException e) // maybe warn users with some gui that content maybe missing
                        {
                            logger.error("Some IO exception was thrown.");
                            e.printStackTrace();
                        }
                        catch (ScriptException e)
                        {
                            logger.error("Some script exception was thrown.");
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        else // print or maybe show via some gui a message instructing on how to get started with mod.
        {
            logger.info("UDA found no script files, for help getting started see: https://www.helpwithmymod.exampleurl/");
        }
    }

    private void exposeContent(ScriptEngineManager manager)
    {
        manager.put("Block", BlockBuilder.Factory.INSTANCE);
        manager.put("Item", (Supplier<ItemBuilder>) ItemBuilder::new);
        manager.put("FoodComponent", (Supplier<FoodComponentBuilder>) FoodComponentBuilder::new);
        manager.put("Tool", Tools.INSTANCE);
        manager.put("MaterialColor", MaterialColors.INSTANCE);
        manager.put("Material", Materials.INSTANCE);
        manager.put("ItemGroup", ItemGroups.INSTANCE);
        manager.put("StatusEffect", _StatusEffects.INSTANCE);
    }
}
