package com.mygdx.game.worldAttributes.agents.guard;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.gamelogic.World;
import com.mygdx.game.worldAttributes.agents.Agent;
import com.mygdx.game.worldAttributes.agents.guard.ai.GuardAI;
import com.mygdx.game.worldAttributes.agents.guard.ai.GuardAIFactory;
import com.mygdx.game.worldAttributes.agents.guard.explorationAi.ExplorationAI;
import com.mygdx.game.worldAttributes.agents.guard.explorationAi.ExplorationAIFactory;
import com.mygdx.game.worldAttributes.areas.Area;
import com.mygdx.game.worldAttributes.areas.SentryTower;
import com.mygdx.game.worldAttributes.areas.Shade;
import com.mygdx.game.worldAttributes.areas.Structure;

import java.util.ArrayList;

public class Guard extends Agent
{
    public static final float VISIBILITY = 7.5f;

    public Guard(World world)
    {
        super(world);
        visibility = VISIBILITY;
    }

    public void setExplorationAI(ExplorationAI.ExplorationAIType explorationAI)
    {
        ai = ExplorationAIFactory.newExplorationAI(explorationAI, this);
    }

    public void setSimulationAI(GuardAI.GuardAIType guardAIType)
    {
        ArrayList<Area> internalAreas = new ArrayList<>();

        if(ai instanceof ExplorationAI)
            internalAreas = ((ExplorationAI) ai).getInternalAreas();

        ai = GuardAIFactory.newGuardAI(guardAIType, this, internalAreas);
    }

    public boolean areaVisible(Area area)
    {
//        System.out.println(edgeVisible(area.getTopLeft(), area.getTopRight(), area.getVisibility()));
//        System.out.println(edgeVisible(area.getTopRight(), area.getBottomRight(), area.getVisibility()));
//        System.out.println(edgeVisible(area.getBottomRight(), area.getBottomLeft(), area.getVisibility()));
//        System.out.println(edgeVisible(area.getBottomLeft(), area.getTopLeft(), area.getVisibility()));

        return edgeVisible(area.getTopLeft(), area.getTopRight(), area.getVisibility()) ||
                edgeVisible(area.getTopRight(), area.getBottomRight(), area.getVisibility()) ||
                edgeVisible(area.getBottomRight(), area.getBottomLeft(), area.getVisibility()) ||
                edgeVisible(area.getBottomLeft(), area.getTopLeft(), area.getVisibility());
    }

    public boolean edgeVisible(Vector2 edgeStart, Vector2 edgeEnd, float visibility)
    {
        Vector2 beginVector = new Vector2(visibility * visualMultiplier, 0.0f);
        beginVector.rotate(modulo(angleFacing - VISUAL_ANGLE * 0.5f, 360.0f));
        beginVector.add(position);

        Vector2 endVector = new Vector2(visibility * visualMultiplier,0.0f);
        endVector.rotate(modulo(angleFacing + VISUAL_ANGLE * 0.5f, 360.0f));
        endVector.add(position);

        Vector2 closestPoint = getClosestPointOnLineSegment(edgeStart, edgeEnd);

        return (closestPoint.dst2(position) <= visibility * visibility * visualMultiplier * visualMultiplier &&
                pointInVisualAngle(closestPoint)) ||
                Area.intersects(position, beginVector, edgeStart, edgeEnd) ||
                Area.intersects(position, endVector, edgeStart, edgeEnd);
    }

    public Vector2 getClosestPointOnLineSegment(Vector2 lineStart, Vector2 lineEnd)
    {
//        Vector2 deltaPositionEdgeStart = new Vector2(position);
//        deltaPositionEdgeStart.sub(edgeStart);
//
//        Vector2 deltaEdge = new Vector2(edgeEnd);
//        deltaEdge.sub(edgeStart);
//
//        Vector2 orthogonalEdge = deltaEdge.rotate90(0);
//
//        float dot = deltaPositionEdgeStart.dot(orthogonalEdge);
//        float len_sq = orthogonalEdge.len2();
//
//        float distance2 = dot * dot / len_sq;
//
//        return distance2 <= visibility * visibility * visualMultiplier * visualMultiplier;

        return lineStart;
    }

    public ArrayList<Area> getVisibleAreas()
    {
        ArrayList<Area> visibleAreas = new ArrayList<>();

        visibleAreas.addAll(getVisibleSentryTowers());
        visibleAreas.addAll(getVisibleStructures());
        visibleAreas.addAll(getVisibleShades());

        return world.getMap().getAreaList();
    }

    public ArrayList<SentryTower> getVisibleSentryTowers()
    {
        ArrayList<SentryTower> visibleSentryTowers = new ArrayList<>();

        if(active)
            for(SentryTower sentryTower : world.getMap().getSentryTowers())
                if(areaVisible(sentryTower))
                    visibleSentryTowers.add(sentryTower);

        return visibleSentryTowers;
    }

    public ArrayList<Structure> getVisibleStructures()
    {
        ArrayList<Structure> visibleStructures = new ArrayList<>();

        if(active)
            for(Structure structure : world.getMap().getStructures())
                if(areaVisible(structure))
                    visibleStructures.add(structure);

        return visibleStructures;
    }

    public ArrayList<Shade> getVisibleShades()
    {
        ArrayList<Shade> visibleShades = new ArrayList<>();

        if(active)
            for(Shade shade : world.getMap().getShades())
                if(areaVisible(shade))
                    visibleShades.add(shade);

        return visibleShades;
    }

    public float modulo(float dividend, float divisor)
    {
        return ((dividend % divisor) + divisor) % divisor;
    }
}
