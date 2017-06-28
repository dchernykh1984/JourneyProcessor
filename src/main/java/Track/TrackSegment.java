package Track;

import Metrics.SpaceMetrics;

public class TrackSegment {
	private TrackPoint firstPoint;
	private TrackPoint secondPoint;
	public TrackSegment(TrackPoint first, TrackPoint second) {
		firstPoint = first;
		secondPoint = second;
	}
	public double distTo(TrackPoint point) {
		return SpaceMetrics.distance(point, this);
	}
	public double minDistTo(TrackSegment segment) {
		return distTo(segment, false);
	}
	public double maxDistTo(TrackSegment segment) {
		return distTo(segment, true);
	}
	public double norm() {
		return SpaceMetrics.norm(this);
	}
	private double distTo(TrackSegment segment, boolean maxDist) {
		double dist1 = distTo(segment.getFirstPoint());
		double dist2 = distTo(segment.getSecondPoint());
		double dist3 = segment.distTo(firstPoint);
		double dist4 = segment.distTo(secondPoint);
		if(maxDist) {
			return Math.max(Math.max(dist1, dist2), Math.max(dist3, dist4));
		}
		return Math.min(Math.min(dist1, dist2), Math.min(dist3, dist4));
	}
	public double minDistTo(Track track) {
		return distTo(track, false);
	}
	public double maxDistTo(Track track) {
		return distTo(track, true);
	}
	public double distTo(Track track, boolean maxDist) {
		double minDistance = Double.MAX_VALUE; 
		for(int i = 1;i<track.size();i++) {
			minDistance = Math.min(distTo(track.getSegment(i-1), maxDist), minDistance);
		}
		return minDistance;
	}
	public double minDistTo(PLTFile pltFile) {
		return distTo(pltFile, false);
	}
	public double maxDistTo(PLTFile pltFile) {
		return distTo(pltFile, true);
	}
	public double distTo(PLTFile pltFile, boolean maxDist){
		int numberOfPointsInSegment = 25;
		double poi = numberOfPointsInSegment-1;
		double[] distances = new double[numberOfPointsInSegment];
		TrackPoint[] points = new TrackPoint[numberOfPointsInSegment];
		for(int i = 0;i<numberOfPointsInSegment;i++) {
			distances[i] = Double.MAX_VALUE;
			points[i] = firstPoint.multiply(i/poi).sum(secondPoint.multiply(1-i/poi));
		}
		for(Track track : pltFile.getListOfTracks()) {
			for(int i = 0;i<track.size()-1;i++) {
				for(int j = 0;j<numberOfPointsInSegment;j++) {
					distances[j] = Math.min(distances[j], points[j].distTo(track.getSegment(i)));
				}
			}
		}
		double returnValue = maxDist?0:Double.MAX_VALUE;
		for(int i = 0;i<numberOfPointsInSegment;i++) {
			if(maxDist) {
				returnValue = Math.max(returnValue, distances[i]);
			} else {
				returnValue = Math.min(returnValue, distances[i]);
			}
		}
		return returnValue;
	}
	
	public TrackPoint getFirstPoint() {
		return firstPoint;
	}
	public void setFirstPoint(TrackPoint firstPoint) {
		this.firstPoint = firstPoint;
	}
	public TrackPoint getSecondPoint() {
		return secondPoint;
	}
	public void setSecondPoint(TrackPoint secondPoint) {
		this.secondPoint = secondPoint;
	}

}
