import javafx.util.Pair;
import java.util.HashSet;
import java.util.Set;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Collections;
import java.util.TreeMap;
import java.util.Collection;

/**
* Write a description of class PageRank here.
*
* @author (your name)
* @version (a version number or a date)
*/
public class PageRank
{
    //class members 
    private static Map<Gnode, Double> rankMap = new TreeMap<>(Comparator.comparing(Gnode::getName));

    private static double dampingFactor = .85;
    private static int iter = 10;
    /**
    * build the fromLinks and toLinks 
    */
    //TODO: Build the data structure to support Page rank. Compute the fromLinks and toLinks for each node
    public static void computeLinks(Graph graph){
        // TODO
        //Map<String, Gnode> nodes = graph.getNodes();
        
        
        for (Edge edge : graph.getOriginalEdges()) {
            Gnode fromNode = edge.fromNode();
            Gnode toNode = edge.toNode();

            
            fromNode.addToLinks(toNode);
            
            toNode.addFromLinks(fromNode);
        }
        
        printPageRankGraphData(graph);  ////may help in debugging
        // END TODO
    }

    public static void printPageRankGraphData(Graph graph){
        System.out.println("\nPage Rank Graph");

        for (Gnode node : graph.getNodes().values()){
            System.out.print("\nNode: "+node.toString());
            //for each node display the in edges 
            System.out.print("\nIn links to nodes:");
            for(Gnode c:node.getFromLinks()){

                System.out.print("["+c.getId()+"] ");
            }

            System.out.print("\nOut links to nodes:");
            //for each node display the out edges 
            for(Gnode c: node.getToLinks()){
                System.out.print("["+c.getId()+"] ");
            }
            System.out.println();;

        }    
        System.out.println("=================");
    }
    //TODO: Compute rank of all nodes in the network and display them at the console
    public static void computePageRank(Graph graph){
        // TODO
        
        int nNodes = graph.getNodes().size();
        
        for(Gnode node:graph.getNodes().values()){
            rankMap.put(node, (1.0/nNodes));
        }
        
    
        for (int count = 1; count <= iter; count++){
            Map<Gnode, Double> localRankMap = new HashMap<>(); //local map for updated PageRank values
    
            //calculate contribution from nodes with no outlinks
            double noOutLinkShare = 0;
            for (Gnode node : graph.getNodes().values()) {
                if (node.getToLinks().isEmpty()) {
                    noOutLinkShare += dampingFactor * (rankMap.get(node) / nNodes);
                }
            }
    
            //update PageRank for each node based on contributions from neighbors
            for (Gnode node : graph.getNodes().values()) {
                double nRank = noOutLinkShare + (1 - dampingFactor) / nNodes;
                double neighboursShare = 0;
                for (Gnode backNeighbour : node.getFromLinks()) {
                    neighboursShare += rankMap.get(backNeighbour) / backNeighbour.getToLinks().size();
                }
                double newPageRank = nRank + dampingFactor * neighboursShare;
                localRankMap.put(node, newPageRank); // Store updated PageRank in the local map
            }
    
            //update global rankMap with values from local map after each iteration
            rankMap = new TreeMap<>(Comparator.comparing(Gnode::getName)); // may not need this
            rankMap.putAll(localRankMap);
        }
        
        
        // END TODO
    }
    
    public static void computeMostImpneighbour(Graph graph){
        // TODO
          for (Gnode node : graph.getNodes().values()) {
            Gnode mostImpNeighbour = null;
            double maxRankDrop = 0.0;
    
            //create a copy of the in-neighbors set to modify
            Set<Gnode> tempFromLinks = new HashSet<>(node.getFromLinks());
    
            //iterate through in-neighbors and find the most important neighbor
            for (Gnode inNeighbour : tempFromLinks) {
                //temporarily remove the in-neighbor and calculate the change in PageRank
                node.removeFromLinks(inNeighbour);
    
                double oldPageRank = rankMap.get(node);
                computePageRank(graph); // Recalculate PageRank without the in-neighbor
                double newPageRank = rankMap.get(node);
                double rankDrop = oldPageRank - newPageRank;
    
                //update most important neighbor if current neighbor causes larger drop in PageRank
                if (rankDrop > maxRankDrop) {
                    mostImpNeighbour = inNeighbour;
                    maxRankDrop = rankDrop;
                }
    
                //restore the original set of in-neighbors
                node.addFromLinks(inNeighbour); //was addTolinks
                computePageRank(graph); //added this 
            }
    
            //print the most important neighbor for the current node
            if (mostImpNeighbour != null) {
                System.out.println("Node " + node.getName() + ": " + mostImpNeighbour.getName());
            } else if(mostImpNeighbour == null){
                System.out.println("Node " + node.getName() + ": Null");
            }
        }
        // END TODO
    
    
    }
    
    public static void computePageRankPrint(Graph graph){
        System.out.println("Iteration 10:");
        System.out.println();
        
        for (Map.Entry<Gnode, Double> entry : rankMap.entrySet()) {
            System.out.println(entry.getKey().getName() + "[" + entry.getKey().getId() + "]: "+ entry.getValue());
        }
        double sum = 0;
        for (double value : rankMap.values()) {
            sum += value;
        }
        System.out.println("Sum: " + sum);
        System.out.println();
    }
    
}
