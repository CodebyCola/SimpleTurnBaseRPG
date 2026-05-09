/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package kelompok11.turnbaserpg.skill;

import kelompok11.turnbaserpg.enums.SkillType;
import kelompok11.turnbaserpg.model.Character;

/**
 *
 * @author Pongo
 */
public abstract class Skill {

    protected String name;
    protected String description;
    protected int effectValue;
    protected int manaCost;
    protected int cooldown;
    protected SkillType type;

    public Skill(String name, String description, int effectValue, int manaCost, int cooldown, SkillType type) {
        this.name = name;
        this.description = description;
        this.effectValue = effectValue;
        this.manaCost = manaCost;
        this.cooldown = cooldown;
        this.type = type;
    }

    protected void dealDamage(Character caster, Character target) {
        int totalDamage = caster.getStats().getBaseAttack() + effectValue;

        target.takeDamage(totalDamage);
    }

    protected void applyDefend(Character caster) {
        int totalDefense = caster.getStats().getBaseDefense() + effectValue;
        // Masih mandek, perlu lanjutan untuk logic gimana menambahkan buff sementara ke total stat defense 
    }

    public String getName() {
        return name;
    }
    
    public String getDescription() {
        return description;
    }
}
