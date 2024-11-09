package com.Teenkung123.lootTableAdder.Commands;

import com.Teenkung123.lootTableAdder.LootTableAdder;
import de.tr7zw.nbtapi.NBT;
import de.tr7zw.nbtapi.iface.ReadWriteNBT;
import de.tr7zw.nbtapi.iface.ReadableNBT;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Container;
import org.bukkit.block.Vault;
import org.bukkit.command.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.loot.Lootable;
import org.bukkit.util.RayTraceResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class MainCommand implements CommandExecutor, TabCompleter {

    private final LootTableAdder plugin;

    // Color themes
    private static final String PREFIX = "<gradient:#BB4AFF:#FF80C8>[LootTable]</gradient>";
    private static final String ERROR_GRADIENT = "<gradient:#FF0000:#CA0000>";
    private static final String INFO_GRADIENT = "<gradient:#00FF00:#008000>";
    private static final MiniMessage MM = MiniMessage.miniMessage();

    public MainCommand(LootTableAdder plugin) {
        this.plugin = plugin;
    }

    /**
     * Sends an error message with the predefined error gradient.
     *
     * @param sender  The command sender.
     * @param message The message to send.
     */
    private void sendErrorMessage(CommandSender sender, String message) {
        sender.sendMessage(MM.deserialize(PREFIX + " " + ERROR_GRADIENT + message + "</gradient>"));
    }

    /**
     * Sends an informational message with the predefined info gradient.
     *
     * @param sender  The command sender.
     * @param message The message to send.
     */
    private void sendInfoMessage(CommandSender sender, String message) {
        sender.sendMessage(MM.deserialize(PREFIX + " " + INFO_GRADIENT + message + "</gradient>"));
    }

    /**
     * Displays the help menu to the command sender.
     *
     * @param sender The command sender.
     */
    private void sendHelpMenu(CommandSender sender) {
        String helpHeader = PREFIX + " <gradient:#BB4AFF:#FF80C8>Available Commands:</gradient>";
        String reloadCommand = "<click:suggest_command:/lt reload><gradient:#BB4AFF:#FF80C8>/lt reload</gradient></click> - <gray>Reload the loot tables.</gray>";
        String getCommand = "<click:suggest_command:/lt get><gradient:#BB4AFF:#FF80C8>/lt get</gradient></click> - <gray>Get the loot table of the targeted block or entity.</gray>";
        String getChestCommand = "<click:suggest_command:/lt get_chest ><gradient:#BB4AFF:#FF80C8>/lt get_chest loot_table [seed]</gradient></click> - <gray>Get a chest with the specified loot table.</gray>";
        String setLootCommand = "<click:suggest_command:/lt set_loot ><gradient:#BB4AFF:#FF80C8>/lt set_loot loot_table [seed]</gradient></click> - <gray>Set the loot table of the targeted container.</gray>";

        sender.sendMessage(MM.deserialize(helpHeader));
        sender.sendMessage(MM.deserialize(reloadCommand));
        sender.sendMessage(MM.deserialize(getCommand));
        sender.sendMessage(MM.deserialize(getChestCommand));
        sender.sendMessage(MM.deserialize(setLootCommand));
    }

    @Override
    public boolean onCommand(
            @NotNull CommandSender sender,
            @NotNull Command command,
            @NotNull String label,
            @NotNull String[] args) {

        if (!sender.hasPermission("loottable.admin")) {
            sendErrorMessage(sender, "You do not have permission to use this command.");
            return true;
        }

        if (args.length == 0) {
            sendHelpMenu(sender);
            return true;
        }

        String subCommand = args[0].toLowerCase();

        switch (subCommand) {
            case "reload":
                handleReloadCommand(sender);
                break;

            case "get":
                if (sender instanceof Player player) {
                    handleGetCommand(player);
                } else {
                    sendErrorMessage(sender, "This command can only be used by a player.");
                }
                break;

            case "get_chest":
                if (sender instanceof Player player) {
                    if (args.length < 2) {
                        sendErrorMessage(sender, "Usage: /lt get_chest <loot_table> [seed]");
                    } else {
                        handleGetChestCommand(args, player);
                    }
                } else {
                    sendErrorMessage(sender, "This command can only be used by a player.");
                }
                break;

            case "set_loot":
                if (sender instanceof Player player) {
                    if (args.length < 2) {
                        sendErrorMessage(sender, "Usage: /lt set_loot <loot_table> [seed]");
                    } else {
                        handleSetLootCommand(args, player);
                    }
                } else {
                    sendErrorMessage(sender, "This command can only be used by a player.");
                }
                break;

            default:
                sendHelpMenu(sender);
                break;
        }

        return true;
    }

    /**
     * Handles the 'reload' subcommand.
     *
     * @param sender The command sender.
     */
    private void handleReloadCommand(CommandSender sender) {
        sendInfoMessage(sender, "Reloading loot tables...");
        plugin.initializeLootConfigManager();
        sendInfoMessage(sender, "Loot tables reloaded.");
    }

    /**
     * Handles the 'get' subcommand.
     *
     * @param player The player executing the command.
     */
    private void handleGetCommand(Player player) {
        RayTraceResult blockResult = player.rayTraceBlocks(5.0);
        Entity targetEntity = player.getTargetEntity(5);

        if (blockResult != null && blockResult.getHitBlock() != null) {
            Block block = blockResult.getHitBlock();
            if (block.getState() instanceof Lootable lootable) {
                if (lootable.getLootTable() == null) {
                    sendErrorMessage(player, "No loot table found for this block. or a loot table is already generated for this block");
                    return;
                }
                displayLootTable(player, lootable.getLootTable().getKey().toString());
            } else if (block.getState() instanceof Vault vault) {
                NBT.get(vault, nbt -> {
                    ReadableNBT lootNbt = nbt.getCompound("config");
                    if (lootNbt == null) {
                        sendErrorMessage(player, "No loot table found for this Vault. or a loot table is already generated for this block");
                        return;
                    }
                    String table = lootNbt.getString("loot_table");
                    if (table != null && !table.isEmpty()) {
                        displayLootTable(player, table);
                    } else {
                        sendErrorMessage(player, "No loot table found for this Vault.");
                    }
                });
            } else {
                sendErrorMessage(player, "This block is not lootable.");
            }
        } else if (targetEntity instanceof Lootable lootable) {
            if (lootable.getLootTable() == null) {
                sendErrorMessage(player, "No loot table found for this entity. or a loot table is already generated for this entity");
                return;
            }
            displayLootTable(player, lootable.getLootTable().getKey().toString());
        } else {
            sendErrorMessage(player, "No block or entity targeted.");
        }
    }

    /**
     * Displays the loot table to the player with clickable and hoverable text.
     *
     * @param player       The player.
     * @param lootTableKey The loot table key.
     */
    private void displayLootTable(Player player, String lootTableKey) {
        String message = PREFIX + " " + INFO_GRADIENT + "Loot table:</gradient> " +
                "<hover:show_text:'" + INFO_GRADIENT + "Click to copy loot table to chat</gradient>'>" +
                "<click:suggest_command:" + lootTableKey + ">" +
                INFO_GRADIENT + lootTableKey + "</gradient></click></hover>";
        player.sendMessage(MM.deserialize(message));
    }

    /**
     * Handles the 'get_chest' subcommand.
     *
     * @param args   The command arguments.
     * @param player The player executing the command.
     */
    private void handleGetChestCommand(String[] args, Player player) {
        String lootTable = args[1];
        long seed = 0L;

        if (args.length > 2) {
            try {
                seed = Long.parseLong(args[2]);
            } catch (NumberFormatException e) {
                sendErrorMessage(player, "Invalid seed: " + args[2]);
                return;
            }
        }

        giveChestWithLootTable(player, lootTable, seed);
        sendInfoMessage(player, "Given chest with loot table: " + lootTable);
    }

    /**
     * Gives the player a chest with the specified loot table.
     *
     * @param player    The player.
     * @param lootTable The loot table key.
     * @param seed      The seed.
     */
    private void giveChestWithLootTable(Player player, String lootTable, Long seed) {
        ItemStack chest = new ItemStack(Material.CHEST);
        if (seed == 0L) {
            seed = System.currentTimeMillis();
        }
        Long finalSeed = seed;

        // Do not edit the NBT code as per your request
        NBT.modifyComponents(chest, nbt -> {
            ReadWriteNBT lootNbt = nbt.getOrCreateCompound("minecraft:container_loot");
            lootNbt.setString("loot_table", lootTable);
            lootNbt.setLong("seed", finalSeed);
        });

        player.getInventory().addItem(chest);
    }

    /**
     * Handles the 'set_loot' subcommand.
     *
     * @param args   The command arguments.
     * @param player The player executing the command.
     */
    private void handleSetLootCommand(String[] args, Player player) {
        RayTraceResult blockResult = player.rayTraceBlocks(5.0);
        if (blockResult != null && blockResult.getHitBlock() != null) {
            Block block = blockResult.getHitBlock();
            if (block.getState() instanceof Container container) {
                container.getInventory().clear();

                // Do not edit the NBT code as per your request
                NBT.modify(
                        container,
                        nbt -> {
                            nbt.setString("LootTable", args[1]);
                            nbt.setLong("LootTableSeed", System.currentTimeMillis());
                        }
                );
                sendInfoMessage(player, "Loot table set to " + args[1]);
            } else {
                sendErrorMessage(player, "This block is not a container.");
            }
        } else {
            sendErrorMessage(player, "No block targeted.");
        }
    }

    @Override
    public @Nullable List<String> onTabComplete(
            @NotNull CommandSender sender,
            @NotNull Command command,
            @NotNull String label,
            @NotNull String[] args) {

        if (!sender.hasPermission("loottable.admin")) {
            return null;
        }

        if (args.length == 1) {
            return List.of("reload", "get", "get_chest", "set_loot");
        }

        if (args.length == 2 && (args[0].equalsIgnoreCase("get_chest") || args[0].equalsIgnoreCase("set_loot"))) {
            args[1] = args[1].toLowerCase();
            return plugin.getLootTableScanner().getLootTables().stream()
                    .filter(lootTable -> lootTable.contains(args[1]))
                    .toList();
        }

        return null;
    }
}
