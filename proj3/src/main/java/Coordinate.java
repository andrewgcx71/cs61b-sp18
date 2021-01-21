/** Coordinate has latitude and longitude. */

public class Coordinate {
    private double latitude;
    private double longitude;

    public Coordinate(double lat, double lon) {
        this.latitude = lat;
        this.longitude = lon;
    }

    public double getLat() {
        return latitude;
    }

    public double getLon() {
        return longitude;
    }

    @Override
    public String toString() {
        return "Latitude: " + latitude + ", " +"Longitude: " + longitude;
    }
}
