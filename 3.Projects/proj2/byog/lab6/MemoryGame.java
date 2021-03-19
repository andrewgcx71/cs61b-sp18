package byog.lab6;

import byog.TileEngine.TERenderer;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.*;
import java.util.Random;

public class MemoryGame {
    private int width;
    private int height;
    private int round;
    private Random rand;
    private boolean gameOver;
    private boolean playerTurn;
    private static final char[] CHARACTERS = "abcdefghijklmnopqrstuvwxyz".toCharArray();
    private static final String[] ENCOURAGEMENT = {"You can do this!", "I believe in you!",
                                                   "You got this!", "You're a star!", "Go Bears!",
                                                   "Too easy for you!", "Wow, so impressive!"};

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Please enter a seed");
            return;
        }

        int seed = Integer.parseInt(args[0]);
        MemoryGame game = new MemoryGame(40, 40, seed);
        TERenderer ter = new TERenderer();
        //game.startGame();
    }

    public MemoryGame(int width, int height, int seed) {
        /* Sets up StdDraw so that it has a width by height grid of 16 by 16 squares as its canvas
         * Also sets up the scale so the top left is (0,0) and the bottom right is (width, height)
         */
        this.width = width;
        this.height = height;
        StdDraw.setCanvasSize(this.width * 16, this.height * 16);
        StdDraw.setXscale(0, this.width);
        StdDraw.setYscale(0, this.height);
        StdDraw.clear(Color.BLACK);
        StdDraw.enableDoubleBuffering();

        //TODO: Initialize random number generator
        this.rand = new Random(seed);

    }

    public String generateRandomString(int n) {
        //TODO: Generate random string of letters of length n
        String res = new String();
        for (int i = 0; i < n; i++) {
            int r = rand.nextInt(26);
            res += Character.toString(CHARACTERS[r]);
        }
        return res;
    }

    public void drawFrame(String s) {
        //TODO: Take the string and display it in the center of the screen
        //TODO: If game is not over, display relevant game information at the top of the screen
        StdDraw.clear(Color.black);
        StdDraw.setPenColor(Color.white);
        Font font = new Font("Arial", Font.BOLD, 40);
        StdDraw.setFont(font);
        int centerX = this.width / 2;
        int centerY = this.height / 2;
        StdDraw.text(centerX, centerY, s);
        if (!gameOver) {
            font = new Font("Arial", Font.BOLD, 20);
            StdDraw.setFont(font);
            StdDraw.text(width -6 , this.height - 2, ENCOURAGEMENT[rand.nextInt(ENCOURAGEMENT.length)]);
            StdDraw.text(3, height - 2, "Round: " + this.round);
            StdDraw.text(centerX, height - 2, playerTurn? "Type!" : "Watch!");
            StdDraw.line(0,height - 3, width, this.height -3);
        }
        StdDraw.show();

    }

    public void flashSequence(String letters) {
        //TODO: Display each character in letters, making sure to blank the screen between letters
        for (int i = 0; i < letters.length(); i++) {
            drawFrame(letters.substring(i, i + 1));
            StdDraw.pause(1000);
            StdDraw.clear(Color.black);
            StdDraw.pause(500);
        }
    }

    public String solicitNCharsInput(int n) {
        //TODO: Read n letters of player input
        String res = "";

        outer:while (res.length() < n) {
            if (!StdDraw.hasNextKeyTyped()) {
                continue outer; // if no input from user, go back outside wait for input
            } else {
                String letter = Character.toString(StdDraw.nextKeyTyped());
                res += letter;
                drawFrame(res);
            }
        }

        return res;
    }

    public void startGame() {
        //TODO: Set any relevant variables before the game starts
        this.gameOver = false;
        this.playerTurn = false;
        this.round = 1;
        //TODO: Establish Game loop
        while (!gameOver) {
            String target = generateRandomString(round);
            flashSequence(target);

            this.playerTurn = true;
            drawFrame(" ");
            String userInput = solicitNCharsInput(round);
            if (!target.equals(userInput)) {
                gameOver = true;
                drawFrame("Game Over, Final Level: " + round );
            } else {
                round++;
                playerTurn = false;
                drawFrame("Correct, well done!");
                StdDraw.pause(1000);
            }
        }
    }

}
