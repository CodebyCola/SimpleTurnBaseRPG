package kelompok11.turnbaserpg.model.character;

import java.util.concurrent.ThreadLocalRandom;
import kelompok11.turnbaserpg.utils.GameConstants;

public class Enemy extends Character {

    public Enemy(String characterName) {
        super(characterName, new Stats(
                GameConstants.BASE_ENEMY_HP,
                GameConstants.BASE_ENEMY_ATK,
                GameConstants.BASE_ENEMY_DEF,
                0,
                0
        ));
    }

    @Override
    public int basicAttack(Character target) {
        return target.takeDamage(stats.getTotalAttack());
    }

    public int skillAttack(Character target) {
        double multiplier = ThreadLocalRandom.current().nextDouble(
                GameConstants.ENEMY_SKILL_MIN_MULTIPLIER,
                GameConstants.ENEMY_SKILL_MAX_MULTIPLIER
        );
        int damage = (int) (stats.getTotalAttack() * multiplier);
        return target.takeDamage(damage);
    }
}
