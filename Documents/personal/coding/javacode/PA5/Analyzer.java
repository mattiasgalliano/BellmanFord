/*
Mattias Galliano 3347016
COP-3530 Data Structures
Programming Assignment 5:
Graph Algorithms

BellmanNode.java

Description:
This analyzes uses a Bellman Ford algorithm to determine the shortest path between
each vertex and all other vertices in a dataset. Specifically, the dataset represents
currencies, and their exchange rates between all other currencies in the set as a matrix.
The program output prints the start vertex or currency, and its relation or exchange rate
between all other currencies, as well as the maximal exchange rate between saif currencies
as determined by the Bellman Ford algorithm.

Input:
The input is a csv file with a 55x55 matrix containing currencies and their exchange rates
in a 54x54 matrix. The program prompts the user to input a currency to the keyboard.

Process:
The outermost row and column of the matrix represent data labels or currencies,
while the inner 54x54 an adjacency matrix therefore. Accordingly, the file is read and used
to construct Bellman Ford nodes for each currency type, and an adjacency matrix to relate them. 
Exchange rate values for use in the adjacency matrix are additionally converted to weights 
(weight = -log(exchange rate)). The log operation ensures that a compound probability is calculated
by the algorithm, which only sums weights, where a compound probability would normally be determined 
by multiplying probabilities, and the negation operation ensures the algorithm, which normally searches
for shortest or minimal distance, returns instead a maximal value, as the desired output
is maximmum exchange rate.

Output:
The program prints to the console maximum exchange rate (as determined by the Bellman Ford algorithm) and the
direct exchange rate (as determined by the given adjacency matrix / input data) for the input source
currency to every other currency in the dataset.

Dependencies:
n/a

Instructions:
javac Analyzer.java BellmanGraph.java BellmanNode.java
java BellmanNode
*/

import java.util.Scanner; // to read file input, keyboard input
import java.io.File; // to read file input
import java.io.FileNotFoundException; // to read file input and handle resultant errors
import java.util.Arrays; // used to print list of currencies when prompting user for currency input

public class Analyzer {

    public static final int DATA_DIMENSIONS = 55; // file data dimensions are 55x55
    public static final int GRAPH_DIMENSIONS = 54; // graph vertices are 54, adj matrix dimensions are 54x54

