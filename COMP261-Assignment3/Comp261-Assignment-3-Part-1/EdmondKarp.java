
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.HashMap;
import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Map;
import javafx.util.Pair;
import java.util.List;

/** Edmond karp algorithm to find augmentation paths and network flow.
 * 
 * This would include building the supporting data structures:
 * 
 * a) Building the residual graph(that includes original and backward (reverse) edges.)
 *     - maintain a map of Edges where for every edge in the original graph we add a reverse edge in the residual graph.
 *     - The map of edges are set to include original edges at even indices and reverse edges at odd indices (this helps accessing the corresponding backward edge easily)
 *     
 *     
 * b) Using this residual graph, for each city maintain a list of edges out of the city (this helps accessing the neighbours of a node (both original and reverse))

 * The class finds : augmentation paths, their corresponing flows and the total flow
 * 
 * 
 */

public class EdmondKarp {
    // class members

    //data structure to maintain a list of forward and reverse edges - forward edges stored at even indices and reverse edges stored at odd indices
    private static Map<String,Edge> edges; 

    // Augmentation path and the corresponding flow
    private static ArrayList<Pair<ArrayList<String>, Integer>> augmentationPaths =null;

    
    //TODO:Build the residual graph that includes original and reverse edges 
    public static void computeResidualGraph(Graph graph){
        // TODO
        edges = new HashMap<>();
        int i = 0;
        for(Edge originalEdge:graph.getOriginalEdges()){
            originalEdge.setFlow(0);
            //originalEdge.setCapacity(originalEdge.firstCapacity);
            City from = originalEdge.fromCity();
            City to = originalEdge.toCity();
            Edge reverseEdge = new Edge(to, from,0,0);
            
            edges.put(String.valueOf(i), originalEdge);
            edges.put(String.valueOf(i +1), reverseEdge);
            from.addEdgeId(String.valueOf(i));
            to.addEdgeId(String.valueOf(i+1));
            i = i + 2;
            
        }

        printResidualGraphData(graph);  //may help in debugging
        // END TODO
    }

    // Method to print Residual Graph 
    public static void printResidualGraphData(Graph graph){
        System.out.println("\nResidual Graph");
        System.out.println("\n=============================\nCities:");
        for (City city : graph.getCities().values()){
            System.out.print(city.toString());

            // for each city display the out edges 
            for(String eId: city.getEdgeIds()){
                System.out.print("["+eId+"] ");
            }
            System.out.println();
        }
        System.out.println("\n=============================\nEdges(Original(with even Id) and Reverse(with odd Id):");
        edges.forEach((eId, edge)->
                System.out.println("["+eId+"] " +edge.toString()));

        System.out.println("===============");
    }

    //=============================================================================
    //  Methods to access data from the graph. 
    //=============================================================================
    /**
     * Return the corresonding edge for a given key
     */

    public static Edge getEdge(String id){
        return edges.get(id);
    }

    /** find maximum flow
     * 
     */
    // TODO: Find augmentation paths and their corresponding flows
    public static ArrayList<Pair<ArrayList<String>, Integer>> calcMaxflows(Graph graph, City from, City to) {
        //TODO
        augmentationPaths = new ArrayList<>();
        computeResidualGraph(graph);

        // Find augmentation paths until there are none left
        int maxFlow = 0;
        while (true) {
            Pair<ArrayList<String>, Integer> augmentationPath = bfs(graph, from, to);
            if(augmentationPath.getKey() == null)break;
            augmentationPaths.add(augmentationPath);
            //maxFlow += augmentationPath.getValue();
            
            
            updateFlow(augmentationPath.getKey(), augmentationPath.getValue());
            
        }
        //System.out.println(maxFlow);
        // END TODO
        
        return augmentationPaths;
    }

    // TODO:Use BFS to find a path from s to t along with the correponding bottleneck flow
    // Use BFS to find a path from s to t along with the corresponding bottleneck flow
public static Pair<ArrayList<String>, Integer> bfs(Graph graph, City s, City t) {
    ArrayList<String> augmentationPath = new ArrayList<>();
    HashMap<String, String> backPointer = new HashMap<>();

    // Initialize queue for BFS and add source node
    Queue<City> queue = new ArrayDeque<>();
    queue.offer(s);

    // Initialize back pointers
    for(City v:graph.getCities().values()){
            backPointer.put(v.getId(),null);
    }

    while (!queue.isEmpty()) {
        City current = queue.poll();

        // Explore all outgoing edges from the current city
        for (String edgeId : current.getEdgeIds()) {
            Edge e = getEdge(edgeId);
            City neighbour = e.toCity();

            // Check if the neighbour has not been visited and the edge has remaining capacity
            if (!neighbour.equals(s) && backPointer.get(neighbour.getId()) == null && e.capacity() > 0) {
                // Update back pointer for the neighbour and add it to the queue
                backPointer.put(neighbour.getId(), edgeId);
                queue.offer(neighbour);

                // If the neighbour is the destination, reconstruct the augmentation path
                if (neighbour.equals(t)) {
                    String pathEdge = backPointer.get(t.getId());
                    while (pathEdge != null) {
                        augmentationPath.add(pathEdge);
                        pathEdge = backPointer.get(getEdge(pathEdge).fromCity().getId());
                    }
                    Collections.reverse(augmentationPath);
                    // Compute the bottleneck flow of the augmentation path
                    int bottleneckFlow = computeBottleneck(augmentationPath);
                    return new Pair<>(augmentationPath, bottleneckFlow);
                }
            }
        }
    }

    // If no path from s to t was found, return null
    return new Pair<>(null, 0);
}

    
    // Compute the bottleneck flow of an augmentation path
       public static int computeBottleneck(ArrayList<String> augmentationPath) {
        int bottleneckFlow = Integer.MAX_VALUE;

        for (String edgeId : augmentationPath) {
            Edge edge = getEdge(edgeId);
            int edgeCapacity = edge.capacity();
            bottleneckFlow = Math.min(bottleneckFlow, edgeCapacity);
        }

        return bottleneckFlow;
    }
    
    // Update the flow of the graph based on the augmentation path and bottleneck flow
    public static void updateFlow(List<String> toUpdate, int flow){
        
        for(String i : toUpdate){
            Edge forw, back;
            int index = Integer.parseInt(i);
            if(index%2 == 0){
                 forw = edges.get(i);
                 back = edges.get(String.valueOf(Integer.parseInt(i)+1));
                 
                 forw.setCapacity(forw.capacity()-flow);
                 forw.setFlow(forw.flow() + flow);
                 back.setCapacity(back.capacity() + flow);
            }else if(index%2 == 1){
                 back = edges.get(i);
                 forw = edges.get(String.valueOf(Integer.parseInt(i)-1));
                 
                 back.setCapacity(back.capacity()-flow);
                 back.setFlow(back.flow() + flow);
                 forw.setCapacity(forw.capacity() + flow);
            }
            
        }
    }
    
}


