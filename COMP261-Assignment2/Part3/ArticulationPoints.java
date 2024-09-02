import java.util.Collection;
import java.util.List;
import java.util.ArrayList;
import java.util.*;

//=============================================================================
//   Finding Articulation Points
//   Finds and returns a collection of all the articulation points in the undirected
//   graph.
//   Uses the algorithm from the lectures, but modified to cope with a not completely
//   connected graph. For a not fully connected graph, an articulation point is one
//   that would break a currently connected component into two or more components
//=============================================================================

public class ArticulationPoints{

    /**
     * Return a collection of all the Stops in the graph that are articulation points.
     */

    public static Collection<Stop> findArticulationPoints(Graph graph) {
        Collection<Stop> aPoints = new HashSet<>();
        Map<Stop,Integer> depthMap = new HashMap<>();
        //List<Set<Stop>> components = findConnectedComponents(graph);
        
        for(Stop stop:graph.getStops()){
                depthMap.put(stop,-1);
        }
          
        
            for(Stop stop:graph.getStops()){
                if(depthMap.get(stop) == -1){
                    int numSubtrees = 0;
                    depthMap.put(stop,0);
                    for(Stop neighbour: stop.getNeighbours()){
                        if(depthMap.get(neighbour) == -1){
                            recArtPts(neighbour,1,stop,aPoints, depthMap);
                            numSubtrees++;
                        }
                    }
                    if(numSubtrees>1){
                        aPoints.add(stop);
                    }
                }
            }
        
        //System.out.println("Components: " + components.size()); //debugging
        return aPoints;
    }
    
    private static int recArtPts(Stop currentStop, int depth, Stop fromNode,Collection<Stop> aPoints, Map<Stop, Integer> depthMap){
        depthMap.put(currentStop, depth);
        int reachBack = depth;
        
        for(Stop neighbour:currentStop.getNeighbours()){
            if(neighbour == fromNode){
                continue;
            } else if(depthMap.get(neighbour) != -1){
                reachBack = Math.min(depthMap.get(neighbour),reachBack);
            } else{
                int childReach = recArtPts(neighbour, depth + 1, currentStop, aPoints, depthMap);
                if(childReach >= depth){
                  aPoints.add(currentStop);  
                }
                reachBack = Math.min(childReach,reachBack);
                
            }
        }
        
        return reachBack;
    }
    
    public static List<Set<Stop>> findConnectedComponents(Graph graph){
         List<Set<Stop>> components = new ArrayList<>();
        Set<Stop> visited = new HashSet<>();

        for (Stop stop : graph.getStops()) {
            if (!visited.contains(stop)) {
                Set<Stop> component = new HashSet<>();
                dfs(stop, visited, component);
                components.add(component);
            }
        }

        return components;
    }

    private static void dfs(Stop currentStop, Set<Stop> visited, Set<Stop> component) {
        visited.add(currentStop);
        component.add(currentStop);

        for (Stop neighbour : currentStop.getNeighbours()) {
            if (!visited.contains(neighbour)) {
                dfs(neighbour, visited, component);
            }
        }
    }
}
