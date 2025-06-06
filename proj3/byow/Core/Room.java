package byow.Core;

import com.github.javaparser.utils.Pair;

import java.util.ArrayList;
import java.util.Random;
import java.util.TreeMap;

public class Room {
    public int roomWidth;
    public int roomHeight;
    public int topRightX; // max X
    public int bottomLeftX; // min X
    public int bottomLeftY; // min Y
    public int topRightY; // max Y

    /** Room constructor */
    public Room(Random r){
        // maybe make max values smaller
        // maybe don't use uniform (have other function at the bottom)
        roomWidth = RandomUtils.uniform(r,3, 10);
        roomHeight = RandomUtils.uniform(r, 3,10);
        bottomLeftX = RandomUtils.uniform(r, 0, MapGenerator.mapWidth);
        bottomLeftY = RandomUtils.uniform(r, 0, MapGenerator.mapHeight);
        topRightX = bottomLeftX + roomWidth;
        topRightY = bottomLeftY + roomHeight;
    }
    public int getWidth(){
        return this.roomWidth;

    }

    public int getHeight(){
        return this.roomHeight;
    }

    public int getTopRightX(){
        // returns the maximum X coordinate of the room
        return this.topRightX;
    }

    public int getBottomLeftX(){
        // returns the minimum X coordinate of the room
        return this.bottomLeftX;
    }

    public int getTopRightY(){
        // returns the maximum Y coordinate of the room
        return this.topRightY;
    }

    public int getBottomLeftY(){
        // returns the minimum Y coordinate of the room
        return this.bottomLeftY;
    }

    /** public int getRandom(Random r, int min, int max) {
        return (min + r.nextInt(max - min + 1));
    } */

}
