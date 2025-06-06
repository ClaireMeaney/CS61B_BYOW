package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import com.github.javaparser.utils.Pair;
import edu.princeton.cs.algs4.StdDraw;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.TreeMap;

public class MapGenerator {
    public static final int mapWidth = 80;
    public static final int mapHeight = 30;
    public TETile[][] world;
    public int totalNumberRooms = 0;
    public Random r;
    // map that keeps track of locations of each room (or nested list w/ variable that keeps track of total # rooms)
    private TreeMap<Integer, Room> allRooms;
    // key : ID, value : room object
    private ArrayList<Room> arrayRoomObj;
    // arraylist or room objects
    private static final int moveableHeight = 29;
    public int AvatarX;
    public int AvatarY;
    public boolean gameOver;

    /** GENERATE AVATAR IN THIS CLASS */

    // key : room #
    // value : arraylist of tuples [bottom left(x,y), bottom right(x,y), top left(x,y), top right(x,y)] */

    /** constructor */
    public MapGenerator(long seed) {
        world = new TETile[mapWidth][mapHeight];
        r = new Random(seed);
        allRooms = new TreeMap<>();
        arrayRoomObj = new ArrayList<>();
        // initialize world to nothing tiles
        for (int x = 0; x < mapWidth; x++) {
            for (int y = 0; y < mapHeight; y++) {
                world[x][y] = Tileset.NOTHING;
                /** // initialize bottom row to water
                if (y == 0) {
                    world[x][0] = Tileset.WATER;
                } */
            }
        }
        generateAllRooms();
        generateHallways();
        generateAvatar(); /** WHERE IT ERRORED */
    }

    /** makes the random rooms */
    public void generateAllRooms(){
        // call room generator random #(numRooms) of times
        int randomFinalNumRooms = RandomUtils.uniform(r, 2, 50);
        for (int i = 0; i < randomFinalNumRooms; i++){
            generateOneRoom();
        }
    }

    public void generateOneRoom(){
        Room oneRoom = new Room(r);
        totalNumberRooms += 1;
        // check if it overlaps with any other rooms and if it would go off the map
        if (overlaps(oneRoom) == false){
            return;
        }
        allRooms.put(totalNumberRooms, oneRoom);
        // add new room to array of all room objects
        arrayRoomObj.add(oneRoom);
        // draw the room into the world
        drawRoom(oneRoom.getBottomLeftX(),oneRoom.getBottomLeftY(),oneRoom.getWidth(),oneRoom.getHeight());
    }

    /** method that draws the room */
    public void drawRoom(int startX, int startY, int width, int height) {
        // leave a random square open for the entry to the room
        // store opening in the allRooms map
        for (int x = startX; x <= width + startX; x++) {
            for (int y = startY; y <= height + startY; y++) {
                if (x < mapWidth && y < moveableHeight) {
                    if (x == startX || x == startX + width || y == startY || y == startY + height) { // if its on edge make it a wall tile
                        world[x][y] = Tileset.WALL;
                    } else {// if its anything else make it a floor tile
                        world[x][y] = Tileset.FLOOR;
                    }
                }
            }
        }
    }

    /** checks if room overlaps with already made rooms or goes off the map */
    public boolean overlaps(Room newRoom) {
        if (newRoom.getTopRightX() > (mapWidth - 1) || newRoom.getTopRightY() > (moveableHeight - 1)){
            return false;
        }
        for (int x = newRoom.getBottomLeftX(); x < (newRoom.getWidth() + newRoom.getBottomLeftX()); x++) {
            for (int y = newRoom.getBottomLeftY(); y < (newRoom.getBottomLeftY() + newRoom.getHeight()); y++) {
                if (world[x][y] != Tileset.NOTHING) {
                    return false;
                }
            }
        }
        return true;
    }
    public TETile[][] returnWorld() {
        return world;
    }

