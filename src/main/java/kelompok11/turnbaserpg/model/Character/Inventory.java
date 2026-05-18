/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package kelompok11.turnbaserpg.model.character;

import java.util.ArrayList;
import kelompok11.turnbaserpg.model.item.Item;
import kelompok11.turnbaserpg.model.item.Usable;
import kelompok11.turnbaserpg.utils.GameConstants;

/**
 *
 * @author Pongo
 */
public class Inventory {

    private ArrayList<InventorySlot> slots;

    public Inventory() {
        slots = new ArrayList<>();
    }

    public ArrayList<InventorySlot> getSlots() {
        return slots;
    }

    public void addItem(Item item) {

        // cek apakah item stackable dan sudah ada
        for (InventorySlot slot : slots) {

            Item slotItem = slot.getItem();

            if (slotItem.getClass() == item.getClass()
                    && slotItem.getName().equals(item.getName())) {

                slot.addQuantity(1);

                System.out.println(item.getName()
                        + " quantity increased!");

                return;
            }
        }

        // kalau belum ada slot
        if (slots.size() >= GameConstants.MAX_INVENTORY_SLOT) {

            System.out.println("Inventory full!");
            return;
        }

        slots.add(new InventorySlot(item, 1));

        System.out.println(item.getName()
                + " added to inventory!");
    }

    public InventorySlot getSlot(int index) {

        if (index < 0 || index >= slots.size()) {

            System.out.println("Invalid slot index");
            return null;
        }

        return slots.get(index);
    }

    public void removeItem(int index) {

        InventorySlot slot = getSlot(index);

        if (slot == null) {
            return;
        }

        slot.reduceQuantity(1);

        if (slot.isEmpty()) {
            slots.remove(index);
        }
    }

    public void useItem(int index, Character target) {

        InventorySlot slot = getSlot(index);

        if (slot == null) {
            return;
        }

        Item item = slot.getItem();

        if (item instanceof Usable usable) {

            usable.use(target);

            removeItem(index);
        }
    }

    public void showInventory() {

        if (slots.isEmpty()) {

            System.out.println("Inventory empty");
            return;
        }

        System.out.println("=== INVENTORY ===");

        for (int i = 0; i < slots.size(); i++) {

            InventorySlot slot = slots.get(i);

            Item item = slot.getItem();

            System.out.println(
                    "[" + (i + 1) + "] "
                    + item.getName()
                    + " x" + slot.getQuantity()
            );

            System.out.println(
                    "Price : " + item.getPrice()
            );

            System.out.println(
                    "Description : "
                    + item.getDescription()
            );

            System.out.println("----------------");
        }
    }

    public boolean isEmpty() {
        return slots.isEmpty();
    }
}
