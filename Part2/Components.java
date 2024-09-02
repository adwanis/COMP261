import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;

//=============================================================================
//   Finding Components
//   Finds all the strongly connected subgraphs in the graph
//   Constructs a Map recording the number of the subgraph for each Stop
//=============================================================================

public class Components{

    // Based on Kosaraju's_algorithm
    // https://en.wikipedia.org/wiki/Kosaraju%27s_algorithm
    // Use a visited set to record which stops have been visited

    
    public static Map<Stop,Integer> findComponents(Graph graph) {
        Map<Stop, Integer> fcomp = new HashMap<>(); 
        for(Stop stop:graph.getStops()){
            fcomp.put(stop,-1);
        }
        int componentNum = 0;
        List<Stop> nodeList = new ArrayList<>();
        Set<Stop> visited = new HashSet<>();
        
        for(Stop stop: graph.getStops()){
            if(!visited.contains(stop)){
                forwardVisit(stop,nodeList,visited);
            }
        }
        
        for(int i = nodeList.size()-1; i>=0;i--){
            Stop stop = nodeList.get(i);
            if(fcomp.get(stop) == -1){
                backwardVisit(stop, componentNum,fcomp);
                componentNum++;
            }
        }
        
        
        return fcomp; 
    }
    
    private static void forwardVisit(Stop currentStop, List<Stop> nodeList, Set<Stop> visited){
        if(!visited.contains(currentStop)){
            visited.add(currentStop);
            for (Edge edge : currentStop.getOutEdges()) {
                Stop neighbour = edge.toStop();
                forwardVisit(neighbour, nodeList, visited);
            }  
            nodeList.add(currentStop);
        }
    }
    
    private static void backwardVisit(Stop currentStop, int componentNum, Map<Stop, Integer> fcomp){
        if(fcomp.get(currentStop) == -1){
            fcomp.put(currentStop, componentNum);
            for(Edge edge : currentStop.getInEdges()){
                Stop neighbour = edge.fromStop();
                backwardVisit(neighbour, componentNum, fcomp);
            }
        }
    }




}
