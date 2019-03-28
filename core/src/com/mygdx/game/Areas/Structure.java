package com.mygdx.game.Areas;

import com.badlogic.gdx.graphics.Color;

import java.util.ArrayList;

public class Structure extends Area
{
    private String type;
    private double visibility;
    private ArrayList<Entrance> doors = new ArrayList<Entrance>();
    private ArrayList<Entrance> windows = new ArrayList<Entrance>();

    protected double[] topLeftCorner = topLeft;
    protected double[] bottomLeftCorner = {topLeft[0], bottomRight[1]};
    protected double[] topRightCorner = {bottomRight[0], topLeft[1]};
    protected double[] bottomRightCorner = bottomRight;


    public Structure(double[] topLeft, double[] bottomRight) {
        super(topLeft, bottomRight, Color.DARK_GRAY, Color.GRAY);
        visibility = 10.0; //if it's not a tower it's a regular structure
    }

    public Entrance createEntrance(String type, double[] begPosition, double[] endPosition)
    {
        double x1 = begPosition[0];
        double y1 = begPosition[1];
        double x2 = endPosition[0];
        double y2 = endPosition[1];
        Entrance entrance = null;

        //Maybe not necessary... depends on the front-end implementation

        //the entrance is going to be vertical
        if((x1 == x2) && (x1 == topLeft[0] || x1 == bottomRight[0]))
        {
            if(	(y1 > topLeft[1]) && (y2 > topLeft[1]) &&
                    (y1 < bottomRight[1]) && (y2 < bottomRight[1]))
            {
                entrance = new Entrance(type, begPosition, endPosition);
            }
        }
        else {
            //the entrance is going to be horizontal
            if (y1 == y2) {
                if ((x1 > topLeft[0]) && (x2 > topLeft[0]) &&
                        (y1 < bottomRight[0]) && (y2 < bottomRight[0])) {
                    entrance = new Entrance(type, begPosition, endPosition);
                }
            }
        }

        if(type=="door") doors.add(entrance);
        else windows.add(entrance);

        return entrance;
    }

    public Structure(double[] topLeft, double[] bottomRight, Color line, Color in) {
        super(topLeft, bottomRight, line, in);
        visibility = 10.0; //if it's not a tower it's a regular structure
    }

    public double[][][] getWalls()
    {
        //Walls built with (x,y) of th efirst point followed with(x,y) of the second
        double[][] leftWall = {topLeftCorner, bottomLeftCorner};
        double[][] rightWall = {topRightCorner, bottomRightCorner};
        double[][] topWall = {topLeftCorner, topRightCorner};
        double[][] bottomWall = {bottomLeftCorner, bottomRightCorner};
        double[][][] walls = {leftWall, rightWall, topWall, bottomWall};
        return walls;
    }

    public String getType()
    {
        return type;
    }

    public double getVisibility(){
        return visibility;
    }

    public ArrayList<Entrance> getDoors()
    {
        return doors;
    }

    public ArrayList<Entrance> getWindows()
    {
        return windows;
    }

}
