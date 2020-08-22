package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.io.*;

public class Game {
    TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    public static final int WIDTH = 85;
    public static final int HEIGHT = 45;


    /**
     * Method used for playing a fresh game. The game should start from the main menu.
     */
    public void playWithKeyboard() {
    }

    /**
     * Method used for autograding and testing the game code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The game should
     * behave exactly as if the user typed these characters into the game after playing
     * playWithKeyboard. If the string ends in ":q", the same world should be returned as if the
     * string did not end with q. For example "n123sss" and "n123sss:q" should return the same
     * world. However, the behavior is slightly different. After playing with "n123sss:q", the game
     * should save, and thus if we then called playWithInputString with the string "l", we'd expect
     * to get the exact same world back again, since this corresponds to loading the saved game.
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] playWithInputString(String input) {
        // TODO: Fill out this method to run the game using the input passed in,
        // and return a 2D tile representation of the world that would have been
        // drawn if the same inputs had been given to playWithKeyboard().]
        input = input.toLowerCase();
        File path = new File("//Users//andrew//OneDrive//CS61B//skeleton-sp18//proj2//byog//Core//save.txt");
        if (input.substring(0, 1).equals("l")) {
            if (fileIsEmpty(path)) {
                System.exit(0);
            }
            return getWorld(path);

        } else {
            long seed = getSeed(input);
            MapGeneration map = new MapGeneration(seed);
            TETile[][] newWorld = map.generate(WIDTH, HEIGHT);
            if (input.substring(input.length() - 1, input.length()).equals("q")) {
                save(path, newWorld);
            }
            return newWorld;
        }
    }

    /** Return the seed. */
    public long getSeed(String input) {
        int l = 0;
        for (int i = input.length() - 1; i >= 0; i--) {
            if(isSingleDigitNumber(input.charAt(i))) {
                l = i;
                break;
            }
        }
        return Long.parseLong(input.substring(1, l + 1));
    }

    //check if the current character is a number between 0 to 9.
    private boolean isSingleDigitNumber(char c) {
        if (c >= '0' && c <= '9') {
            return true;
        }
        return false;
    }

    //return true if the file is empty, otherwise false
    private boolean fileIsEmpty(File path) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            if (br.readLine() == null) {
                return true;
            }
        } catch (Exception e) {
            System.out.println("File doesn't exist");
        }
        return false;
    }

    // Return a previous saved world
    private TETile[][] getWorld(File path) {
        TETile[][] world = new TETile[WIDTH][HEIGHT];
        try {
            FileInputStream fis = new FileInputStream(path);
            ObjectInputStream ois = new ObjectInputStream(fis);
            for (int i = 0; i < WIDTH; i++) {
                for (int j = 0; j < HEIGHT; j++) {
                    world[i][j] = (TETile) ois.readObject();
                }
            }
        } catch (Exception e) {
            System.out.println("Error on reading file");
        }
        return world;
    }

    // Save a world to a txt file
    private void save(File path, TETile[][] world) {
        try {
            FileOutputStream fileOutput = new FileOutputStream(path);
            ObjectOutputStream oos = new ObjectOutputStream(fileOutput);
            for (int i = 0; i < WIDTH; i++) {
                for (int j = 0; j < HEIGHT; j++) {
                    oos.writeObject(world[i][j]);
                }
            }
        } catch (Exception e) {
            System.out.println("Error on writing file");
        }
    }
}
