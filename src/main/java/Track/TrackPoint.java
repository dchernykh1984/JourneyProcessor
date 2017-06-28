package Track;

import java.text.ParseException;

import Metrics.SpaceMetrics;

public class TrackPoint {
	private double latitude;
	private double longitude;
	private int breakTrackLine;
	private String deliminer;
	
	public TrackPoint(double longit, double latit) {
		this();
		latitude = latit;
		longitude = longit;
	}
	public TrackPoint(TrackPoint point) {
		latitude = point.latitude;
		longitude = point.longitude;
		breakTrackLine = point.breakTrackLine;
		deliminer = point.deliminer;
	}
	public TrackPoint() {
		deliminer = ",";
	}
	public double distTo(Track track) {
		double distan = distTo(track.getSegment(0));
		for(int i = 1; i<track.size()-1;i++) {
			distan = Math.min(distTo(track.getSegment(i)), distan);
		}
		return distan;
	}
	public double distTo(PLTFile pltFile) {
		double distan = Double.MAX_VALUE;
		for(Track track:pltFile.getListOfTracks()) {
			distan = Math.min(distTo(track), distan);
		}
		return distan;
	}

	public TrackPoint proectTo(TrackSegment segment) {
		return SpaceMetrics.proection(segment, this);
	}
	public TrackPoint multiply(double alfa) {
		TrackPoint point = new TrackPoint(this);
		point.setLongitude(this.getLongitude()*alfa);
		point.setLatitude(this.getLatitude()*alfa);
		return point;
	}
	public TrackPoint sum(TrackPoint sumPoint) {
		TrackPoint point = new TrackPoint(this);
		point.setLongitude(this.getLongitude() + sumPoint.getLongitude());
		point.setLatitude(this.getLatitude() + sumPoint.getLatitude());
		return point;
	}
	public TrackPoint(String trackString) throws ParseException {
		this();
		latitude = Double.parseDouble(trackString.replace(",", ", ").split(deliminer)[0].trim());
		longitude = Double.parseDouble(trackString.replace(",", ", ").split(deliminer)[1].trim());
		breakTrackLine = Integer.parseInt(trackString.replace(",", ", ").split(deliminer)[2].trim());
		
	}
	public String toString() {
		String joinedString = latitude + deliminer + longitude +
		deliminer + breakTrackLine + deliminer + "0.0" + 
		deliminer + "0.0" + deliminer + deliminer;
		return joinedString;
		
	}
	public double distTo(TrackSegment segment) {
		return SpaceMetrics.distance(this, segment);
	}
	public double distTo(TrackPoint point) {
		return SpaceMetrics.distance(this, point);
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}
	public Double getLongitude() {
		return longitude;
	}
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}
	public boolean getBreakTrackLine() {
		return breakTrackLine==1;
	}
	public void setBreakTrackLine(boolean breakTrackLine) {
		this.breakTrackLine = breakTrackLine?1:0;
	}
}
