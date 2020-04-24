package ninjaphenix.expandedstorage.api;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import ninjaphenix.expandedstorage.api.block.CursedChestBlock;
import ninjaphenix.expandedstorage.api.block.OldChestBlock;
import ninjaphenix.expandedstorage.api.block.entity.CursedChestBlockEntity;
import ninjaphenix.expandedstorage.api.block.entity.CustomBlockEntityType;
import ninjaphenix.expandedstorage.api.block.entity.OldChestBlockEntity;
import ninjaphenix.expandedstorage.api.client.ExpandedStorageAPIClient;
import ninjaphenix.expandedstorage.api.plugins.ExpandedStoragePluginV0;

import java.util.function.Consumer;

public final class ExpandedStorageAPI implements ModInitializer
{
    @SuppressWarnings("unused") // Used as entry point for the api.
    public static final ExpandedStorageAPI INSTANCE = new ExpandedStorageAPI();

    public static final CustomBlockEntityType<CursedChestBlockEntity> CHEST;
    public static final CustomBlockEntityType<OldChestBlockEntity> OLD_CHEST;

    static
    {
        CHEST = new CustomBlockEntityType<>(() -> new CursedChestBlockEntity(null), b -> b instanceof CursedChestBlock);
        OLD_CHEST = new CustomBlockEntityType<>(() -> new OldChestBlockEntity(null), b -> b instanceof OldChestBlock);
    }

    private ExpandedStorageAPI() {}

    public static void forEachPlugin(Consumer<ExpandedStoragePluginV0> pluginConsumer)
    {
        FabricLoader.getInstance().getEntrypoints("expandedstorage_plugins_v0", ExpandedStoragePluginV0.class).forEach(pluginConsumer);
    }

    @Override
    public void onInitialize()
    {
        Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier("expandedstorage", "cursed_chest"), CHEST);
        Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier("expandedstorage", "old_cursed_chest"), OLD_CHEST);
        forEachPlugin(ExpandedStoragePluginV0::initialize);
        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) { ExpandedStorageAPIClient.onInitializeClient(); }
    }
}
