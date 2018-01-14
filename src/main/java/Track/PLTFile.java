package Track;

import Archievers.QualityDecreaser;
import Archievers.TrackProector;
import image_processing.ImageCoordinates;
import image_processing.ImagesGetter;
import image_processing.SegmentsQueue;
import image_processing.WorkerProjector;

import java.io.*;
import java.nio.file.Files;
import java.text.ParseException;
import java.util.LinkedList;

public class PLTFile {
    static int LOGGER_QUALITY = 10000;
    long numberOfFiles = 0;
    boolean needToShowNumberOfFiles = false;
    long numberOfDeletedFiles = 0;
    long numberOfCopiedFiles = 0;
    long numberOfMovedFiles = 0;
    private String inputFileName;
    private String outputFileName;
    private BufferedWriter outputFile;
    private BufferedReader inputFile;
    private LinkedList<Track> listOfTracks;
    private String fileTypeAndVersion;
    private String geodaticDatum;
    private String altitudeInFeet;
    private String reservedString;

    private Integer alwaysZero;
    private Integer widthOfTrackLine;
    private Integer rgb;
    private String trackDescription;
    private Integer trackSkipValue;
    private Integer trackType;
    private Integer trackFillStyle;
    private Integer trackFillColor;

    private long numberOfTrackPoints;

    private String deliminer;
    private boolean makeBreak;

    public PLTFile() {
        makeBreak = true;
        listOfTracks = new LinkedList<Track>();
        deliminer = ",";
    }
    public PLTFile(String inputFileName, String outputFileName) {
        this();
        this.inputFileName = inputFileName;
        this.outputFileName = outputFileName;
    }
    public PLTFile(PLTFile inputFile) {
        this();
        inputFileName = inputFile.inputFileName;
        outputFileName = inputFile.outputFileName;
        fileTypeAndVersion = inputFile.fileTypeAndVersion;
        geodaticDatum = inputFile.geodaticDatum;
        altitudeInFeet = inputFile.altitudeInFeet;
        reservedString = inputFile.reservedString;

        alwaysZero = inputFile.alwaysZero;
        widthOfTrackLine = inputFile.widthOfTrackLine;
        rgb = inputFile.rgb;
        trackDescription = inputFile.trackDescription;
        trackSkipValue = inputFile.trackSkipValue;
        trackType = inputFile.trackType;
        trackFillStyle = inputFile.trackFillStyle;
        trackFillColor = inputFile.trackFillColor;

        deliminer = inputFile.deliminer;
        makeBreak = inputFile.makeBreak;

    }

    /********************************Input File Operations*****************/
    public void openInputFile() throws FileNotFoundException {
        inputFile = new BufferedReader(new FileReader(inputFileName));
    }
    public void readFile(TrackPoint center, double distance, double acceptableDeviation, boolean simplify, double maxEdgeLength) throws Exception {
        readHeader();
        readData(center, distance, acceptableDeviation, simplify, maxEdgeLength);
        System.out.println("Reading data from file finished");
    }
    public void readHeader() throws Exception {
        fileTypeAndVersion = inputFile.readLine();
        geodaticDatum = inputFile.readLine();
        altitudeInFeet = inputFile.readLine();
        reservedString = inputFile.readLine();
        String[] dataString = inputFile.readLine().split(deliminer);
        if(!dataString[0].trim().equalsIgnoreCase("0")) {
            throw new Exception("First value in data string is not zero:" + dataString[0]);
        }
        alwaysZero = 0;
        widthOfTrackLine = Integer.parseInt(dataString[1].trim());
        rgb = Integer.parseInt(dataString[2].trim());
        trackDescription = dataString[3].trim();
        trackSkipValue = Integer.parseInt(dataString[4].trim());
        trackType = Integer.parseInt(dataString[5].trim());
        trackFillStyle = Integer.parseInt(dataString[6].trim());
        trackFillColor = Integer.parseInt(dataString[7].trim());

        inputFile.readLine();
        numberOfTrackPoints = 0;
    }

