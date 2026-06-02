package kelompok11.turnbaserpg.model.character;

import java.util.ArrayList;
import kelompok11.turnbaserpg.model.item.Item;
import kelompok11.turnbaserpg.model.item.Usable;
import kelompok11.turnbaserpg.utils.GameConstants;

public class Inventory {

    private ArrayList<InventorySlot> slots;

    public Inventory() {
        slots = new ArrayList<>();
    }

    public ArrayList<InventorySlot> getSlots() {
        return slots;
    }

    public void addItem(Item item) {
        for (InventorySlot slot : slots) {
            Item slotItem = slot.getItem();
            if (slotItem.getClass() == item.getClass()
                    && slotItem.getName().equals(item.getName())) {
                slot.addQuantity(1);
                return;
            }
        }
        if (slots.size() >= GameConstants.MAX_INVENTORY_SLOT) {
            return;
        }
        slots.add(new InventorySlot(item, 1));
    }

    public InventorySlot getSlot(int index) {
        if (index < 0 || index >= slots.size()) {
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

    public void useItem(int index, Player target) {
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

    public boolean isEmpty() {
        return slots.isEmpty();
    }
}
