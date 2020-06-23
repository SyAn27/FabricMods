package ninjaphenix.containerlib.api;

/**
 * Entry point for declaring different screen types and adding different sizes of screens. Entry point ID: ninjaphenix-container-library-screens
 *
 * @see ninjaphenix.containerlib.impl.DefaultScreenSizes for the inbuilt screen types and sizes.
 */
public interface ContainerLibraryExtension
{
    /**
     * This is where you should register callbacks for when a screen size is registered. {@link ContainerLibraryAPI#declareScreenSizeRegisterCallback}
     */
    default void declareScreenSizeCallbacks() {}

    ;

    /**
     * This is where you should register new screen sizes.
     */
    default void declareScreenSizes() {}

    ;
}
