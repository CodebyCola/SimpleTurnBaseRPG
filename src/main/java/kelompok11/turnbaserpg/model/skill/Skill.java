package kelompok11.turnbaserpg.model.skill;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;
import kelompok11.turnbaserpg.model.enums.SkillType;
import kelompok11.turnbaserpg.model.character.Character;
import kelompok11.turnbaserpg.model.character.Player;

public abstract class Skill {

    protected String name;
    protected String description;
    protected int effectValue;
    protected int manaCost;
    protected int cooldown;
    protected int currentCoolDown;
    protected SkillType type;

    public Skill(String name, String description, int effectValue, int manaCost,
                 int cooldown, SkillType type) {
        this.name = name;
        this.description = description;
        this.effectValue = effectValue;
        this.manaCost = manaCost;
        this.cooldown = cooldown;
        this.type = type;
    }

    protected boolean canCast(Character caster) {
        return currentCoolDown <= 0 && caster.getStats().getCurrentMana() >= manaCost;
    }

    protected void applyManaAndCooldown(Character caster) {
        caster.getStats().decreaseCurrentMana(manaCost);
        currentCoolDown = cooldown;
    }

    public abstract boolean cast(Character caster, Character target);

    public String getName() { return name; }
    public String getDescription() { return description; }
    public int getManaCost() { return manaCost; }

    public void reduceCooldown() {
        if (currentCoolDown > 0) {
            currentCoolDown--;
        }
    }

    public int getCurrentCooldown() { return currentCoolDown; }

    public static Skill getRandomSkill(Player player) {
        ArrayList<Skill> skillPool = new ArrayList<>();
        skillPool.add(new FireBall());
        skillPool.add(new ThunderStrike());
        skillPool.add(new IceSpear());
        skillPool.add(new IronWall());
        skillPool.add(new ShadowSlash());
        skillPool.add(new EarthCrusher());
        skillPool.add(new DragonFury());
        skillPool.add(new LifeDrain());
        skillPool.add(new StoneBody());
        skillPool.add(new GreaterHeal());
        skillPool.add(new GuardianAura());
        skillPool.add(new ArcanePower());
        skillPool.add(new BerserkerRage());
        

        ArrayList<Skill> availableSkills = new ArrayList<>();
        for (Skill skill : skillPool) {
            boolean alreadyOwned = false;
            for (Skill unlocked : player.getUnlockedSkills()) {
                if (unlocked.getName().equals(skill.getName())) {
                    alreadyOwned = true;
                    break;
                }
            }
            if (!alreadyOwned) {
                availableSkills.add(skill);
            }
        }

        if (availableSkills.isEmpty()) {
            return null;
        }
        return availableSkills.get(ThreadLocalRandom.current().nextInt(availableSkills.size()));
    }
}
