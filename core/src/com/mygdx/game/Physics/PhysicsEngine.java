package com.mygdx.game.Physics;

import com.mygdx.game.AI.Agent;
import com.mygdx.game.AI.Body;
import com.mygdx.game.Areas.Area;
import com.mygdx.game.Areas.Structure;

import java.awt.geom.Line2D;
import java.util.ArrayList;

public class PhysicsEngine {

    ArrayList<Agent> agents;
    ArrayList<Structure> structures;

    public PhysicsEngine(ArrayList<Agent> a, ArrayList<Structure> s) {
        agents = a;
        structures = s;
    }

    //Check for collision of an agent with a structure
    public static void updatePosition(ArrayList<Area> areas, Agent a) {
        Body b = a.getBody();

        ArrayList<Structure> structuresInVision = new ArrayList<Structure>();
        ArrayList<Agent> agentsInVision = new ArrayList<Agent>();

        //Look for every structure to see if there's any collision with its walls during the next step
        double[] oldPos = b.getPosition();
        double[] newPos = newPos(b.getVelocity(), b.getPosition(), b.getAngleFacing());

        boolean bumpIntoWall = inStructure(areas, oldPos, newPos);

        //If the agent doesnt bump into a wall, update his position
        if (!bumpIntoWall) a.getBody().setPosition(newPos);


        //Check if other agents are in visual range
       // for (Agent other : agents) {
       //     if (a != other) {
       //         a.getBody().getVisualArea().getVisualArea();
       //     }
       // }

    }

    public static boolean inStructure(ArrayList<Area> areas, double[] oldPos, double[] newPos){

        boolean inStructure = false;
        for (Area area : areas) {
            for (double[][] wall : area.getBorders()) {

                if (Line2D.linesIntersect(
                        oldPos[0], oldPos[1], newPos[0], newPos[1],
                        wall[0][0], wall[0][1], wall[1][0], wall[1][1])) {

                    //The agent will intersect this structure during its next move
                    System.out.println("You gonna enter in a wall dummie!");
                    inStructure = true;
                }
            }
        }
        return inStructure;
    }


    //New position of the agent calculate with its position, velocity and angle
    public static double[] newPos(double v, double[] p, double a){

        double updateX = v*Math.cos(Math.toRadians(a));
        double updateY = v*Math.sin(Math.toRadians(a));
        double[] newPos = {p[0] + updateX, p[1] + updateY};

        return newPos;
    }

    public boolean isColision(double[] newPos, double[] endPos, Structure s){
        return false;
    }

    //TAKE CARE OF COLLISIONS
}
