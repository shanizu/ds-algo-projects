package graphs.minspantrees;

import disjointsets.DisjointSets;
import disjointsets.UnionBySizeCompressingDisjointSets;
import graphs.BaseEdge;
import graphs.KruskalGraph;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

/**
 * Computes minimum spanning trees using Kruskal's algorithm.
 * @see MinimumSpanningTreeFinder for more documentation.
 */
public class KruskalMinimumSpanningTreeFinder<G extends KruskalGraph<V, E>, V, E extends BaseEdge<V, E>>
    implements MinimumSpanningTreeFinder<G, V, E> {

    protected DisjointSets<V> createDisjointSets() {
        return new UnionBySizeCompressingDisjointSets<>();
        //return new QuickFindDisjointSets<>();
        /*
        Disable the line above and enable the one below after you've finished implementing
        your `UnionBySizeCompressingDisjointSets`.
         */

        /*
        Otherwise, do not change this method.
        We override this during grading to test your code using our correct implementation so that
        you don't lose extra points if your implementation is buggy.
         */
    }

    @Override
    public MinimumSpanningTree<V, E> findMinimumSpanningTree(G graph) {
        List<E> edges = new ArrayList<>(graph.allEdges());
        HashSet<E> mstEdges = new HashSet<>();
        // sort edges in the graph in ascending weight order
        edges.sort(Comparator.comparingDouble(E::weight));
        if (edges.size() == 0 && graph.allVertices().size() < 2) { // 0E 1V
            return new MinimumSpanningTree.Success<>();
        } else if (edges.size() == 0 && graph.allVertices().size() >= 2) {
            return new MinimumSpanningTree.Failure<>();
        }
        DisjointSets<V> disjointSets = createDisjointSets();
        for (V vertex : graph.allVertices()) {
            disjointSets.makeSet(vertex);
        }
        for (E edge : edges) {
            V to = edge.to();
            V from = edge.from();
            if (disjointSets.union(to, from)) {
                mstEdges.add(edge);
            }
        }
        if (mstEdges.size() + 1 == graph.allVertices().size()) {
            return new MinimumSpanningTree.Success<>(mstEdges);
        } else {
            return new MinimumSpanningTree.Failure<>();
        }
    }
}
