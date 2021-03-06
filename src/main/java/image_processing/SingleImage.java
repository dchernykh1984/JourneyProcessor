package image_processing;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

public class SingleImage {
	BufferedImage thisImage;
	String filePath;
	public SingleImage(String path) {
		filePath = path;
	}
	public boolean fileExists() {
		return new File(filePath).exists();
	}

	public void read() {
		read(filePath);
	}

	public void read(String filePath) {
		try{
		File tmpFile = new File(filePath);
		thisImage = ImageIO.read(tmpFile);
		}catch (Exception e) {
            e.printStackTrace();
			System.out.print(filePath + " - read file failed at SingleImage.java function read()\n");
		}
	}
	public int getPixel(double x, double y) {
		return thisImage.getRGB((int)(x*thisImage.getWidth()), (int)(y*thisImage.getHeight()));
	}
	public synchronized void setPixel(double x, double y, int color) {
		if(thisImage != null) {
			thisImage.setRGB((int)((x)*thisImage.getWidth()), (int)((y)*thisImage.getHeight()), color);
		} else {
			System.out.print("Problem in setting pixel: SingleImage, function setPixel(...)\n");
		}
	}
	public void write() {
		File tmpFile = new File(filePath);
		tmpFile.mkdirs();
		try {
		ImageIO.write(thisImage, filePath.substring(filePath.lastIndexOf(".")+1, filePath.length()), tmpFile);
		} catch (Exception e) {
			System.out.print("Writing file failed at SingleImage.java, function write()\n");
		}
//		System.out.print("Image writed to: " + filePath + "\n");
	}
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
}
