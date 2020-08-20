package ninjaphenix.expandedstorage.client.models;

public final class BottomChestModel extends SingleChestModel
{
    public BottomChestModel()
    {
        super(64, 32);
        base.setTextureOffset(0, 0);
        base.addCuboid(0, 0, 0, 14, 16, 14, 0);
        base.setPivot(1, 0, 1);
    }
}