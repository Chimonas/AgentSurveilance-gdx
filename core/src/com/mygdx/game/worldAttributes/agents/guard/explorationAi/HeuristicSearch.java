package com.mygdx.game.worldAttributes.agents.guard.explorationAi;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.gamelogic.Map;
import com.mygdx.game.worldAttributes.agents.guard.Guard;

public class HeuristicSearch extends ExplorationAI
{
    float maxVelocity=1.4f, visibleRange=12; //use agent.getmaxvelocity and area.getvisibility please, NO MAGIC NUMBERS!
    Vector2 startPosition;

    public HeuristicSearch(Guard guard) {
        super(guard);
        startPosition = agent.getPosition();
    }

    @Override
    public void update(){
        super.update();

        //if guard sees an area, then save it in exploredAreas
        //if(guard.getVisibleAreas().size() != 0){
        //    exploredAreas.addAll(guard.getVisibleAreas());
    }

    public void heuristicMethod(Map map){
        //look left and right at every position

        //special cases for start:
        if(agent.getPosition().y == startPosition.y && agent.getPosition().x == startPosition.x)
            setNewAngle(startPosition.x+visibleRange,startPosition.y);


        //removed getdivide map, spawning now done from within ai itself, use static variable for increment

        //all other normal cases:
//        if((agent.getPosition().x+visibleRange)%4 == 0 && agent.getPosition().y != (agent.getWorld().getDivideMap()-visibleRange))
//            setNewAngle(agent.getPosition().x,agent.getWorld().getDivideMap()-visibleRange);
//
//        if(agent.getPosition().y == (agent.getWorld().getDivideMap()-visibleRange) && (agent.getPosition().x+3*visibleRange)%4 != 0)
//            setNewAngle(agent.getPosition().x+2*visibleRange,agent.getPosition().y);

        if((agent.getPosition().x+3*visibleRange)%4 == 0 && agent.getPosition().y != (startPosition.y+visibleRange))
            setNewAngle(agent.getPosition().x,startPosition.y+visibleRange);

        if(agent.getPosition().y == (startPosition.y+visibleRange) && (agent.getPosition().x+visibleRange)%4 != 0)
            setNewAngle(agent.getPosition().x+2*visibleRange, agent.getPosition().y);
    }

    public float setNewAngle( float newXpos, float newYpos) {
        if(agent.getPosition().x == newXpos){
            if(agent.getPosition().y < newYpos)
                return 90.0f;
            if(agent.getPosition().y > newYpos)
                return 270.0f;
        }
        if(agent.getPosition().y == newYpos){
            if(agent.getPosition().x < newXpos)
                return 0.0f;
            if(agent.getPosition().x > newXpos)
                return 180.0f;
        }
        return 0.0f;
    }

    public float setNewVelocity() {
        return maxVelocity;
    }

//    @Override
//    public void aiSpawn(Map map)
//    {
//        Vector2 position = new Vector2();
//        position.x = 0.0f;
//        position.y = divideMap*guardNumber;
//
//        agent.spawn(position, 0.0f);
//    }
}
