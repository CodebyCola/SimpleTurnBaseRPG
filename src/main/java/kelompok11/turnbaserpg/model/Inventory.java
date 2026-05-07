/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package kelompok11.turnbaserpg.model;

import java.util.List;

/**
 *
 * @author Pongo
 */
public class Inventory {

    private List<Item> items;

    public List<Item> getItems() {
        return items;
    }

    public void setItems(Item item) {
        this.items.add(item);
    }

}
