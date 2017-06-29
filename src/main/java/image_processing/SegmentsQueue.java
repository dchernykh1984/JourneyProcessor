package image_processing;

import Track.SegmentOnLayer;
import Track.TrackSegment;

import java.util.LinkedList;
import java.util.Random;

/**
 * Created by Denis on 6/28/2017.
 */
public class SegmentsQueue {
    Random rGen = new Random();
    LinkedList<SegmentOnLayer> segmentsQueue = new LinkedList<SegmentOnLayer>();
    public synchronized void putSegment(TrackSegment segment, long depth) {
        segmentsQueue.addLast(new SegmentOnLayer(segment, depth));
    }
    public synchronized LinkedList<SegmentOnLayer> getSegments(int numberOfSegments) {
        LinkedList<SegmentOnLayer> listOfSegments = new LinkedList<SegmentOnLayer>();
        if(getLength() == 0) {
            return listOfSegments;
        }
        int nextIndex = rGen.nextInt(getLength());
        for(int counter = 0;counter<numberOfSegments;counter++) {
            if(segmentsQueue.size() <= nextIndex) {
                return listOfSegments;
            }
            listOfSegments.addLast(segmentsQueue.get(nextIndex));
            segmentsQueue.remove(nextIndex);
        }
        return listOfSegments;
    }

    public synchronized int getLength() {
        return segmentsQueue.size();
    }
}
