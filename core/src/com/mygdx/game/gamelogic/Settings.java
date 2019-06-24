package com.mygdx.game.gamelogic;

import com.mygdx.game.worldAttributes.agents.guard.ai.GuardAI;
import com.mygdx.game.worldAttributes.agents.guard.explorationAi.ExplorationAI;
import com.mygdx.game.worldAttributes.agents.intruder.IntruderAI;

public class Settings
{
    private GuardAI.GuardAIType guardAIType;
    private ExplorationAI.ExplorationAIType explorationAIType;
    private IntruderAI.IntruderAIType intruderAIType;
    private int guardAmount, intruderAmount, simulationAmount;
    private float explorationTime, maxTime;
    private boolean explorationPhase, multipleSimulations;

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

    public int getSimulationAmount() {
        return simulationAmount;
    }

    public void setSimulationAmount(int simulationAmount) {
        this.simulationAmount = simulationAmount;
    }

    public boolean isExplorationPhase() {
        return explorationPhase;
    }

    public void setExplorationPhase(boolean explorationPhase) {
        this.explorationPhase = explorationPhase;
    }

    public boolean isMultipleSimulations() {
        return multipleSimulations;
    }

    public void setMultipleSimulations(boolean multipleSimulations) {
        this.multipleSimulations = multipleSimulations;
    }
}