    public static void main(String args[]) {

        String filepath = "C:/Users/Mattias/Documents/personal/coding/javacode/PA5/exchange rates.csv"; // filepath with data

        try {
            /* build exchange rate graph from file data */
            BellmanGraph exchangeRateGraph = constructGraphFromFile(filepath);

            /* get currency input from keyboard */
            final String[] CURRENCIES = exchangeRateGraph.getNodeNames(); // currencies from graph
            String currency = getCurrencyInput(CURRENCIES);

            /* print exchange rate output */
            findMaxAndDirectExchangeRatesFor(currency, CURRENCIES, exchangeRateGraph);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static BellmanGraph constructGraphFromFile(String filepath) throws FileNotFoundException {

        /* scan file rows */
        String[] rowArray = scanRows(filepath);

        /* construct graph nodes */
        BellmanNode[] nodes = constructNodes(rowArray);

        /* construct adj matrix */
        double[][] adjMatrix = constructAdjMatrix(rowArray);

        /* construct graph */
        BellmanGraph exchangeRateGraph = new BellmanGraph(nodes, adjMatrix);

        return exchangeRateGraph;
    }

    /**
     * Scans file data and returns file data rows as array
     * @param filepath
     * @return
     * @throws FileNotFoundException
     */
    public static String[] scanRows(String filepath) throws FileNotFoundException {
        
        File myFile = new File("C:/Users/Mattias/Documents/personal/coding/javacode/PA5/exchange rates.csv");

        Scanner rowScanner = new Scanner(myFile).useDelimiter("\n");

        String[] rowArray = new String[DATA_DIMENSIONS];

        for (int i = 0; i < 55; i++) {
            rowArray[i] = rowScanner.next();
        }

        rowScanner.close();

        return rowArray;
    }

    /**
     * Constructs nodes for use in graph from file data rows
     */
    public static BellmanNode[] constructNodes(String[] rowArray) {

        BellmanNode[] nodes = new BellmanNode[GRAPH_DIMENSIONS];
        
        String[] preNodes = rowArray[0].split(","); // split rows by commas

        for (int i = 0; i < preNodes.length; i++) { // trim whitespace
            preNodes[i] = preNodes[i].trim();
        }

        for (int i = 0; i < GRAPH_DIMENSIONS; i++) { // construct nodes and place in array
            nodes[i] = new BellmanNode(preNodes[i + 1]); // skip preNodes[0]
        }

        return nodes;
    }

    /**
     * Constructs adjacency matrix for use in graph from file data rows
     * @param rowArray
     * @return
     */
    public static double[][] constructAdjMatrix(String[] rowArray) {
        
        double[][] adjMatrix = new double[GRAPH_DIMENSIONS][GRAPH_DIMENSIONS];

        for (int i = 0; i < GRAPH_DIMENSIONS; i++) {

            String[] splitRow = rowArray[i + 1].split(","); // split rows by commas, skip first element

            for (int j = 0; j < GRAPH_DIMENSIONS; j++) {

                String exchangeRateString = splitRow[j + 1]; // skip first element

                double exchangeRate = Double.parseDouble(exchangeRateString); // convert exchange rate to double

                double edgeWeight = exchangeToWeight(exchangeRate); // convert exchange rate to edge weight

                adjMatrix[i][j] = edgeWeight;
            }
        }

        return adjMatrix;
    }

    /**
     * Returns valid currency input from keyboard
     * @param CURRENCIES
     * @return
     */
    public static String getCurrencyInput(String[] CURRENCIES) {
        Scanner in = new Scanner(System.in);

        String currency = "";

        boolean validCurrency = false;

        while (!validCurrency) { // checks currency input against currencies from graph
            System.out.println("Currencies are: " + Arrays.toString(CURRENCIES));
            System.out.print("Please enter a currency from list above to find its max and direct exchange rates... ");

            currency = in.nextLine();

            for (int i = 0; i < CURRENCIES.length; i++) {
                //System.out.println("comparing " + CURRENCIES[i] + " to " + currency + " " + CURRENCIES[i].equals(currency));
                if (currency.equals(CURRENCIES[i])) { validCurrency = true; }
            }            
        }

        in.close();

        return currency;
    }

    /**
     * Finds and prints max and direct exchange rates for passed currency
     * against all other currencies. Uses BellmanFord for max exchange rate,
     * adjacency matrix for direct exchange rate in graph model
     * @param currency
     * @param CURRENCIES
     * @param currencyGraph
     */
    public static void findMaxAndDirectExchangeRatesFor(String currency, String[] CURRENCIES, BellmanGraph currencyGraph) {

        System.out.println("Source currency is " + currency);

        for (int i = 0; i < CURRENCIES.length; i++) { // find exchange rates for CURRENCIES[i]
            if (!currency.equals(CURRENCIES[i])) { // do not find exchange rates for self

                /* find max exchange rate; find shortest path using BellmanFord, convert weight to exchange rate */
                double maxExchangeRate = weightToExchange(currencyGraph.shortestPath(currency, CURRENCIES[i]));

                /* find direct exchange rate; find direct path / edge using adj matrix, convert weight to exchange rate */
                double directExchangeRate = weightToExchange(currencyGraph.adjMatrix[currencyGraph.getNodeIndex(currency)][i]);

                System.out.println(CURRENCIES[i] + ": max Exchange Rate is " + maxExchangeRate + ", and direct rate is " + directExchangeRate);
            }
        }
    }

    /**
     * Converts exchange rate to edge weight for graph
     * @param exchangeRate
     * @return
     */
    public static double exchangeToWeight(double exchangeRate) {
        return (-Math.log(exchangeRate));
    }

    /**
     * Converts edge weight to exchange rate for output
     * @param pathLength
     * @return
     */
    public static double weightToExchange(double pathLength) {
        return (Math.pow(Math.E, -pathLength));
    }
}