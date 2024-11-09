package com.Teenkung123.lootTableAdder.Listeners;

import com.Teenkung123.lootTableAdder.LootTableAdder;
import com.Teenkung123.lootTableAdder.Elements.LootConfig;
import com.Teenkung123.lootTableAdder.Elements.ItemBuilder;
import de.tr7zw.nbtapi.NBT;
import de.tr7zw.nbtapi.iface.ReadableNBT;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.Vault;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDispenseLootEvent;
import org.bukkit.event.world.LootGenerateEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Random;

public class LootGenerate implements Listener {

    private final LootTableAdder plugin;
    private final Random random;
    private final NamespacedKey key = new NamespacedKey("minecraft", "loot_table");

    /**
     * Constructs a LootGenerate event handler.
     *
     * @param plugin The LootConfigManager instance managing loot configurations.
     */
    public LootGenerate(LootTableAdder plugin) {
        this.plugin = plugin;
        this.random = new Random();

    }

    /**
     * Handles the LootGenerateEvent to inject custom items into loot tables.
     *
     * @param event The LootGenerateEvent triggered when loot is generated.
     */
    @EventHandler
    public void onLootGenerate(LootGenerateEvent event) {
        // Retrieve the loot table key as a string (e.g., "minecraft:chests/end_city_treasure")
        NamespacedKey lootTableKey = event.getLootTable().getKey();
        String lootTableName = lootTableKey.toString();

        // Fetch the corresponding LootConfig from the LootConfigManager
        LootConfig lootConfig = plugin.getLootConfigManager().getLootConfig(lootTableName);
        if (lootConfig == null) {
            return; // No custom configuration for this loot table
        }

        // Retrieve the list of custom items to potentially add
        List<ItemBuilder> loots = lootConfig.getLoots();

        // Iterate through each custom item and decide whether to add it based on weight
        for (ItemBuilder itemBuilder : loots) {
            if (itemBuilder.getType() == null) {
                continue;
            }

            // Determine whether to add the item based on its weight
            if (shouldAddItem(itemBuilder.getWeight())) {
                ItemStack item = itemBuilder.getItemStack(true);
                if (item != null) {
                    event.getLoot().add(item);
                }
            }
        }
    }

    @EventHandler
    public void blockDispenseLoot(BlockDispenseLootEvent event) {
        Block block = event.getBlock();
        List<ItemStack> loots = event.getDispensedLoot();
        if (block.getState() instanceof Vault vault) {
            String table = NBT.get(vault, nbt -> {
                ReadableNBT lootNbt = nbt.getCompound("config");
                return lootNbt.getString("loot_table");
            });

            Bukkit.broadcastMessage("loot table = " + table);
            LootConfig config = plugin.getLootConfigManager().getLootConfig(table);
            if (config == null) {
                return;
            }

            List<ItemBuilder> items = config.getLoots();
            for (ItemBuilder itemBuilder : items) {
                if (itemBuilder.getType() == null) {
                    continue;
                }

                if (shouldAddItem(itemBuilder.getWeight())) {
                    ItemStack item = itemBuilder.getItemStack(true);
                    if (item != null) {
                        loots.add(item);
                    }
                }
            }

            event.setDispensedLoot(loots);
        }
    }

    /**
     * Determines whether to add an item based on its weight.
     *
     * @param weight The weight of the item.
     * @return True if the item should be added, false otherwise.
     */
    private boolean shouldAddItem(int weight) {
        return random.nextInt(100) < weight;
    }
}
