package Archievers;

import Metrics.SpaceMetrics;
import Track.Track;
import Track.TrackSegment;

public class QualityDecreaser extends Thread{
	private Track track;
	private double acceptableDeviation;
	private String name;
	public QualityDecreaser(Track track, double deviation, String name) {
		this.track = track;
		this.acceptableDeviation = deviation;
		this.name = name;
	}
	
	public void run() {
		descreaseQuality(this.acceptableDeviation);
		System.out.println("Quality decreased for track:" + name);

	}
	public void descreaseQuality(Double possibleDeviation) {
		Integer pointNumber = 0;
		while(pointNumber<track.size()) {
			track.removePoints(pointNumber, findPointsRemovable(pointNumber, acceptableDeviation));
			pointNumber++;
		}
	}
	private Integer findPointsRemovable(Integer firstPoint, Double acceptableDeviation) {
		for(Integer i = firstPoint+2;i<track.getPointsList().size();i++) {
			if(!checkPointsRemovable(firstPoint,i,acceptableDeviation)) {
				return i-1;
			}
		}
		return track.getPointsList().size()-1;
	}
	private boolean checkPointsRemovable(Integer firstPoint, Integer lastPoint, Double acceptableDeviation) {
		if(track.getPointsList().get(lastPoint).getBreakTrackLine()) {return false;}
		for(Integer i = firstPoint + 1;i < lastPoint;i++) {
			if(track.getPointsList().get(i).getBreakTrackLine()) {return false;}
			if(SpaceMetrics.distance(track.getPointsList().get(i), new TrackSegment(track.getPointsList().get(firstPoint), track.getPointsList().get(lastPoint)))>acceptableDeviation) {
				return false;
			}
		}
		return true;	
	}


}
