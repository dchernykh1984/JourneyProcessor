package Metrics;

import Track.TrackPoint;
import Track.TrackSegment;

public class SpaceMetrics {
	
	private static double gradusesPerMeterSqred = 0.000000000081;
	private static double scalarMultiple(TrackSegment[] vectors) {
		double[] latitudes = new double[2];
		double[] longitudes = new double[2];
		for(int i = 0;i<2;i++){
			latitudes[i] = vectors[i].getSecondPoint().getLatitude() - 
				vectors[i].getFirstPoint().getLatitude();
			longitudes[i] = vectors[i].getSecondPoint().getLongitude() - 
				vectors[i].getFirstPoint().getLongitude();
		}

		return (latitudes[0] * latitudes[1] + 
				longitudes[0] * longitudes[1] * 
				(Math.pow(Math.cos(Math.PI*vectors[0].getFirstPoint().getLatitude()/180),2))) / 
				gradusesPerMeterSqred;
	}
	public static double angleCos(TrackSegment first, TrackSegment second) {
		return scalarMultiple(first, second)/(norm(first)*norm(second));
	}
	public static double scalarMultiple(TrackSegment first, TrackSegment second) {
		TrackSegment[] vectors = new TrackSegment[2];
		vectors[0]=first;
		vectors[1]=second;
		return scalarMultiple(vectors);
	}
	public static double scalarMultiple(TrackPoint point, TrackSegment segment) {
		return scalarMultiple(segment, new TrackSegment(point, segment.getFirstPoint()));
	}
	public static double scalarMultiple(TrackSegment segment, TrackPoint point) {
		return scalarMultiple(segment, new TrackSegment(segment.getFirstPoint(), point));
	}
	public static double norm(TrackSegment segment) {
		return Math.sqrt(scalarMultiple(segment,segment));
	}
	public static double distance(TrackPoint pointFirst, TrackPoint pointSecond) {
		return norm(new TrackSegment(pointFirst, pointSecond));
	}
	public static double distance(TrackSegment segment, TrackPoint point) {
		double segmentNorm=norm(segment);
		TrackSegment pointVector=new TrackSegment(segment.getFirstPoint(), point);
		double proection = scalarMultiple(segment, pointVector)/segmentNorm;
		if(proection>segmentNorm) return distance(segment.getSecondPoint(), point);
		if(proection<0) return distance(segment.getFirstPoint(), point);
		return Math.sqrt(Math.pow(norm(pointVector),2) - Math.pow(proection,2));
	}
	public static double distance(TrackPoint point, TrackSegment segment) {
		return distance(segment, point);
	}
	public static TrackPoint proection(TrackSegment segment, TrackPoint point) {
		boolean makeBreak = point.getBreakTrackLine();
		double proectionLen = scalarMultiple(segment, point);
		if(proectionLen < 0) {
			return segment.getFirstPoint();
		}
		double segmentNorm = norm(segment);
		if(proectionLen > segmentNorm) {
			return segment.getSecondPoint();
		} 
		double alfa = proectionLen/segmentNorm;
		TrackPoint resultPoint = segment.getFirstPoint().multiply(alfa).sum(segment.getSecondPoint().multiply(1.0-alfa));
		resultPoint.setBreakTrackLine(makeBreak);
		return resultPoint;
	}

}
