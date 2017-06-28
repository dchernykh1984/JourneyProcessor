package Archievers;

import Track.Track;
import Track.TrackSegment;

public class TrackProector extends Thread {
	Track prTrack;
	Track toTrack;
	double distance;
	public TrackProector(Track track, Track trackTo, double dist) {
		prTrack = track;
		toTrack = trackTo;
		distance = dist;
	}
	public void run() {
		for(int i = 0;i<prTrack.size()-1;i++) {
			if(segmentRemovable(prTrack.getSegment(i))) {
				prTrack.getPoint(i+1).setBreakTrackLine(true);
			}
		}
//			prTrack.setPoint(i, proectPoint(prTrack.getPoint(i)));
		int counter = 0;
		while(counter < prTrack.size()-1 && prTrack.getPoint(counter+1).getBreakTrackLine()) {
			prTrack.removePoint(counter);
		}
		counter++;
		while(counter < prTrack.size()) {
			if(prTrack.getPoint(counter-1).getBreakTrackLine() && prTrack.getPoint(counter).getBreakTrackLine()) {
				prTrack.removePoint(counter-1);
			} else {
				counter++;
			}
		}
		
	}
	public boolean segmentRemovable(TrackSegment segment) {
		for(int i = 0;i<toTrack.size()-1;i++) {
			if(!toTrack.getPoint(i+1).getBreakTrackLine()) {
				boolean removable = true;
				Double numberOfSumsegments = segment.norm()/distance;
				numberOfSumsegments = 1.0/numberOfSumsegments.intValue();
				for(double alfa = 0;alfa<=1.0;alfa+=numberOfSumsegments) {
					if(segment.getFirstPoint().multiply(alfa).sum(segment.getSecondPoint().multiply(1.0-alfa)).distTo(toTrack.getSegment(i)) > distance) {
						removable = false;
						break;
					}
				}
				if(removable) {
					return true;
				}
			}
		}
		return false;
	}
/*	public TrackPoint proectPoint(TrackPoint point) {
		TrackSegment segment = toTrack.getSegment(0);
		double distance = point.distTo(segment);
		for(int i = 1;i<toTrack.size()-1;i++) {
			TrackSegment candSegm = toTrack.getSegment(i);
			double candDist = candSegm.distTo(point);
			if(candDist != 0 && candDist < distance) {
				distance = candDist;
				segment = candSegm;
			}
		}
		if(distance < this.distance) {
			return point.proectTo(segment);
		}
		return point;
		
	}*/
}
