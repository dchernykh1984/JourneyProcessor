package image_processing;

import Track.Track;

import java.util.LinkedList;

public class ImageProcessor extends Thread {
	ImagesCache imgsC;
	private LinkedList<Track> listOfTracks;
    private int tracksColor;
    private String layerName;

	public ImageProcessor(String rootPath, long minDepth, long maxDepth, long maxNumberOfCache, LinkedList<Track> listOfTracks, int tracksColor, String layerName) {
		imgsC = new ImagesCache(rootPath, minDepth, maxDepth, maxNumberOfCache);
		this.listOfTracks = listOfTracks;
        this.tracksColor = tracksColor;
        this.layerName = layerName;
	}
	public void run(){
		long startTime = (long) (System.nanoTime() /Math.pow(10, 9));
		try {
		double allSegme = 0;
		for(Track track : listOfTracks) {
				allSegme += track.size();
			}
		double counterSegme = 0;
		for(Track track : listOfTracks) {
			for(int i = 0;i<track.size()-1;i++) {
				counterSegme++;
				imgsC.paintLine(track.getSegment(i), tracksColor);
				if(counterSegme % (134217728/Math.pow(2,imgsC.minDepth)) == 0) {
					long currentTime = (long) (System.nanoTime()/Math.pow(10, 9) - startTime);
					long seconds = currentTime % 60;
					long minutes = (currentTime % 3600)/60;
					long hours = currentTime /3600;
					long estimation = (long)(currentTime *(allSegme/counterSegme));
					long secondsEST = estimation % 60;
					long minutesEST = (estimation % 3600)/60;
					long hoursEST = estimation /3600;
					
					System.out.print("Done "+ (100*counterSegme/allSegme) +"% of layer " + layerName + " " + imgsC.minDepth + ". Layer " + hours+":" +minutes+":" +seconds + ". Full layer estimation:" + hoursEST+":" +minutesEST+":" +secondsEST + "\n");
				}
			}
		}
		imgsC.writeImages();
		System.out.print("Processed layer: " + imgsC.minDepth + "\n");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
