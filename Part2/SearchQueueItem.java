/**
 * AStar search uses a priority queue of partial paths
 * that the search is building.
 * Each partial path needs several pieces of information
 * to specify the path to that point, its cost so far, and
 * its estimated total cost
 */

public class SearchQueueItem implements Comparable<SearchQueueItem> {
    private Stop node;
    private Edge edge;
    private double lengthToNode;
    private double estimateTotalPath;

    public SearchQueueItem(Stop node, Edge edge, double lengthToNode, double estimateTotalPath) {
        this.node = node;
        this.edge = edge;
        this.lengthToNode = lengthToNode;
        this.estimateTotalPath = estimateTotalPath;
    }

    public Stop getNode() {
        return node;
    }

    public Edge getEdge() {
        return edge;
    }

    public double getLengthToNode() {
        return lengthToNode;
    }

    public double getEstimateTotalPath() {
        return estimateTotalPath;
    }

    // stub needed for file to compile.
    public int compareTo(SearchQueueItem other) {
        return Double.compare(this.estimateTotalPath, other.estimateTotalPath);
    }


}