    /** makes the random hallways */
    // similar to room, but different dimensions
    public void generateHallways(){
        // go through all rooms and connect room to a random room in all our rooms list
            // pick random point in the starting room (not a wall)
            // pick random point in the random room (not a wall)
            // make min x coordinate point A
            // connect two points

        // normally -1
        for (int i = 0; i < arrayRoomObj.size() - 1; i++) {
            // maybe have to randomly select the other room
            Room aRoom = arrayRoomObj.get(i);
            Room bRoom = arrayRoomObj.get(i + 1);
            generateOneHallway(aRoom, bRoom);
        }
    }

    /** generate one hallway */
    public void generateOneHallway(Room aRoom, Room bRoom) {
        ArrayList<Integer> aRoomCords = randomRoomPoint(aRoom); //[x1, y1]
        ArrayList<Integer> bRoomCords = randomRoomPoint(bRoom); //[x2, y2]
        // if room A has min x and min y build right then up
        if (aRoomCords.get(0) <= bRoomCords.get(0) && aRoomCords.get(1) <= bRoomCords.get(1)) {
            // start x, end x, y
            buildHallwayRight(aRoomCords.get(0), bRoomCords.get(0), aRoomCords.get(1));
            // start y, end y, x
            buildHallwayUp(aRoomCords.get(1), bRoomCords.get(1), bRoomCords.get(0));
        }
        // if room B has min x and min y build right then up
        else if (bRoomCords.get(0) < aRoomCords.get(0) && bRoomCords.get(1) < aRoomCords.get(1)) {
            buildHallwayRight(bRoomCords.get(0), aRoomCords.get(0), bRoomCords.get(1));
            buildHallwayUp(bRoomCords.get(1), aRoomCords.get(1), aRoomCords.get(0));
        }
        // if room A has min x and max y
        else if (aRoomCords.get(0) < bRoomCords.get(0) && aRoomCords.get(1) > bRoomCords.get(1)) {
            buildHallwayRight(aRoomCords.get(0), bRoomCords.get(0), bRoomCords.get(1));
            buildHallwayUp(bRoomCords.get(1), aRoomCords.get(1), aRoomCords.get(0));
        }
        // if room B has min x and max y
        // (bRoomCords.get(0) < aRoomCords.get(0) && bRoomCords.get(1) > aRoomCords.get(1))
        else {
            buildHallwayRight(bRoomCords.get(0), aRoomCords.get(0), aRoomCords.get(1));
            buildHallwayUp(aRoomCords.get(1), bRoomCords.get(1), bRoomCords.get(0));
        }
        }

        // find X direction we should go (x1 - x2)
            // find min x and make that starting point of x coordinate
            // for x from x1 to x2
                // if tile we're currently at is a wall or nothing replace with floor
                // place floor tiles in that direction until x1 = x2
        // find Y direction we should go (y1 - y2)
            // find min y and make that starting point of y
            // for y from y1 to y2
                // if tile we're currently at is a wall or nothing replace with floor
                // place floor tiles in that direction until y1 = y2

    /** find random point in a room */
    public ArrayList<Integer> randomRoomPoint(Room room) {
        ArrayList<Integer> coordinates = new ArrayList<>();
        int Xcord = RandomUtils.uniform(r,room.getBottomLeftX()+1, room.getTopRightX()-1);
        int Ycord = RandomUtils.uniform(r,room.getBottomLeftY()+1, room.getTopRightY()-1);
        coordinates.add(Xcord);
        coordinates.add(Ycord);
        return coordinates;
    }

    /** builds hallway to the right */
    public void buildHallwayRight(int startingX, int endingX, int Y) {
        if (world[startingX - 1][Y] == Tileset.NOTHING) {
            world[startingX - 1][Y] = Tileset.WALL;
        }
        if (world[endingX + 1][Y] == Tileset.NOTHING) {
            world[endingX + 1][Y] = Tileset.WALL;
        }
        if (world[startingX-1][Y - 1] == Tileset.NOTHING) {
            world[startingX-1][Y - 1] = Tileset.WALL;
        }
        if (world[endingX + 1][Y - 1] == Tileset.NOTHING) {
            world[endingX + 1][Y - 1] = Tileset.WALL;
        }
        for (int x = startingX; x <= endingX; x++) {
            if(world[x][Y] == Tileset.NOTHING || world[x][Y] == Tileset.WALL) {
                world[x][Y] = Tileset.FLOOR;
                if (world[x][Y + 1] == Tileset.NOTHING) {
                    world[x][Y + 1] = Tileset.WALL;
                }
                if (world[x][Y - 1] == Tileset.NOTHING) {
                    world[x][Y - 1] = Tileset.WALL;
                }
            }
        }
    }

