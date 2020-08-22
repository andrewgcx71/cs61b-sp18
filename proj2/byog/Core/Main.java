package byog.Core;

import byog.TileEngine.TETile;

/** This is the main entry point for the program. This class simply parses
 *  the command line inputs, and lets the byog.Core.Game class take over
 *  in either keyboard or input string mode.
 */
public class Main {
    public static void main(String[] args) {
        if (args.length > 1) {
            System.out.println("Can only have one argument - the input string");
            System.exit(0);
        } else if (args.length == 1) {
            Game game = new Game();
            TETile[][] worldState = game.playWithInputString("n5197880843569031643s");
            System.out.println(TETile.toString(worldState));

            System.out.println("-----------------");

            TETile[][] w = game.playWithInputString("n5197880843569031643s");
            System.out.println(TETile.toString(w));

            System.out.println("-----------------");



        } else {
            Game game = new Game();
            game.playWithKeyboard();
        }
    }
}
