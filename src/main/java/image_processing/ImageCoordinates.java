package image_processing;

import Track.TrackPoint;

/**
 * Created by Denis on 6/29/2017.
 */
public class ImageCoordinates {
    private double xDouble;
    private double yDouble;
    private long x;
    private long y;
    private long depth;
    public String toString() {
        return "x" + x + "y" + y + "z" + depth;
    }
    private ImageCoordinates(double xDouble, double yDouble, long depth) {
        this.x = getIntegerPart(xDouble);
        this.y = getIntegerPart(yDouble);
        this.xDouble = xDouble;
        this.yDouble = yDouble;
        this.depth = depth;
    }

    private static double getImageX(double longitude, long depth) {
        return Math.pow(2.0, depth-1.0) * (longitude + 180.0)/360.0;
    }
    public static double getImageY(double latitude, long depth) {
        double tempLat = latitude;
        double tempVal = Math.log(Math.tan(Math.toRadians(tempLat))
                +1.0/Math.cos(Math.toRadians(tempLat)));
        return Math.pow(2.0, depth-1.0)*((1.0-tempVal/Math.PI)/2.0); //for google, yahoo etc
//		return Math.pow(2.0, depth-1.0)*(0.997813403*(1.0-tempVal/PI)/2.0+0.001565095862); //for yandex
    }
    public ImageCoordinates (TrackPoint point, long depth) {
//		images: 0 to 2^(depth-1)-1
//		coordinates: latitude -180 to 180, longitude -90 90
        this(getImageX(point.getLongitude(), depth),
                getImageY(point.getLatitude(), depth),
                depth);
    }
    public double getFractionalX() {
        return xDouble - x;
    }


    public double getFractionalY() {
        return yDouble - y;
    }

    private static long getIntegerPart(double value) {
        return (long)value;
    }
    private static double getFractionalPart(double value) {
        return value - (double)getIntegerPart(value);
    }
    public String getPathByCoordinates(String rootPath) {
        long xFolder = x/1024;
        long yFolder = y/1024;
        return rootPath + "\\z" + depth + "\\" + xFolder + "\\x" + x + "\\" + yFolder + "\\y" + y + ".png";
        //       return rootPath + "\\z" + depth + "\\" + xFolder + "\\x" + imgC.x + "\\" + yFolder + "\\y" + imgC.y + ".gif";
    }


}
