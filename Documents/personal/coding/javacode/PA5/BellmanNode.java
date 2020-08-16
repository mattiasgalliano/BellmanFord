/*
Mattias Galliano 3347016
COP-3530 Data Structures
Programming Assignment 5:
Graph Algorithms

BellmanNode.java

Description:
This class implements a node or vertex for use in a graph that implements
the Bellman Ford algorithm. Accordingly, the default class constructor
initializes the node with infinite distance (represented by double max),
and null predecessor value as is appropriate for non-start nodes. These values
are changed as the algorithm is executed.

Input:
n/a

Process:
n/a

Output:
n/a

Dependencies:
n/a

Instructions:
javac BellmanNode.java
*/

public class BellmanNode {
    String name;
    double pathLength;
    String predecessor;

    public BellmanNode(String name) {
        this.name = name;
        this.pathLength = Double.MAX_VALUE;
        this.predecessor = null;
    }
}