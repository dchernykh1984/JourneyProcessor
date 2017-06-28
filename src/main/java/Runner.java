import Track.PLTFile;
import Track.TrackPoint;
import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifSubIFDDirectory;
import downloader.CacheDownloader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.Locale;

public class Runner {

    public static int getUnsignedInt(int x) {
        int miniMumUnsigned = 0x800000;
        if(x < miniMumUnsigned)
            return x;
        else
            return x - 0x1000000;
    }

    public static void paintTracks(int color_custom, String trackFileName, String tilesFolder, boolean clearTracksRequired, int deep, String coordinates, double distanceMeters) throws Exception {

        PLTFile tracks = new PLTFile("F:\\distribs\\!navigation\\SAS.Planet.Release.121010\\cache\\!!!!!tracks\\" + trackFileName, "d:\\southOut4.plt");
        tracks.openInputFile();
        tracks.readFile(new TrackPoint(coordinates), distanceMeters,10.0, false, 3000.0);
        tracks.closeInputFile();
        tracks.proectToImage("F:\\distribs\\!navigation\\SAS.Planet.Release.121010\\cache\\" + tilesFolder, 1, deep, 50, 2, color_custom, "!!!" + trackFileName + " to " + tilesFolder + "!!!", clearTracksRequired);
    }

    public static Calendar withoutSeconds(Calendar calendar) {
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar;
    }

    public static void rename(String source, String destination) throws ImageProcessingException, IOException {
        File sourceFolder = new File(source);
        File outputFolder = new File(destination);
        if(!outputFolder.exists()) {
            outputFolder.mkdirs();
        }
        for(File inputPhoto:sourceFolder.listFiles()){
            if(inputPhoto.isDirectory()) {
                continue;
            }
            String[] camerasArray = {"Canon EOS 1100D"};
            Metadata metadata = ImageMetadataReader.readMetadata(inputPhoto);
            Date date = new Date();
            boolean dateFound = false;
            for (Directory directory : metadata.getDirectories()) {
                if(directory.containsTag(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL)) {
                    date = directory.getDate(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL);
                    dateFound = true;
                    break;
                }
            }
            if(!dateFound) {
                throw new RuntimeException("Date notfound");
            }
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss", Locale.ENGLISH);
            date.setTime(date.getTime() - 1000*(60*(60*(1+3+1+1)+26)));
            String fileName = formatter.format(date);
            File outputFile = new File(inputPhoto.getParentFile().getPath().replace(source, destination) + "\\" + fileName + ".jpg");
            try {
                if(outputFile.exists()) {
                    throw new RuntimeException(outputFile.getPath() + " - output file already exists for input: " + inputPhoto.getPath());
                }
                Files.copy(inputPhoto.toPath(),outputFile.toPath());
//                inputPhoto.delete();
            } catch (IOException e) {
                System.out.println("Something went wrong");
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }

        }

    }

    public static void copyPhotos(String rootFolder) throws ImageProcessingException, IOException {
        File allPhotos = new File(rootFolder);
        for(File eachDayPhotos:allPhotos.listFiles()){
            if(eachDayPhotos.getName().equals("!best")) {
                continue;
            }
            if(!eachDayPhotos.isDirectory()) {
                continue;
            }

            String[] camerasArray = {"1100", "450"};
            for(String camera:camerasArray) {
                File jpegsFolder = new File(String.format("%s\\%s\\best", eachDayPhotos.getPath(), camera));
                if(!jpegsFolder.exists()) {
                    System.out.println("FOLDER NOT FOUND: " + jpegsFolder.getPath());
                    continue;
                }
                for(File eachBestPhoto:jpegsFolder.listFiles()) {
                    File bestRawPhoto = new File(eachBestPhoto.getPath().replace("\\best\\",  "\\raw\\").replace(".jpg", ".cr2"));
                    Metadata metadata = ImageMetadataReader.readMetadata(bestRawPhoto);
                    Date date = new Date();
                    boolean dateFound = false;
                    for (Directory directory : metadata.getDirectories()) {
                        if(directory.containsTag(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL)) {
                            date = directory.getDate(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL);
                            dateFound = true;
                            break;
                        }
                    }
                    if(!dateFound) {
                        throw new RuntimeException("Date notfound");
                    }
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss", Locale.ENGLISH);
                    if(camera == "450") {
                        date.setTime(date.getTime() - 1000*(60*3+22));
                    }
                    date.setTime(date.getTime() - 1000*(60*(60*(1+3)+26)));
                    String finalFileName = formatter.format(date);
                    File finalRawFile = new File(String.format("%s\\!best\\raw\\%s.cr2", rootFolder, finalFileName));
                    for(int i = 1;i<100;i++) {
                        if(!finalRawFile.exists()) {
                            break;
                        }
//                        System.out.println("File exists " + finalRawFile.getPath());
                        finalRawFile = new File(String.format("%s\\!best\\raw\\%s_%d.cr2", rootFolder, finalFileName, i));
                    }

                    try {
                        Files.copy(bestRawPhoto.toPath(),finalRawFile.toPath());
                    } catch (IOException e) {
                        System.out.println("Something went wrong");
                        e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    }
                }

            }
            System.out.println(eachDayPhotos.getPath());
        }

    }

