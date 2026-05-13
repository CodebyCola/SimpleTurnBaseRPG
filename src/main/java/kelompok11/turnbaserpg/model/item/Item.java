/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package kelompok11.turnbaserpg.model.item;

/**
 *
 * @author Pongo
 */
public abstract class Item {

    protected String name;
    protected String description;
    protected int price;

    public Item(String name, String description, int price) {
        this.name = name;
        this.description = description;
        this.price = price;
    }

}
