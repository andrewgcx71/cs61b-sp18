package byog.Core;

import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

/** a builder can either build a room or a hallway (either horizontal or vertical)
 * @author Andrew Zhu */
public class Builder {

    /**length of room.*/
    private int l;

    /**width of room.*/
    private int w;

    /**Position of Room, p is locate at the center of room.*/
    private Position position;

    // getter method.
    public boolean[] getDirections() {
        return directions;
    }

    /**
     * directions[0]:true if left has been explored, otherwise false.
     * directions[1]:true if bottom has been explored, otherwise false.
     * directions[2]:true if right has been explored, otherwise false.
     * directions[3]:true if top has been explored, otherwise false.
     */
    private boolean[] directions = new boolean[4];

    //Constructor with parameters.
    public Builder(int L, int W, Position P) {
        l = L;
        w = W;
        position = P;
    }

    //Return true if a room, otherwise false.
    public boolean isRoom() {
        return l > 1 && w > 1;
    }

    //Return true if a Vertical Hallway, otherwise false.
    public boolean isVerticalHallway() {
        return l == 1;
    }

    //Return true if a Horizontal Hallway, otherwise false.
    public boolean isHorizontalHallway() {
        return w == 1;
    }

    /** add a new room or hallway at current builder's build, and return the new room or hallway was build */
    public Builder expand(TETile[][] world, int l, int w, int d) {

        int x = getEdgePos(d).getX();
        int y = getEdgePos(d).getY();
        if (d == 0) {
            x = x + ((w - 1) / 2 + 2);
        } else if (d == 1) {
            y = y - ((l - 1) / 2 + 2);
        } else if (d == 2) {
            x = x - ((w - 1) / 2 + 2);
        } else {
            y = y + ((l - 1) / 2 + 2);
        }
        Builder next = new Builder(l, w, new Position(x, y));
        next.drawToWorld(world);
        connectWithFloor(world, next, d);
        directions[d] = true;
        next.directions[getOpposite(d)] = true;
        return next;
    }

    /** check if room/hallway with a particular length and width can be expanded at a specific direction, if expandable, return true, otherwise return false.*/
    public boolean isExpand(TETile[][] world, int l, int w, int d, int X, int Y) {
        if (isNotOutOfBoundaries(l, w, d, X, Y) && isNotOverlap(world, l, w, d)) {
            return true;
        }
        return false;
    }

    /**Return true if all directions have been explored, otherwise false */
    public boolean exploredAllDirections() {
        for (boolean temp: directions) {
            if (!temp) {
                return false;
            }
        }
        return true;
    }

    /** Call drawWall() and DrawFloor().
     * p is in the center of object.
     *   x x x x x
     *   x   p   x
     *   x x x x x
     */
    public void drawToWorld(TETile[][] world) {
        int y = getEdgePos(2).getY() - ((l - 1) / 2 + 1);
        int x = getEdgePos(2).getX();
        drawFloor(world, new Position(x, y));
        drawWall(world, new Position(x, y));
    }

    /** Return a position at the edge. Either at left, bottom, right, or top */
    public Position getEdgePos(int d) {
        if (d == 0) {
            return new Position(position.getX() + ((w - 1) / 2 + 1), position.getY());
        } else if (d == 1) {
            return new Position(position.getX(), position.getY() - ((l - 1) / 2 + 1));
        } else if (d == 2) {
            return new Position(position.getX() - ((w - 1) / 2 + 1), position.getY());
        } else {
            return new Position(position.getX(), position.getY() + ((l - 1) / 2 + 1));
        }
    }


    //fill row with tiles, n is the numbers of tiles.
    private void fillRow(TETile[][] world, int n, Position p) {
        int x = p.getX();
        int y = p.getY();
        for (int i = 0; i < n; i++) {
            world[x][y] = Tileset.WALL;
            x++;
        }
    }

    /** Draw the wall of the object.*/
    private void drawWall(TETile[][] world, Position p) {
        fillRow(world, w + 2, p);
        fillRow(world, w + 2, new Position(p.getX(), p.getY() + l + 1));
        for (int i = 1; i <= l; i++) {
            world[p.getX()][p.getY() + i] = Tileset.WALL;
            world[p.getX() + w + 1][p.getY() + i] = Tileset.WALL;
        }
    }

