
import java.util.HashMap;
import java.util.Map;

/**
 * This class provides all code necessary to take a query box and produce
 * a query result. The getMapRaster method must return a Map containing all
 * seven of the required fields, otherwise the front end code will probably
 * not draw the output correctly.
 */
public class Rasterer {

    //first file's row in the query box, will update when latUpLt is called.
    private int upperLeftRow;

    //first file's column in the query box, will update when lonUpLt is called.
    private int upperLeftColumn;

    //last file's row in the query box, will update when latLowRt is called.
    private int lowerRightRow;

    //last file's column in the query box, will update when lonLowRt is called.
    private int lowerRightColumn;

    //Root Coordinate - upper left
    private Coordinate upperLeftRoot;

    //Root Coordinate - lower right
    private Coordinate lowerRightRoot;

    // Results return to front end
    private Map<String, Object> result = new HashMap<>();

    public Rasterer() {
        // YOUR CODE HERE
        result.put("query_success", false);
        upperLeftRoot = new Coordinate(MapServer.ROOT_ULLAT, MapServer.ROOT_ULLON);
        lowerRightRoot = new Coordinate(MapServer.ROOT_LRLAT, MapServer.ROOT_LRLON);
    }

    /**
     * Takes a user query and finds the grid of images that best matches the query. These
     * images will be combined into one big image (rastered) by the front end. <br>
     * <p>
     * The grid of images must obey the following properties, where image in the
     * is referred to as a "tile".
     * <ul>
     *     <li>The tiles collected must cover the most longitudinal distance per pixel
     *     (LonDPP) possible, while still covering less than or equal to the amount of
     *     longitudinal distance per pixel in the query box for the user viewport size. </li>
     *     <li>Contains all tiles that intersect the query bounding box that fulfill the
     *     above condition.</li>
     *     <li>The tiles must be arranged in-order to reconstruct the full image.</li>
     * </ul>s
     *
     * @param params Map of the HTTP GET request's query parameters - the query box and
     *               the user viewport width and height.
     * @return A map of results for the front end as specified: <br>
     * "render_grid"   : String[][], the files to display. <br>
     * "raster_ul_lon" : Number, the bounding upper left longitude of the rastered image. <br>
     * "raster_ul_lat" : Number, the bounding upper left latitude of the rastered image. <br>
     * "raster_lr_lon" : Number, the bounding lower right longitude of the rastered image. <br>
     * "raster_lr_lat" : Number, the bounding lower right latitude of the rastered image. <br>
     * "depth"         : Number, the depth of the nodes of the rastered image <br>
     * "query_success" : Boolean, whether the query was able to successfully complete; don't
     * forget to set this to true on success! <br>
     */
    public Map<String, Object> getMapRaster(Map<String, Double> params) {
        //tell front end the query is nonsense.
        if (!isValidQueryBox(params)) {
            return result;
        }
        int depth = getDepth(params);
        Coordinate topLeftCoordinate = upperLeft(params, depth);
        Coordinate bottomRightCoordinate = lowerRight(params, depth);
        String[][] fileNames = getFileNames(topLeftCoordinate, bottomRightCoordinate, depth);
        Map<String, Object> res = new HashMap<>();
        res.put("render_grid", fileNames);
        res.put("raster_ul_lon", topLeftCoordinate.getLon());
        res.put("raster_ul_lat", topLeftCoordinate.getLat());
        res.put("raster_lr_lon", bottomRightCoordinate.getLon());
        res.put("raster_lr_lat", bottomRightCoordinate.getLat());
        res.put("depth", depth);
        res.put("query_success", true);
        return res;
    }

    /**
     * Return a list of images in the query box
     */
    private String[][] getFileNames(Coordinate upperLeft, Coordinate lowerRight, int depth) {
        int column = lowerRightColumn - upperLeftColumn + 1;
        int row = lowerRightRow - upperLeftRow + 1;
        String[][] res = new String[row][column];
        for (int r = 0; r < row; r++) {
            for (int c = 0; c < column; c++) {
                res[r][c] = "d" + depth + "_" + "x" + (upperLeftColumn + c) + "_" + "y" + (upperLeftRow + r) + ".png";
            }
        }
        return res;
    }


