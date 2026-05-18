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
public class InventorySlot {

    private Item item;
    private int quantity;

    public InventorySlot(Item item, int quantity) {
        this.item = item;
        this.quantity = quantity;
    }

    public Item getItem() {
        return item;
    }

    public int getQuantity() {
        return quantity;
    }

    public void addQuantity(int amount) {
        quantity += amount;
    }

    public void reduceQuantity(int amount) {
        quantity -= amount;
    }

    public boolean isEmpty() {
        return quantity <= 0;
    }

}
