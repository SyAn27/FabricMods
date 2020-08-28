package ninjaphenix.container_library.impl;

import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.screen.ScreenHandlerType;
import ninjaphenix.container_library.impl.client.screen.PagedScreen;
import ninjaphenix.container_library.impl.client.screen.ScrollableScreen;
import ninjaphenix.container_library.impl.client.screen.SingleScreen;
import ninjaphenix.container_library.impl.common.Const;
import ninjaphenix.container_library.impl.common.inventory.PagedScreenHandler;
import ninjaphenix.container_library.impl.common.inventory.ScrollableScreenHandler;
import ninjaphenix.container_library.impl.common.inventory.SingleScreenHandler;

public final class BuiltinScreenTypes
{
    public static final ScreenHandlerType<PagedScreenHandler> PAGED_HANDLER_TYPE;
    public static final ScreenHandlerType<SingleScreenHandler> SINGLE_HANDLER_TYPE;
    public static final ScreenHandlerType<ScrollableScreenHandler> SCROLLABLE_HANDLER_TYPE;

    static {
        SCROLLABLE_HANDLER_TYPE = ScreenHandlerRegistry.registerExtended(Const.id("scrollable"), new ScrollableScreenHandler.Factory());
        PAGED_HANDLER_TYPE = ScreenHandlerRegistry.registerExtended(Const.id("paged"), new PagedScreenHandler.Factory());
        SINGLE_HANDLER_TYPE = ScreenHandlerRegistry.registerExtended(Const.id("single"), new SingleScreenHandler.Factory());
    }

    public static void register() { }

    public static void registerScreens()
    {
        ScreenRegistry.register(SCROLLABLE_HANDLER_TYPE, ScrollableScreen::new);
        ScreenRegistry.register(PAGED_HANDLER_TYPE, PagedScreen::new);
        ScreenRegistry.register(SINGLE_HANDLER_TYPE, SingleScreen::new);
    }
}