    public void readData(TrackPoint center, double distance, double acceptableDeviation, boolean simplify, double maxEdgeLength) throws Exception {
        String nextString;
        numberOfTrackPoints = 0;
        double pointsProcessed = 0;
        boolean startNewTrack = true;
        while((nextString = inputFile.readLine())!=null) {
            pointsProcessed++;
            TrackPoint nextPoint = new TrackPoint(nextString);
            if(listOfTracks != null && listOfTracks.size() != 0 &&
                    listOfTracks.getLast().pointsList != null && listOfTracks.getLast().pointsList.size() != 0 &&
                    nextPoint.distTo(listOfTracks.getLast().
                            pointsList.getLast())>maxEdgeLength) {
                startNewTrack = true;
            }
            if(center.distTo(nextPoint) > distance) {
                startNewTrack = true;
            } else if(startNewTrack || nextPoint.getBreakTrackLine()) {
 //               System.out.print("Track number: " + listOfTracks.size() + " points number: " + numberOfTrackPoints + " Points processed:" + pointsProcessed + " of file " + inputFileName + "\n");
                if(simplify && listOfTracks.size()>0) {
                    new QualityDecreaser(listOfTracks.getLast(), acceptableDeviation, "DQ" + listOfTracks.size()).start();
                }
                nextPoint.setBreakTrackLine(true);
                listOfTracks.add(new Track(nextPoint));
                startNewTrack = false;
                numberOfTrackPoints++;
            } else {
//                if(pointsProcessed > 108038)  {
//                    System.out.print("Track number: " + listOfTracks.size() + " points number: " + numberOfTrackPoints + " Points processed:" + pointsProcessed + " of file " + inputFileName + "\n");
//                }
//                System.out.print("Track number: " + listOfTracks.size() + " points number: " + numberOfTrackPoints + " Points processed:" + pointsProcessed + "\n");
                listOfTracks.getLast().addPoint(nextPoint);
                numberOfTrackPoints++;
            }
        }
        System.out.println(String.format("Number of track points is %d", numberOfTrackPoints));
    }
    public void closeInputFile() throws IOException {
        inputFile.close();
    }
    /************************************* Directory prepare operations **********************/

    public void clearDuplicates(String rootPath, String toPath, String extension) throws Exception {
        File currentDirectory = new File(rootPath);
        if(!currentDirectory.exists()) {
            return;
        }
        File toDirectory = new File(toPath);
        if(!toDirectory.exists()) {
            toDirectory.mkdirs();
        }
        for(File currentFile:currentDirectory.listFiles()) {
            if(currentFile.isDirectory()) {
                clearDuplicates(currentFile.getPath(), toPath + currentFile.getPath().replace(rootPath, ""), extension);
            } else if(currentFile.isFile()) {
                numberOfFiles++;
                if(currentFile.getName().contains("." + extension)) {
                    for(File isDuplicate:currentDirectory.listFiles()) {
                        if(isDuplicate.getName().contains(currentFile.getName().replace("." + extension, "")) && !isDuplicate.getName().equals(currentFile.getName())) {
                            numberOfDeletedFiles++;
                            currentFile.delete();
                            break;
                        }
                    }
                }
            } else {
                throw new Exception("Invalid type of file '" + currentFile.toString()+"' - not file or directory");
            }

        }
        currentDirectory = new File(rootPath);
        toDirectory = new File(toPath);
        for(File currentFile:currentDirectory.listFiles()) {
            if(currentFile.isFile() && !currentFile.getName().contains("." + extension)) {
                currentFile.renameTo(new File(toDirectory.getPath() + "\\" + currentFile.getName()));
                numberOfMovedFiles++;
            }
        }
        currentDirectory = new File(rootPath);
        if(currentDirectory.listFiles().length == 0) {
            currentDirectory.delete();
//            System.out.println("Directory deleted: " + rootPath);
        }
        System.out.println(currentDirectory.getPath() + ". Files processed(clear duplicates):" + numberOfFiles + ". Files deleted: " + numberOfDeletedFiles + ". Files moved: " + numberOfMovedFiles);

    }

