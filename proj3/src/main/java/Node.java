

// no need this helper class for now, if needed, will come back.
public class Node {


    //use wrapper class for Long, because we need to use the overrided hashCode() method in Long class
    private Long id;
    private double lat;
    private double lon;

    public Node(Long id, double lat, double lon) {
        this.id = id;
        this.lat = lat;
        this.lon = lon;
    }

    public Long getId() {
        return id;
    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }
}
