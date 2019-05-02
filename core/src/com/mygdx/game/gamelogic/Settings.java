package com.mygdx.game.gamelogic;

import com.mygdx.game.worldAttributes.agents.ai.guard.GuardAI;
import com.mygdx.game.worldAttributes.agents.ai.intruder.IntruderAI;

public class Settings
{
    private GuardAI guardAI;
    private IntruderAI intruderAI;
    private int guardAmount, intruderAmount;
    private float explorationTime, maxTime, timeWeight, movementWeight, directComWeight, indirectComWeight;
    private boolean explorationPhase;

    public GuardAI getGuardAI() {
        return guardAI;
    }

    public void setGuardAI(GuardAI guardAI) {
        this.guardAI = guardAI;
    }

    public IntruderAI getIntruderAI() {
        return intruderAI;
    }

    public void setIntruderAI(IntruderAI intruderAI) {
        this.intruderAI = intruderAI;
    }

    public int getGuardAmount() {
        return guardAmount;
    }

    public void setGuardAmount(int guardAmount) {
        this.guardAmount = guardAmount;
    }

    public int getIntruderAmount() {
        return intruderAmount;
    }

    public void setIntruderAmount(int intruderAmount) {
        this.intruderAmount = intruderAmount;
    }

    public float getExplorationTime() {
        return explorationTime;
    }

    public void setExplorationTime(float explorationTime) {
        this.explorationTime = explorationTime;
    }

    public float getMaxTime() {
        return maxTime;
    }

    public void setMaxTime(float maxTime) {
        this.maxTime = maxTime;
    }

    public float getTimeWeight() {
        return timeWeight;
    }

    public void setTimeWeight(float timeWeight) {
        this.timeWeight = timeWeight;
    }

    public float getMovementWeight() {
        return movementWeight;
    }

    public void setMovementWeight(float movementWeight) {
        this.movementWeight = movementWeight;
    }

    public float getDirectComWeight() {
        return directComWeight;
    }

    public void setDirectComWeight(float directComWeight) {
        this.directComWeight = directComWeight;
    }

    public float getIndirectComWeight() {
        return indirectComWeight;
    }

    public void setIndirectComWeight(float indirectComWeight) {
        this.indirectComWeight = indirectComWeight;
    }

    public boolean isExplorationPhase() {
        return explorationPhase;
    }

    public void setExplorationPhase(boolean explorationPhase) {
        this.explorationPhase = explorationPhase;
    }
}
