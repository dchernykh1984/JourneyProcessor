package Track;

/**
 * Created by Denis on 6/28/2017.
 */
public class SegmentOnLayer extends TrackSegment {
    long depth;
    public SegmentOnLayer(TrackSegment segment, long depth) {
        super(segment.getFirstPoint(), segment.getSecondPoint());
        this.depth = depth;
    }
    public long getDepth() {
        return depth;
    }
}
