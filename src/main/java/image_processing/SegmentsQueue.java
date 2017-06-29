package image_processing;

import Track.SegmentOnLayer;
import Track.TrackSegment;

import java.util.LinkedList;
import java.util.Random;

public class SegmentsQueue {
    Random rGen = new Random();
    private int returnSize;
    private int numberOfThreads;
    private int capacity = 3;
    private int maxSize;
    public SegmentsQueue(int returnSize, int threads) {
        this.returnSize = returnSize;
        numberOfThreads = threads;
        maxSize = this.returnSize * numberOfThreads * capacity;
    }
    LinkedList<SegmentOnLayer> segmentsQueue = new LinkedList<SegmentOnLayer>();


    public synchronized void putSegment(TrackSegment segment, long depth) {
        if(getLength() >=maxSize) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        segmentsQueue.addLast(new SegmentOnLayer(segment, depth));
    }


    public synchronized LinkedList<SegmentOnLayer> getSegments() {
        if(getLength() <= maxSize/2) {
            notify();
        }
        LinkedList<SegmentOnLayer> listOfSegments = new LinkedList<SegmentOnLayer>();
        if(getLength() == 0) {
            return listOfSegments;
        }
        int nextIndex = rGen.nextInt(Math.max(1,getLength()-returnSize));
        for(int counter = 0;counter<returnSize;counter++) {
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
