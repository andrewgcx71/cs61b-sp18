package byog.Core;

import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

/** a MapBuilder instance can build a room/hallway and can be expand with room/hallway(s) at its adjacency.
 * (a room/hallway is considered as adjacency to another room/hallway when they connected.)
 * @author Andrew Zhu */
public class MapBuilder {

    /**length of room.*/
    private int l;

    /**width of room.*/
    private int w;

    /**position represents the x-coordinate and y-coordinate in the center of room.*/
    private Position position;


    /**
     *Each element in the boolean array represents a direction, by default: false.
     * visits[0]: set to true if it has been visited (either expanded or it's a dead end) on the left.
     * visits[1]:set to true if it has been visited (either expanded or it's a dead end) on the bottom.
     * visits[2]:set to true if it has been visited (either expanded or it's a dead end).
     * visits[3]:set to true if it has visited (either expanded or it's a dead end).
     */
    private boolean[] visits = new boolean[4];

    //Constructor with parameters.
    public MapBuilder(int L, int W, Position P) {
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

    /** work with expandAtD() method, has to call expandAtD() method before calling this method.
     * This method expand current room/hallway with a new room/hallway at direction d, and return the new room/hallway created */
    public MapBuilder expandD(TETile[][] world, int l, int w, int d) {

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
        MapBuilder newRoom = new MapBuilder(l, w, new Position(x, y));
        newRoom.BuildToWorld(world);
        connectWithFloor(world, newRoom, d);
        visits[d] = true;
        newRoom.visits[getOpposite(d)] = true;
        return newRoom;
    }

    /** check if room/hallway with a particular shape can be expanded at direction d,
     * if expandable, return true, otherwise false.*/
    public boolean expandAtD(TETile[][] world, int l, int w, int d, int X, int Y) {
        if (!outOfBoundaries(l, w, d, X, Y) && !isOverlap(world, l, w, d)) {
            return true;
        }
        return false;
    }

    /**Return true if 4 directions have been visited, otherwise false */
    public boolean visited() {
        for (boolean temp: visits) {
            if (!temp) {
                return false;
            }
        }
        return true;
    }

    /** build the room/hallway to the world.
     * p (position) is in the center of room/hallway.
     *   x x x x x
     *   x   p   x
     *   x x x x x
     */
    public void BuildToWorld(TETile[][] world) {
        int y = getEdgePos(2).getY() - ((l - 1) / 2 + 1);
        int x = getEdgePos(2).getX();
        BuildFloorsToWorld(world, new Position(x, y));
        BuildWallsToWorld(world, new Position(x, y));
    }

    /** Return a position at the edge of current room/hallway. */
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


    /** Helper function, use by BuildWallsToWorld(). (Build Horizontal walls at a starter position, n is the size of walls.)  */
    private void BuildHorizontalWalls(TETile[][] world, int n, Position p) {
        int x = p.getX();
        int y = p.getY();
        for (int i = 0; i < n; i++) {
            world[x][y] = Tileset.WALL;
            x++;
        }
    }

    /** Helper function, use by BuildWallsToWorld(). (Build vertical walls at a starter position, n is the size of walls.)  */
    private void BuildVerticalWalls(TETile[][] world, int n, Position p) {
        int x = p.getX();
        int y = p.getY();
        for (int i = 0; i < n; i++) {
            world[x][y] = Tileset.WALL;
            y++;
        }
    }

    /** Helper function, use by BuildToWorld(). */
    private void BuildWallsToWorld(TETile[][] world, Position p) {
        BuildHorizontalWalls(world, w + 2, p);
        BuildHorizontalWalls(world, w + 2, new Position(p.getX(), p.getY() + l + 1));
        BuildVerticalWalls(world, l, new Position(p.getX(), p.getY() + 1));
        BuildVerticalWalls(world, l, new Position(p.getX() + w + 1, p.getY() + 1));
    }

    /** Helper function, use by drawToWorld(). */
    private void BuildFloorsToWorld(TETile[][] world, Position p) {
        for (int y = p.getY() + 1; y <= p.getY() + l + 1; y++) {
            for (int x = p.getX() + 1; x <= p.getX() + w + 1; x++){
                world[x][y] = Tileset.FLOOR;
            }
        }
    }


    /** Helper function, use by expandAtD(). (Return true if out of boundaries at d, otherwise false.) */
    private boolean outOfBoundaries(int l, int w, int d, int X, int Y) {
        Position EdgePos = getEdgePos(d);
        if (d == 0) {
            if (EdgePos.getX() + (w + 2) >= X || EdgePos.getY() - ((l - 1) / 2 + 1) < 0 || EdgePos.getY() + ((l - 1) / 2 + 1) >= Y) {
                return true;
            }
        } else if (d == 1) {
            if (EdgePos.getY() - (l + 2) < 0 || EdgePos.getX() - ((w - 1) / 2 + 1) < 0 || EdgePos.getX() + ((w - 1) / 2 + 1) >= X) {
                return true;
            }
        } else if (d == 2) {
            if (EdgePos.getX() - (w + 2) < 0 || EdgePos.getY() - ((l - 1) / 2 + 1) < 0 || EdgePos.getY() + ((l - 1) / 2 + 1) >= Y) {
                return true;
            }
        } else {
            if (EdgePos.getY() + (l + 2) >= Y || EdgePos.getX() - ((w - 1) / 2 + 1) < 0 || EdgePos.getX() + ((w - 1) / 2 + 1) >= X) {
                return true;
            }
        } return false;
    }

    /** Helper function, use by expandAtD(). (Return true if another room/hallway stand the way at d, otherwise false.) */
    private boolean isOverlap(TETile[][] world, int l, int w, int d) {
        Position p = getEdgePos(d);
        switch (d) {
            case 0:
                return overlap(world, l, w, new Position(p.getX() + 1, p.getY() - ((l - 1) / 2 + 1)));
            case 1:
                return overlap(world, l, w, new Position(p.getX() - ((w - 1) / 2 + 1), p.getY() - (l + 2)));
            case 2:
                return overlap(world, l, w, new Position(p.getX() - (w + 2), p.getY() - ((l - 1) / 2 + 1)));
            case 3:
                return overlap(world, l, w, new Position(p.getX() - ((w - 1) / 2 + 1), p.getY() + 1));
        }
        return true;
    }

    /** Helper function, use by isOverlap(). (return true is the new room/hallway about to expand will be overlap with other room/hallway at direction d, otherwise false.) */
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

    //Helper function, use by overlap(). (check each tile in that row, return false a tile is not "Nothing", otherwise false.)*/
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

    /** Helper function, use by expandAtD(). (break the walls, connect two MapBuilder instances (Room/hallway) together with floors.)  */
    private void connectWithFloor(TETile[][] world, MapBuilder b, int d) {
        int x = getEdgePos(d).getX();
        int y = getEdgePos(d).getY();
        world[x][y] = Tileset.FLOOR;
        x = b.getEdgePos(getOpposite(d)).getX();
        y = b.getEdgePos(getOpposite(d)).getY();
        world[x][y] = Tileset.FLOOR;
    }

    /**Helper function, use by connectWithFloor() and ExpandD(). (Get the opposite direction of current direction.)*/
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

    /** return visits.*/
    public boolean[] getVisits() {
        return visits;
    }
}
