package mazes.logic.carvers;

import graphs.EdgeWithData;
import graphs.minspantrees.MinimumSpanningTree;
import graphs.minspantrees.MinimumSpanningTreeFinder;
import mazes.entities.Room;
import mazes.entities.Wall;
import mazes.logic.MazeGraph;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * Carves out a maze based on Kruskal's algorithm.
 */
public class KruskalMazeCarver extends MazeCarver {
    MinimumSpanningTreeFinder<MazeGraph, Room, EdgeWithData<Room, Wall>> minimumSpanningTreeFinder;
    private final Random rand;

    public KruskalMazeCarver(MinimumSpanningTreeFinder
                                 <MazeGraph, Room, EdgeWithData<Room, Wall>> minimumSpanningTreeFinder) {
        this.minimumSpanningTreeFinder = minimumSpanningTreeFinder;
        this.rand = new Random();
    }

    public KruskalMazeCarver(MinimumSpanningTreeFinder
                                 <MazeGraph, Room, EdgeWithData<Room, Wall>> minimumSpanningTreeFinder,
                             long seed) {
        this.minimumSpanningTreeFinder = minimumSpanningTreeFinder;
        this.rand = new Random(seed);
    }

    @Override
    protected Set<Wall> chooseWallsToRemove(Set<Wall> walls) {
        Set<EdgeWithData<Room, Wall>> weightedWalls = new HashSet<>();
        for (Wall wall : walls) {
            EdgeWithData<Room, Wall> newWall = new EdgeWithData<>(
                wall.getRoom1(),
                wall.getRoom2(),
                rand.nextDouble(),
                wall
            );
            weightedWalls.add(newWall);
        }
        MinimumSpanningTree<Room, EdgeWithData<Room, Wall>> wallsToRemove =
            this.minimumSpanningTreeFinder.findMinimumSpanningTree(new MazeGraph(weightedWalls));

        HashSet<Wall> result = new HashSet<>();
        if (wallsToRemove.exists()) {
            for (EdgeWithData<Room, Wall> edge : wallsToRemove.edges()) {
                result.add(edge.data());
            }
        }
        return result;

        // Hint: you'll probably need to include something like the following:
        // this.minimumSpanningTreeFinder.findMinimumSpanningTree(new MazeGraph(edges));
        //   throw new UnsupportedOperationException("Not implemented yet.");
    }
}
