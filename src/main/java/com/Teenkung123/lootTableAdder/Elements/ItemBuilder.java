package com.Teenkung123.lootTableAdder.Elements;

import com.Teenkung123.lootTableAdder.Elements.Enum.ItemType;
import com.Teenkung123.lootTableAdder.LootTableAdder;
import dev.lone.itemsadder.api.CustomStack;
import net.Indyuce.mmoitems.MMOItems;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class ItemBuilder {

    private final LootTableAdder plugin;
    private final ItemType type;
    private final String[] itemParts;
    private final int amount;
    private final int weight;
    private final boolean isValid;

    /**
     * Constructs an ItemBuilder instance by parsing the item string and validating the item.
     *
     * @param plugin The main plugin instance.
     * @param item   The item identifier string (e.g., "items_adder:type:name").
     * @param amount The quantity of the item.
     * @param weight The weight of the item in the loot table.
     */
    public ItemBuilder(LootTableAdder plugin, String item, int amount, int weight) {
        this.plugin = plugin;
        this.itemParts = item.split(":");
        this.type = ItemType.fromString(this.itemParts[0].toUpperCase());
        this.amount = amount;
        this.weight = weight;
        this.isValid = validateItem();
    }

    /**
     * Validates the item based on its type and existence.
     *
     * @return True if the item is valid, false otherwise.
     */
    private boolean validateItem() {
        if (this.type == null) {
            plugin.getLogger().warning("Item type not found: " + this.itemParts[0]);
            return false;
        }

        return switch (this.type) {
            case VANILLA -> validateVanillaItem();
            case ITEMSADDER -> validateItemsAdderItem();
            case MMOITEMS -> validateMMOItemsItem();
            default -> {
                plugin.getLogger().warning("Unhandled ItemType: " + this.type);
                yield false;
            }
        };
    }

    /**
     * Validates a Vanilla Minecraft item.
     *
     * @return True if the material exists, false otherwise.
     */
    private boolean validateVanillaItem() {
        if (itemParts.length < 2) {
            plugin.getLogger().warning("Invalid Vanilla item format: " + String.join(":", itemParts));
            return false;
        }
        Material material = Material.getMaterial(itemParts[1].toUpperCase());
        if (material == null) {
            plugin.getLogger().warning("Material not found: " + itemParts[1]);
            return false;
        }
        return true;
    }

    /**
     * Validates an ItemsAdder custom item.
     *
     * @return True if the custom item exists in the registry, false otherwise.
     */
    private boolean validateItemsAdderItem() {
        if (itemParts.length < 3) {
            plugin.getLogger().warning("Invalid ItemsAdder item format: " + String.join(":", itemParts));
            return false;
        }
        String itemsAdderKey = String.join(":", java.util.Arrays.copyOfRange(itemParts, 1, itemParts.length));
        if (!CustomStack.isInRegistry(itemsAdderKey)) {
            plugin.getLogger().warning("ItemsAdder item not found: " + itemsAdderKey);
            return false;
        }
        return true;
    }

    /**
     * Validates an MMOItems custom item.
     *
     * @return True if the MMOItem exists, false otherwise.
     */
    private boolean validateMMOItemsItem() {
        if (itemParts.length < 3) {
            plugin.getLogger().warning("Invalid MMOItems item format: " + String.join(":", itemParts));
            return false;
        }
        String mmoType = itemParts[1].toUpperCase();
        String mmoName = itemParts[2].toUpperCase();
        Plugin mmoPlugin = Bukkit.getPluginManager().getPlugin("MMOItems");
        if (mmoPlugin == null) {
            plugin.getLogger().warning("MMOItems plugin not found.");
            return false;
        }
        // Assuming MMOItems integration via MythicLib or similar

        ItemStack mmoItem = MMOItems.plugin.getItem(MMOItems.plugin.getTypes().get(mmoType), mmoName);

        if (mmoItem == null) {
            plugin.getLogger().warning("MMOItem not found: " + mmoType + ":" + mmoName);
            return false;
        }
        return true;
    }

    // Getters

    public ItemType getType() {
        return type;
    }

    /**
     * Returns the full item identifier.
     *
     * @return The item ID as a colon-separated string.
     */
    public String getItemId() {
        return String.join(":", itemParts);
    }

    public int getAmount() {
        return amount;
    }

    public int getWeight() {
        return weight;
    }

    /**
     * Generates and retrieves a new ItemStack based on the item type.
     *
     * @return A new ItemStack with potentially randomized stats, or null if invalid.
     */
    public ItemStack getItemStack(boolean randomAmount) {
        if (!isValid) {
            plugin.getLogger().warning("Attempted to generate ItemStack for invalid item: " + getItemId());
            return null;
        }

        ItemStack s = switch (type) {
            case VANILLA -> buildVanillaItem();
            case ITEMSADDER -> buildItemsAdderItem();
            case MMOITEMS -> buildMMOItemsItem();
            default -> {
                plugin.getLogger().warning("Unhandled ItemType when generating ItemStack: " + type);
                yield null;
            }
        };

        if (s != null && randomAmount) {
            s.setAmount(Math.max((int) (Math.random() * amount), 1));
        }
        return s;
    }

    /**
     * Builds a Vanilla Minecraft ItemStack.
     *
     * @return A new ItemStack or null if an error occurs.
     */
    private ItemStack buildVanillaItem() {
        try {
            Material material = Material.getMaterial(itemParts[1].toUpperCase());
            if (material == null) {
                plugin.getLogger().warning("Material not found during ItemStack generation: " + itemParts[1]);
                return null;
            }
            return new ItemStack(material, amount);
        } catch (Exception e) {
            plugin.getLogger().severe("Error generating Vanilla ItemStack: " + e.getMessage());
            return null;
        }
    }

    /**
     * Builds an ItemsAdder custom ItemStack.
     *
     * @return A new ItemStack with random stats or null if an error occurs.
     */
    private ItemStack buildItemsAdderItem() {
        try {
            String itemsAdderKey = String.join(":", java.util.Arrays.copyOfRange(itemParts, 1, itemParts.length));
            CustomStack customStack = CustomStack.getInstance(itemsAdderKey);
            if (customStack == null) {
                plugin.getLogger().warning("Failed to retrieve ItemsAdder item during ItemStack generation: " + itemsAdderKey);
                return null;
            }
            ItemStack stack = customStack.getItemStack();
            stack.setAmount(amount);
            return stack;
        } catch (Exception e) {
            plugin.getLogger().severe("Error generating ItemsAdder ItemStack: " + e.getMessage());
            return null;
        }
    }

    /**
     * Builds an MMOItems custom ItemStack.
     *
     * @return A new ItemStack with random stats or null if an error occurs.
     */
    private ItemStack buildMMOItemsItem() {
        try {
            String mmoType = itemParts[1].toUpperCase();
            String mmoName = itemParts[2].toUpperCase();
            Plugin mmoPlugin = Bukkit.getPluginManager().getPlugin("MMOItems");
            if (mmoPlugin == null) {
                plugin.getLogger().warning("MMOItems plugin not found during ItemStack generation.");
                return null;
            }
            // Assuming MMOItems integration via MythicLib or similar
            ItemStack mmoItem = MMOItems.plugin.getItem(MMOItems.plugin.getTypes().get(mmoType), mmoName);
            if (mmoItem == null) {
                plugin.getLogger().warning("MMOItem not found during ItemStack generation: " + mmoType + ":" + mmoName);
                return null;
            }
            mmoItem.setAmount(amount);
            return mmoItem;
        } catch (Exception e) {
            plugin.getLogger().severe("Error generating MMOItems ItemStack: " + e.getMessage());
            return null;
        }
    }

    /**
     * Checks if the item was successfully validated.
     *
     * @return True if valid, false otherwise.
     */
    public boolean isValid() {
        return isValid;
    }
}
