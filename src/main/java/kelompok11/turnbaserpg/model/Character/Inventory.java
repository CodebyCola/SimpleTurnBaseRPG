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

    private ArrayList<Item> items;

    public Inventory() {
        items = new ArrayList<>();
    }

    public Item getItem(int index) {
        if (index < 0 || index >= items.size()) {
            System.out.println("Invalid item index");
            return null;
        }

        return items.get(index);
    }

    public void addItem(Item item) {
        if (items.size() < GameConstants.MAX_INVENTORY_SLOT) {
            items.add(item);
        } else {
            System.out.println("Inventory full, cant add more item!");
        }

    }

    public void removeItem(int index) {

        if (index < 0 || index >= items.size()) {

            System.out.println("Invalid item index");
            return;

        }
        if (items.isEmpty()) {
            System.out.println("Inventory is empty");
            return;
        }

        items.remove(index);
    }

    public ArrayList<Item> getItems() {
        return items;
    }

    public void showInventory() {

        if (!items.isEmpty()) {

            for (int i = 0; i < items.size(); i++) {
                Item item = items.get(i);

                System.out.println("[" + (i + 1) + "] " + item.getName());
                System.out.println("Price: " + item.getPrice());
                System.out.println("Description" + item.getDescription());
                System.out.println("----------------");
            }
        } else {
            System.out.println("Inventory empty");
        }
    }

    public void useItem(int index, Character target) {
        Item item = getItem(index);
        if (item == null) {
            return;
        }

        if (item instanceof Usable usable) {
            usable.use(target);
            removeItem(index);
        }
    }

}
