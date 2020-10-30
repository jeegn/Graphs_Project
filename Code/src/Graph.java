import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Arrays;

public class Graph {
    private String[] codes;//the airport codes
    private HashMap<String, Integer> codeMap;//key = airport code; value = vertex number
    private ArrayList<Integer>[] adjList;//adjacency list representation
    private ArrayList<Edge> edges;//edge list representation
    private Edge[][] matrix;//adjacency matrix representation
    private int n;//number of vertices
    private int m;//number of edges
    private int w;//total weight of graph
    private boolean stray[];//keeps track of vertices that have no incident edges
    private int strayCount;//keeps track of the number of stray vertices

    //constructor: empty graph with n vertices
    public Graph(int n) {
	initialize(n);
    }

    //constructor: builds graph from file input
    public Graph(String fn) {
	BufferedReader br;
	try {
	    br = new BufferedReader(new FileReader(fn));
	    String line = br.readLine();
	    if(line != null) {
		String[] split = line.split(",");
		initialize(split.length);
		for(int i = 0; i < n; i++) { 
		    codes[i] = split[i];
		    codeMap.put(split[i], i);
		}
	    }
	    line = br.readLine();
	    while(line != null) {
		String[] split = line.split(",");
		if(split.length < 3)
		    break;
		addEdge(split[0], split[1], Integer.parseInt(split[2]));
		line = br.readLine();
	    }
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    //sets airport codes to argument and 
    //inputs them into the code map
    public void setCodes(String[] codes) {
	this.codes = codes;
	for(int i = 0; i < codes.length; i++)
	    codeMap.put(codes[i], i);
    }

    //returns the airport codes
    public String[] getCodes() {
	return this.codes;
    }

    //returns the airport corresponding
    //to the vertex number v
    public String getCode(int v) {
	return codes[v];
    }

    //returns the number of vertices
    public int V() {
	return this.n;
    }

    //returns the Edge object connecting
    //u and v (vertex numbers)
    public Edge getEdge(int u, int v) {
	return matrix[u][v];
    }

    //returns the weight of the edge connecting
    //u and v (vertex numbers)
    public int getEdgeWeight(int u, int v) {
	Edge e = getEdge(u, v);
	if(e != null)
	    return e.w;
	return 0;
    }

    //returns the number of edges
    public int E() {
	return this.m;
    }

    //returns the degree of vertex v (airport code)
    public int deg(String v) {
	ArrayList<Integer> adj = adj(v);
	if(adj != null)
	    return adj.size();
	return 0;
    }

    //returns the total weight of the graph
    public int totalWeight() {
	return w;
    }

    //adds edge (u, v, w) where u and v are airport codes
    //and w is the weight
    public void addEdge(String u, String v, int w) {
	Edge e = new Edge(u, v, w);
	addEdge(e);
    }

    //adds edge (u, v, w) where u and v are vertex numbers
    //and w is the weight
    public void addEdge(int u, int v, int w) {
	Edge e = new Edge(codes[u], codes[v], w);
	addEdge(e);
    }

    //adds edge e
    public void addEdge(Edge e) {
	if(e == null)
	    return;
	int iu = index(e.u);
	int iv = index(e.v);
	e.setUIndex(iu);
	e.setVIndex(iv);
	if(iu == -1 || iv == -1)
	    return;
	if(matrix[iu][iv] != null || matrix[iv][iu] != null)
	    return;
	if(adjList[iu] == null)
	    adjList[iu] = new ArrayList<Integer>();
	adjList[iu].add(iv);
	if(adjList[iv] == null)
	    adjList[iv] = new ArrayList<Integer>();
	adjList[iv].add(iu);
	edges.add(e);
	matrix[iu][iv] = e;
	matrix[iv][iu] = e;
	this.w += e.w;
	m++;
    }
    
    //returns the vertex number of airport code v
    public int index(String v) {
	Integer i = codeMap.get(v);
	if(i != null)
	    return i.intValue();
	return -1;
    }

    //removes edge e from the graph
    public void removeEdge(Edge e) {
	ArrayList<Integer> list1 = adj(e.v);
	if(removeEdgeFromList(list1, e)) {
	    ArrayList<Integer> list2 = adj(e.u);
	    if(removeEdgeFromList(list2, e)) {
		for(int i = 0; i < edges.size(); i++) {
		    if(edges.get(i).equals(e)) {
			edges.remove(i);
			break;
		    }
		}
		if(list1.isEmpty()) {
		    stray[e.vi()] = true;
		    strayCount++;
		}
		if(list2.isEmpty()) {
		    stray[e.ui()] = true;
		    strayCount++;
		}
		m--;
		w -= e.w;
		matrix[e.ui()][e.vi()] = null;
	        matrix[e.vi()][e.ui()] = null;
	    }
	}    
    }

    //returns the adjancency list for vertex v (vertex number)
    public ArrayList<Integer> adj(int v) {
	return adjList[v];
    }

    //returns the adjacency list for vertex v (airport code)
    public ArrayList<Integer> adj(String v) {
	return adjList[codeMap.get(v)];
    }

    //returns the edges in sorted order (smallest weight to largest weight)
    public ArrayList<Edge> sortedEdges() {
	edges.sort(new EdgeSort());
	return edges;
    }

    //returns the list of edges
    public ArrayList<Edge> edges() {
	return edges;
    }

    //returns true if the Graph's String representations match
    //and false otherwise
    public boolean equals(Object O) {
	if(O != null) {
	    Graph G = (Graph) O;
	    if(this.toString().equals(O.toString()))
		return true;
	}
	return false;
    }

    //returns a String representation of the graph
    //in adjancency list form
    public String toString() {
	String str = "";
	for(int i = 0; i < n; i++) {
	    str += codes[i] + ": ";
	    ArrayList<Integer> adj = adj(codes[i]);
	    if(adj != null)
		for(Integer u : adj)
		    str += matrix[i][u].toString();
	    str += "\n";
	}
	return str;
    }

    //returns a String representation of the graph
    //in the form necessary for creating the graph
    //from an input file
    public String toInputString() {
	String str = "";
	for(int i = 0; i < codes.length - 1; i++) 
	    str += codes[i] + ",";
	str += codes[n-1] + "\n";
	for(Edge e : edges) 
	    str += e.u + "," + e.v + "," + e.w + "\n";
	return str;
    } 

    //creates a subgraph of Graph G including the 
    //airport codes in the array "vertices" and all 
    //the edges that connect them
    public Graph subgraph(String[] vertices) {
	Graph newGraph = new Graph(vertices.length);
	newGraph.setCodes(vertices);
	for(String v : vertices) {
	    ArrayList<Integer> adj = adj(v);
	    if(adj != null) {
		for(Integer u : adj) {
		    newGraph.addEdge(matrix[codeMap.get(v)][u]);
		}
	    }
	}
	return newGraph;
    }

    //creates a new graph from the current graph
    //that removes all the stray vertices
    public Graph connGraph() {
	int n = this.n - strayCount;
	String[] codes = new String[n];
	int i = 0;
	for(int j = 0; j < this.stray.length; j++) {
	    if(stray[j] == false) {
		codes[i] = this.codes[j];
		i++;
	    }
	}
	Graph N = new Graph(n);
	N.setCodes(codes);
	for(Edge e : this.edges) 
	    N.addEdge(e);
	return N;
    }

    /* PRIVATE METHODS */
    //initializes data structures and variables
    private void initialize(int n) {
	this.n = n;
	this.m = 0;
	this.w = 0;
	this.codes = new String[n];
	this.adjList = new ArrayList[n];
	this.edges = new ArrayList<Edge>();
	this.matrix = new Edge[n][n];
	this.codeMap = new HashMap<String, Integer>(n);
	this.stray = new boolean[n];
	this.strayCount = 0; 
    }

    //helper method to remove edge e from list "list"
    private boolean removeEdgeFromList(ArrayList<Integer> list, Edge e) {
	for(int i = 0; i < list.size(); i++) {
	    if(list.get(i).intValue() == codeMap.get(e.v) ||
	       list.get(i).intValue() == codeMap.get(e.u)) {
		list.remove(i);
		return true;
	    }
	}  
	return false;
    }
}