package ninjaphenix.expandedstorage.client.models;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class RightChestModel extends SingleChestModel
{
    public RightChestModel()
    {
        super(64, 48);
        lid.addCuboid(0, 0, 0, 15, 5, 14, 0);
        lid.addCuboid(0, -2, 14, 1, 4, 1, 0);
        lid.setPivot(0, 9, 1);
        base.addCuboid(0, 0, 0, 15, 10, 14, 0);
        base.setPivot(0, 0, 1);
    }
}
