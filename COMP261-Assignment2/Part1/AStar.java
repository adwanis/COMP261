/**
 * Implements the A* search algorithm to find the shortest path
 *  in a graph between a start node and a goal node.
 * If start or goal are null, it returns null
 * If start and goal are the same, it returns an empty path
 * Otherwise, it returns a Path consisting of a list of Edges that will
 * connect the start node to the goal node.
 */

import java.util.Collections;
import java.util.Collection;
import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;

import java.util.List;
import java.util.ArrayList;
import java.util.PriorityQueue;
//

public class AStar {

    /**
     * Finds the shortest path between two stops
     */
    public static List<Edge> findShortestPath(Stop start, Stop goal) {
         if(start == null || goal == null){
            return null;
        }
        if(start.equals(goal)){
            return new ArrayList<>();
        }
        PriorityQueue<SearchQueueItem> fringe = new PriorityQueue<>();
        Map<Stop, Edge> backpointers = new HashMap<>();
        Set<Stop> visited = new HashSet<Stop>();
        fringe.add(new SearchQueueItem(start, null,0,start.distanceTo(goal)));
        
        while(!fringe.isEmpty()){
            SearchQueueItem currentItem = fringe.poll();
            Stop node = currentItem.getNode();
            Edge edge = currentItem.getEdge();
            double lengthToNode = currentItem.getLengthToNode();
            double estimateTotalPath = currentItem.getEstimateTotalPath();
            if(!visited.contains(node)){
                visited.add(node);
                backpointers.put(node,edge);
                if(node.equals(goal)){
                    return reconstructPath(start,goal,backpointers);
                }
                
                for(Edge nEdge:node.getEdges()){
                    Stop neighbour = nEdge.toStop();
                    if(!visited.contains(neighbour)){
                        double LengthToNeighbour = lengthToNode + nEdge.distance();
                        double estimateTotalPathToNeighbour = LengthToNeighbour+neighbour.distanceTo(goal);
                        fringe.add(new SearchQueueItem(neighbour, nEdge, LengthToNeighbour,estimateTotalPathToNeighbour));
                    }
                }
            }
        }
        return null; // to make the template compile!
    }
    
    public static List<Edge> reconstructPath(Stop start, Stop goal, Map<Stop, Edge> backpointers){
       List<Edge> path = new ArrayList<>();
       Stop current = goal;
       while(current!=start){
           Edge edge = backpointers.get(current);
           path.add(edge);
           current = edge.fromStop();
       }
       Collections.reverse(path);
       return path;
    }
    
}
