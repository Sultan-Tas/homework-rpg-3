package com.narxoz.rpg;

import com.narxoz.rpg.adapter.EnemyCombatantAdapter;
import com.narxoz.rpg.adapter.HeroCombatantAdapter;
import com.narxoz.rpg.battle.BattleEngine;
import com.narxoz.rpg.battle.Combatant;
import com.narxoz.rpg.battle.EncounterResult;
import com.narxoz.rpg.enemy.Goblin;
import com.narxoz.rpg.hero.Mage;
import com.narxoz.rpg.hero.Warrior;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== RPG Battle Engine Demo ===\n");

        //Create heroes and enemies
        Warrior warrior = new Warrior("Aragorn");
        Mage mage = new Mage("Gandalf");
        Goblin goblin = new Goblin();

        //Wrap with adapters
        ArrayList<Combatant> heroes = new ArrayList<>();
        heroes.add(new HeroCombatantAdapter(warrior));
        heroes.add(new HeroCombatantAdapter(mage));

        ArrayList<Combatant> enemies = new ArrayList<>();
        enemies.add(new EnemyCombatantAdapter(goblin));

        //Demonstrate Singleton behavior
        BattleEngine engineA = BattleEngine.getInstance();
        BattleEngine engineB = BattleEngine.getInstance();
        System.out.println("Same instance? " + (engineA == engineB));
        System.out.println();

        //Run battle and print summary
        engineA.setRandomSeed(42L);
        EncounterResult result = engineA.runEncounter(heroes, enemies);

        for (String line : result.getBattleLog()) {
            System.out.println(line);
        }
        System.out.println("Winner: " + result.getWinner());
        System.out.println("Rounds: " + result.getRounds());


        System.out.println("\n=== Demo Complete ===");
    }
}
