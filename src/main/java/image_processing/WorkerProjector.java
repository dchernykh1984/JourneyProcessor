package image_processing;

import Track.SegmentOnLayer;
import Track.TrackPoint;

import java.util.LinkedList;

public class WorkerProjector implements Runnable {
	private SingleImage currentImage;
    private int tracksColor;
	private ImagesGetter imgsGetter;
	private SegmentsQueue queue;
	private Thread t;
	private boolean needToContinue = true;
	private double loggingPeriod = 10.0;
	private int checkingLoggingPeriod = 500;
	public Thread getT() {
		return t;
	}
	public void stopWorker() {
		needToContinue = false;
	}

	public WorkerProjector(SegmentsQueue queue, int tracksColor, ImagesGetter imgsGet, String workerName) {
		this.imgsGetter = imgsGet;
		this.queue = queue;
        this.tracksColor = tracksColor;
		t = new Thread(this, workerName);
		t.start();
	}
	public void run(){
		long counter = 0;
		long startTime = (long) (System.nanoTime() /Math.pow(10, 9));
		while(needToContinue) {
            LinkedList<SegmentOnLayer> segments = queue.getSegments(checkingLoggingPeriod);
				paintLine(segments, tracksColor);
				counter+=segments.size();
				if((long) (System.nanoTime() /Math.pow(10, 9)) - startTime > loggingPeriod) {
                    System.out.println(String.format("%s worker processed %d segments. Remaining %d", t.getName(), counter, queue.getLength()));
					startTime = (long) (System.nanoTime() /Math.pow(10, 9));
				}
		}
		System.out.println(String.format("%s worker processed %d segments. Remaining %d", t.getName(), counter, queue.getLength()));
	}
	public void paintLine(LinkedList<SegmentOnLayer> segms, int color) {
        for(SegmentOnLayer segm:segms) {
            long numberOfPoints = 1 + (long) (512.0 * Math.pow(2.0, segm.getDepth() - 1) * segm.norm() / 40e6);
            double step = 1.0 / numberOfPoints;
            for (double alfa = 0; alfa <= 1; alfa += step) {
                setPoint(segm.getFirstPoint().multiply(alfa).sum(segm.getSecondPoint().multiply(1.0 - alfa)), color, segm.getDepth());
            }
        }
        if(currentImage != null) {
            currentImage.write();
            imgsGetter.unlockImage(currentImage.getFilePath());
            currentImage = null;
        }
	}

	private void setPoint(TrackPoint point, int color, long depth) {
		ImageCoordinates coordinates = new ImageCoordinates(point, depth);
		String requiredPath = coordinates.getPathByCoordinates(imgsGetter.getRootPath());
		if(currentImage == null || !currentImage.getFilePath().equals(requiredPath)) {
			if(currentImage != null) {
				currentImage.write();
				imgsGetter.unlockImage(currentImage.getFilePath());
			}
			imgsGetter.lockImage(requiredPath);
			currentImage = new SingleImage(requiredPath);
			if (currentImage.fileExists()) {
				currentImage.read();
			} else {
				currentImage.read(getTemplatePath());
			}
		}
		currentImage.setPixel(coordinates.getFractionalX(), coordinates.getFractionalY(), color);
	}
	private String getTemplatePath() {
		return "tmpl.png";
	}


}
