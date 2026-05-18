/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package kelompok11.turnbaserpg.model.buff;

import kelompok11.turnbaserpg.enums.BuffType;
import kelompok11.turnbaserpg.model.character.Character;

/**
 *
 * @author Pongo
 */
public class ManaBuff extends Buff {

    private int effectValue;

    public ManaBuff(int effectValue) {
        super("Mana Buff", 3);
        this.effectValue = effectValue;
    }

    @Override
    public void use(Character target) {
        target.getStats().applyBuff(effectValue, BuffType.MANA);
    }

    @Override
    public void remove(Character target) {
        target.getStats().removeBuff(effectValue, BuffType.MANA);
    }
}
