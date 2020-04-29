package ninjaphenix.expandedstorage.api.plugins;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Identifier;

import java.util.function.Consumer;

public interface ExpandedStoragePluginV0
{
    /**
     * Called when creating texture atlas for all expanded storage blocks that has a block entity renderer. Currently only used for CursedChestBlockEntity.
     *
     * Note: You will still have to register the sprites yourself.
     *
     * @param textureConsumer A consumer which accepts textures identifiers to be added to the atlas.
     */
    @Environment(EnvType.CLIENT)
    void appendTexturesToAtlas(Consumer<Identifier> textureConsumer);

    /**
     * Called after Expanded Storage API has been loaded. Register all you custom expanded storage blocks and tiers here.
     */
    void initialize();
}
