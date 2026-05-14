/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package kelompok11.turnbaserpg.model.skill;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;
import kelompok11.turnbaserpg.enums.SkillType;
import kelompok11.turnbaserpg.model.Character.Character;
import kelompok11.turnbaserpg.model.Character.Player;

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
    protected int currentCoolDown;
    protected SkillType type;

    public Skill(String name, String description, int effectValue, int manaCost, int cooldown, SkillType type) {
        this.name = name;
        this.description = description;
        this.effectValue = effectValue;
        this.manaCost = manaCost;
        this.cooldown = cooldown;
        this.type = type;
    }

    public abstract boolean cast(Character caster, Character target);

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
    
    public int getManaCost() {
        return manaCost;
    }

    public void reduceCooldown() {

        if (currentCoolDown > 0) {
            currentCoolDown--;
        }
    }

    public int getCurrentCooldown() {
        return currentCoolDown;
    }

    // Fungsi ambil random skill yang belum dimiliki oleh player
    public static Skill getRandomSkill(Player player) {
        ArrayList<Skill> skillPool = new ArrayList<>();

        skillPool.add(new FireBall());
        skillPool.add(new ThunderStrike());
        skillPool.add(new IceSpear());
        skillPool.add(new IronWall());
        skillPool.add(new ShadowSlash());
        skillPool.add(new EarthCrusher());
        skillPool.add(new DragonFury());

        ArrayList<Skill> availableSkills = new ArrayList<>();

        for (Skill skill : skillPool) {

            boolean alreadyOwned = false;

            // cek apakah skill sudah dimiliki oleh player
            for (Skill unlocked : player.getUnlockedSkills()) {

                if (unlocked.getName().equals(skill.getName())) {
                    alreadyOwned = true;
                    break;
                }
            }

            // kalau belum dimiliki masukkan ke list 
            if (!alreadyOwned) {
                availableSkills.add(skill);
            }
        }

        // Semua skill sudah dimiliki
        if (availableSkills.isEmpty()) {
            return null;
        }

        // ambil random index dari list skill yang bisa dimiliki
        int randomIndex = ThreadLocalRandom.current()
                .nextInt(availableSkills.size());

        return availableSkills.get(randomIndex);
    }

}