    public void clearConverted(String rootPath, String toPath, int treeLevel) throws Exception {
        File currentDirectory = new File(rootPath);
        if(!currentDirectory.exists()) {
            System.out.println("Path does not exist: " + rootPath);
            return;
        }
        for(File currentFile:currentDirectory.listFiles()) {
            if(currentFile.isDirectory()) {
                clearConverted(currentFile.getPath(), toPath + currentFile.getPath().replace(rootPath, ""), treeLevel + 1);
            } else if(currentFile.isFile()) {
                numberOfFiles++;
                if(numberOfFiles % LOGGER_QUALITY == 0) {
                    needToShowNumberOfFiles = true;
                }
                File toFile = new File(currentFile.getPath().replace(rootPath, toPath));
                if(toFile.exists()) {
                    currentFile.delete();
                    numberOfDeletedFiles++;
                }
            }

        }
        currentDirectory = new File(rootPath);
        if(currentDirectory.listFiles().length == 0) {
            currentDirectory.delete();
//            System.out.println("Directory deleted: " + rootPath);
        }
        if(needToShowNumberOfFiles || treeLevel == 0) {
            needToShowNumberOfFiles = false;
            System.out.println(currentDirectory.getPath() + " of level " + treeLevel + ". Files processed(clear converted):" + numberOfFiles + ". Files deleted: " + numberOfDeletedFiles + ". Filed remained: " + (numberOfFiles - numberOfDeletedFiles));
        }
    }

    public void copyConverted(String rootPath, String toPath, int treeLevel) throws Exception {
        File currentDirectory = new File(rootPath);
        if(!currentDirectory.exists()) {
            System.out.println("Path does not exist: " + rootPath);
            return;
        }
        for(File currentFile:currentDirectory.listFiles()) {
            if(currentFile.isDirectory()) {
                copyConverted(currentFile.getPath(), toPath + currentFile.getPath().replace(rootPath, ""), treeLevel + 1);
            } else if(currentFile.isFile()) {
                numberOfFiles++;
                if(numberOfFiles % LOGGER_QUALITY == 0) {
                    needToShowNumberOfFiles = true;
                }
                File toFile = new File(currentFile.getPath().replace(rootPath, toPath));
                if(!toFile.exists()) {
                    if(!toFile.getParentFile().exists()) {
                        toFile.getParentFile().mkdirs();
                    }
                    Files.copy(currentFile.toPath(), toFile.toPath());
                    numberOfCopiedFiles++;
                }
            }

        }
        currentDirectory = new File(rootPath);
        if(needToShowNumberOfFiles || treeLevel < 2) {
            needToShowNumberOfFiles = false;
            System.out.println(currentDirectory.getPath() + " of level " + treeLevel + ". Files processed (copy converted):" + numberOfFiles + ". Files copied: " + numberOfCopiedFiles + ". Filed already existed: " + (numberOfFiles - numberOfCopiedFiles));
        }
    }


    static boolean less(double a, double b) {
        return b-a>0;
    }
    static double vectorMulti(double ax, double ay, double bx, double by) {
        return ax*by - bx*ay;
    }
    static boolean pointInsidePolygon(double xCoord, double yCoord, int depth, String ... coordinates) throws ParseException {
        String previousCoord = null;
        int crossesNumber = 0;
        for(String coordinate: coordinates) {
            if(previousCoord == null) {
                previousCoord = coordinates[coordinates.length-1];
            }
            TrackPoint prevousPoint = new TrackPoint(previousCoord);
            TrackPoint currentPoint = new TrackPoint(coordinate);
            double x1 = ImageCoordinates.getImageX(prevousPoint.getLongitude(), depth), y1 = ImageCoordinates.getImageY(prevousPoint.getLatitude(), depth),
                    x2 = ImageCoordinates.getImageX(currentPoint.getLongitude(), depth), y2 = ImageCoordinates.getImageY(currentPoint.getLatitude(), depth),
                    x3 = xCoord, y3 = yCoord, x4 = xCoord, y4 = Math.pow(2, depth);

            double v1 = vectorMulti(x4-x3, y4-y3, x1-x3, y1-y3),
                    v2 = vectorMulti(x4-x3, y4-y3, x2-x3, y2-y3),
                    v3 = vectorMulti(x2-x1, y2-y1, x3-x1, y3-y1),
                    v4 = vectorMulti(x2-x1, y2-y1, x4-x1, y4-y1);
            if(less(v1*v2, 0) && less(v3*v4, 0)) {
                crossesNumber++;
            }
            previousCoord = coordinate;
        }
        return crossesNumber % 2 == 1;
    }
    static int counterRemoved = 0;
    static int counterTotal = 0;

