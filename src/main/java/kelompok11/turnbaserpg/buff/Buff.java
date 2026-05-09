/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package kelompok11.turnbaserpg.buff;
import kelompok11.turnbaserpg.model.Character;

/**
 *
 * @author Pongo
 */
public abstract class Buff {

    private String name;
    private int duration;

    public Buff(String name, int duration) {
        this.name = name;
        this.duration = duration;
    }

    public abstract void use(Character target);

    public abstract void remove(Character target);

    public int getDuration() {
        return duration;
    }

    public void decreaseDuration() {
        duration--;
    }

    public String getName() {
        return name;
    }
}
