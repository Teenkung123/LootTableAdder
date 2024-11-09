package com.Teenkung123.lootTableAdder.Listeners;

import com.Teenkung123.lootTableAdder.LootTableAdder;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import dev.lone.itemsadder.api.Events.ItemsAdderLoadDataEvent; // Adjust the package based on ItemsAdder's actual event class location

public class ItemsAdderListener implements Listener {

    private final LootTableAdder plugin;

    /**
     * Constructs an ItemsAdderListener instance.
     *
     * @param plugin The main plugin instance.
     */
    public ItemsAdderListener(LootTableAdder plugin) {
        this.plugin = plugin;
    }

    /**
     * Handles the ItemsAdderLoadDataEvent to initialize loot configurations after ItemsAdder has loaded.
     *
     * @param event The ItemsAdderLoadDataEvent triggered when ItemsAdder has finished loading data.
     */
    @EventHandler
    public void onItemsAdderLoadData(ItemsAdderLoadDataEvent event) {
        plugin.getLogger().info("ItemsAdder has loaded its data. Initializing LootConfigManager...");
        plugin.initializeLootConfigManager();
    }
}