    /** builds hallway up */
    public void buildHallwayUp(int startingY, int endingY, int X) {
        if (world[X][startingY - 1] == Tileset.NOTHING) {
            world[X][startingY - 1] = Tileset.WALL;
        }
        if (world[X][endingY + 1] == Tileset.NOTHING) {
            world[X][endingY + 1] = Tileset.WALL;
        }
        for (int y = startingY; y <= endingY; y++) {
            if(world[X][y] == Tileset.NOTHING || world[X][y] == Tileset.WALL) {
                world[X][y] = Tileset.FLOOR;
                if (world[X + 1][y] == Tileset.NOTHING) {
                    world[X + 1][y]=Tileset.WALL;
                }
                if (world[X - 1][y] == Tileset.NOTHING) {
                    world[X - 1][y]=Tileset.WALL;
                }
            }
        }
    }

    /** generate avatar in a random location in the map */
    public void generateAvatar(){
        // int randRoomNum = RandomUtils.uniform(r,0,arrayRoomObj.size() - 2); /** ERROR HERE */
        // ArrayList<Integer> point = randomRoomPoint(arrayRoomObj.get(randRoomNum));
        /** CHANGED IT TO ALWAYS GENERATE AVATAR IN FIRST ROOM */
        ArrayList<Integer> point = randomRoomPoint(arrayRoomObj.get(0));
        int xpoint = point.get(0);
        int ypoint = point.get(1);
        world[xpoint][ypoint] = Tileset.AVATAR;
        AvatarX = xpoint;
        AvatarY = ypoint;
    }

    /** move avatar up ("W") */
    public void moveUp(){
        if (world[AvatarX][AvatarY + 1] == Tileset.FLOOR) {
            world[AvatarX][AvatarY + 1] = Tileset.AVATAR;
            world[AvatarX][AvatarY] = Tileset.FLOOR;
            AvatarY += 1;
        }
    }

    /** move avatar left ("A") */
    public void moveLeft(){
        if (world[AvatarX - 1][AvatarY] == Tileset.FLOOR) {
            world[AvatarX - 1][AvatarY] = Tileset.AVATAR;
            world[AvatarX][AvatarY] = Tileset.FLOOR;
            AvatarX -= 1;
        }
    }

    /** move avatar down ("S") */
    public void moveDown(){
        if (world[AvatarX][AvatarY - 1] == Tileset.FLOOR) {
            world[AvatarX][AvatarY - 1] = Tileset.AVATAR;
            world[AvatarX][AvatarY] = Tileset.FLOOR;
            AvatarY -= 1;
        }
    }

    /** move avatar right ("D") */
    public void moveRight(){
        if (world[AvatarX + 1][AvatarY] == Tileset.FLOOR) {
            world[AvatarX + 1][AvatarY] = Tileset.AVATAR;
            world[AvatarX][AvatarY] = Tileset.FLOOR;
            AvatarX += 1;
        }
    }

    /** method that returns the tiletype of a certain coordinate */
    public TETile tileType(int xCord, int yCord) {
        if (world[xCord][yCord] == Tileset.FLOOR) {
            return Tileset.FLOOR;
        }
        if (world[xCord][yCord] == Tileset.NOTHING) {
            return Tileset.NOTHING;
        }
        if (world[xCord][yCord] == Tileset.WALL) {
            return Tileset.WALL;
        }
        //  if (world[xCord][yCord] == Tileset.AVATAR)
        else {
            return Tileset.AVATAR;
        }
    }

    /** returns x coordinate of avatar */
    public int getAvatarX() {
        return AvatarX;
    }

    /** returns y coordinate of avatar */
    public int getAvatarY(){
        return AvatarY;
    }
}
