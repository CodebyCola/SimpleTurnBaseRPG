package kelompok11.turnbaserpg.model.character;

import kelompok11.turnbaserpg.model.item.Item;

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
