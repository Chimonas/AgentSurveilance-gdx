package com.mygdx.game.gamelogic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.worldAttributes.areas.Area;
import com.mygdx.game.worldAttributes.areas.AreaFactory;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class FileHandler {

    public static void saveMap(Map map, String mapName) {
        String path = "core/maps/" + mapName + ".txt";
        ArrayList<Area> areas = map.getAreaList();

        String mapText = map.getWidth() + " " + map.getHeight();
        for (Area a : areas) {

            mapText += "\n" + a.getClass().getSimpleName();
            mapText += " " + a.getTopLeft().x + " " + a.getTopLeft().y;
            mapText += " " + a.getBottomRight().x + " " + a.getBottomRight().y;
        }
        FileHandle file = Gdx.files.local(path);
        file.writeString(mapText,true);

    }

    public static Map importMap(String mapName) {
        String path = "core/maps/" + mapName + ".txt";
        if (Gdx.files.local(path).exists() && !Gdx.files.local(path).readString().isEmpty()) {
            Map map = new Map();

            FileHandle file = Gdx.files.local(path);
            String text = file.readString();

            Scanner in = new Scanner(text);

            //Default size
            float mapWidth = 50;
            float mapHeight = 50;

            try{
                mapWidth = Float.valueOf(in.next());
                mapHeight = Float.valueOf(in.next());
            }catch(NumberFormatException e){
                System.out.println("Size of map is not specified. Default size of 50x50 is set.");
            }

            map.setSize(mapWidth,mapHeight);

            try {
                in.nextLine();
            }catch(NoSuchElementException e){
                return map;
            }
            while (in.hasNextLine()) {
                String areaData = in.nextLine();
                 String[] parts = areaData.split(" ");

                Area.AreaType areaType = null;

                switch(parts[0]){
                    case "Structure": areaType = Area.AreaType.STRUCTURE; break;
                    case "SentryTower": areaType = Area.AreaType.SENTRY_TOWER; break;
                    case "Shade": areaType = Area.AreaType.SHADE; break;
                    case "Target": areaType = Area.AreaType.TARGET; break;
                }

                AreaFactory.setAreaType(areaType);

                Vector2 newAreaStart = new Vector2();
                newAreaStart.x = Float.parseFloat(parts[1]);
                newAreaStart.y = Float.parseFloat(parts[2]);
                Vector2 newAreaEnd = new Vector2();
                newAreaEnd.x = Float.parseFloat(parts[3]);
                newAreaEnd.y = Float.parseFloat(parts[4]);
                Area newArea = AreaFactory.newArea(newAreaStart, newAreaEnd);

                map.addArea(newArea);

            }

            return map;
        }

        return null;

    }

    public static Object[] getListOfMaps() {
        FileHandle dirHandle = Gdx.files.internal("core/maps");

        ArrayList<String> list = new ArrayList<>();
        for (FileHandle entry: dirHandle.list()) {
            list.add(entry.name().replaceFirst("[.][^.]+$", ""));
        }

        return list.toArray();
    }

    public static int index = 0;
    public static void saveIntrudersResults(String results) {
        String path = "core/results/intruders_res_test_" + index +  ".txt";
        FileHandle file = Gdx.files.local(path);
        file.writeString(results,false);
    }

    public static void saveGuardsResults(String results) {
        String path = "core/results/guards_res_test_" + index +  ".txt";
        FileHandle file = Gdx.files.local(path);
        file.writeString(results,false);
        index ++;
    }
}