    public static void deleteTiles(String rootPath, int smalestDeep, int biggestDeep, String ... coordinates) throws Exception {
        if(!new File(rootPath).exists()) {
            throw new Exception("Directory not exists: " + rootPath);
        }
        for(int depth = smalestDeep;depth <= biggestDeep; depth++) {
            File layerDirectory = new File(rootPath + "\\z" + depth);
            for(File x1coord:layerDirectory.listFiles()) {
                for(File x2coord: x1coord.listFiles()) {
                    for(File y1coord:x2coord.listFiles()) {
                        for(File y2coord:y1coord.listFiles()) {
                            counterTotal++;
                            double x2 = Integer.parseInt(x2coord.getName().replace("x", ""));
                            double y2 = Integer.parseInt(y2coord.getName().replace("y", "").split("\\.")[0]);
                            if(pointInsidePolygon(x2, y2, depth, coordinates) ||
                                    pointInsidePolygon(x2 + 1, y2, depth, coordinates) ||
                                    pointInsidePolygon(x2 + 1, y2 + 1, depth, coordinates) ||
                                    pointInsidePolygon(x2, y2 + 1, depth, coordinates)) {
//                                System.out.println(x2 + " " + y2);
//                                System.out.println(counter);
                            } else {
                                y2coord.delete();
                                counterRemoved++;
                            }
                            if(counterTotal %1000 == 0) {
                                System.out.println(String.format("Total processed %d, total removed %d, current layer %d, delete percentage %f",  counterTotal, counterRemoved, depth,  counterRemoved * 100.0 / counterTotal));
                            }
                        }
                        if(y1coord.listFiles().length == 0) {
                            y1coord.delete();
                        }
                    }
                    if(x2coord.listFiles().length == 0) {
                        x2coord.delete();
                    }
                }
                if(x1coord.listFiles().length == 0) {
                    x1coord.delete();
                }
            }
        }

    }



    public void clearEmptyFiles(String rootPath, long minimumSize, int treeLevel) throws Exception {
        File currentDirectory = new File(rootPath);
        if(!currentDirectory.exists()) {
            return;
        }
        for(File currentFile:currentDirectory.listFiles()) {
            if(currentFile.isDirectory()) {
                clearEmptyFiles(currentFile.getPath(), minimumSize, treeLevel + 1);
            } else if(currentFile.isFile()) {
                numberOfFiles++;
                if(numberOfFiles % LOGGER_QUALITY == 0) {
                    needToShowNumberOfFiles = true;
//                    System.out.println(currentDirectory.getPath() + ". Files processed (clear empty files):" + numberOfFiles + ". Files deleted: " + numberOfDeletedFiles + ". Deleted percentage: " + (100.0 *numberOfDeletedFiles/numberOfFiles));
                }
                if(currentFile.length() < minimumSize) {
                    numberOfDeletedFiles++;
                    currentFile.delete();
                }
            } else {
                throw new Exception("Invalid type of file '" + currentFile.toString()+"' - not file or directory");
//                System.out.println("Invalid type of file '" + currentFile.toString()+"' - not file or directory");
            }

        }
        currentDirectory = new File(rootPath);
        numberOfFiles++;
        if(currentDirectory.listFiles().length == 0) {
            currentDirectory.delete();
            numberOfDeletedFiles++;
            if(numberOfFiles % LOGGER_QUALITY == 0) {
                needToShowNumberOfFiles = true;
//                System.out.println(currentDirectory.getPath() + ". Files processed (clear empty files):" + numberOfFiles + ". Files deleted: " + numberOfDeletedFiles + ". Deleted percentage: " + (100.0 *numberOfDeletedFiles/numberOfFiles));
            }
        }
        if(needToShowNumberOfFiles || treeLevel == 0) {
            System.out.println(currentDirectory.getPath() + " of level " + treeLevel + ". Files processed(clear empty):" + numberOfFiles + ". Files deleted: " + numberOfDeletedFiles + ". Deleted percentage: " + (100.0 *numberOfDeletedFiles/numberOfFiles) + ". Files remained: "  + (numberOfFiles - numberOfDeletedFiles));
            needToShowNumberOfFiles = false;
        }
    }
    /*************************************Output File Operations  **********************/

