package Track;

import java.util.LinkedList;

public class Track {
	LinkedList<TrackPoint> pointsList;
	public Track(TrackPoint point) throws Exception {
		if(!point.getBreakTrackLine()) {
			throw new Exception("It's not point of new track");
		}
		pointsList = new LinkedList<TrackPoint>();
		pointsList.add(point);
	}
	public Track(LinkedList<TrackPoint> listOfPoints) {
		this.pointsList = listOfPoints;
		this.pointsList.get(0).setBreakTrackLine(true);
	}
	public Track() {
		pointsList = new LinkedList<TrackPoint>();
	}
	public double length() {
		double leng = 0;
		for(int i = 1;i<size();i++) {
			leng += getPoint(i).distTo(getPoint(i-1));
		}
		return leng;
	}
	public int size() {
		return pointsList.size();
	}
	public TrackSegment getSegment(int i) {
		return new TrackSegment(getPoint(i), getPoint(i+1));
	}
	
	public void addPoint(TrackPoint point) throws Exception {
		if(point.getBreakTrackLine()) {
			throw new Exception("It's impossible to add break point to track");
		}
		pointsList.add(point);
	}
	public void removePoint(int pointNumber, boolean makeBreak) throws Exception {
		pointsList.remove(pointNumber);
		if(pointNumber < pointsList.size()) {
			pointsList.get(pointNumber).setBreakTrackLine(makeBreak);
		} else {
			throw new Exception("Number of point to remove out of range");
		}
	}
	
	public void removePoints(int firstPoint, int lastPoint) {
		for(int i = firstPoint;i<lastPoint-1;i++) {
			pointsList.remove(firstPoint+1);
		}
	}
	
	public void removePoint(int pointNumber) {
		pointsList.remove(pointNumber);
	}
	public TrackPoint getPoint(int pointNumber) {
		return pointsList.get(pointNumber);
	}
	public void setPoint(int pointNumber, TrackPoint poi) {
		pointsList.set(pointNumber, poi);
	}
	public void removePoint(TrackPoint point) {
		pointsList.remove(point);
	}

	public LinkedList<TrackPoint> getPointsList() {
		return pointsList;
	}

	public void setPointsList(LinkedList<TrackPoint> pointsList) {
		this.pointsList = pointsList;
	}

}
