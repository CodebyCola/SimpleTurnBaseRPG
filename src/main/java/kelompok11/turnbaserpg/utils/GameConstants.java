/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package kelompok11.turnbaserpg.utils;

/**
 *
 * @author Pongo
 */
// Class ini isinya untuk variable global, jadi kalau kita mau ganti atau konfigurasinya
// gak perlu ubah satu per satu per filenya.
// Segala konfigurasi base stats dan lain lain di atur disini!.
public class GameConstants {
//    Character Constants

    public static final int MAX_LEVEL = 100;
    public static final int INITIAL_EXP_REQUIRED = 500;
    public static final double EXP_SCALING_MULTIPLIER = 1.2;
    public static final int INITIAL_GOLD = 100;
    public static final int DEFAULT_LEVEL = 1;
    public static final int DEFAULT_FLOOR = 1;

//    Character role initial stats
//    Warrior Stats
    public static final class WarriorStats {

        public static final int INITIAL_HP = 120;
        public static final int INITIAL_ATK = 15;
        public static final int INITIAL_DEF = 30;
        public static final int INITIAL_MAGIC = 5;
        public static final int INITIAL_MANA = 30;
        public static final int LEVEL_UP_HP_BONUS = 30;
        public static final int LEVEL_UP_DEF_BONUS = 10;
    }

//    Mage Stats
    public static final class MageStats {

        public static final int INITIAL_HP = 70;
        public static final int INITIAL_ATK = 10;
        public static final int INITIAL_DEF = 5;
        public static final int INITIAL_MAGIC = 30;
        public static final int INITIAL_MANA = 70;
        public static final int LEVEL_UP_MAGIC_BONUS = 5;
        public static final int LEVEL_UP_MANA_BONUS = 15;
    }

//    Archer Stats
    public static final class ArcherStats {

        public static final int INITIAL_HP = 90;
        public static final int INITIAL_ATK = 25;
        public static final int INITIAL_DEF = 8;
        public static final int INITIAL_MAGIC = 10;
        public static final int INITIAL_MANA = 50;
        public static final int LEVEL_UP_ATK_BONUS = 8;
        public static final int LEVEL_UP_DEF_BONUS = 3;
        public static final int LEVEL_UP_HP_BONUS = 10;
    }

//    Level up stats bonus
    public static final int LEVEL_UP_HP_BONUS = 10;
    public static final int LEVEL_UP_ATK_BONUS = 2;
    public static final int LEVEL_UP_DEF_BONUS = 2;
    public static final int LEVEL_UP_MAGIC_BONUS = 2;
    public static final int LEVEL_UP_MANA_BONUS = 5;

//    Battle System Constants
    public static final int MIN_DAMAGE = 1;
    public static final double BASE_CRITICAL_CHANCE = 0.2; // in percentage
    public static final int CRITICAL_HIT_MULTIPLIER = 2;
//    public static final double PLAYER_ATTACK_VARIANCE = 0.1; // in percentage
//    public static final double ENEMY_ATTACK_VARIANCE = 0.2; // in percentage
    public static final double ENEMY_SKILL_MIN_MULTIPLIER = 1.1;
    public static final double ENEMY_SKILL_MAX_MULTIPLIER = 2.0;
    public static final int DEFEND_BONUS = 25;

//    Dungeon System Constants
    public static final int MAX_FLOOR = 100;
    public static final int FLOOR_MILESTONE = 10;
    public static final int EASY_FLOOR_MAX = 10;
    public static final int NORMAL_FLOOR_MAX = 30;
    public static final int HARD_FLOOR_MAX = 60;

//    Enemy Scaling
    public static final int BASE_ENEMY_HP = 50;
    public static final int ENEMY_HP_PER_LEVEL = 15;
    public static final int BASE_ENEMY_ATK = 10;
    public static final int ENEMY_ATK_PER_LEVEL = 5;
    public static final int BASE_ENEMY_DEF = 5;
    public static final int ENEMY_DEF_PER_LEVEL = 5;

//    Rewards Scaling
    public static final int BASE_GOLD_REWARD = 100;
    public static final int BASE_EXP_REWARD = 50;
    public static final int EXP_SCALING_PER_LEVEL = 1;
    public static final int GOLD_SCALING_PER_LEVEL = 2;

//    Skills Constants
    public static final int MAX_SKILL_SLOTS = 11;
    public static final int SKILL_UNLOCK_INTERVAL = 10; // New Skill unlocked every 10 floors
    public static final int MAX_ACTIVE_BUFFS = 5;

//    Buff Constants
    public static final int BUFF_ATK_SMALL = 5;
    public static final int BUFF_ATK_MEDIUM = 10;
    public static final int BUFF_ATK_HIGH = 25;

    public static final int BUFF_DEF_SMALL = 3;
    public static final int BUFF_DEF_MEDIUM = 8;
    public static final int BUFF_DEF_HIGH = 15;

    public static final int BUFF_MAGIC_SMALL = 5;
    public static final int BUFF_MAGIC_MEDIUM = 10;
    public static final int BUFF_MAGIC_HIGH = 25;

    public static final int BUFF_MANA_SMALL = 10;
    public static final int BUFF_MANA_MEDIUM = 30;
    public static final int BUFF_MANA_HIGH = 50;

    public static final int BUFF_PERMANENT_DURATION = -1; // -1 means buff applied permanently, this number represents turn

//    Cooldown Constants
    public static final int SKILL_COOLDOWN_DEFAULT = 1;
    public static final int SKILL_COOLDOWN_MEDIUM = 2;
    public static final int SKILL_COOLDOWN_HEAVY = 3;
    public static final int SKILL_COOLDOWN_ULTIMATE = 5;

//    Inventory & Equipments Constants
    public static final int MAX_INVENTORY_SLOT = 30;
    public static final double LOOT_DROP_RATE = 0.4;
    
//    Consumable Constants
    public static final int SMALL_POTION_VALUE = 10;
    public static final int MEDIUM_POTION_VALUE = 25;
    public static final int LARGE_POTION_VALUE = 50;

//    Gacha Constants
    public static final int GACHA_SINGLE_COST = 500; // Gold
    public static final int GACHA_TENFOLD_COST = 4800;

//    Rarity Rates
    public static final double GACHA_COMMON_RATE = 0.50;
    public static final double GACHA_RARE_RATE = 0.35;
    public static final double GACHA_EPIC_RATE = 0.14;
    public static final double GACHA_LEGENDARY_RATE = 0.01;

//    Message Templates
    public static final String MESSAGE_INVALID_INPUT = "Input tidak valid! mohon coba kembali.";
    public static final String MESSAGE_LOADING = "⏳ Loading...";
}
