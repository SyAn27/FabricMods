package ninjaphenix.expandedstorage.client.models;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class LeftChestModel extends SingleChestModel
{

    public LeftChestModel()
    {
        super(64, 48);
        lid.addCuboid(0, 0, 0, 15, 5, 14, 0);
        lid.addCuboid(14, -2, 14, 1, 4, 1, 0);
        lid.setPivot(1, 9, 1);
        base.addCuboid(0, 0, 0, 15, 10, 14, 0);
        base.setPivot(1, 0, 1);
    }
}
