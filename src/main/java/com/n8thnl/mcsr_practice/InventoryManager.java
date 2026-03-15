package com.n8thnl.mcsr_practice;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

public class InventoryManager {

    public static void applyInventoryFromFile(PlayerEntity player, String filename) {
        try {
            // 1. Resolve the path to .minecraft/config/mcsr-practice/[filename]
            Path configPath = FabricLoader.getInstance().getConfigDir()
                    .resolve("mcsr-practice")
                    .resolve(filename);

            File file = configPath.toFile();
            if (!file.exists()) {
                System.out.println("Inventory file not found: " + configPath);
                return;
            }

            // 2. Read the file content and parse as JSON
            String content = new String(Files.readAllBytes(configPath));
            JsonObject root = new JsonParser().parse(content).getAsJsonObject();
            JsonArray slots = root.getAsJsonArray("slots");

            // 3. Clear the player's existing inventory first
            player.inventory.clear();

            // 4. Iterate and apply items
            for (JsonElement element : slots) {
                JsonObject slotObj = element.getAsJsonObject();
                int slotIdx = slotObj.get("slot").getAsInt();
                String itemId = slotObj.get("id").getAsString();
                int count = slotObj.get("count").getAsInt();

                // Convert the ID string (e.g., "minecraft:iron_pickaxe") to a Registry Item
                Item item = Registry.ITEM.get(new Identifier(itemId));

                if (item != null) {
                    // Set the stack into the specific slot index
                    player.inventory.setStack(slotIdx, new ItemStack(item, count));
                    System.out.println("set inventory for item " + itemId);
                } else {
                    System.out.println("could not find item " + itemId);
                }
            }

            player.inventory.markDirty();
            if (player instanceof ServerPlayerEntity) {
                ServerPlayerEntity serverPlayer = (ServerPlayerEntity) player;
                serverPlayer.playerScreenHandler.sendContentUpdates();
            }

            System.out.println("Successfully applied inventory: " + filename);

        } catch (Exception e) {
            System.err.println("Failed to parse inventory JSON: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
