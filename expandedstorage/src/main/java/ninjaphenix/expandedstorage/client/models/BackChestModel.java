package ninjaphenix.expandedstorage.client.models;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class BackChestModel extends SingleChestModel
{
    public BackChestModel()
    {
        // LongChestModel
        super(96, 80);
        lid.addCuboid(0, 0, 0, 14, 5, 15, 0);
        lid.setPivot(1, 9, 1);
        base.setTextureOffset(0, 0);
        base.addCuboid(0, 0, 0, 14, 10, 15, 0);
        base.setPivot(1, 0, 1);
    }
}
