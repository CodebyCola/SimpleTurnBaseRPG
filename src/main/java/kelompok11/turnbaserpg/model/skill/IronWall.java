/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package kelompok11.turnbaserpg.model.skill;

import kelompok11.turnbaserpg.enums.SkillType;
import kelompok11.turnbaserpg.model.Character.Character;
import kelompok11.turnbaserpg.utils.GameConstants;

/**
 *
 * @author Pongo
 */
public class IronWall extends Skill {

    public IronWall() {
        super(
                "Iron Wall",
                "Increase defense temporarily",
                20,
                10,
                3,
                SkillType.DEFEND
        );
    }
    
    public void use() {
        
    }

}
