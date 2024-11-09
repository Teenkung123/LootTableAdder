package com.Teenkung123.lootTableAdder.Elements;

import com.Teenkung123.lootTableAdder.LootTableAdder;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class LootConfig {

    private final LootTableAdder plugin;
    private final String lootTable;
    private final List<ItemBuilder> loots;

    public LootConfig(LootTableAdder plugin, File file) {
        this.plugin = plugin;
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        this.lootTable = config.getString("lootTable");
        this.loots = new ArrayList<>();

        if (lootTable == null || lootTable.isEmpty()) {
            plugin.getLogger().warning("Loot table name is missing in file: " + file.getName());
            return;
        }

        ConfigurationSection lootsSection = config.getConfigurationSection("loots");
        if (lootsSection == null) {
            plugin.getLogger().warning("No 'loots' section found in loot table: " + lootTable);
            return;
        }

        for (String key : lootsSection.getKeys(false)) {
            String itemPath = "loots." + key;
            String item = config.getString(itemPath + ".item", "");
            int amount = config.getInt(itemPath + ".amount", 1);
            int weight = config.getInt(itemPath + ".weight", 1);
            ItemBuilder itemBuilder = new ItemBuilder(plugin, item, amount, weight);
            if (itemBuilder.isValid()) {
                loots.add(itemBuilder);
            } else {
                plugin.getLogger().warning("Invalid item in loot table '" + lootTable + "': " + item);
            }
        }
    }

    /**
     * Retrieves the name of the loot table.
     *
     * @return The loot table name.
     */
    public String getLootTable() {
        return lootTable;
    }

    /**
     * Retrieves the list of ItemBuilders for this loot table.
     *
     * @return A list of ItemBuilder objects.
     */
    public List<ItemBuilder> getLoots() {
        return loots;
    }
}
