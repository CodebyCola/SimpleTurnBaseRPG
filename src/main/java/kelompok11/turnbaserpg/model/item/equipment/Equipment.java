/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package kelompok11.turnbaserpg.model.item.equipment;

import kelompok11.turnbaserpg.enums.BonusStat;
import kelompok11.turnbaserpg.enums.EquipmentType;
import kelompok11.turnbaserpg.enums.ItemRarity;
import kelompok11.turnbaserpg.model.character.Player;
import kelompok11.turnbaserpg.model.item.Equipable;
import kelompok11.turnbaserpg.model.item.Item;
import kelompok11.turnbaserpg.utils.GameLogger;

/**
 *
 * @author Pongo
 */
public class Equipment extends Item implements Equipable {
    
    protected ItemRarity rarity;
    protected String name;
    protected int effectValue;
    protected BonusStat stat;
    protected EquipmentType type;
    
    public Equipment(String name, String description, int price,
            ItemRarity rarity, int effectValue, BonusStat stat, EquipmentType type) {
        super(name, description, price);
        this.rarity = rarity;
        this.effectValue = effectValue;
        this.stat = stat;
        this.type = type;
    }
    
    public BonusStat getEquipmentStat() {
        return stat;
    }
    
    public EquipmentType getEquipmentType() {
        return type;
    }
    
    public ItemRarity getRarity() {
        return rarity;
    }
    
    public String getName() {
        return name;
    }
    
    public int getEffectValue() {
        return effectValue;
    }
    
    public void equip(Player player) {
        player.equip(this);
    }
    
    public void unequip(Player player) {
        player.unequip(this);
    }
    
}
