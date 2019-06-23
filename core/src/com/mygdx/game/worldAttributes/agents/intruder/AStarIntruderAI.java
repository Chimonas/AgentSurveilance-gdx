package com.mygdx.game.worldAttributes.agents.intruder;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.worldAttributes.agents.AStarAI;
import com.mygdx.game.worldAttributes.areas.Area;
import com.mygdx.game.worldAttributes.areas.Target;

public class AStarIntruderAI extends IntruderAI {

    AStarAI ai;

    public AStarIntruderAI(Intruder intruder) {
        super(intruder);
        newVelocity = agent.MAX_VELOCITY;
    }

    public void setTargetAreaAsGoal(){
        for(Area a : ai.knownAreas)
            if(a instanceof Target)
                ai.setGoal(new Vector2(a.getTopLeft().x + a.getWidth()/2, a.getBottomRight().y + a.getHeight()/2));

        //If there's no target area, the goal is to get to the center of the map
        if(ai.getGoal() == null)
            ai.setGoal(new Vector2(agent.getWorld().getMap().getWidth()/2, agent.getWorld().getMap().getHeight()/2));
    }

    boolean notDone = true;
    public void update(){
//        System.out.println(agent.getPosition() + " " + agent.getAngleFacing());
//        System.out.println("New angle= " + getNewAngle());
//        System.out.println("New velocity= " + getNewVelocity());
//        System.out.println("Velocity: " + agent.getVelocity());
        super.update();
        if(notDone){
            ai = new AStarAI(agent, agent.getWorld().getMap().getAreaList());
            setTargetAreaAsGoal();
            ai.createGraph(null);
            ai.aStar();
            notDone = false;
        }
        if(ai.checkDeviations()) ai.aStar();
        ai.applyPath();
    }

}
