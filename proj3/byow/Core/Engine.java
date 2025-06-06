package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Out;
import edu.princeton.cs.algs4.StdDraw;

import java.text.SimpleDateFormat;
import java.util.Date;


import java.awt.*;

public class Engine {
    TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    public static final int WIDTH = 80;
    public static final int HEIGHT = 30;
    TETile[][] worldFrame;
    MapGenerator currMap;
    // public boolean gameOver = false; // unsure if this variable is necessary
    private String gameHistory; // string that keeps track of all movements in the game
    private boolean mousePressed = false;
    public static final int FIFTEEN = 15;
    public static final int TWOHUNDRED = 200;
    public static final int SIXTEEN = 16;
    public static final double POINTSEVEN = 0.7;
    public static final double POINTSIX = 0.6;
    public static final double POINTFOUR = 0.4;
    public static final double POINTTHREE = 0.3;
    public static final int THIRTY = 30;
    public static final int TWENTY = 20;
    public static final int TEN = 10;


    /**
     * Method used for exploring a fresh world. This method should handle all inputs,
     * including inputs from the main menu.
     */
    public void interactWithKeyboard() {
        String seedString = "";
        menuGenerator();
        boolean seedIndicator = false;
        // while loop to display menu and collect seed, shows menu until user types something
        while (true) {
            boolean hasTyped = StdDraw.hasNextKeyTyped();
            if (hasTyped) {
                Character currCharacter = StdDraw.nextKeyTyped();
                if (currCharacter == 's' || currCharacter == 'S') {
                    gameHistory += currCharacter;
                    seedIndicator = false;
                    StdDraw.clear(Color.BLACK);
                    StdDraw.show();
                    Long seed = Long.parseLong(seedString);
                    currMap = new MapGenerator(seed);
                    break;
                }
                if (currCharacter == 'n' || currCharacter == 'N') {
                    displayString("Type String!");
                    gameHistory = Character.toString(currCharacter);
                    seedIndicator = true;
                } else if (seedIndicator) {
                    gameHistory += currCharacter;
                    seedString += Character.toString(currCharacter);
                    displayString(seedString);
                    // stop showing menu and display new screen that shows what they're typing as they type
                }
                if (currCharacter == 'l' || currCharacter == 'L') {
                    // In in = new In("./out/history.txt");
                    In in = new In("./byow/history.txt");
                    String saved = in.readLine();
                    gameHistory = saved;
                    if (saved == null) {
                        System.exit(0);
                    }
                    worldFrame = interactWithInputString(saved);
                    gameHistory += currCharacter;
                    break;
                }
                if (currCharacter == 'q' || currCharacter == 'Q') {
                    // if the person quits from the menu it shouldn't save any history
                    System.exit(0);
                }
            }
            StdDraw.pause(FIFTEEN);
        }
        ter.initialize(WIDTH, HEIGHT);
        displayCurrWorld();
        displayHUD();
        // separate while loop down here that controls movements
        while (true) {
            moveWhileLoop();
        }
    }

