package com.Teenkung123.lootTableAdder;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class LootTableScanner {

    private final LootTableAdder plugin;
    private final Set<String> lootTables = new HashSet<>();
    private final List<String> VlootTables = Arrays.asList(
            "minecraft:loot_table/archaeology/desert_pyramid",
            "minecraft:loot_table/archaeology/desert_well",
            "minecraft:loot_table/archaeology/ocean_ruin_cold",
            "minecraft:loot_table/archaeology/ocean_ruin_warm",
            "minecraft:loot_table/archaeology/trail_ruins_common",
            "minecraft:loot_table/archaeology/trail_ruins_rare",
            "minecraft:loot_table/chests/abandoned_mineshaft",
            "minecraft:loot_table/chests/ancient_city",
            "minecraft:loot_table/chests/ancient_city_ice_box",
            "minecraft:loot_table/chests/bastion_bridge",
            "minecraft:loot_table/chests/bastion_hoglin_stable",
            "minecraft:loot_table/chests/bastion_other",
            "minecraft:loot_table/chests/bastion_treasure",
            "minecraft:loot_table/chests/buried_treasure",
            "minecraft:loot_table/chests/desert_pyramid",
            "minecraft:loot_table/chests/end_city_treasure",
            "minecraft:loot_table/chests/igloo_chest",
            "minecraft:loot_table/chests/jungle_temple",
            "minecraft:loot_table/chests/jungle_temple_dispenser",
            "minecraft:loot_table/chests/nether_bridge",
            "minecraft:loot_table/chests/pillager_outpost",
            "minecraft:loot_table/chests/ruined_portal",
            "minecraft:loot_table/chests/shipwreck_map",
            "minecraft:loot_table/chests/shipwreck_supply",
            "minecraft:loot_table/chests/shipwreck_treasure",
            "minecraft:loot_table/chests/simple_dungeon",
            "minecraft:loot_table/chests/spawn_bonus_chest",
            "minecraft:loot_table/chests/stronghold_corridor",
            "minecraft:loot_table/chests/stronghold_crossing",
            "minecraft:loot_table/chests/stronghold_library",
            "minecraft:loot_table/chests/trial_chambers/corridor",
            "minecraft:loot_table/chests/trial_chambers/entrance",
            "minecraft:loot_table/chests/trial_chambers/intersection",
            "minecraft:loot_table/chests/trial_chambers/intersection_barrel",
            "minecraft:loot_table/chests/trial_chambers/reward",
            "minecraft:loot_table/chests/trial_chambers/reward_common",
            "minecraft:loot_table/chests/trial_chambers/reward_ominous",
            "minecraft:loot_table/chests/trial_chambers/reward_ominous_common",
            "minecraft:loot_table/chests/trial_chambers/reward_ominous_rare",
            "minecraft:loot_table/chests/trial_chambers/reward_ominous_unique",
            "minecraft:loot_table/chests/trial_chambers/reward_rare",
            "minecraft:loot_table/chests/trial_chambers/reward_unique",
            "minecraft:loot_table/chests/trial_chambers/supply",
            "minecraft:loot_table/chests/underwater_ruin_big",
            "minecraft:loot_table/chests/underwater_ruin_small",
            "minecraft:loot_table/chests/village/village_armorer",
            "minecraft:loot_table/chests/village/village_butcher",
            "minecraft:loot_table/chests/village/village_cartographer",
            "minecraft:loot_table/chests/village/village_desert_house",
            "minecraft:loot_table/chests/village/village_fisher",
            "minecraft:loot_table/chests/village/village_fletcher",
            "minecraft:loot_table/chests/village/village_mason",
            "minecraft:loot_table/chests/village/village_plains_house",
            "minecraft:loot_table/chests/village/village_savanna_house",
            "minecraft:loot_table/chests/village/village_shepherd",
            "minecraft:loot_table/chests/village/village_snowy_house",
            "minecraft:loot_table/chests/village/village_taiga_house",
            "minecraft:loot_table/chests/village/village_tannery",
            "minecraft:loot_table/chests/village/village_temple",
            "minecraft:loot_table/chests/village/village_toolsmith",
            "minecraft:loot_table/chests/village/village_weaponsmith",
            "minecraft:loot_table/chests/woodland_mansion",
            "minecraft:loot_table/dispensers/trial_chambers/chamber",
            "minecraft:loot_table/dispensers/trial_chambers/corridor",
            "minecraft:loot_table/dispensers/trial_chambers/water",
            "minecraft:loot_table/equipment/trial_chamber",
            "minecraft:loot_table/equipment/trial_chamber_melee",
            "minecraft:loot_table/equipment/trial_chamber_ranged",
            "minecraft:loot_table/gameplay/cat_morning_gift",
            "minecraft:loot_table/gameplay/fishing/fish",
            "minecraft:loot_table/gameplay/fishing/junk",
            "minecraft:loot_table/gameplay/fishing/treasure",
            "minecraft:loot_table/gameplay/fishing",
            "minecraft:loot_table/gameplay/hero_of_the_village/armorer_gift",
            "minecraft:loot_table/gameplay/hero_of_the_village/butcher_gift",
            "minecraft:loot_table/gameplay/hero_of_the_village/cartographer_gift",
            "minecraft:loot_table/gameplay/hero_of_the_village/cleric_gift",
            "minecraft:loot_table/gameplay/hero_of_the_village/farmer_gift",
            "minecraft:loot_table/gameplay/hero_of_the_village/fisherman_gift",
            "minecraft:loot_table/gameplay/hero_of_the_village/fletcher_gift",
            "minecraft:loot_table/gameplay/hero_of_the_village/leatherworker_gift",
            "minecraft:loot_table/gameplay/hero_of_the_village/librarian_gift",
            "minecraft:loot_table/gameplay/hero_of_the_village/mason_gift",
            "minecraft:loot_table/gameplay/hero_of_the_village/shepherd_gift",
            "minecraft:loot_table/gameplay/hero_of_the_village/toolsmith_gift",
            "minecraft:loot_table/gameplay/hero_of_the_village/weaponsmith_gift",
            "minecraft:loot_table/gameplay/panda_sneeze",
            "minecraft:loot_table/gameplay/piglin_bartering",
            "minecraft:loot_table/gameplay/sniffer_digging",
            "minecraft:loot_table/pots/trial_chambers/corridor",
            "minecraft:loot_table/shearing/bogged",
            "minecraft:loot_table/spawners/ominous/trial_chamber/consumables",
            "minecraft:loot_table/spawners/ominous/trial_chamber/key",
            "minecraft:loot_table/spawners/trial_chamber/consumables",
            "minecraft:loot_table/spawners/trial_chamber/items_to_drop_when_ominous",
            "minecraft:loot_table/spawners/trial_chamber/key"
            );

    /**
     * Constructor for LootTableScanner.
     *
     * @param plugin Your main plugin instance.
     */
    public LootTableScanner(LootTableAdder plugin) {
        this.plugin = plugin;
    }

    /**
     * Retrieves all loot tables from the server's data packs.
     */
    public void scan() {
        lootTables.clear(); // Clear previous scan results

        // Iterate through all loaded worlds
        for (World world : Bukkit.getServer().getWorlds()) {
            String worldName = world.getName();
            File datapacksFolder = new File(world.getWorldFolder(), "datapacks");

            if (!datapacksFolder.exists() || !datapacksFolder.isDirectory()) {
                plugin.getLogger().warning("Data packs folder not found for world '" + worldName + "' at: " + datapacksFolder.getAbsolutePath());
                continue;
            }

            // Iterate through each data pack (could be a folder or a zip file)
            File[] dataPacks = datapacksFolder.listFiles();
            if (dataPacks == null || dataPacks.length == 0) {
                plugin.getLogger().warning("No data packs found in: " + datapacksFolder.getAbsolutePath());
                continue;
            }

            for (File dataPack : dataPacks) {
                if (dataPack.isDirectory()) {
                    scanDataPackFolder(dataPack, worldName);
                } else if (dataPack.isFile() && dataPack.getName().endsWith(".zip")) {
                    scanDataPackZip(dataPack, worldName);
                }
            }
        }

        lootTables.addAll(VlootTables);

        plugin.getLogger().info("Total loot tables found: " + lootTables.size());
    }

    /**
     * Scans a data pack folder for loot tables.
     *
     * @param dataPack  The data pack folder.
     * @param worldName The name of the world.
     */
    private void scanDataPackFolder(File dataPack, String worldName) {
        File dataFolder = new File(dataPack, "data");
        if (!dataFolder.exists() || !dataFolder.isDirectory()) return;

        // Iterate through each namespace within the data pack
        File[] namespaces = dataFolder.listFiles(File::isDirectory);
        if (namespaces == null) return;

        for (File namespace : namespaces) {
            File lootTablesFolder = new File(namespace, "loot_tables");
            if (!lootTablesFolder.exists() || !lootTablesFolder.isDirectory()) continue;

            // Recursively scan for JSON files
            scanLootTablesRecursively(lootTablesFolder, namespace.getName());
        }
    }

    /**
     * Scans a data pack zip file for loot tables.
     *
     * @param dataPackZip The data pack zip file.
     * @param worldName   The name of the world.
     */
    private void scanDataPackZip(File dataPackZip, String worldName) {
        try (ZipFile zipFile = new ZipFile(dataPackZip)) {
            zipFile.stream()
                    .filter(entry -> !entry.isDirectory() && entry.getName().endsWith(".json"))
                    .filter(entry -> entry.getName().startsWith("data/") && entry.getName().contains("/loot_tables/"))
                    .forEach(entry -> {
                        String[] parts = entry.getName().split("/");
                        if (parts.length >= 4) { // data/<namespace>/loot_tables/<path>/<loot_table>
                            String namespace = parts[1];
                            String namespaceKey = getString(parts, namespace);
                            lootTables.add(namespaceKey);
                        }
                    });
        } catch (Exception e) {
            plugin.getLogger().warning("Failed to scan data pack zip: " + dataPackZip.getName() + " - " + e.getMessage());
        }
    }

    private static @NotNull String getString(String[] parts, String namespace) {
        StringBuilder pathBuilder = new StringBuilder();
        for (int i = 3; i < parts.length; i++) {
            pathBuilder.append(parts[i]);
            if (i < parts.length - 1) {
                pathBuilder.append("/");
            }
        }
        String tableName = pathBuilder.toString().replace(".json", "");
        return namespace + ":" + tableName;
    }

    /**
     * Recursively scans a directory for loot table JSON files.
     *
     * @param folder     The current folder to scan.
     * @param namespace  The namespace of the data pack.
     */
    private void scanLootTablesRecursively(File folder, String namespace) {
        File[] files = folder.listFiles();
        if (files == null) return;

        for (File file : files) {
            if (file.isDirectory()) {
                scanLootTablesRecursively(file, namespace);
            } else if (file.isFile() && file.getName().endsWith(".json")) {
                String tableName = file.getName().substring(0, file.getName().length() - 5); // Remove ""
                // To include subdirectories in the table name
                String relativePath = folder.toPath().relativize(file.toPath()).toString().replace(".json", "").replace(File.separator, "/");
                String namespaceKey = namespace + ":" + relativePath;
                lootTables.add(namespaceKey);
            }
        }
    }

    /**
     * Retrieves all loot tables found during the scan.
     *
     * @return A Set of loot table names in the format "namespace:table_name".
     */
    public Set<String> getLootTables() {
        return new HashSet<>(lootTables);
    }
}