    public static void main(String[] args) throws ImageProcessingException, IOException {
//        rename("F:\\pictures\\walls\\not_done\\correct_time", "F:\\pictures\\walls\\done");
//        copyPhotos("F:\\pictures\\photos\\2016-09-17_wedding_ant");
        try {
            //KRASNODAR
//done            for(int i = 1;i<=3;i++) { paintTracks(getUnsignedInt(0xFFFFFF), String.format("walk%d.plt", i), "googletraf",  false, 18, "44.0,41.0,1,0.0,0.0,,", 500000.0); }
//done            for(int i = 10;i<=16;i++) { paintTracks(getUnsignedInt(0xFF0000), String.format("GpsiesTrack%d.plt", i), "googletraf",  false, 18, "44.0,41.0,1,0.0,0.0,,", 500000.0); }
//done            for(int i = 1;i<=3;i++) { paintTracks(getUnsignedInt(0xFF0000), String.format("walk%d.plt", i), "walk",  false, 18, "44.0,41.0,1,0.0,0.0,,", 500000.0); }
//done            for(int i = 16;i<=16;i++) { paintTracks(getUnsignedInt(0xFF0000), String.format("GpsiesTrack%d.plt", i), "bike",  false, 18, "44.0,41.0,1,0.0,0.0,,", 500000.0); }
//done            new PLTFile().clearEmptyFiles("F:\\distribs\\!navigation\\SAS.Planet.Release.121010\\cache\\Both", 200, 0);
//done            new PLTFile().clearConverted("F:\\distribs\\!navigation\\SAS.Planet.Release.121010\\cache\\Both", "F:\\distribs\\!navigation\\SAS.Planet.Release.121010\\cache\\Both_done", 0);
//done            new PLTFile().copyConverted("F:\\distribs\\!navigation\\SAS.Planet.Release.121010\\cache\\Both_done", "F:\\distribs\\!navigation\\SAS.Planet.Release.121010\\cache\\Both_bike", 0);
//done            new PLTFile().copyConverted("F:\\distribs\\!navigation\\SAS.Planet.Release.121010\\cache\\Both_done", "F:\\distribs\\!navigation\\SAS.Planet.Release.121010\\cache\\Both_walk", 0);
//done            new PLTFile().copyConverted("F:\\distribs\\!navigation\\SAS.Planet.Release.121010\\cache\\Both_done", "F:\\distribs\\!navigation\\SAS.Planet.Release.121010\\cache\\Both_walk_bike", 0);
//done            for(int i = 6;i<=16;i++) { paintTracks(getUnsignedInt(0xFF0000), String.format("GpsiesTrack%d.plt", i), "Both_bike",  false, 18, "44.0,41.0,1,0.0,0.0,,", 500000.0); }
//done            for(int i = 1;i<=3;i++) { paintTracks(getUnsignedInt(0xFFFFFF), String.format("walk%d.plt", i), "Both_walk_bike",  false, 18, "44.0,41.0,1,0.0,0.0,,", 500000.0); }
//done            for(int i = 16;i<=16;i++) { paintTracks(getUnsignedInt(0xFF0000), String.format("GpsiesTrack%d.plt", i), "Both_walk_bike",  false, 18, "44.0,41.0,1,0.0,0.0,,", 500000.0); }
//done            for(int i = 1;i<=3;i++) { paintTracks(getUnsignedInt(0xFF0000), String.format("walk%d.plt", i), "Both_walk",  false, 18, "44.0,41.0,1,0.0,0.0,,", 500000.0); }




//            paintTracks(getUnsignedInt(0xFF0000), "diveevo.plt", "googletraf",  false, 18, "55.0,43.0,1,0.0,0.0,,", 500000.0);
//            new PLTFile().clearEmptyFiles("F:\\distribs\\!navigation\\SAS.Planet.Release.121010\\cache\\delete", 5000000, 0);
//done            new PLTFile().clearEmptyFiles("F:\\distribs\\!navigation\\SAS.Planet.Release.121010\\cache\\Both", 200, 0);
//done            new PLTFile().clearConverted("F:\\distribs\\!navigation\\SAS.Planet.Release.121010\\cache\\Both", "F:\\distribs\\!navigation\\SAS.Planet.Release.121010\\cache\\Both_done", 0);
//done            new PLTFile().copyConverted("F:\\distribs\\!navigation\\SAS.Planet.Release.121010\\cache\\Both_done", "F:\\distribs\\!navigation\\SAS.Planet.Release.121010\\cache\\Both_bike", 0);
//done            new PLTFile().copyConverted("F:\\distribs\\!navigation\\SAS.Planet.Release.121010\\cache\\Both", "F:\\distribs\\!navigation\\SAS.Planet.Release.121010\\cache\\Both_walk", 0);
//done            new PLTFile().copyConverted("F:\\distribs\\!navigation\\SAS.Planet.Release.121010\\cache\\Both", "F:\\distribs\\!navigation\\SAS.Planet.Release.121010\\cache\\Both_walk_bike", 0);
//done            new PLTFile().clearEmptyFiles("F:\\distribs\\!navigation\\SAS.Planet.Release.121010\\cache\\yahyb", 350, 0);


            /*BIKE AND WALK BOTH*/
//done            for(int i = 1;i<=7;i++) { paintTracks(getUnsignedInt(0xFFFFFF), String.format("Georgia_walk%d.plt", i), "googletraf",  false, 18, "42.0,44.0,1,0.0,0.0,,", 500000.0); }
//done            for(int i = 1;i<=23;i++) { paintTracks(getUnsignedInt(0xFF0000), String.format("Georgia_more%d.plt", i), "googletraf",  false, 18, "42.0,44.0,1,0.0,0.0,,", 500000.0); }
//done            for(int i = 1;i<=21;i++) { paintTracks(getUnsignedInt(0xFF0000), String.format("Georgia%d.plt", i), "googletraf",  false, 18, "42.0,44.0,1,0.0,0.0,,", 500000.0); }
//done            for(int i = 1;i<=7;i++) { paintTracks(getUnsignedInt(0xFFFFFF), String.format("Georgia_walk%d.plt", i), "Both_walk_bike",  false, 18, "42.0,44.0,1,0.0,0.0,,", 500000.0); }
//done            for(int i = 1;i<=23;i++) { paintTracks(getUnsignedInt(0xFF0000), String.format("Georgia_more%d.plt", i), "Both_walk_bike",  false, 18, "42.0,44.0,1,0.0,0.0,,", 500000.0); }
//done            for(int i = 1;i<=21;i++) { paintTracks(getUnsignedInt(0xFF0000), String.format("Georgia%d.plt", i), "Both_walk_bike",  false, 18, "42.0,44.0,1,0.0,0.0,,", 500000.0); }



            /*BIKE CLEAR*/
//done            for(int i = 1;i<=23;i++) { paintTracks(getUnsignedInt(0xFF0000), String.format("Georgia_more%d.plt", i), "googletraf_bike",  false, 18, "42.0,44.0,1,0.0,0.0,,", 500000.0); }
//done            for(int i = 1;i<=23;i++) { paintTracks(getUnsignedInt(0xFF0000), String.format("Georgia_more%d.plt", i), "Both_bike",  false, 18, "42.0,44.0,1,0.0,0.0,,", 500000.0); }
//done            for(int i = 1;i<=21;i++) { paintTracks(getUnsignedInt(0xFF0000), String.format("Georgia%d.plt", i), "googletraf_bike",  false, 18, "42.0,44.0,1,0.0,0.0,,", 500000.0); }
//done            for(int i = 1;i<=21;i++) { paintTracks(getUnsignedInt(0xFF0000), String.format("Georgia%d.plt", i), "Both_bike",  false, 18, "42.0,44.0,1,0.0,0.0,,", 500000.0); }


            /*WALK CLEAR*/
//done            for(int i = 1;i<=7;i++) { paintTracks(getUnsignedInt(0xFF0000), String.format("Georgia_walk%d.plt", i), "googletraf_walk",  false, 18, "42.0,44.0,1,0.0,0.0,,", 500000.0); }
//done            for(int i = 1;i<=7;i++) { paintTracks(getUnsignedInt(0xFF0000), String.format("Georgia_walk%d.plt", i), "Both_walk",  false, 18, "42.0,44.0,1,0.0,0.0,,", 500000.0); }







            /*April2017 traf*/
//done            for(int i = 1;i<=9;i++) { paintTracks(getUnsignedInt(0xFF0000), String.format("GpsiesTrack0%d.plt", i), "googletraf",  false, 16, "56.0,38.0,1,0.0,0.0,,", 1500000.0); }
//done            for(int i = 71;i<=71;i++) { paintTracks(getUnsignedInt(0xFF0000), String.format("GpsiesTrack%d.plt", i), "googletraf",  false, 16, "56.0,38.0,1,0.0,0.0,,", 1500000.0); }
//done            new PLTFile().clearConverted("F:\\distribs\\!navigation\\SAS.Planet.Release.121010\\cache\\Both", "F:\\distribs\\!navigation\\SAS.Planet.Release.121010\\cache\\Both_done", 0);
//done            new PLTFile().clearEmptyFiles("F:\\distribs\\!navigation\\SAS.Planet.Release.121010\\cache\\Both", 200, 0);
//done            new PLTFile().clearEmptyFiles("F:\\distribs\\!navigation\\SAS.Planet.Release.121010\\cache\\yahyb", 350, 0);
//            for(int i = 1;i<=16;i++) new CacheDownloader().downloadLayer("F:\\distribs\\!navigation\\april2017.hlg", i, "yaSat","F:\\distribs\\!navigation\\SAS.Planet.Release.121010\\cache\\yasat");
//done            for(int i = 1;i<=50;i++) { paintTracks(getUnsignedInt(0xFF0000), String.format("belorussia%d.plt", i), "googletraf",  false, 16, "56.0,38.0,1,0.0,0.0,,", 1500000.0); }
//done            for(int i = 71;i<=73;i++) { paintTracks(getUnsignedInt(0xFF0000), String.format("GpsiesTrack%d.plt", i), "googletraf",  false, 16, "56.0,38.0,1,0.0,0.0,,", 1500000.0); }

//done            new PLTFile().clearEmptyFiles("F:\\distribs\\!navigation\\SAS.Planet.Release.121010\\cache\\Both_bike", 2000000, 0);

//done            new PLTFile().copyConverted("F:\\distribs\\!navigation\\SAS.Planet.Release.121010\\cache\\Both", "F:\\distribs\\!navigation\\SAS.Planet.Release.121010\\cache\\Both_bike", 0);
//done            for(int i = 1;i<=9;i++) { paintTracks(getUnsignedInt(0xFF0000), String.format("GpsiesTrack0%d.plt", i), "Both_bike",  false, 16, "56.0,38.0,1,0.0,0.0,,", 1500000.0); }
//done            for(int i = 59;i<=73;i++) { paintTracks(getUnsignedInt(0xFF0000), String.format("GpsiesTrack%d.plt", i), "Both_bike",  false, 16, "56.0,38.0,1,0.0,0.0,,", 1500000.0); }
//done            for(int i = 1;i<=50;i++) { paintTracks(getUnsignedInt(0xFF0000), String.format("belorussia%d.plt", i), "Both_bike",  false, 16, "56.0,38.0,1,0.0,0.0,,", 1500000.0); }

//            for(int i = 1;i<=3;i++) { paintTracks(getUnsignedInt(0xFF0000), String.format("GpsiesTrack%d.plt", i), "googletraf",  false, 16, "56.0,38.0,1,0.0,0.0,,", 1500000.0); }
//            for(int i = 1;i<=3;i++) { paintTracks(getUnsignedInt(0xFF0000), String.format("GpsiesTrack%d.plt", i), "Both_bike",  false, 16, "56.0,38.0,1,0.0,0.0,,", 1500000.0); }
//            for(int i = 1;i<=16;i++) new CacheDownloader().downloadLayer("F:\\distribs\\!navigation\\april2017.hlg", i, "yaSat","F:\\distribs\\!navigation\\SAS.Planet.Release.121010\\cache\\yasat");

//            for(int i = 1;i<=3;i++) { paintTracks(getUnsignedInt(0xFF0000), String.format("IssykKul%d.plt", i), "googletraf",  false, 16, "42.26,77.11,1,0.000000,0.0000000,,", 1500000.0); }







            /*VELEGOJ traf*/
//done            new PLTFile().clearEmptyFiles("F:\\distribs\\!navigation\\SAS.Planet.Release.121010\\cache\\googletraf", 5000000, 0);
//done            for(int i = 1;i<=31;i++) { paintTracks(getUnsignedInt(0xFFFFFF), String.format("velegoj%d.plt", i), "googletraf",  false, 18, "54.5,37.0,1,0.0,0.0,,", 150000.0); }
//done            paintTracks(getUnsignedInt(0xFF0000), "velegoj_road.plt", "googletraf",  false, 18, "54.5,37.0,1,0.0,0.0,,", 150000.0);
//done            paintTracks(getUnsignedInt(0xFF0000), "velegoj_road.plt", "googletraf_road",  false, 18, "54.5,37.0,1,0.0,0.0,,", 150000.0);
//done            for(int i = 1;i<=31;i++) { paintTracks(getUnsignedInt(0xFF0000), String.format("velegoj%d.plt", i), "googletraf_all",  false, 18, "54.5,37.0,1,0.0,0.0,,", 150000.0); }

            /*VELEGOJ google*/
//done            new PLTFile().clearEmptyFiles("F:\\distribs\\!navigation\\SAS.Planet.Release.121010\\cache\\Both", 200, 0);
//done            new PLTFile().clearConverted("F:\\distribs\\!navigation\\SAS.Planet.Release.121010\\cache\\Both", "F:\\distribs\\!navigation\\SAS.Planet.Release.121010\\cache\\Both_done", 0);
//done            new PLTFile().copyConverted("F:\\distribs\\!navigation\\SAS.Planet.Release.121010\\cache\\Both_done", "F:\\distribs\\!navigation\\SAS.Planet.Release.121010\\cache\\Both_bike", 0);
//done            new PLTFile().copyConverted("F:\\distribs\\!navigation\\SAS.Planet.Release.121010\\cache\\Both_done", "F:\\distribs\\!navigation\\SAS.Planet.Release.121010\\cache\\Both_road", 0);
//done            new PLTFile().copyConverted("F:\\distribs\\!navigation\\SAS.Planet.Release.121010\\cache\\Both_done", "F:\\distribs\\!navigation\\SAS.Planet.Release.121010\\cache\\Both_all", 0);
//done            new PLTFile().clearEmptyFiles("F:\\distribs\\!navigation\\SAS.Planet.Release.121010\\cache\\yahyb", 350, 0);
//done            for(int i = 1;i<=31;i++) { paintTracks(getUnsignedInt(0xFFFFFF), String.format("velegoj%d.plt", i), "Both_bike",  false, 18, "54.5,37.0,1,0.0,0.0,,", 50000.0); }
//done            paintTracks(getUnsignedInt(0xFF0000), "velegoj_road.plt", "Both_bike",  false, 18, "54.5,37.0,1,0.0,0.0,,", 50000.0);
//done            paintTracks(getUnsignedInt(0xFF0000), "velegoj_road.plt", "Both_road",  false, 18, "54.5,37.0,1,0.0,0.0,,", 50000.0);
//done            for(int i = 1;i<=31;i++) { paintTracks(getUnsignedInt(0xFF0000), String.format("velegoj%d.plt", i), "Both_all",  false, 18, "54.5,37.0,1,0.0,0.0,,", 50000.0); }

        } catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("Done!");
	}
}
