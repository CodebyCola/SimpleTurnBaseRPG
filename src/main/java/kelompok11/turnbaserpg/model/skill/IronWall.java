/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package kelompok11.turnbaserpg.model.skill;

import kelompok11.turnbaserpg.enums.SkillType;
import kelompok11.turnbaserpg.model.character.Character;
import kelompok11.turnbaserpg.model.buff.*;
import kelompok11.turnbaserpg.utils.GameConstants;

/**
 *
 * @author Pongo
 */
public class IronWall extends Skill {

    public IronWall() {
        super("Iron Wall", "Increase defense temporarily", 20, 10,
                GameConstants.SKILL_COOLDOWN_MEDIUM, SkillType.DEFEND);
    }

    @Override
    public boolean cast(Character caster, Character target) {
        if (currentCoolDown > 0) {
            System.out.println("Skill is on cooldown!");
            return false;
        }
        if (caster.getStats().getCurrentMana() < manaCost) {
            System.out.println("Not enough mana!");
            return false;
        }
        caster.addBuff(new DefenseBuff(effectValue));
        caster.getStats().decreaseCurrentMana(manaCost);
        currentCoolDown = cooldown;
        System.out.println(caster.getCharacterName() + " casts " + name + "!");
        System.out.println("Defense +" + effectValue + " for 3 turns.");
        return true;
    }

}
