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

    public static CursedChestType valueOf(final ChestType type)
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

    public String asString() { return name; }
}