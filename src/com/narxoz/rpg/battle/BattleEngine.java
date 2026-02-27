package com.narxoz.rpg.battle;

import java.util.List;
import java.util.Iterator;
import java.util.Random;

public final class BattleEngine {
    private static BattleEngine instance;
    private Random random = new Random(1L);

    private BattleEngine() {
    }

    public static BattleEngine getInstance() {
        if (instance == null) {
            instance = new BattleEngine();
        }
        return instance;
    }

    public BattleEngine setRandomSeed(long seed) {
        this.random = new Random(seed);
        return this;
    }

    public void reset() {
        // TODO: reset any battle state if you add it
    }

    public EncounterResult runEncounter(List<Combatant> teamA, List<Combatant> teamB) {
        //validate inputs and run round-based battle
        // TODO: use random if you add critical hits or target selection
        int rounds = 0;
        Combatant dead = null;
        //checks for empty strings
        if(teamA.isEmpty() || teamB.isEmpty()) {
            EncounterResult fail = new EncounterResult();
            fail.setWinner("One of the teams is empty");
            fail.setRounds(0);
            fail.addLog("Error in team input");
            return fail;
        }

        EncounterResult result = new EncounterResult();
        result.addLog("►Battle started");
        //Round process
        while (!teamA.isEmpty() && !teamB.isEmpty()) {
            rounds++;
            result.addLog("Round: " + rounds);
            for(Combatant hero : teamA) {
                if(!hero.isAlive()){
                    result.addLog(String.format("[%s fainted...]", hero.getName()));
                    teamA.set(teamA.indexOf(hero), dead);
                }
                else{
                    Combatant heroTarget = teamB.get(0);
                    heroTarget.takeDamage(hero.getAttackPower());
                    result.addLog(String.format("\t[\"%s\" took %d damage from \"%s\"]", heroTarget.getName(), hero.getAttackPower(), hero.getName()));
                }

            }
            while(teamA.contains(dead)){
                teamA.remove(dead);
            }
            for(Combatant enemy : teamB) {
                if(!enemy.isAlive()){
                    result.addLog(String.format("[%s fainted...]", enemy.getName()));
                    teamB.set(teamB.indexOf(enemy), dead);
                }
                else{
                    Combatant enemyTarget = teamA.get(0);
                    enemyTarget.takeDamage(enemy.getAttackPower());
                    result.addLog(String.format("\t[\"%s\" took %d damage from \"%s\"]", enemyTarget.getName(), enemy.getAttackPower(), enemy.getName()));
                }
            }
            while(teamB.contains(dead)){
                teamB.remove(dead);
            }
        }
        if(teamA.isEmpty()) {
            result.setWinner("Enemies won...");
        }
        else{
            result.setWinner("Heroes won!");
        }
        result.setRounds(rounds);
        result.addLog("►Battle ended");
        return result;
    }
}
