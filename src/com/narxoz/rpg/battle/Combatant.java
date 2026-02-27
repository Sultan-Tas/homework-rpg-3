package com.narxoz.rpg.battle;

public interface Combatant {
    String getName();
    int getAttackPower();
    void takeDamage(int amount);
    boolean isAlive();
    int getHealth();
    double getCritChance();
    int getAggro();
}
