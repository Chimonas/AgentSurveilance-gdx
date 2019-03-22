package com.mygdx.game.GameLogic;

public class Settings
{
    private static AI.GuardAI guardAI;
    private static AI.IntruderAI intruderAI;
    private static int guardAmount, intruderAmount, explorationTime, maxTime, timeWeight, movementWeight, directComWeight, indirectComWeight;
    private static boolean explorationPhase;

    public static AI.GuardAI getGuardAI() {
        return guardAI;
    }

    public static void setGuardAI(AI.GuardAI guardAI) {
        Settings.guardAI = guardAI;
    }

    public static AI.IntruderAI getIntruderAI() {
        return intruderAI;
    }

    public static void setIntruderAI(AI.IntruderAI intruderAI) {
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
