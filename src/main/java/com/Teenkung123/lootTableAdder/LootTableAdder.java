package com.Teenkung123.lootTableAdder;

import com.Teenkung123.lootTableAdder.Commands.MainCommand;
import com.Teenkung123.lootTableAdder.Listeners.LootGenerate;
import com.Teenkung123.lootTableAdder.Listeners.ItemsAdderListener;
import com.Teenkung123.lootTableAdder.Manager.LootConfigManager;
import dev.lone.itemsadder.api.ItemsAdder;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public final class LootTableAdder extends JavaPlugin {

    private LootConfigManager lootConfigManager;
    private LootTableScanner lootTableScanner;
    private boolean itemsAdderLoaded = false;
    private boolean mmoItemsLoaded = false;

    @Override
    public void onEnable() {
        // Plugin startup logic
        getLogger().info("Starting LootTableAdder...");

        // Check if ItemsAdder is present and enabled
        Plugin itemsAdder = getServer().getPluginManager().getPlugin("ItemsAdder");
        Plugin mmoItems = getServer().getPluginManager().getPlugin("MMOItems");

        if (mmoItems != null && mmoItems.isEnabled()) {
            mmoItemsLoaded = true;
        }

        if (itemsAdder != null && itemsAdder.isEnabled()) {
            itemsAdderLoaded = true;
            if (ItemsAdder.getAllItems() != null || !ItemsAdder.getAllItems().isEmpty()) {
                getLogger().info("ItemsAdder has loaded its data. Initializing LootConfigManager...");
                initializeLootConfigManager();
            } else {
                getLogger().info("ItemsAdder detected and enabled. Waiting for ItemsAdder to load data...");
                // Register the ItemsAdderLoadDataEvent listener
                getServer().getPluginManager().registerEvents(new ItemsAdderListener(this), this);
            }
        } else {
            initializeLootConfigManager();
        }
        getServer().getPluginManager().registerEvents(new LootGenerate(this), this);
        //noinspection DataFlowIssue
        getCommand("loottable").setExecutor(new MainCommand(this));
        getLogger().info("LootTableAdder has been enabled.");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getLogger().info("LootTableAdder has been disabled.");
    }

    /**
     * Initializes the LootConfigManager and registers necessary listeners and commands.
     * This method should be called after ensuring that ItemsAdder has fully loaded its data.
     */
    public void initializeLootConfigManager() {
        // Initialize LootConfigManager
        lootConfigManager = new LootConfigManager(this);
        lootTableScanner = new LootTableScanner(this);
        lootConfigManager.loadConfig();
        lootTableScanner.scan();
    }

    /**
     * Provides access to the LootConfigManager.
     *
     * @return The LootConfigManager instance.
     */
    public LootConfigManager getLootConfigManager() {
        return lootConfigManager;
    }

    /**
     * Checks if ItemsAdder is loaded.
     * @return True if ItemsAdder is loaded, false otherwise.
     */
    public boolean isItemsAdderLoaded() {
        return itemsAdderLoaded;
    }

    /**
     * Checks if MMOItems is loaded.
     * @return True if MMOItems is loaded, false otherwise.
     */
    public boolean isMmoItemsLoaded() {
        return mmoItemsLoaded;
    }

    /**
     * Provides access to the LootTableScanner.
     *
     * @return The LootTableScanner instance.
     */
    public LootTableScanner getLootTableScanner() {
        return lootTableScanner;
    }
}
