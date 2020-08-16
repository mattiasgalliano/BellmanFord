/*
Mattias Galliano 3347016
COP-3530 Data Structures
Programming Assignment 5:
Graph Algorithms

BellmanNode.java

Description:
This class uses Bellman nodes to implement a Bellman graph, which can
utilize the Bellman Ford algorithm as one of its class methods. The
outline for the Bellman Ford algorithm was borrowed from the textbook.
Also provided are a negative cycle check method for the Bellman Ford algorithm,
as well as convenient utility methods used for retrieiving node objects from names,
and also lists of node names.

Input:
n/a

Process:
n/a

Output:
n/a

Dependencies:
BellmanNode.java

Instructions:
javac BellmanGraph.java BellmanNode.java
*/

public class BellmanGraph {

    BellmanNode[] nodes;
    double[][] adjMatrix;

    /**
     * Constructs BellmanFord graph from BellmanFord nodes and
     * adjacency matrix
     * @param nodes
     * @param adjMatrix
     */
    public BellmanGraph(BellmanNode[] nodes, double[][] adjMatrix) {
        this.nodes = nodes;
        this.adjMatrix = adjMatrix;
    }

    /**
     * Returns shortest path between two nodes in graph using BellmanFord
     * algorithm
     */
    public double shortestPath(String startNodeName, String endNodeName) {
        /* create nodes from name strings */
        BellmanNode startNode = getNode(startNodeName);
        BellmanNode endNode = getNode(endNodeName);

        /* initialize nodes */
        // non start nodes initialized by default node constructor
        startNode.pathLength = 0; // intialize start node

        /* find shortest path */
        for (int i = 0; i < nodes.length - 1; i++) { // main iterations

            for (int j = 0; j < nodes.length; j++) { // nodes[j] is current node

                for (int k = 0; k < adjMatrix[j].length; k++) { // adjMatrix[j] are adjacent node edges, nodes[k] is adjacent node
                    
                    if (j == k) {;} // ignore self-edge

                    double edgeWeight = adjMatrix[j][k]; // weight of edge from current to adjacent node
                    
                    double alternativePathLength = nodes[j].pathLength + edgeWeight;

                    if (alternativePathLength < nodes[k].pathLength) {

                        nodes[k].pathLength = alternativePathLength;
                        nodes[k].predecessor = nodes[j].name; // only if extra path information required?
                    }
                }
            }
        }

        checkNegativeCycle(); // check for negative cycles in BellmanFord algorithm in graph

        return endNode.pathLength; // shortest path from start to end node
    }

    /**
     * Returns true if negative cycles found in BellmanFord
     * graph, indicates that no shortest path determined
     * @return
     */
    public boolean checkNegativeCycle() {

        for (int i = 0; i < nodes.length; i++) { // nodes[i] is current node 

            for (int j = 0; j < adjMatrix[i].length; j++) { // adjMatrix[i] are adjacent node edges, nodes[j] is adjacent node

                double edgeWeight = adjMatrix[i][j];
                double alternativePathLength = nodes[i].pathLength + edgeWeight;

                if (alternativePathLength < nodes[j].pathLength) {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * Returns matching node in nodes array from passed node name as string
     * @param name
     * @return
     */
    public BellmanNode getNode(String name) {
        return nodes[(getNodeIndex(name))];
    }

    /**
     * Returns matching node index in nodes array from passed node name as string
     * @param name
     * @return
     */
    public int getNodeIndex(String name) {

        for (int i = 0; i < nodes.length; i++) {

            if (nodes[i].name.equals(name)) {
                return i;
            }
        }

        return -1; // if node name not in nodes array
    }

    /**
     * Returns node names from nodes as string array 
     * @return
     */
    public String[] getNodeNames() {

        String[] nodeNames = new String[nodes.length];

        for (int i = 0; i < nodes.length; i++) {
            nodeNames[i] = nodes[i].name;
        }

        return nodeNames;
    }
}