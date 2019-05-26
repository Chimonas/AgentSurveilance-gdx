package com.mygdx.game.gamelogic;

import com.mygdx.game.worldAttributes.agents.guard.ai.GuardAI;
import com.mygdx.game.worldAttributes.agents.guard.explorationAi.ExplorationAI;
import com.mygdx.game.worldAttributes.agents.intruder.IntruderAI;

public class Settings
{
    private GuardAI.GuardAIType guardAIType;
    private ExplorationAI.ExplorationAIType explorationAIType;
    private IntruderAI.IntruderAIType intruderAIType;
    private int guardAmount, intruderAmount;
    private float explorationTime, maxTime, timeWeight, movementWeight, directComWeight, indirectComWeight;
    private boolean explorationPhase;

    public GuardAI.GuardAIType getGuardAIType() {
        return guardAIType;
    }

    public void setGuardAIType(GuardAI.GuardAIType guardAIType) {
        this.guardAIType = guardAIType;
    }

    public ExplorationAI.ExplorationAIType getExplorationAIType()
    {
        return explorationAIType;
    }

    public void setExplorationAIType(ExplorationAI.ExplorationAIType explorationAIType)
    {
        this.explorationAIType = explorationAIType;
    }

    public IntruderAI.IntruderAIType getIntruderAIType() {
        return intruderAIType;
    }

    public void setIntruderAIType(IntruderAI.IntruderAIType intruderAIType) {
        this.intruderAIType = intruderAIType;
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
