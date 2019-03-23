package com.mygdx.game.GameLogic;

import com.mygdx.game.AI.Guard;
import com.mygdx.game.AI.Intruder;

public class Settings
{
    private static Guard guardAI;
    private static Intruder intruderAI;
    private static int guardAmount, intruderAmount, explorationTime, maxTime, timeWeight, movementWeight, directComWeight, indirectComWeight;
    private static boolean explorationPhase;

    public static Guard getGuardAI() {
        return guardAI;
    }

    public static void setGuardAI(Guard guardAI) {
        Settings.guardAI = guardAI;
    }

    public static Intruder getIntruderAI() {
        return intruderAI;
    }

    public static void setIntruderAI(Intruder intruderAI) {
        Settings.intruderAI = intruderAI;
    }

    public static int getGuardAmount() {
        return guardAmount;
    }

    public static void setGuardAmount(int guardAmount) {
        Settings.guardAmount = guardAmount;
    }

    public static int getIntruderAmount() {
        return intruderAmount;
    }

    public static void setIntruderAmount(int intruderAmount) {
        Settings.intruderAmount = intruderAmount;
    }

    public static int getExplorationTime() {
        return explorationTime;
    }

    public static void setExplorationTime(int explorationTime) {
        Settings.explorationTime = explorationTime;
    }

    public static int getMaxTime() {
        return maxTime;
    }

    public static void setMaxTime(int maxTime) {
        Settings.maxTime = maxTime;
    }

    public static int getTimeWeight() {
        return timeWeight;
    }

    public static void setTimeWeight(int timeWeight) {
        Settings.timeWeight = timeWeight;
    }

    public static int getMovementWeight() {
        return movementWeight;
    }

    public static void setMovementWeight(int movementWeight) {
        Settings.movementWeight = movementWeight;
    }

    public static int getDirectComWeight() {
        return directComWeight;
    }

    public static void setDirectComWeight(int directComWeight) {
        Settings.directComWeight = directComWeight;
    }

    public static int getIndirectComWeight() {
        return indirectComWeight;
    }

    public static void setIndirectComWeight(int indirectComWeight) {
        Settings.indirectComWeight = indirectComWeight;
    }

    public static boolean isExplorationPhase() {
        return explorationPhase;
    }

    public static void setExplorationPhase(boolean explorationPhase) {
        Settings.explorationPhase = explorationPhase;
    }
}