    /** Draw floor of the object. */
    private void drawFloor(TETile[][] world, Position p) {
        for (int y = p.getY() + 1; y <= p.getY() + l + 1; y++) {
            for (int x = p.getX() + 1; x <= p.getX() + w + 1; x++){
                world[x][y] = Tileset.FLOOR;
            }
        }
    }


    /** return true if not out of boundaries, otherwise false. */
    private boolean isNotOutOfBoundaries(int l, int w, int d, int X, int Y) {
        Position EdgePos = getEdgePos(d);
        if (d == 0) {
            if (EdgePos.getX() + (w + 2) >= X || EdgePos.getY() - ((l - 1) / 2 + 1) < 0 || EdgePos.getY() + ((l - 1) / 2 + 1) >= Y) {
                return false;
            }
        } else if (d == 1) {
            if (EdgePos.getY() - (l + 2) < 0 || EdgePos.getX() - ((w - 1) / 2 + 1) < 0 || EdgePos.getX() + ((w - 1) / 2 + 1) >= X) {
                return false;
            }
        } else if (d == 2) {
            if (EdgePos.getX() - (w + 2) < 0 || EdgePos.getY() - ((l - 1) / 2 + 1) < 0 || EdgePos.getY() + ((l - 1) / 2 + 1) >= Y) {
                return false;
            }
        } else {
            if (EdgePos.getY() + (l + 2) >= Y || EdgePos.getX() - ((w - 1) / 2 + 1) < 0 || EdgePos.getX() + ((w - 1) / 2 + 1) >= X) {
                return false;
            }
        } return true;
    }

    /** return true if no other hallways or rooms at the area about to expand, otherwise false. */
    private boolean isNotOverlap(TETile[][] world, int l, int w, int d) {
        Position p = getEdgePos(d);
        if (d == 0) {
            if (overlap(world, l, w, new Position(p.getX() + 1, p.getY() - ((l - 1) / 2 + 1)))) {
                return false;
            }
        } else if (d == 1) {
            if (overlap(world, l, w, new Position(p.getX() - ((w - 1) / 2 + 1), p.getY() - (l + 2)))) {
                return false;
            }
        } else if (d == 2) {
            if (overlap(world, l, w, new Position(p.getX() - (w + 2), p.getY() - ((l - 1) / 2 + 1)))) {
                return false;
            }
        } else {
            if (overlap(world, l, w, new Position(p.getX() - ((w - 1) / 2 + 1), p.getY() + 1))) {
                return false;
            }
        }
        return true;
    }

    /** return true if overlap, otherwise false.*/
    private boolean overlap(TETile[][] world, int l, int w, Position p) {
        if (!checkRow(world, w + 2, p) || !checkRow(world, w + 2, new Position(p.getX(), p.getY() + l + 1))) {
            return true;
        }
        for (int i = 1; i <= l; i++) {
            if(!world[p.getX()][p.getY() + i].equals(Tileset.NOTHING) || !world[p.getX() + w + 1][p.getY() + i].equals(Tileset.NOTHING)) {
                return true;
            }
        }
        return false;
    }

    //check each tile in that row, return false we found a tile is not "NOTHING", otherwise true.*/
    private boolean checkRow(TETile[][] world, int n, Position p) {
        int x = p.getX();
        int y = p.getY();
        for (int i = 0; i < n; i++) {
            if (!world[x][y].equals(Tileset.NOTHING)) {
                return false;
            }
            x++;
        } return true;
    }

    /** Connect two objects with floors at specific direction, d is the direction of current object want to  */
    private void connectWithFloor(TETile[][] world, Builder b, int d) {
        int x = getEdgePos(d).getX();
        int y = getEdgePos(d).getY();
        world[x][y] = Tileset.FLOOR;
        x = b.getEdgePos(getOpposite(d)).getX();
        y = b.getEdgePos(getOpposite(d)).getY();
        world[x][y] = Tileset.FLOOR;
    }
    /**Return the opposite direction.*/
    private int getOpposite(int d) {
        if (d == 0) {
            return 2;
        } else if(d == 1) {
            return 3;
        } else if (d == 2) {
            return 0;
        } else {
            return 1;
        }
    }
}
