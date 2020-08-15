package ninjaphenix.expandedstorage.impl.client.models;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public final class FrontChestModel extends SingleChestModel
{
    public FrontChestModel()
    {
        super(64, 48);
        lid.addCuboid(0, 0, 15, 14, 5, 15, 0);
        lid.addCuboid(6, -2, 30, 2, 4, 1, 0);
        lid.setPivot(1, 9, -15);
        base.setTextureOffset(0, 20);
        base.addCuboid(0, 0, 0, 14, 10, 15, 0);
        base.setPivot(1, 0, 0);
    }
}