    /**
     * Method used for autograding and testing your code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The engine should
     * behave exactly as if the user typed these characters into the engine using
     * interactWithKeyboard.
     *
     * Recall that strings ending in ":q" should cause the game to quite save. For example,
     * if we do interactWithInputString("n123sss:q"), we expect the game to run the first
     * 7 commands (n123sss) and then quit and save. If we then do
     * interactWithInputString("l"), we should be back in the exact same state.
     *
     * In other words, running both of these:
     *   - interactWithInputString("n123sss:q")
     *   - interactWithInputString("lww")
     *
     * should yield the exact same world state as:
     *   - interactWithInputString("n123sssww")
     *
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] interactWithInputString(String input) {
        // passed in as an argument, and return a 2D tile representation of the
        // world that would have been drawn if the same inputs had been given
        // to interactWithKeyboard().
        // See proj3.byow.InputDemo for a demo of how you can make a nice clean interface
        // that works for many different input types.

        // handle input -- "N#######S" w/ arbitrary amount of #
        // should work with input "###N###S"

        String seedString = "";
        boolean seedIndicator = false;
        for (int i = 0; i < input.length(); i++) {
            if (input.charAt(i) == 'l' || input.charAt(i) == 'L') {
                // In in = new In("./out/history.txt");
                In in = new In("./byow/history.txt");
                String saved = in.readLine();
                gameHistory = saved;
                if (saved == null) {
                    System.exit(0);
                }
                if (i == input.length() - 1) {
                    return interactWithInputString(saved);
                } else {
                    String afterL = input.substring(i + 1, input.length());
                    String newInput = saved + afterL;
                    return interactWithInputString(newInput);
                }
            }
            if (seedIndicator && input.charAt(i) == 's' || input.charAt(i) == 'S') {
                seedIndicator = false;
                gameHistory += input.charAt(i);
            }
            if (seedIndicator) {
                seedString += input.charAt(i);
                gameHistory += input.charAt(i);
            }
            if (input.charAt(i) == 'n' || input.charAt(i) == 'N') {
                seedIndicator = true;
                gameHistory = Character.toString(input.charAt(i));
            }
        }
        long seed = Long.parseLong(seedString);
        currMap = new MapGenerator(seed);
        for (int i = seedString.length() + 2; i < input.length(); i++) {
            if (input.charAt(i) == 'w' || input.charAt(i) == 'W') {
                currMap.moveUp();
                gameHistory += input.charAt(i);
            } else if (input.charAt(i) == 's' || input.charAt(i) == 'S') {
                currMap.moveDown();
                gameHistory += input.charAt(i);
            } else if (input.charAt(i) == 'd' || input.charAt(i) == 'D') {
                currMap.moveRight();
                gameHistory += input.charAt(i);
            } else if (input.charAt(i) == 'a' || input.charAt(i) == 'A') {
                currMap.moveLeft();
                gameHistory += input.charAt(i);
            } else if (input.charAt(i) == ':') {
                gameHistory += input.charAt(i);
            } else if (input.charAt(i) == 'q' || input.charAt(i) == 'Q') {
                if (gameHistory.charAt(gameHistory.length() - 1) == ':') {
                    // save the worldHistory in a new file
                    // quit the game
                    gameHistory = gameHistory.substring(0, gameHistory.length() - 1);
                    // Out out = new Out("./out/history.txt");
                    Out out = new Out("./byow/history.txt");
                    out.print(gameHistory); /** !!!!!! ERROR HERE !!!!! */
                }
            }
        }

        // read string from after "s" to end and call movements corresponding to the string character
        // ex. if next character in string is "W" move character up in the world
        worldFrame = currMap.returnWorld();
        return worldFrame;
    }

    public String toString() {
        ter.initialize(WIDTH, HEIGHT);
        ter.renderFrame(worldFrame);
        return TETile.toString(worldFrame);
    }

    /** method that initializes everything in StdDraw */
    public void menuGenerator() {
        StdDraw.setCanvasSize(WIDTH * SIXTEEN, HEIGHT * SIXTEEN);
        Font font = new Font("Monaco", Font.BOLD, TWENTY);
        StdDraw.setFont(font);
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.clear(Color.BLACK);
        StdDraw.enableDoubleBuffering();
        StdDraw.text(0.5, POINTSEVEN, "Jordan and Claire's World");
        StdDraw.text(0.5, POINTSIX, "New World (N)");
        StdDraw.text(0.5, 0.5, "Load World (L)");
        StdDraw.text(0.5, POINTFOUR, "Quit (Q)");
        StdDraw.show();
    }

    /** method that displays the HUD (always running) */
    public void displayHUD() {
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        StdDraw.clear(Color.BLACK);
        if (mousePressed) {
            displayAvatarWorld();
        } else {
            displayCurrWorld();
        }
        String locationString = "";
        int xCord = (int) StdDraw.mouseX();
        int yCord = (int) StdDraw.mouseY();

        if (currMap.tileType(xCord, yCord) == Tileset.FLOOR) {
            locationString = "floor";
        } else if (currMap.tileType(xCord, yCord) == Tileset.WALL) {
            locationString = "wall";
        } else if (currMap.tileType(xCord, yCord) == Tileset.NOTHING) {
            locationString = "nothing";
        } else {
            locationString = "avatar";
        }
        Font font = new Font("Monaco", Font.BOLD, TEN);
        StdDraw.setFont(font);
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.enableDoubleBuffering();
        StdDraw.textLeft(POINTTHREE, POINTTHREE, locationString);
        StdDraw.textRight(WIDTH / 5, POINTTHREE, formatter.format(date));
        StdDraw.show();
    }

    /** method that displays what the user has typed so far */
    public void displayString(String s) {
        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(Color.WHITE);
        Font fontBig = new Font("Monaco", Font.BOLD, THIRTY);
        StdDraw.setFont(fontBig);
        StdDraw.text(0.5, 0.5, s);
        StdDraw.show();
    }

    /** method that displays current state of the world */
    public void displayCurrWorld() {
        worldFrame = currMap.returnWorld();
        ter.renderFrame(worldFrame);
    }

    /** displays small avatar square of currWorld */
    public void displayAvatarWorld() {
        worldFrame = currMap.returnWorld();
        int avatarXCord = currMap.getAvatarX();
        int avatarYCord = currMap.getAvatarY();
        ter.renderAvatarFrame(worldFrame, avatarXCord, avatarYCord);
    }

    /** method that chooses which world to display*/
    public void displaySomeWorld() {
        if (mousePressed) {
            displayAvatarWorld();
        } else {
            displayCurrWorld();
        }
    }

    /** second while loop in interactWithKeyboard */
    public void moveWhileLoop() {
        boolean hasTyped = StdDraw.hasNextKeyTyped();
        if (StdDraw.isMousePressed()) {
            mousePressed = !mousePressed;
            StdDraw.pause(TWOHUNDRED);
        }
        if (hasTyped) {
            Character currCharacter = StdDraw.nextKeyTyped();
            if (currCharacter == 'w' || currCharacter == 'W') {
                gameHistory += currCharacter;
                currMap.moveUp();
                displaySomeWorld();
            }
            if (currCharacter == 'a' || currCharacter == 'A') {
                gameHistory += currCharacter;
                currMap.moveLeft();
                displaySomeWorld();
            }
            if (currCharacter == 's' || currCharacter == 'S') {
                gameHistory += currCharacter;
                currMap.moveDown();
                displaySomeWorld();
            }
            if (currCharacter == 'd' || currCharacter == 'D') {
                gameHistory += currCharacter;
                currMap.moveRight();
                displaySomeWorld();
            }
            if (currCharacter == ':') {
                gameHistory += currCharacter;
            }
            if (currCharacter == 'q' || currCharacter == 'Q') {
                if (gameHistory.charAt(gameHistory.length() - 1) == ':') {
                    gameHistory += currCharacter;
                    // Out out = new Out("./out/history.txt");
                    Out out = new Out("./byow/history.txt");
                    out.print(gameHistory);
                    System.exit(0);
                }
            }
        } else {
            if (mousePressed) {
                displayAvatarWorld();
            }
            displayHUD();
        }
    }
}
