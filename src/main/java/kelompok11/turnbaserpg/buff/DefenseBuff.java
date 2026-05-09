/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package kelompok11.turnbaserpg.buff;
import kelompok11.turnbaserpg.enums.BuffType;
import kelompok11.turnbaserpg.model.Character;
/**
 *
 * @author Pongo
 */
public class DefenseBuff extends Buff {

    private int effectValue;
    private BuffType type;
    
    public DefenseBuff(int effectValue) {
        super("Defense Buff", 3);
        this.effectValue = effectValue;
    }

    @Override
    public void use(Character target) {
        target.getStats().applyBuff(effectValue, type.DEFENSE);
    }

    @Override
    public void remove(Character target) {
        target.getStats().removeBuff(effectValue, type.DEFENSE);
    }

}
