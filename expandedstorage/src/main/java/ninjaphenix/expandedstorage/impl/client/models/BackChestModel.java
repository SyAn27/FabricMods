package ninjaphenix.expandedstorage.impl.client.models;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public final class BackChestModel extends SingleChestModel
{
    public BackChestModel()
    {
        super(48, 48);
        lid.addCuboid(0, 0, 0, 14, 5, 15, 0);
        lid.setPivot(1, 9, 1);
        base.setTextureOffset(0, 20);
        base.addCuboid(0, 0, 0, 14, 10, 15, 0);
        base.setPivot(1, 0, 1);
    }
}
