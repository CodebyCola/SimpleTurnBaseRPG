/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package kelompok11.turnbaserpg.model.buff;

import kelompok11.turnbaserpg.enums.BuffType;
import kelompok11.turnbaserpg.model.Character.Character;

/**
 *
 * @author Pongo
 */
public class AttackBuff extends Buff {

    private int effectValue;

    public AttackBuff(int effectValue) {
        super("Attack Buff", 3);
        this.effectValue = effectValue;
    }

    @Override
    public void use(Character target) {
        target.getStats().applyBuff(effectValue, BuffType.ATTACK);
    }

    @Override
    public void remove(Character target) {
        target.getStats().removeBuff(effectValue, BuffType.ATTACK);
    }

}
