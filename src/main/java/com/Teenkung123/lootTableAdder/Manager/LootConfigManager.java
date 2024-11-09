package com.Teenkung123.lootTableAdder.Manager;

import com.Teenkung123.lootTableAdder.Elements.LootConfig;
import com.Teenkung123.lootTableAdder.LootTableAdder;

import java.io.File;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class LootConfigManager {

    private final LootTableAdder plugin;
    private final Map<String, LootConfig> lootConfigs = new ConcurrentHashMap<>();

    public LootConfigManager(LootTableAdder plugin) {
        this.plugin = plugin;
    }

    /**
     * Loads all loot configuration files from the 'lootTables' directory
     * and stores them in a HashMap with the lootTable name as the key.
     */
    public void loadConfig() {
        File dir = new File(plugin.getDataFolder(), "lootTables");
        if (!dir.exists()) {
            boolean created = dir.mkdirs();
            if (!created) {
                plugin.getLogger().warning("Failed to create lootTables directory.");
                return;
            }
        }
        File[] files = dir.listFiles();
        if (files == null) {
            plugin.getLogger().warning("No loot table files found in 'lootTables' directory.");
            return;
        }
        for (File file : files) {
            if (file.isFile() && file.getName().endsWith(".yml")) {
                plugin.getLogger().info("Loading loot table: " + file.getName());
                LootConfig lootConfig = new LootConfig(plugin, file);
                if (lootConfig.getLootTable() != null) {
                    lootConfigs.put(lootConfig.getLootTable(), lootConfig);
                    plugin.getLogger().info("Loaded loot table: " + lootConfig.getLootTable());
                } else {
                    plugin.getLogger().warning("Loot table name is missing or invalid in file: " + file.getName());
                }
            }
        }
    }

    /**
     * Retrieves the LootConfig associated with the given lootTable name.
     *
     * @param lootTableName The name of the loot table.
     * @return The LootConfig object, or null if not found.
     */
    public LootConfig getLootConfig(String lootTableName) {
        return lootConfigs.get(lootTableName);
    }

    /**
     * Retrieves all loaded loot configurations.
     *
     * @return A Map of lootTable names to LootConfig objects.
     */
    public Map<String, LootConfig> getAllLootConfigs() {
        return lootConfigs;
    }

    /**
     * Clears the lootConfigs map and reloads all loot configuration files.
     */
    public void reloadConfig() {
        lootConfigs.clear();
        loadConfig();
    }

}
