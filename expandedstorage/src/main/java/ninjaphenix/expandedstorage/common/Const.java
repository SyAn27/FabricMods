package ninjaphenix.expandedstorage.common;

import net.minecraft.text.KeybindText;
import net.minecraft.text.MutableText;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

public class Const
{
    public static final String MOD_ID = "expandedstorage";

    public static final MutableText leftShiftRightClick = new TranslatableText("tooltip.expandedstorage.left_shift_right_click",
            new KeybindText("key.sneak"), new KeybindText("key.use")).formatted(Formatting.GOLD);

    public static Identifier id(final String path) { return new Identifier(MOD_ID, path); }
}