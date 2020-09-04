package ninjaphenix.expandedstorage.common;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.item.*;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.minecraft.util.registry.Registry;
import ninjaphenix.chainmail.api.ChainmailCommonApi;
import ninjaphenix.expandedstorage.common.block.BarrelBlock;
import ninjaphenix.expandedstorage.common.block.entity.BarrelBlockEntity;
import ninjaphenix.expandedstorage.common.block.entity.CursedChestBlockEntity;
import ninjaphenix.expandedstorage.common.block.entity.OldChestBlockEntity;
import ninjaphenix.expandedstorage.common.block.CursedChestBlock;
import ninjaphenix.expandedstorage.common.block.OldChestBlock;
import ninjaphenix.expandedstorage.common.inventory.PagedScreenHandler;
import ninjaphenix.expandedstorage.common.inventory.ScrollableScreenHandler;
import ninjaphenix.expandedstorage.common.inventory.SingleScreenHandler;
import ninjaphenix.expandedstorage.common.item.ChestConversionItem;
import ninjaphenix.expandedstorage.common.item.ChestMutatorItem;

public final class ModContent
{
    public static final ScreenHandlerType<PagedScreenHandler> PAGED_HANDLER_TYPE;
    public static final ScreenHandlerType<SingleScreenHandler> SINGLE_HANDLER_TYPE;
    public static final ScreenHandlerType<ScrollableScreenHandler> SCROLLABLE_HANDLER_TYPE;
    public static final BlockEntityType<CursedChestBlockEntity> CHEST;
    public static final BlockEntityType<OldChestBlockEntity> OLD_CHEST;
    public static final BlockEntityType<BarrelBlockEntity> BARREL;
    public static final CursedChestBlock DIAMOND_CHEST;

    static
    {
        SCROLLABLE_HANDLER_TYPE = ScreenHandlerRegistry.registerExtended(Const.id("scrollable"), new ScrollableScreenHandler.Factory());
        PAGED_HANDLER_TYPE = ScreenHandlerRegistry.registerExtended(Const.id("paged"), new PagedScreenHandler.Factory());
        SINGLE_HANDLER_TYPE = ScreenHandlerRegistry.registerExtended(Const.id("single"), new SingleScreenHandler.Factory());
        final ItemGroup group = ChainmailCommonApi.INSTANCE.registerItemGroup((index) -> new ItemGroup(index, Const.MOD_ID)
        {
            @Override
            @Environment(EnvType.CLIENT)
            public ItemStack createIcon() { return new ItemStack(DIAMOND_CHEST); }
        });

        final Identifier WOOD = Const.id("wood");
        final Identifier IRON = Const.id("iron");
        final Identifier GOLD = Const.id("gold");
        final Identifier DIAMOND = Const.id("diamond");
        final Identifier OBSIDIAN = Const.id("obsidian");
        final Identifier NETHERITE = Const.id("netherite");

        CHEST = Registry.register(Registry.BLOCK_ENTITY_TYPE, Const.id("cursed_chest"),
                                  BlockEntityType.Builder.create(() -> new CursedChestBlockEntity(null),
                                                                 chest(Blocks.OAK_PLANKS, Const.id("wood_chest"), WOOD, 3, group),
                                                                 chest(Blocks.PUMPKIN, Const.id("pumpkin_chest"), Const.id("pumpkin"), 3, group),
                                                                 chest(Blocks.OAK_PLANKS, Const.id("christmas_chest"), Const.id("christmas"), 3, group),
                                                                 chest(Blocks.IRON_BLOCK, Const.id("iron_chest"), IRON, 6, group),
                                                                 chest(Blocks.GOLD_BLOCK, Const.id("gold_chest"), GOLD, 9, group),
                                                                 DIAMOND_CHEST = chest(Blocks.DIAMOND_BLOCK, Const.id("diamond_chest"), DIAMOND, 12, group),
                                                                 chest(Blocks.OBSIDIAN, Const.id("obsidian_chest"), OBSIDIAN, 12, group),
                                                                 chest(Blocks.NETHERITE_BLOCK, Const.id("netherite_chest"), NETHERITE, 15, group)).build(null));
        OLD_CHEST = Registry.register(Registry.BLOCK_ENTITY_TYPE, Const.id("old_cursed_chest"),
                                      BlockEntityType.Builder.create(() -> new OldChestBlockEntity(null),
                                                                     old(Blocks.OAK_PLANKS, "wood_chest", WOOD, 3, group),
                                                                     old(Blocks.IRON_BLOCK, "iron_chest", IRON, 6, group),
                                                                     old(Blocks.GOLD_BLOCK, "gold_chest", GOLD, 9, group),
                                                                     old(Blocks.DIAMOND_BLOCK, "diamond_chest", DIAMOND, 12, group),
                                                                     old(Blocks.OBSIDIAN, "obsidian_chest", OBSIDIAN, 12, group),
                                                                     old(Blocks.NETHERITE_BLOCK, "netherite_chest", NETHERITE, 15, group))
                                              .build(null));
        BARREL = Registry.register(Registry.BLOCK_ENTITY_TYPE, Const.id("barrel"),
                                   BlockEntityType.Builder.create(() -> new BarrelBlockEntity(null),
                                                                  barrel(1, 5, 6, Const.id("iron_barrel"), IRON, 6, group),
                                                                  barrel(2, 3, 6, Const.id("gold_barrel"), GOLD, 9, group),
                                                                  barrel(2, 5, 6, Const.id("diamond_barrel"), DIAMOND, 12, group),
                                                                  barrel(3, 50, 1200, Const.id("obsidian_barrel"), OBSIDIAN, 12, group),
                                                                  barrel(4, 50, 1200, Const.id("netherite_barrel"), NETHERITE, 15, group)).build(null));
        registerConversionPath(group,
                               new Pair<>(WOOD, "wood"),
                               new Pair<>(IRON, "iron"),
                               new Pair<>(GOLD, "gold"),
                               new Pair<>(DIAMOND, "diamond"),
                               new Pair<>(OBSIDIAN, "obsidian"),
                               new Pair<>(NETHERITE, "netherite"));
        Registry.register(Registry.ITEM, Const.id("chest_mutator"), new ChestMutatorItem(new Item.Settings().maxCount(1).group(group)));
    }

