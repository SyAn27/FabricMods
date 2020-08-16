package ninjaphenix.expandedstorage.impl;

import net.minecraft.text.KeybindText;
import net.minecraft.text.MutableText;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

public class Const
{
    public static final Identifier SCREEN_SELECT = id("screen_select");
    public static final Identifier OPEN_SCREEN_SELECT = id("open_screen_select");
    public static final Identifier SINGLE_CONTAINER = id("single");
    public static final Identifier SCROLLABLE_CONTAINER = id("scrollable");
    public static final Identifier PAGED_CONTAINER = id("paged");
    public static final String MOD_ID = "expandedstorage";

    public static final MutableText leftShiftRightClick = new TranslatableText("tooltip.expandedstorage.left_shift_right_click",
            new KeybindText("key.sneak"), new KeybindText("key.use")).formatted(Formatting.GOLD);


    public static Identifier id(String path) { return new Identifier(MOD_ID, path); }

}
