package image_processing;

import Track.SegmentOnLayer;
import Track.TrackSegment;

import java.util.LinkedList;

/**
 * Created by Denis on 6/28/2017.
 */
public class SegmentsQueue {
    LinkedList<SegmentOnLayer> segmentsQueue = new LinkedList<SegmentOnLayer>();
    public synchronized void putSegment(TrackSegment segment, long depth) {
        segmentsQueue.addLast(new SegmentOnLayer(segment, depth));
    }
    public synchronized LinkedList<SegmentOnLayer> getSegments(int numberOfSegments) {
        LinkedList<SegmentOnLayer> listOfSegments = new LinkedList<SegmentOnLayer>();
        for(int counter = 0;counter<numberOfSegments;counter++) {
            if(segmentsQueue.size() == 0) {
                return listOfSegments;
            }
            listOfSegments.addLast(segmentsQueue.getFirst());
            segmentsQueue.removeFirst();
        }
        return listOfSegments;
    }

    public synchronized int getLength() {
        return segmentsQueue.size();
    }
}