    public void proectToImage(String rootPath, long minDepth, long maxDepth, int numberOfThreads, int tracksColor, boolean clearDirRequired) throws Exception {
        if(clearDirRequired) {
            clearEmptyFiles(rootPath,200, 0);
        }
        if(numberOfThreads < 1) {
            throw new Exception("Number Of processors should be more than 1");
        }
        SegmentsQueue segmentsQueue = new SegmentsQueue(100, numberOfThreads);
        ImagesGetter imgsCache = new ImagesGetter(rootPath);
        WorkerProjector[] projectors = new WorkerProjector[numberOfThreads];
        for(int i = 0;i<numberOfThreads;i++) {
            projectors[i] = new WorkerProjector(segmentsQueue, tracksColor, imgsCache, "ID=" + i);
        }
        long totalSegmentsCounter = 0;
        for(Track track: listOfTracks) {
            totalSegmentsCounter += track.size();
            }
        totalSegmentsCounter*=(maxDepth-minDepth+1);
        System.out.println("Total segments number is: " + totalSegmentsCounter*(maxDepth-minDepth+1));
        long segmentsCounter = 0;
		double startTime = (long) (System.nanoTime() /Math.pow(10, 9));

        for(Track track: listOfTracks) {
            for(long depth = minDepth;depth <= maxDepth;depth++) {
                for(int segmentCounter = 0;segmentCounter < track.size()-1;segmentCounter++) {
                    segmentsQueue.putSegment(track.getSegment(segmentCounter), depth);
                    segmentsCounter++;
                    double currentTime = (long) (System.nanoTime() /Math.pow(10, 9));
                    double totalTime = (currentTime - startTime)* totalSegmentsCounter/segmentsCounter;
                    double etaTime = totalTime - currentTime + startTime;
                    if(segmentsCounter % 10000L == 0) {
                        System.out.println(String.format("%f percents done. ETA %f minutes. Total estimation %f minutes", (100.0*segmentsCounter)/totalSegmentsCounter, etaTime/60.0, totalTime/60.0));
                    }
                }
            }
        }
        System.out.println("Track segments queue is done. Waiting for processing of segments. Total number of segments: " + totalSegmentsCounter);
        while(segmentsQueue.getLength() > 0) {
            Thread.sleep(100);
        }
        System.out.println("Track segments queue is Processed. Finishing.");
        for(int i = 0;i<numberOfThreads;i++) {
            projectors[i].stopWorker();
        }
        for(int i = 0;i<numberOfThreads;i++) {
            projectors[i].getT().join();
        }
    }
    public void openOutputFile() throws IOException {
        outputFile = new BufferedWriter(new FileWriter(outputFileName));
    }

    public void writeFile() throws IOException {
        writeHeader();
        writeData();
    }
    private void writeLine(String line) throws IOException {
        outputFile.write(line + "\n");
    }
    public void writeHeader() throws IOException {
        numberOfTrackPoints = 0;
        for(Track track:listOfTracks) {
            numberOfTrackPoints+=track.getPointsList().size();
        }
        writeLine(fileTypeAndVersion);
        writeLine(geodaticDatum);
        writeLine(altitudeInFeet);
        writeLine(reservedString);
        writeLine(alwaysZero.toString() + deliminer + widthOfTrackLine.toString() + deliminer +
                rgb.toString() + deliminer + trackDescription + deliminer + trackSkipValue.toString() +
                deliminer + trackType.toString() + deliminer + trackFillStyle.toString() +
                deliminer + trackFillColor.toString());
        writeLine(new Long(numberOfTrackPoints).toString());
    }
    public void writeData() throws IOException {
        numberOfTrackPoints = 0;
        for(Track track: listOfTracks) {
            numberOfTrackPoints += track.getPointsList().size();
        }
        System.out.print("Number of points: " + numberOfTrackPoints + "\n");
        for(Track track:listOfTracks) {
            if(track.getPointsList().size() > 1) {
                for(TrackPoint point:track.getPointsList()) {
                    writeLine(point.toString());
                }
            }
        }
        System.out.print("Wrighting finished!\n");
    }
    public void addTrack(Track track) {
        listOfTracks.add(track);
    }
    public void closeOutputFile() throws IOException {
        outputFile.close();
    }
    public int size() {
        return listOfTracks.size();
    }
    public Track getTrack(int i) {
        return listOfTracks.get(i);
    }
    public LinkedList<Track> getListOfTracks() {
        return listOfTracks;
    }
    public void setListOfTracks(LinkedList<Track> listOfTracks) {
        this.listOfTracks = listOfTracks;
    }

    public void proectPoints(double distMin) {
        int counter = 0;
        for(int i = 0;i<size();i++) {
            for(int j = size()-1;j>i;j--) {
                TrackProector tpro = new TrackProector(getTrack(j), getTrack(i), distMin);
                tpro.start();
            }
            while(Thread.activeCount()>1) {
            }
            counter++;
            System.out.print("Track processed: " + counter + "\n");
        }
    }

}
