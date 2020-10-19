package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.*;
import java.io.*;

public class Game{
    TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    public static final int WIDTH = 85;
    public static final int HEIGHT = 48;
    public static boolean gameOver = false;
    public static boolean selectGameOptionOver = false;
    public static boolean enterSeedOver = false;
    public static GameData gamedata = new GameData();
    public static File path = new File("//Users//andrew//OneDrive//CS61B//skeleton-sp18//proj2//byog//Core//save.txt");

    /**
     * Method used for playing a fresh game. The game should start from the main menu.
     */
    public void playWithKeyboard() {
        // initialize the tile rendering engine with a window size of X * Y
        ter.initialize(WIDTH, HEIGHT);
        String seed = "";
        while(!selectGameOptionOver) {
            drawUI();
            if (StdDraw.hasNextKeyTyped()) {
                String ui = Character.toString(StdDraw.nextKeyTyped()).toLowerCase(); // ui = user input
                if (ui.equals("l")) {
                    gamedata = getGameData(path);
                    selectGameOptionOver = true;
                }
                if (ui.equals("n")) {
                    selectGameOptionOver = true;
                    drawFrame(""); // clear the canvas.
                    while (true) {
                        if (StdDraw.hasNextKeyTyped()) {
                            String i = Character.toString(StdDraw.nextKeyTyped()).toLowerCase();
                            if (seed.length() == 0 && i.equals("s")) {
                                continue;
                            }
                            if (isSingleDigitNumber(i.charAt(0))) {
                                seed += i;
                            }
                            if (seed.length() > 0 && i.equals("s")) {
                                enterSeedOver = true;
                                MapGeneration map = new MapGeneration(Long.parseLong(seed));
                                gamedata = map.generate(WIDTH, HEIGHT);
                                break;
                            }
                        }
                        drawFrame(seed);
                    }
                }
                if (ui.equals("q")) {
                    System.exit(0);
                }
            }
        }
        startGame(gamedata);
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
        if (loadSaveGame(input)) {
            gamedata = getGameData(path); // load last saved game
        } else {
            long seed = getSeed(input); // start a new game
            MapGeneration map = new MapGeneration(seed);
            gamedata = map.generate(WIDTH, HEIGHT);
        }
        //startGameWithInputString(gamedata.getWorld(), gamedata.getPlayer(), input);
        startGame(gamedata);
        if (saveGame(input)) {
            save(path, gamedata);
        }
        return gamedata.getWorld();
    }

    /** Start game with inputString.*/
    public void startGameWithInputString(TETile[][] world, Player player, String input) {
        String movement = UserCommands(input);
        int n = 0;
        ter.initialize(WIDTH, HEIGHT);
        while (!gameOver && n < movement.length()) {
            player.move(world, movement.substring(n, n + 1), this);
            ter.renderFrame(world);
            StdDraw.pause(1000);
            n++;
        }
        StdDraw.clear(Color.black);
        StdDraw.show();
    }

    // return true if want to load last saved, otherwise false
    private boolean loadSaveGame(String input) {
        return input.substring(0, 1).equals("l");
    }

    // return true if want to load last saved, otherwise false
    private boolean saveGame(String input) {
        return input.substring(input.length() - 1, input.length()).equals("q");
    }

    /** Return user commands, w, a, s, d.*/
    private String UserCommands(String input) {
        int start = 0;
        for (int i = 1; i < input.length(); i++) {
            if (!isSingleDigitNumber(input.charAt(i))) {
                start = i;
                break;
            }
        }
        return input.substring(start + 1, input.length());
    }

