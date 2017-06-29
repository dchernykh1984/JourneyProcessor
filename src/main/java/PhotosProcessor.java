/*import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifSubIFDDirectory;
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
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
  */
/**
 * Created by Denis on 6/28/2017.
 */
public class PhotosProcessor {

/*    public static Calendar withoutSeconds(Calendar calendar) {
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

    }*/

}
