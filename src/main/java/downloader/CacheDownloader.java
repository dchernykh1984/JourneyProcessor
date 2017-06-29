package downloader;

import Track.TrackPoint;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.LinkedList;

/**
 * Created by Denis on 3/10/2017.
 */
public class CacheDownloader {
    String POINT_NAME = "Point";
    private LinkedList<TrackPoint> areaPoints;

    public int getPointNumber(String pointString) {
        return Integer.parseInt(pointString.split("_")[1].split("=")[0]);
    }

    public double getPointValue(String pointString) {
        return Double.parseDouble(pointString.split("=")[1]);
    }

    public boolean isLatitude(String pointString) {
        String val = pointString.replace(POINT_NAME, "").split("_")[0];
        if(val.equals("Lon")) return false;
        if(val.equals("Lat")) return true;
        throw new RuntimeException("Not Lon or Lat in string: " + pointString);
    }

    public void readAreaFile(String areaFilePath) throws IOException, ParseException {
        areaPoints = new LinkedList<TrackPoint>();
        BufferedReader inputFile = new BufferedReader(new FileReader(areaFilePath));
        String currentLineFirst = inputFile.readLine();
        while(!currentLineFirst.startsWith(POINT_NAME) && currentLineFirst != null) {
            currentLineFirst = inputFile.readLine();
        }
        String currentLineSecond;
        while(currentLineFirst != null) {
            currentLineSecond = inputFile.readLine();
            areaPoints.add(new TrackPoint(String.format("%f,%f,%d,0.000000,0.0000000,,",
                    getPointValue(currentLineFirst), getPointValue(currentLineSecond), getPointNumber(currentLineFirst))));
            currentLineFirst = inputFile.readLine();
        }
    }

    public void calculateTilesList() {
        throw new RuntimeException("Not implemented: calculate list or required tiles");
    }

    public void downloadTiles() {
        throw new RuntimeException("Not implemented. Tiles calculation placed here: F:\\distribs\\!navigation\\SAS.Planet.Release.121010\\Maps\\sas.maps\\Yandex\\YaSat.zmp");
    }

    public void downloadLayer(String areaFilePath, int layerNumber, String layerName, String folderLocation) throws IOException, ParseException {
        readAreaFile(areaFilePath);
        calculateTilesList();
        downloadTiles();
    }


}
