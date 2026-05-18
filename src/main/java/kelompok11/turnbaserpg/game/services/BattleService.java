package kelompok11.turnbaserpg.game.services;

import java.util.ArrayList;
import java.util.List;
import kelompok11.turnbaserpg.enums.BattleResult;
import static kelompok11.turnbaserpg.enums.PotionTier.*;
import kelompok11.turnbaserpg.model.character.Enemy;
import kelompok11.turnbaserpg.model.character.Player;
import kelompok11.turnbaserpg.model.item.consumable.HealthPotion;
import kelompok11.turnbaserpg.model.item.consumable.ManaPotion;
import kelompok11.turnbaserpg.model.skill.Skill;
import kelompok11.turnbaserpg.utils.GameConstants;
import kelompok11.turnbaserpg.utils.GameLogger;

/**
 * Pure battle logic service. Manages game state transitions for a single
 * player-vs-enemy encounter.
 *
 * All output is returned as {@link BattleEvent} lists — no I/O happens here.
 * Controllers are responsible for presenting events to the view and feeding
 * player actions back via the action methods.
 */
public class BattleService {

    private final Player player;
    private final Enemy enemy;

    private int enemyTurnCounter;
    private boolean isEscaped;
    private boolean playerTurn;
    private boolean playerDefend;

    public BattleService(Player player, Enemy enemy) {
        this.player = player;
        this.enemy = enemy;
        this.isEscaped = false;
        this.playerTurn = true;
        this.playerDefend = false;
        this.enemyTurnCounter = 1;
    }

    // Battle Initialisation
    public List<BattleEvent> initBattle() {
        playerTurn = true;
        enemyTurnCounter = 1;

        List<BattleEvent> events = new ArrayList<>();
        events.add(new BattleEvent(BattleEvent.Type.BATTLE_START,
                "--- Battle Start: " + player.getCharacterName()
                + " vs " + enemy.getCharacterName() + " ---"));
        return events;
    }

    // Turn Lifecycle (called by BattleController per turn)
    /**
     * Prepares the player's turn: regenerates mana, ticks buffs and cooldowns.
     * Returns status events for the view. The controller then asks the player
     * for an action and calls one of the action methods below.
     */
    public List<BattleEvent> beginPlayerTurn() {
        gainMana();
        player.updateBuffs();
        player.updateSkillCooldowns();

        List<BattleEvent> events = new ArrayList<>();
        events.add(new BattleEvent(BattleEvent.Type.PLAYER_STATUS,
                String.format("--- Your Turn ---  HP: %d/%d   Mana: %d/%d   Enemy HP: %d/%d",
                        player.getStats().getCurrentHP(), player.getStats().getMaxHP(),
                        player.getStats().getCurrentMana(), player.getStats().getBaseMana(),
                        enemy.getStats().getCurrentHP(), enemy.getStats().getMaxHP())));
        return events;
    }

//     Executes the enemy's turn and returns events describing what happened.
    public List<BattleEvent> executeEnemyTurn() {
        List<BattleEvent> events = new ArrayList<>();
        events.add(new BattleEvent(BattleEvent.Type.ENEMY_TURN,
                "--- Enemy Turn: " + enemy.getCharacterName() + " ---"));

        if (enemyTurnCounter % 3 == 0) {
            enemy.skillAttack(player);
            events.add(new BattleEvent(BattleEvent.Type.ENEMY_TURN,
                    enemy.getCharacterName() + " used a skill attack!"));
        } else {
            enemy.basicAttack(player);
            events.add(new BattleEvent(BattleEvent.Type.ENEMY_TURN,
                    enemy.getCharacterName() + " attacked!"));
        }

        events.add(new BattleEvent(BattleEvent.Type.PLAYER_STATUS,
                String.format("Player HP: %d/%d",
                        player.getStats().getCurrentHP(), player.getStats().getMaxHP())));

        if (playerDefend) {
            player.setDefend(false);
            playerDefend = false;
        }

        enemyTurnCounter++;
        return events;
    }

    // Player Actions (called by BattleController based on user input)
    // Returns null if the action was invalid and the turn should NOT advance.
    public List<BattleEvent> actionBasicAttack() {
        player.basicAttack(enemy);
        List<BattleEvent> events = new ArrayList<>();
        events.add(new BattleEvent(BattleEvent.Type.ACTION_RESULT,
                player.getCharacterName() + " attacked " + enemy.getCharacterName() + "!"
                + "  Enemy HP: " + enemy.getStats().getCurrentHP()
                + "/" + enemy.getStats().getMaxHP()));
        return events;
    }

//   Player chooses Defend.
    public List<BattleEvent> actionDefend() {
        player.setDefend(true);
        playerDefend = true;
        List<BattleEvent> events = new ArrayList<>();
        events.add(new BattleEvent(BattleEvent.Type.ACTION_RESULT,
                player.getCharacterName() + " takes a defensive stance!"));
        return events;
    }