    /** helper method: Return the seed. */
    private long getSeed(String input) {
        int end = 0;
        for (int i = 1; i < input.length(); i++) {
            if (!isSingleDigitNumber(input.charAt(i))) {
                end = i;
                break;
            }
        }
        return Long.parseLong(input.substring(1, end));
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
    private GameData getGameData(File path) {
        if (fileIsEmpty(path)) {
            System.exit(0);
        }
        TETile[][] world = new TETile[WIDTH][HEIGHT];
        GameData gamedata = new GameData();
        try {
            FileInputStream fis = new FileInputStream(path);
            ObjectInputStream ois = new ObjectInputStream(fis);
            for (int i = 0; i < WIDTH; i++) {
                for (int j = 0; j < HEIGHT; j++) {
                    world[i][j] = (TETile) ois.readObject();
                }
            }
            Position position = (Position) ois.readObject();

            gamedata = new GameData(world, new Player(world, position));
        } catch (Exception e) {
            System.out.println("Error on reading file");
        }
        return gamedata;
    }

    // Save TETile[][] object to a txt file
    //Note: to save object to a file, it doesn't have to be same type of object, it can save generic type of objects in to text file,
    // when you write the objects in to text file, it will be in the same order when you read it. You don't need to worry about the orders
    // which is very handy tools.
    // please note: for object inside an object, you can only write primitive data type in an object, you can't write an object in an object to text file.
    private void save(File path, GameData gamedata) {
        try {
            FileOutputStream fileOutput = new FileOutputStream(path);
            ObjectOutputStream oos = new ObjectOutputStream(fileOutput);
            TETile[][] world = gamedata.getWorld();
            Position position = gamedata.getPlayer().getPosition();
            for (int i = 0; i < WIDTH; i++) {
                for (int j = 0; j < HEIGHT; j++) {
                    oos.writeObject(world[i][j]);
                }
            }
            oos.writeObject(position);
        } catch (Exception e) {
            System.out.println("Error on writing file");
        }
    }

    /**The method takes the world and draw the world with HUD(heads up display) in the screen. it doesn't return anything*/
    public void drawFrame(String Text) {
        StdDraw.clear(Color.black);
        Font font = new Font("Monaco", Font.BOLD, 20);
        StdDraw.setFont(font);
        if (!enterSeedOver) {
            StdDraw.text(WIDTH / 2, HEIGHT / 2 + 5, "Please Enter a Seed. Press S to Finish.");
        }
        StdDraw.text(WIDTH / 2, HEIGHT / 2, Text);
        StdDraw.show();
    }

    /** Draw the HUD (heads up display)*/
    private void drawHUD(TETile[][] world, int x, int y) {
        String text = "";
        StdDraw.setPenColor(Color.white);
        Font font = new Font("Monaco", Font.BOLD, 50);
        if (world[x][y].equals(Tileset.WALL)) {
            text = "WALL";
        }
        if (world[x][y].equals(Tileset.FLOOR)) {
            text = "FLOOR";
        }
        if (world[x][y].equals(Tileset.PLAYER)) {
            text = "PLAYER";
        }
        if (world[x][y].equals(Tileset.NOTHING)) {
            text = "Nothing";
        }
        if (world[x][y].equals(Tileset.LOCKED_DOOR)) {
            text = "Locked Door";
        }

        StdDraw.text(3, HEIGHT - 1, text);
        StdDraw.show();
    }

    //This method draws the UI.*/
    private void drawUI() {
        StdDraw.clear(Color.black);
        StdDraw.setPenColor(Color.white);
        Font font = new Font("", Font.BOLD, 30);
        StdDraw.setFont(font);
        StdDraw.text(WIDTH / 2, HEIGHT - 10, "CS61B: MAZE ESCAPE"); // add title to the canvas
        font = new Font("", Font.BOLD, 20);
        StdDraw.setFont(font);
        StdDraw.text(WIDTH / 2, HEIGHT - 20, "New Game (N)");// add games options to canvas
        StdDraw.text(WIDTH / 2, HEIGHT - 24, "Load Game (L)");
        StdDraw.text(WIDTH / 2, HEIGHT - 28, "Quit (Q)");
        StdDraw.show();
    }

    //start the game.
    public void startGame(GameData gamedata) {
        TETile[][] world = gamedata.getWorld();
        Player player = gamedata.getPlayer();
        ter.initialize(WIDTH, HEIGHT);
        while (!gameOver) {
            ter.renderFrame(world);
            int x = (int) StdDraw.mouseX();
            int y = (int) StdDraw.mouseY();
            if (x < WIDTH && y <HEIGHT) { // since StdDraw will get x and y coordinates out of the canvas, set a constrain.
                drawHUD(world, x, y);// drawHUD() has to call after Ter.RenderFrame() has been called because Ter.RenderFrame() calls StdDraw.clear().
            }
            if (StdDraw.hasNextKeyTyped()) {
                String m = Character.toString(StdDraw.nextKeyTyped()).toLowerCase();
                player.move(world, m, this);
                if (m.equals("q")) {
                    gameOver = true;
                    save(path, gamedata);
                    System.exit(0);
                }
            }
            if (gameOver) {
                ter.renderFrame(world);
                StdDraw.clear(Color.black);
                StdDraw.show();
            }
        }
    }

}
