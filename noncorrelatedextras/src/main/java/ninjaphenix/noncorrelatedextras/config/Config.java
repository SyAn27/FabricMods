package ninjaphenix.noncorrelatedextras.config;

import blue.endless.jankson.Comment;
import blue.endless.jankson.JsonPrimitive;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.util.Identifier;
import ninjaphenix.chainmail.api.config.JanksonConfigParser;
import org.apache.logging.log4j.MarkerManager;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

public class Config
{
    public static Config INSTANCE;

    @Comment("\nThe max range of the magnet.")
    private final Integer magnet_range = 6;
    @Comment("\nWhat fraction of the distance between the item and player the item moves per tick.")
    private final Double magnet_speed = 0.1D;
    @Comment("\nThe chance a projectile is reflected by polarized iron armor. (set bonus)")
    private final Double polarized_iron_armor_reflection_chance = 0.15D;
    @Comment("\nEnabled features.")
    private final HashMap<String, Boolean> enabled_features = initializedMap(map -> {
        map.put("magnet", true);
        map.put("creepers_break_blocks", true);
        map.put("enchantable_shears", true);
        map.put("polarized_iron_armor", true);
        map.put("polarized_iron_armor_reflects_projectiles", true);
        map.put("farming_hoes", true);
        map.put("peaceful_only_recipes", true);
        map.put("structure_compasses", true);
    });
    @Comment("\nMax magnet range increases for each polarized iron armor piece.")
    private final HashMap<EquipmentSlot, Integer> polarized_iron_armor_additional_magnet_range = initializedMap(map -> {
        map.put(EquipmentSlot.HEAD, 2);
        map.put(EquipmentSlot.CHEST, 4);
        map.put(EquipmentSlot.LEGS, 3);
        map.put(EquipmentSlot.FEET, 1);
    });
    @Comment("\nEnabled structure compasses.")
    private final HashMap<Identifier, Boolean> enabled_structure_compasses = initializedMap(map -> {
        map.put(new Identifier("minecraft:pillager_outpost"), false);
        map.put(new Identifier("minecraft:mineshaft"), false);
        map.put(new Identifier("minecraft:mansion"), false);
        map.put(new Identifier("minecraft:jungle_pyramid"), false);
        map.put(new Identifier("minecraft:desert_pyramid"), false);
        map.put(new Identifier("minecraft:igloo"), false);
        map.put(new Identifier("minecraft:ruined_portal"), false);
        map.put(new Identifier("minecraft:shipwreck"), false);
        map.put(new Identifier("minecraft:swamp_hut"), false);
        map.put(new Identifier("minecraft:stronghold"), true);
        map.put(new Identifier("minecraft:monument"), false);
        map.put(new Identifier("minecraft:ocean_ruin"), false);
        map.put(new Identifier("minecraft:fortress"), false);
        map.put(new Identifier("minecraft:endcity"), false);
        map.put(new Identifier("minecraft:buried_treasure"), false);
        map.put(new Identifier("minecraft:village"), false);
        map.put(new Identifier("minecraft:nether_fossil"), false);
        map.put(new Identifier("minecraft:bastion_remnant"), false);
    });

    public static void initialize()
    {
        final Path configDirectory = FabricLoader.getInstance().getConfigDirectory().toPath();
        INSTANCE = new JanksonConfigParser.Builder()
                .deSerializer(JsonPrimitive.class, Identifier.class, (it, marshaller) -> new Identifier(it.asString()),
                        ((identifier, marshaller) -> marshaller.serialize(identifier.toString()))).build()
                .load(Config.class, Config::new, configDirectory.resolve("NonCorrelatedExtras.json"), new MarkerManager.Log4jMarker("noncorrelatedextras"));
    }

    private <T, R> HashMap<T, R> initializedMap(final Consumer<HashMap<T, R>> initializer)
    {
        final HashMap<T, R> map = new HashMap<>();
        initializer.accept(map);
        return map;
    }

    public Boolean isFeatureEnabled(final String feature) { return enabled_features.getOrDefault(feature, Boolean.FALSE); }

    public double getMagnetSpeed() { return magnet_speed; }

    public int getMagnetMaxRange() { return magnet_range; }

    public int getAdditionalMagnetRange(final EquipmentSlot type) { return polarized_iron_armor_additional_magnet_range.getOrDefault(type, 0); }

    public double getProjectileReflectionChance() { return polarized_iron_armor_reflection_chance; }

    public Set<Identifier> getEnabledStructureCompasses()
    {
        final HashSet<Identifier> set = new HashSet<>();
        for (final Map.Entry<Identifier, Boolean> entry : enabled_structure_compasses.entrySet())
        {
            if (Boolean.TRUE.equals(entry.getValue())) { set.add(entry.getKey());}
        }
        return set;
    }
}