package com.mygdx.game.worldAttributes.areas;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.Arrays;

public class Structure extends Area
{
    protected static final Color COLOR = Color.SLATE;
    protected static final float VISIBILITY = 12.0f;

    protected static final float SIZE_WINDOW = 1.0f;
    protected static final float SIZE_DOOR = 1.0f;
    protected static final int MAX_WINDOWS = 4;
    protected static final int MAX_DOORS = 6;
    protected static final int MIN_WINDOWS = 0;
    protected static final int MIN_DOORS = 1;

    private ArrayList<Entrance> doors = new ArrayList<>();
    private ArrayList<Entrance> windows = new ArrayList<>();
    private ArrayList<Float> spawnedPositions = new ArrayList<Float>();

    public Structure(Vector2 topLeft, Vector2 bottomRight)
    {
        super(topLeft, bottomRight, COLOR, VISIBILITY);
    }

    public void createRandomEntrances()
    {
        int numberDoors;
        int numberWindows;

        System.out.println(" ");
        //Random generation of doors and windows depending on the size of the structure
        float sumSize = getWidth() + getHeight();
        if(sumSize < 5){
            numberDoors = MIN_DOORS;
            numberWindows = MIN_WINDOWS;
        } else if(sumSize < 20){
            numberDoors = MIN_DOORS;
            double temp = Math.random()*MAX_WINDOWS;
            numberWindows = (int)temp;
        } else {
            numberDoors = (int)Math.random()*MAX_DOORS + 1 ;
            numberWindows = (int) Math.random()*MAX_WINDOWS;
        }

        System.out.println(numberDoors + " " + numberWindows);

        //Randomly allocate the position of doors and windows
        for(int i = 0; i<numberDoors; i++){
            System.out.println(getHeight() + " " + getWidth());
            createNewEntranceType(Entrance.EntranceType.DOOR, SIZE_DOOR);
        }

        //Randomly allocate the number of doors and windows
        for(int i = 0; i<numberWindows; i++){
            createNewEntranceType(Entrance.EntranceType.WINDOW, SIZE_WINDOW);
        }

    }

    private void createNewEntranceType(Entrance.EntranceType type, float size) {
        Entrance entrance = new Entrance(type);

        float spawnPosition = (float)Math.random() * (2 * (getHeight()) + 2 * (getWidth()));

        //Possible mistake of infinite loop
        while(entranceOverlap(spawnPosition, size) || entranceOnBorder(spawnPosition, size)) {
            System.out.println("Not accepted");
            spawnPosition = (float)Math.random() * (2 * (getHeight()) + 2 * (getWidth()));
        }


        System.out.println(spawnPosition);
        spawnedPositions.add(spawnPosition);

        if(spawnPosition <= getWidth() - size){
            createNewEntrance(entrance,topLeft.x + spawnPosition, topLeft.y, true, true, size);
        } else if(spawnPosition <= (getWidth() + getHeight() - size) ){
            createNewEntrance(entrance, bottomRight.x, topLeft.y - (spawnPosition - getWidth()), false, true, size);
        }else if(spawnPosition <= 2*getWidth() + getHeight() - size){
            createNewEntrance(entrance,bottomRight.x - (spawnPosition - getWidth() - getHeight()), bottomLeft.y, true, false, size);
        }else {
            createNewEntrance(entrance, topLeft.x, bottomRight.y + spawnPosition - 2*getWidth() - getHeight(), false, false, size);
        }

    }

    private boolean entranceOnBorder(float spawnPosition, float size) {
        if((spawnPosition > getWidth() - size && spawnPosition <= getWidth()) ||
            (spawnPosition > (getWidth() + getHeight() - size) && spawnPosition <= (getWidth() + getHeight())) ||
            (spawnPosition > (2*getWidth() + getHeight() - size) && spawnPosition <= (2*getWidth() + getHeight())) ||
            (spawnPosition > (2*getWidth() + 2*getHeight() - size) && spawnPosition <= (2*getWidth() + 2*getHeight())))
                return true;

        return false;
    }

    private boolean entranceOverlap(float spawnPosition, float size) {
        for(float existingPosition:spawnedPositions) {
            if (spawnPosition > existingPosition && spawnPosition < existingPosition + size ||
                spawnPosition + size > existingPosition && spawnPosition + size < existingPosition + size)
                return true;
        }
        return false;
    }

    public void createNewEntrance(Entrance entrance, float x, float y, boolean axis, boolean direction, float size){
        if(axis) {
            if(direction) entrance.setPosition(new Vector2(x, y), new Vector2(x + size, y));
            else entrance.setPosition(new Vector2(x, y), new Vector2(x - size, y));
        } else {
            if(direction) entrance.setPosition(new Vector2(x, y), new Vector2(x, y - size));
            else entrance.setPosition(new Vector2(x, y), new Vector2(x, y + size));
        }
        if(entrance.getType() == Entrance.EntranceType.DOOR) doors.add(entrance);
        else windows.add(entrance);

        System.out.println("ENTRANCE: " + entrance.getStartPosition() + " " + entrance.getEndPosition() + " " + entrance.getType().toString());
        System.out.println("STRUCTURE: " + getTopLeft() + getBottomRight());
    }


    public ArrayList<Entrance> getDoors()
    {
        return doors;
    }

    public ArrayList<Entrance> getWindows()
    {
        return windows;
    }

    public ArrayList<Entrance> getEntrances(){
        ArrayList<Entrance> entrances = new ArrayList<Entrance>();
        entrances.addAll(getWindows());
        entrances.addAll(getDoors());
        return entrances;
    }
}