    /**
     * Player chooses to use a skill by 0-based index into the unlocked skill
     * list. Returns null if the action was invalid (bad index, cooldown, no
     * mana).
     */
    public List<BattleEvent> actionUseSkill(int skillIndex) {
        var skills = player.getUnlockedSkills();
        List<BattleEvent> events = new ArrayList<>();

        if (skills.isEmpty()) {
            events.add(new BattleEvent(BattleEvent.Type.ERROR, "No skills available!"));
            return null;
        }
        if (skillIndex < 0 || skillIndex >= skills.size()) {
            events.add(new BattleEvent(BattleEvent.Type.ERROR, "Invalid skill selection!"));
            return null;
        }

        Skill skill = skills.get(skillIndex);
        boolean success = skill.cast(player, enemy);

        if (!success) {
            events.add(new BattleEvent(BattleEvent.Type.ERROR,
                    "Cannot use " + skill.getName() + " right now (cooldown or insufficient mana)."));
            return null;
        }

        events.add(new BattleEvent(BattleEvent.Type.SKILL_CAST,
                player.getCharacterName() + " cast " + skill.getName() + "!"));
        return events;
    }

    /**
     * Player chooses to use an item by 0-based inventory index. Returns null if
     * the action was invalid.
     */
    public List<BattleEvent> actionUseItem(int inventoryIndex) {
        List<BattleEvent> events = new ArrayList<>();

        if (player.getInventory().isEmpty()) {
            events.add(new BattleEvent(BattleEvent.Type.ERROR, "Inventory is empty!"));
            return null;
        }
        if (player.getInventory().getSlot(inventoryIndex) == null) {
            events.add(new BattleEvent(BattleEvent.Type.ERROR, "Invalid item selection!"));
            return null;
        }

        String itemName = player.getInventory().getSlot(inventoryIndex).getItem().getName();
        player.getInventory().useItem(inventoryIndex, player);
        events.add(new BattleEvent(BattleEvent.Type.ITEM_USED,
                player.getCharacterName() + " used " + itemName + "!"));
        return events;
    }

//     Player attempts to escape (50 % chance).
    public List<BattleEvent> actionEscape() {
        List<BattleEvent> events = new ArrayList<>();
        if (Math.random() < 0.5) {
            isEscaped = true;
            events.add(new BattleEvent(BattleEvent.Type.ESCAPE_SUCCESS,
                    player.getCharacterName() + " escaped!"));
        } else {
            events.add(new BattleEvent(BattleEvent.Type.ESCAPE_FAILED, "Escape failed!"));
        }
        return events;
    }

    // Battle State Queries
    public boolean isBattleOngoing() {
        return enemy.isAlive() && player.isAlive() && !isEscaped;
    }

    public boolean isPlayerTurn() {
        return playerTurn;
    }

    public void setPlayerTurn(boolean v) {
        this.playerTurn = v;
    }

    // -------------------------------------------------------------------------
    // Battle Resolution
    // -------------------------------------------------------------------------
    /**
     * Applies win rewards and returns the final {@link BattleResult}. Call once
     * {@link #isBattleOngoing()} returns false.
     */
    public BattleResult resolveBattle() {
        BattleResult result = computeResult();
        if (result == BattleResult.WIN) {
            applyWinRewards();
        }
        GameLogger.info("Battle resolved: " + result
                + " | Player: " + player.getCharacterName());
        return result;
    }

    // Helpers
    void gainMana() {
        switch (player.getRole()) {
            case WARRIOR ->
                player.getStats().increaseCurrentMana(10);
            case MAGE ->
                player.getStats().increaseCurrentMana(15);
            case ARCHER ->
                player.getStats().increaseCurrentMana(12);
        }
    }

    private void applyWinRewards() {
        int exp = (int) (GameConstants.BASE_EXP_REWARD
                * GameConstants.EXP_SCALING_PER_LEVEL * (player.getLevel() * 0.1));
        player.gainExp(exp);

        if (Math.random() < 0.3) {
            int gold = (int) (GameConstants.BASE_GOLD_REWARD
                    * GameConstants.GOLD_SCALING_PER_LEVEL * player.getLevel());
            player.gainGold(gold);
        }

        if (Math.random() < GameConstants.LOOT_DROP_RATE) {
            if (Math.random() < 0.5) {
                player.getInventory().addItem(new HealthPotion(SMALL));
            } else {
                player.getInventory().addItem(new ManaPotion(SMALL));
            }
        }
    }

    private BattleResult computeResult() {
        if (!player.isAlive()) {
            return BattleResult.LOSE;
        }
        if (isEscaped) {
            return BattleResult.ESCAPED;
        }
        return BattleResult.WIN;
    }
}
