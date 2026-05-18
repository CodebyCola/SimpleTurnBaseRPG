/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package kelompok11.turnbaserpg.game;

import kelompok11.turnbaserpg.game.services.DungeonService;
import java.sql.Connection;
import java.util.Scanner;
import kelompok11.turnbaserpg.database.Connector;
import kelompok11.turnbaserpg.enums.Role;
import kelompok11.turnbaserpg.model.character.Player;
import kelompok11.turnbaserpg.model.skill.BasicHeal;
import kelompok11.turnbaserpg.utils.GameLogger;

/**
 * Central game controller. Manages all user-facing routing: login, new account
 * creation, and main menu. Business logic is delegated to service classes
 * (DungeonService, BattleService) and persistence to SaveManager / LoadManager.
 */
public class GameManager {

    SaveManager save = new SaveManager();
    LoadManager load = new LoadManager();
    
}
