/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package kelompok11.turnbaserpg.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import kelompok11.turnbaserpg.database.Connector;
import kelompok11.turnbaserpg.enums.PotionTier;
import kelompok11.turnbaserpg.model.character.Player;
import kelompok11.turnbaserpg.model.item.consumable.*;
import kelompok11.turnbaserpg.model.character.Inventory;
import kelompok11.turnbaserpg.model.character.InventorySlot;
import kelompok11.turnbaserpg.model.item.Item;
import kelompok11.turnbaserpg.model.item.consumable.Potion;
import kelompok11.turnbaserpg.utils.GameLogger;

/**
 *
 * @author Pongo
 */
public class InventoryDAO {

    public void save(Player p) {
        String deleteQuery
                = "DELETE FROM inventory WHERE player_id = ?";

        String insertQuery = """
        INSERT INTO inventory
        (player_id, item_type, tier, quantity)
        VALUES (?, ?, ?, ?)
    """;

        try (Connection conn = Connector.connect()) {

            // hapus inventory lama
            PreparedStatement deletePs
                    = conn.prepareStatement(deleteQuery);

            deletePs.setInt(1, p.getId());

            deletePs.executeUpdate();

            // insert inventory terbaru
            PreparedStatement insertPs
                    = conn.prepareStatement(insertQuery);

            for (InventorySlot slot : p.getInventory().getSlots()) {

                if (slot == null || slot.getItem() == null) {
                    continue;
                }
                insertPs.setInt(1, p.getId());

                if (slot.getItem() instanceof Potion potion) {

                    insertPs.setString(2,
                            potion.getType().name());

                    insertPs.setString(3,
                            potion.getTier().name());

                }

                insertPs.setInt(4, slot.getQuantity());

                insertPs.addBatch();
            }

            insertPs.executeBatch();

            GameLogger.info("Saving inventory success!");

        } catch (SQLException e) {

            GameLogger.error(
                    "Error saving inventory: "
                    + e.getMessage()
            );
        }
    }

    public void read(Player p) {
        String query = "SELECT * FROM inventory WHERE player_id = ?";
        try (Connection conn = Connector.connect()) {

            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, p.getId());
            ResultSet rs = ps.executeQuery();

            Inventory inventory = new Inventory();

            while (rs.next()) {

                String itemType = rs.getString("item_type");
                String tierStr = rs.getString("tier");
                int quantity = rs.getInt("quantity");
                Item item = null;

                PotionTier tier = null;
                if (tierStr != null) {
                    tier = PotionTier.valueOf(tierStr);
                }

                switch (itemType) {

                    case "HEALTH":
                        item = new HealthPotion(tier);
                        break;

                    case "MANA":
                        item = new ManaPotion(tier);
                        break;
                }

                if (item != null) {

                    InventorySlot slot
                            = new InventorySlot(item, quantity);

                    inventory.getSlots().add(slot);
                }
            }
            p.setInventory(inventory);
            GameLogger.info("loading inventory success");

        } catch (SQLException e) {
            GameLogger.error("failed to load inventory" + e.getMessage());
        }
    }

}
