package image_processing;

import Track.TrackPoint;
import Track.TrackSegment;

import java.io.IOException;
import java.util.Hashtable;


public class ImagesCache {
	private Hashtable<String, SingleImage> imgs;
	String rootPath;
	long minDepth;
	long maxDepth;
	long maxSize;
	static double PI = 3.14159265358979323846264338327950288419716939937510582097494459230781640628620899;

	private class ImageCoordinates {
		public long x;
		public long y;
		public long depth;
		public String toString() {
			return "x" + x + "y" + y + "z" + depth;
		}
		public ImageCoordinates(long x, long y, long depth) {
			this.x = x;
			this.y = y;
			this.depth = depth;
		}
/*		public boolean equals(ImageCoordinates imgC) {
			return (this.x == imgC.x && this.y == imgC.y);
		}*/
	}
	public ImagesCache(String rootPath, long minDepth, long maxDepth, long maxNumberOfCache) {
		this.rootPath = rootPath;
		this.minDepth = minDepth;
		this.maxDepth = maxDepth;
		this.maxSize = maxNumberOfCache;
		imgs = new Hashtable<String, SingleImage>();
	}
	
	private String getPathByCoordinates(ImageCoordinates imgC, long depth) {
		long xFolder = imgC.x/1024;
		long yFolder = imgC.y/1024;
        return rootPath + "\\z" + depth + "\\" + xFolder + "\\x" + imgC.x + "\\" + yFolder + "\\y" + imgC.y + ".png";
 //       return rootPath + "\\z" + depth + "\\" + xFolder + "\\x" + imgC.x + "\\" + yFolder + "\\y" + imgC.y + ".gif";
	}
	private String getTemplatePath() {
		return "tmpl.png";
//		return "tmpl.gif";
	}
	public void readImages() {
		
	}
	public void writeImages() throws IOException {
		for(String imgC: imgs.keySet()) {
			imgs.get(imgC).write();
		}
		imgs = new Hashtable<String, SingleImage>();
	}
	private boolean readImage(ImageCoordinates imgC, long depth) throws Exception {
		SingleImage sImg = new SingleImage(getPathByCoordinates(imgC, depth));
		if(sImg.fileExists()) {
			sImg.read();
			imgs.put(imgC.toString(), sImg);
			return true;
		} else {
			sImg = new SingleImage(getTemplatePath());
			sImg.read();
			sImg.setFilePath(getPathByCoordinates(imgC, depth));
			imgs.put(imgC.toString(), sImg);
			return false;
		}
	}
	private boolean pointInCache(TrackPoint point, long depth) throws Exception {
		ImageCoordinates imgC = getImageCoordinates(point, depth);
		if(imgs.containsKey(imgC.toString())) {
			return true;
		}
		if(readImage(imgC, depth)) {
			return true;
		}
		return false;
	}
	private double getImageX(double longitude, long depth) {
		return Math.pow(2.0, depth-1.0) * (longitude + 180.0)/360.0;
	}
	public double getImageY(double latitude, long depth) {
		double tempLat = latitude;
		double tempVal = Math.log(Math.tan(Math.toRadians(tempLat))
				+1.0/Math.cos(Math.toRadians(tempLat)));
		return Math.pow(2.0, depth-1.0)*((1.0-tempVal/PI)/2.0); //for google, yahoo etc
//		return Math.pow(2.0, depth-1.0)*(0.997813403*(1.0-tempVal/PI)/2.0+0.001565095862); //for yandex
	}
	private ImageCoordinates getImageCoordinates(TrackPoint point, long depth) throws Exception {
//		images: 0 to 2^(depth-1)-1
//		coordinates: latitude -180 to 180, longitude -90 90
		ImageCoordinates imgC = new ImageCoordinates(getIntegerPart(getImageX(point.getLongitude(), depth)), 
				getIntegerPart(getImageY(point.getLatitude(), depth)),
				depth);
		return imgC;
	}
	private SingleImage getImageByCoordinates(TrackPoint point, long depth) throws Exception {
		return imgs.get(getImageCoordinates(point, depth).toString());
	}
	private long getIntegerPart(double value) {
		return (long)value;
	}
	private double getFractionalPart(double value) {
		return value - (double)getIntegerPart(value);
	}
	private void setPixelInImage(TrackPoint point, int color, long depth) throws Exception {
		getImageByCoordinates(point, depth).setPixel(getFractionalPart(getImageX(point.getLongitude(), depth)), getFractionalPart(getImageY(point.getLatitude(), depth)), color);
		if(imgs.size() > maxSize) {
			writeImages();
		}
	}
	private void setPoint(TrackPoint point, int color, long depth) throws Exception {
		try {
		if(pointInCache(point, depth)) {
			setPixelInImage(point, color, depth);
		}
		}catch (Exception e) {
			e.printStackTrace();
		}
	
	}
/*	public void setPointColor(TrackPoint point, int color) throws Exception {
		for(long depth = minDepth; depth<=maxDepth;depth++) {
			setPoint(point, color, depth);
		}
	}*/
	public void paintLine(TrackSegment segm, int color) throws Exception {
		for(long depth = minDepth;depth<=maxDepth;depth++) {
			long numberOfPoints = 1+(long) (512.0*Math.pow(2.0, depth-1) * segm.norm()/40e6);
			double step = 1.0/numberOfPoints;
			for(double alfa = 0;alfa<=1;alfa += step) {
				setPoint(segm.getFirstPoint().multiply(alfa).sum(segm.getSecondPoint().multiply(1.0-alfa)), color, depth);
			}
		}
		
	}

}
