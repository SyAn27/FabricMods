package ninjaphenix.expandedstorage.impl.block.misc;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.enums.ChestType;
import net.minecraft.util.StringIdentifiable;
import ninjaphenix.expandedstorage.impl.client.models.*;

public enum CursedChestType implements StringIdentifiable
{
    SINGLE("single"), TOP("top"), BACK("back"), RIGHT("right"), BOTTOM("bottom"), FRONT("front"), LEFT("left");

    private final String name;
    private SingleChestModel model = null;

    CursedChestType(String string) { name = string; }

    public static CursedChestType valueOf(ChestType type)
    {
        if (type == ChestType.SINGLE) { return SINGLE; }
        else if (type == ChestType.RIGHT) { return LEFT; }
        else if (type == ChestType.LEFT) { return RIGHT; }
        throw new IllegalArgumentException("Unexpected chest type passed.");
    }

    public CursedChestType getOpposite()
    {
        if (this == FRONT) { return BACK; }
        else if (this == BACK) { return FRONT; }
        else if (this == BOTTOM) { return TOP; }
        else if (this == TOP) { return BOTTOM; }
        else if (this == LEFT) { return RIGHT; }
        else if (this == RIGHT) { return LEFT; }
        throw new IllegalArgumentException("CursedChestType#getOpposite is not supported for type SINGLE");
    }

    public boolean isMainType() { return this == FRONT || this == BOTTOM || this == LEFT || this == SINGLE; }

    @Environment(EnvType.CLIENT)
    public SingleChestModel getModel()
    {
        if(model == null)
        {
            if(this == FRONT) { model = new FrontChestModel(); }
            else if(this == BACK) { model = new BackChestModel(); }
            else if(this == TOP) { model = new TopChestModel(); }
            else if(this == BOTTOM) { model = new BottomChestModel(); }
            else if(this == LEFT) { model = new LeftChestModel(); }
            else if(this == RIGHT) { model = new RightChestModel(); }
            else if(this == SINGLE) { model = new SingleChestModel(); }
        }
        return model;
    }

    public String asString() { return name; }
}