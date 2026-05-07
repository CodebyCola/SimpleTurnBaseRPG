/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package kelompok11.turnbaserpg.model;

/**
 *
 * @author Pongo
 */
public class Buff {

    private int attackBonus;
    private String buffName;
    private int duration; // berapa turn

    public Buff(int attackBonus, int duration, String buffName) {
        this.attackBonus = attackBonus;
        this.duration = duration;
        this.buffName = buffName;
    }

    public int getAttackBonus() {
        return attackBonus;
    }

    public int getDuration() {
        return duration;
    }

    public void reduceDuration() {
        duration--;
    }

    public boolean isExpired() {
        return duration <= 0;
    }
}
