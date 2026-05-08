/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package kelompok11.turnbaserpg.enums;

/**
 *
 * @author Pongo
 */
public enum SkillType {
    ATTACK("Attack"),
    DEFEND("Defend"),
    HEAL("Heal"),
    DEBUFF("Debuff"),
    BUFF("Buff");

    private String displayName;

    SkillType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
