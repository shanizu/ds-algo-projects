package graphs.shortestpaths;

import graphs.BaseEdge;
import graphs.Graph;
import priorityqueues.DoubleMapMinPQ;
import priorityqueues.ExtrinsicMinPQ;

// import javax.print.attribute.HashAttributeSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
// import java.util.PriorityQueue;

/**
 * Computes the shortest paths using Dijkstra's algorithm.
 * @see SPTShortestPathFinder for more documentation.
 */
public class DijkstraShortestPathFinder<G extends Graph<V, E>, V, E extends BaseEdge<V, E>>
    extends SPTShortestPathFinder<G, V, E> {

    protected <T> ExtrinsicMinPQ<T> createMinPQ() {
        return new DoubleMapMinPQ<>();
        /*
        If you have confidence in your heap implementation, you can disable the line above
        and enable the one below.
         */
        // return new ArrayHeapMinPQ<>();

        /*
        Otherwise, do not change this method.
        We override this during grading to test your code using our correct implementation so that
        you don't lose extra points if your implementation is buggy.
         */
    }

    @Override
    protected Map<V, E> constructShortestPathsTree(G graph, V start, V end) {
        //throw new UnsupportedOperationException("Not implemented yet.");
        // only construct shortest path tree while end vertex in not known
        HashMap<V, Double> distances = new HashMap<>();  // distance to vertex
        HashMap<V, E> paths = new HashMap<>();           // set of edges in the shortest paths
        HashSet<V> known = new HashSet<>();
        DoubleMapMinPQ<V> active = new DoubleMapMinPQ<>();
        active.add(start, 0.0);
        known.add(start);
        distances.put(start, 0.0);
        for (E edge : graph.outgoingEdgesFrom(start)) {
            distances.put(edge.to(), Double.POSITIVE_INFINITY);
        }
        while (!active.isEmpty()) {
            V minPath = active.removeMin();
            known.add(minPath);
            double pathWeight = distances.get(minPath);
            //if (!minPath.equals(end)) {
            if (!known.contains(end)) {
                for (E edge : graph.outgoingEdgesFrom(minPath)) {
                    if (!distances.containsKey(edge.to())) {
                        distances.put(edge.to(), Double.POSITIVE_INFINITY);
                    }
                    double newPath = pathWeight + edge.weight();
                    if ((distances.containsKey(edge.to()) && distances.get(edge.to()) > newPath)) {
                        // if new distance is new or shorter than existing then added.
                        distances.put(edge.to(), newPath);
                        paths.put(edge.to(), edge);

                        if (active.contains(edge.to())) {
                            active.changePriority(edge.to(), newPath);
                        } else {
                            active.add(edge.to(), newPath);
                        }
                    }
                }
            }
        }
        return paths;
    }


    @Override
    protected ShortestPath<V, E> extractShortestPath(Map<V, E> spt, V start, V end) {
        if (end.equals(start)) {
            return new ShortestPath.SingleVertex<>(start);
        }
        V curr = end;
        List<E> shortestEdges = new ArrayList<>();
        while (spt.get(curr) != null) {
            E edge = spt.get(curr);
                shortestEdges.add(edge);
            curr = edge.from();
        }
        if (!curr.equals(start)) {
            return new ShortestPath.Failure<>();
        }
        else {
            Collections.reverse(shortestEdges);
            return new ShortestPath.Success<>(shortestEdges);
        }
        //throw new UnsupportedOperationException("Not implemented yet.");
    }
}
