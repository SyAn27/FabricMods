package ninjaphenix.creativebuttonmover.client;

import net.minecraft.util.Identifier;

public class Config
{
    public static Config INSTANCE;

    public Boolean UseCustomButtons = Boolean.TRUE;

    public Button PrevButton = new Button(167, 4, 12, 12, new Identifier("creativebuttonmover", "textures/gui/creativebuttons/prev/default.png"), 12, 12);
    public Button NextButton = new Button(179, 4, 12, 12, new Identifier("creativebuttonmover", "textures/gui/creativebuttons/next/default.png"), 12, 12);

    public static class Button
    {
        public Integer x;
        public Integer y;
        public final Integer width;
        public final Integer height;
        public final Identifier texture;
        private final Integer textureWidth;
        private final Integer textureHeight;

        Button(Integer x, Integer y, Integer width, Integer height, Identifier texture, Integer textureWidth, Integer textureHeight)
        {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.texture = texture;
            this.textureWidth = textureWidth;
            this.textureHeight = textureHeight;
        }
    }
}