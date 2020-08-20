package ninjaphenix.expandedstorage.client.models;

public final class TopChestModel extends SingleChestModel
{
    public TopChestModel()
    {
        super(64, 48);
        lid.addCuboid(0, 0, 0, 14, 5, 14, 0);
        lid.addCuboid(6, -2, 14, 2, 4, 1, 0);
        lid.setPivot(1, 9, 1);
        base.addCuboid(0, 0, 0, 14, 10, 14, 0);
        base.setPivot(1, 0, 1);
    }
}