    private static BarrelBlock barrel(final int miningLevel, final float hardness, final float resistance, final Identifier registryId,
                                      final Identifier genericTier, final int rows, final ItemGroup group)
    {
        final FabricBlockSettings settings = FabricBlockSettings.copyOf(Blocks.BARREL).strength(hardness, resistance)
                .requiresTool().breakByTool(FabricToolTags.AXES, miningLevel);
        final BarrelBlock block = new BarrelBlock(settings, genericTier);
        Registry.register(Registry.BLOCK, registryId, block);
        Registry.register(Registry.ITEM, registryId, new BlockItem(block, new Item.Settings().group(group)));
        Registry.register(Registries.BARREL, genericTier, new Registries.TierData(
                rows * 9, new TranslatableText("container.expandedstorage." + registryId.getPath()), registryId));
        if(FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT)
        {
            BlockRenderLayerMap.INSTANCE.putBlock(block, RenderLayer.getCutoutMipped());
        }
        return block;
    }

    @SuppressWarnings("EmptyMethod")
    public static void register() { }

    private static OldChestBlock old(final Block material, final String name, final Identifier genericTier, final int rows,
                                     final ItemGroup group)
    {
        final Identifier registryId = Const.id("old_" + name);
        final OldChestBlock block = new OldChestBlock(FabricBlockSettings.copyOf(material), genericTier);
        Registry.register(Registry.BLOCK, registryId, block);
        Registry.register(Registry.ITEM, registryId, new BlockItem(block, new Item.Settings().group(group)));
        Registry.register(Registries.OLD_CHEST, genericTier, new Registries.TierData(
                rows * 9, new TranslatableText("container.expandedstorage." + name), registryId));
        return block;
    }

    private static CursedChestBlock chest(final Block material, final Identifier registryId, final Identifier genericTier, final int rows,
                                          final ItemGroup group)
    {
        final CursedChestBlock block = new CursedChestBlock(FabricBlockSettings.copyOf(material), genericTier);
        Registry.register(Registry.BLOCK, registryId, block);
        Registry.register(Registry.ITEM, registryId, new BlockItem(block, new Item.Settings().group(group)));
        Registry.register(Registries.CHEST, genericTier, new Registries.ChestTierData(
                rows * 9, new TranslatableText("container.expandedstorage." + registryId.getPath()), registryId,
                type -> Const.id(String.format("entity/%s/%s", registryId.getPath(), type.asString()))));
        return block;
    }

    @SafeVarargs
    private static void registerConversionPath(final ItemGroup group, final Pair<Identifier, String>... values)
    {
        final int length = values.length;
        for (int i = 0; i < length - 1; i++)
        {
            for (int x = i + 1; x < length; x++)
            {
                final Pair<Identifier, String> from = values[i];
                final Pair<Identifier, String> to = values[x];
                final Identifier id = Const.id(from.getRight() + "_to_" + to.getRight() + "_conversion_kit");
                Registry.register(Registry.ITEM, id, new ChestConversionItem(new Item.Settings().group(group).maxCount(16), from, to));
            }
        }
    }
}