package ninjaphenix.chainmail.api.blockentity;

public interface ExpandedBlockEntity
{
    /**
     * Called when a block entity is placed or the chunk it is in is loaded.
     *
     * @since 0.0.1
     */
    default void onLoad() { }
}