    /**
     * return latitude gap: gap measured at latitude between each adjacent file.
     */
    private double latGap(int depth) {
        return (upperLeftRoot.getLat() - lowerRightRoot.getLat()) / Math.pow(2, depth);
    }

    /**
     * return longitude gap: gap measured at longitude between each adjacent file.
     */
    private double lonGap(int depth) {
        return (lowerRightRoot.getLon() - upperLeftRoot.getLon()) / Math.pow(2, depth);
    }

    /**
     * return adjusted upper left coordinate.
     */
    private Coordinate upperLeft(Map<String, Double> params, int depth) {
        Coordinate ul = new Coordinate(params.get("ullat"), params.get("ullon"));
        return new Coordinate(latUpLt(ul.getLat(), depth), lonUpLt(ul.getLon(), depth));
    }

    /**
     * return adjusted lower right coordinate.
     */
    private Coordinate lowerRight(Map<String, Double> params, int depth) {
        Coordinate lr = new Coordinate(params.get("lrlat"), params.get("lrlon"));
        return new Coordinate(latLowRt(lr.getLat(), depth), lonLowRt(lr.getLon(), depth));
    }

    /**
     * return adjusted upper left latitude.
     */
    private double latUpLt(double latul, int depth) {
        upperLeftRow = 0;
        double latGap = latGap(depth);
        double compare = upperLeftRoot.getLat() - latGap;
        if (latul > upperLeftRoot.getLat()) {
            return upperLeftRoot.getLat();
        }
        while (compare > latul) {
            upperLeftRow++;
            compare -= latGap;
        }
        return compare + latGap;
    }

    /**
     * return adjusted upper left longitude.
     */
    private double lonUpLt(double lonul, int depth) {
        upperLeftColumn = 0;
        double lonGap = lonGap(depth);
        double compare = upperLeftRoot.getLon() + lonGap;
        if (lonul < upperLeftRoot.getLon()) {
            return upperLeftRoot.getLon();
        }
        while (compare < lonul) {
            upperLeftColumn++;
            compare += lonGap;
        }
        return compare - lonGap;
    }

    /**
     * return adjusted lower right latitude.
     */
    private double latLowRt(double latlr, int depth) {
        lowerRightRow = (int) (Math.pow(2, depth)) - 1;
        double latGap = latGap(depth);
        double compare = lowerRightRoot.getLat() + latGap;
        if (latlr < lowerRightRoot.getLat()) {
            return lowerRightRoot.getLat();
        }
        while (compare < latlr) {
            lowerRightRow--;
            compare += latGap;
        }
        return compare - latGap;
    }

    /**
     * return adjusted lower right longitude.
     */
    private double lonLowRt(double lonlr, int depth) {
        lowerRightColumn = (int) (Math.pow(2, depth)) - 1;
        double lonGap = lonGap(depth);
        double compare = lowerRightRoot.getLon() - lonGap;
        if (lonlr > lowerRightRoot.getLon()) {
            return lowerRightRoot.getLon();
        }
        while (compare > lonlr) {
            lowerRightColumn--;
            compare -= lonGap;
        }
        return compare + lonGap;
    }

    /** Return a number represent which depth of files should use. */
    private int getDepth(Map<String, Double> params) {
        double lonDPP = (params.get("lrlon") - params.get("ullon")) / params.get("w");
        int depth = 0;
        //current lonDPP starts at depth 0;
        double rootLonDPP = (lowerRightRoot.getLon() - upperLeftRoot.getLon()) / 256;
        double currentLonDPP = rootLonDPP;
        while (true) {
            if (depth == 7 || currentLonDPP <= lonDPP) {
                break;
            }
            depth++;
            currentLonDPP = rootLonDPP / Math.pow(2, depth);
        }
        return depth;
    }

    /* return false if query box is out of range. */
    private boolean isValidQueryBox(Map<String, Double> params) {
        if (params.get("lrlon") < params.get("ullon") ||
                params.get("lrlat") > params.get("ullat")) {
            return false;
        }
        if (params.get("lrlon") < upperLeftRoot.getLon() ||
                params.get("lrlat") > upperLeftRoot.getLat() ||
                params.get("ullon") > lowerRightRoot.getLon() ||
                params.get("ullat") < lowerRightRoot.getLat()) {
            return false;
        }
        return true;
    }

}
