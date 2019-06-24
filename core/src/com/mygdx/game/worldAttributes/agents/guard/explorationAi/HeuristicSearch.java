package com.mygdx.game.worldAttributes.agents.guard.explorationAi;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.gamelogic.Map;
import com.mygdx.game.worldAttributes.agents.guard.Guard;
import com.mygdx.game.worldAttributes.areas.Area;

import java.util.ArrayList;

public class HeuristicSearch extends ExplorationAI {
    float maxVelocity=agent.MAX_VELOCITY, visibleRange=agent.getVisibility();
    ArrayList<Area> exploredAreas; //arraylist of areas found
    Vector2 startPosition;
    int guardNumber = agent.getNumberOfGuards(), increment=1;

    public HeuristicSearch(Guard guard) {
        super(guard);
    }

    @Override
    public void update(){
        super.update();
        newVelocity = maxVelocity;
        heuristicMethod();

        //if guard sees an area, then save it in exploredAreas
        //if(guard.getVisibleAreas().size() != 0){
        //    exploredAreas.addAll(guard.getVisibleAreas());
    }

    public void heuristicMethod(){
        //look left and right at every position

        //special cases for start:
        System.out.println(startPosition);
        //System.out.prinln()
        if(agent.getPosition().y == startPosition.y && agent.getPosition().x == startPosition.x)
            setNewAngle(startPosition.x+visibleRange,startPosition.y);

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
                newAngle= 90.0f;
            if(agent.getPosition().y > newYpos)
                newAngle= 270.0f;
        }
        if(agent.getPosition().y == newYpos){
            if(agent.getPosition().x < newXpos)
                newAngle= 0.0f;
            if(agent.getPosition().x > newXpos)
                newAngle= 180.0f;
        }
        return newAngle;
    }

    public float setNewVelocity() {

        System.out.println(maxVelocity);
        return maxVelocity;
    }

    @Override
    public void spawn(Map map){
        Vector2 position = new Vector2();
        float divideMap = map.getHeight()/(guardNumber+1);
        position.x = 0.1f;
        position.y = divideMap*increment;
        increment++;
        agent.spawnPosition(position, 0.0f);
        startPosition = agent.getPosition();

    }
}


