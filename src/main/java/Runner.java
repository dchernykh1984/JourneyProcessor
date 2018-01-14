import Track.PLTFile;
import Track.TrackPoint;

import java.io.IOException;


public class Runner {

    public static int getUnsignedInt(int x) {
        int miniMumUnsigned = 0x800000;
        if(x < miniMumUnsigned)
            return x;
        else
            return x - 0x1000000;
    }

    public static void paintTracks(int color_custom, String trackFileName, String tilesFolder, boolean clearTracksRequired, int deep, String coordinates, double distanceMeters) throws Exception {
        String fullFileName = "F:\\distribs\\!navigation\\SAS.Planet.Release.121010\\cache\\!!!!!tracks\\" + trackFileName;
        System.out.println("Start painting tracks: " + fullFileName);
        PLTFile tracks = new PLTFile(fullFileName, "d:\\southOut4.plt");
        tracks.openInputFile();
        tracks.readFile(new TrackPoint(coordinates), distanceMeters,10.0, false, 3000.0);
        tracks.closeInputFile();
        tracks.proectToImage("F:\\distribs\\!navigation\\SAS.Planet.Release.121010\\cache\\" + tilesFolder, 5, deep, 5, color_custom/*, "!!!" + trackFileName + " to " + tilesFolder + "!!!"*/, clearTracksRequired);
    }

    public static void main(String[] args) throws IOException {
//        rename("F:\\pictures\\walls\\not_done\\correct_time", "F:\\pictures\\walls\\done");
//        copyPhotos("F:\\pictures\\photos\\2016-09-17_wedding_ant");
        try {
/*            PLTFile.deleteTiles("F:\\distribs\\!navigation\\SAS.Planet.Release.121010\\cache\\yasat",16,17,
                    "55.5,31.0,1,0.0,0.0,,",
                    "57.0,38.0,1,0.0,0.0,,",
                    "57.0,42.0,1,0.0,0.0,,",
                    "55.0,45.0,1,0.0,0.0,,",
                    "53.0,43.0,1,0.0,0.0,,",
                    "53.0,33.0,1,0.0,0.0,,",
                    "54.0,31.0,1,0.0,0.0,,");*/
            paintTracks(getUnsignedInt(0xFF0000), "Kazakh01.plt", "googletraf",  false, 18, "44.0,79.0,1,0.0,0.0,,", 5000000.0);

            //KRASNODAR
//            long startTime = (long) (System.nanoTime() /Math.pow(10, 9));
            new PLTFile().clearEmptyFiles("F:\\distribs\\!navigation\\SAS.Planet.Release.121010\\cache\\yahyb", 1, 0);
/*            new PLTFile().clearEmptyFiles("F:\\distribs\\!navigation\\cyprus\\cache\\WikiMapHyb", 500, 0);
//            new PLTFile().clearEmptyFiles("F:\\distribs\\!navigation\\cyprus\\cache\\yahyb", 400, 0);
//            new PLTFile().clearConverted("F:\\distribs\\!navigation\\cyprus\\cache\\Both", "F:\\distribs\\!navigation\\cyprus\\cache\\both2", 0);
//            new PLTFile().copyConverted("F:\\distribs\\!navigation\\cyprus\\cache\\both2", "F:\\distribs\\!navigation\\cyprus\\cache\\both_road", 0);
//            new PLTFile().copyConverted("F:\\distribs\\!navigation\\cyprus\\cache\\both2", "F:\\distribs\\!navigation\\cyprus\\cache\\both_mtb", 0);
            for(int i = 1;i<=12;i++) {
                paintTracks(getUnsignedInt(0xFF0000), String.format("mtb%d.plt", i), "mtb",  false, 18, "35.0,33.0,1,0.0,0.0,,", 5000000.0);
                paintTracks(getUnsignedInt(0xFF0000), String.format("mtb%d.plt", i), "both_mtb",  false, 18, "35.0,33.0,1,0.0,0.0,,", 5000000.0);
            }
            for(int i = 1;i<=3;i++) {
                paintTracks(getUnsignedInt(0xFF0000), String.format("road%d.plt", i), "both_road",  false, 18, "35.0,33.0,1,0.0,0.0,,", 5000000.0);
//                paintTracks(getUnsignedInt(0xFF0000), String.format("road%d.plt", i), "road",  false, 18, "35.0,33.0,1,0.0,0.0,,", 5000000.0);
            }
            System.out.println("Took seconds: " + ((long) (System.nanoTime() /Math.pow(10, 9)) - startTime));*/
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
