package Track;

import Archievers.QualityDecreaser;
import Archievers.TrackProector;
import image_processing.ImageProcessor;

import java.io.*;
import java.nio.file.Files;
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

    private Integer numberOfTrackPoints;

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
        System.out.println("Quality decreasing finished!");
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

    public void proectToImage(String rootPath, long minDepth, long maxDepth, long maxNumberOfCache, int numberOfThreads, int tracksColor, String layerName, boolean clearDirRequired) throws Exception {
        if(clearDirRequired) {
            clearEmptyFiles(rootPath,200, 0);
        }
        if(numberOfThreads < 1) {
            throw new Exception("Number Of processors should be more than 1");
        }
        for(long depth = maxDepth;depth>=minDepth;depth--) {
            while(Thread.activeCount() > numberOfThreads+1) {
                Thread.sleep(100);
            }
            ImageProcessor imgsC = new ImageProcessor(rootPath, depth, depth, maxNumberOfCache, listOfTracks, tracksColor, layerName);
            imgsC.start();
        }
        while(Thread.activeCount() > 2) {
            Thread.sleep(1000);
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
        writeLine(numberOfTrackPoints.toString());
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
