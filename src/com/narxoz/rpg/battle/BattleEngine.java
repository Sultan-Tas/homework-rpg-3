package com.narxoz.rpg.battle;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public final class BattleEngine {
    private static BattleEngine instance;
    private Random random = new Random();

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

    public EncounterResult runEncounter(List<Combatant> teamA, List<Combatant> teamB) {
        //validate inputs and run round-based battle
        //use random if you add critical hits or target selection
        int rounds = 0;
        double critRoll;

        //checks for empty strings
        if(teamA.isEmpty() || teamB.isEmpty()) {
            EncounterResult fail = new EncounterResult();
            fail.setWinner("One of the teams is empty");
            fail.setRounds(0);
            fail.addLog("Error in team input");
            return fail;
        }

        EncounterResult result = new EncounterResult();

        //list characters
        result.addLog("►Battle started");
        result.addLog("Team A:");
        for(Combatant a : teamA) {
            result.addLog("\t" + a.getName() + " → hp: " + a.getHealth() + "; atk: " + a.getAttackPower() + "; crit%: " + a.getCritChance()*100 + "%");
        }
        result.addLog("Team B:");
        for(Combatant b : teamB) {
            result.addLog("\t" + b.getName() + " → hp: " + b.getHealth() + "; atk: " + b.getAttackPower());
        }
        //Round process
        while (!teamA.isEmpty() && !teamB.isEmpty()) {
            rounds++;
            result.addLog("—".repeat(20) + "\nRound: " + rounds);
            //heroes attack
            for(Combatant hero : teamA) {
                Combatant heroTarget = teamB.get(selectRandom(teamB));
                critRoll = random.nextDouble(0, 1);
                int damage = hero.getAttackPower();
                if(critRoll <= hero.getCritChance()){
                    damage *= 2;
                }
                heroTarget.takeDamage(damage);
                result.addLog(String.format("\t[\"%s\" took %d damage from \"%s\"]", heroTarget.getName(), damage, hero.getName()));
                //if targeted enemy dies
                if(!heroTarget.isAlive()){
                    result.addLog(String.format("[%s fainted...]", heroTarget.getName()));
                    teamB.remove(heroTarget);
                }

            }
            //enemy attacks
            for(Combatant enemy : teamB) {
                if(!enemy.isAlive()){
                }
                else{
                    Combatant enemyTarget = teamA.get(selectRandom(teamA));
                    enemyTarget.takeDamage(enemy.getAttackPower());
                    result.addLog(String.format("\t[\"%s\" took %d damage from \"%s\"]", enemyTarget.getName(), enemy.getAttackPower(), enemy.getName()));
                    //if targeted hero dies
                    if(!enemyTarget.isAlive()){
                        result.addLog(String.format("[%s fainted...]", enemyTarget.getName()));
                        teamA.remove(enemyTarget);
                    }
                }
            }

            //result after round
            result.addLog("Team A:");
            for(Combatant a : teamA) {
                result.addLog("\t" + a.getName() + " → hp: " + a.getHealth() + "; atk: " + a.getAttackPower());
            }
            result.addLog("Team B:");
            for(Combatant b : teamB) {
                result.addLog("\t" + b.getName() + " → hp: " + b.getHealth() + "; atk: " + b.getAttackPower());
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

    private int selectRandom(List<Combatant> opposingTeam) {
        int fullAggro = 1;
        ArrayList<Integer> aggroList = new ArrayList<>();
        int id = 0;
        for(Combatant character : opposingTeam){
            fullAggro += character.getAggro();
            aggroList.add(character.getAggro());
        }
        int enemySelects = random.nextInt(1, fullAggro);
        for(int i = 0; i < aggroList.size(); i++){
            if(enemySelects >= aggroList.get(i)){
                enemySelects -= aggroList.get(i);
            }
            else{
                id = i;
                break;
            }
        }
        return id;
    }
}
