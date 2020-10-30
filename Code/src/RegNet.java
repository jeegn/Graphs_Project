import java.util.ArrayList;

public class RegNet {
    //creates a regional network
    //G: the original graph
    //max: the budget
    public static Graph run(Graph G, int max) {
        //To be implemented
        System.out.println("Max: " + max);
        Graph copy = G;
        Graph MST = KruskalsMST(copy);

        if (MST.totalWeight() > max) {
            MST = maintainBudget(MST, max);
          }
        ArrayList<ArrayList<Edge>> stops = computeAirports(MST, G);
        MST = reAdd(MST, stops, max);
          return MST;
    }

    //private helper methods
    private static Graph KruskalsMST(Graph G) {
        ArrayList<Edge> edgeList = G.sortedEdges();
        UnionFind UF = new UnionFind(G.V());
        Graph MST = new Graph(G.V());
        MST.setCodes(G.getCodes());
        int count = 0;
        int i = 0;
        while (count < (G.V() - 1)) {
            Edge e = edgeList.get(i);
            i++;
            if (!UF.connected(e.ui(), e.vi())) {
                MST.addEdge(e);
                //System.out.println(e.u+" "+e.v+" "+e.w);
                UF.union(e.ui(), e.vi());
                count++;
            }
        }
        return MST;
    }

    private static Graph maintainBudget(Graph G, int max) {
        Graph updated = G.connGraph();
        ArrayList<Edge> edgeList = updated.sortedEdges();
        int i = edgeList.size() - 1;
        while ((updated.totalWeight() > max)) {
            for (int j = edgeList.size() - 1; j >= 0; j--) {
                int size = edgeList.size();
                Edge e = edgeList.get(j);
                Graph temp = updated.connGraph();
                temp.removeEdge(e);
                Graph connGraph = temp.connGraph();
                if (connectedComponents(connGraph.V(), connGraph.edges()) == 1) {
                    updated.removeEdge(e);
                    updated = updated.connGraph();
                    if (edgeList.size() == size)
                        edgeList.remove(j);
                    break;
                }
            }
        }
        return updated;
    }

    private static int connectedComponents(int vertices, ArrayList<Edge> edgeList) {
        UnionFind UF = new UnionFind(vertices);
        for (int i = 0; i < edgeList.size(); i++) {
            Edge e = edgeList.get(i);
            UF.union(e.ui(), e.vi());
        }
        return UF.connectedCount();
    }

    private static ArrayList<ArrayList<Edge>> computeAirports(Graph G, Graph OG) {
        ArrayList<ArrayList<Edge>> stops = new ArrayList<ArrayList<Edge>>();
        for (int i = 0; i < G.V(); i++) {
            stops.add(new ArrayList<Edge>());
        }
        for (int i = 0; i < G.V(); i++) {
            for (int j = i + 1; j < G.V(); j++) {
                int stopCount = stopCount(G, i, j);
                Edge e = OG.getEdge(OG.index(G.getCode(i)), OG.index(G.getCode(j)));
                ArrayList<Edge> column = stops.get(stopCount);
                if ((column.size() == 0) || stopCount == 0) {
                    column.add(e);
                    continue;
                }
                for (int k = 0; k < column.size(); k++) {
                    if (column.get(k).w < e.w) {
                        if (k == column.size() - 1) {
                            column.add(e);
                            break;
                        }
                        continue;
                    }
                    column.add(k, e);
                    break;
                }
            }
        }
        return stops;
    }

    private static int stopCount(Graph G, int source, int target) {
        ArrayList<Integer> Q = new ArrayList<Integer>();
        int[] edgeTo = new int[G.V()];
        Boolean[] marked = new Boolean[G.V()];
        for (int i = 0; i < G.V(); i++) {
            marked[i] = false;
        }
        Q.add(source);
        while (!Q.isEmpty()) {
            int v = Q.remove(0);
            marked[v] = true;
            ArrayList<Integer> adjacent = G.adj(v);
            for (int i = 0; i < adjacent.size(); i++) {
                int w = adjacent.get(i);
                if (marked[w])
                    continue;
                edgeTo[w] = v;
                if (w == target) {
                    int stops = 0;
                    while (v != source) {
                        stops++;
                        v = edgeTo[v];
                    }
                    return stops;
                }
                Q.add(w);
            }
        }
        return 0;
    }

    private static Graph reAdd(Graph G, ArrayList<ArrayList<Edge>> stops, int max) {
        int maxStops = G.V() - 1;
        while (stops.get(maxStops).size() == 0)
            maxStops--;
        if (maxStops == 0)
            return G;
        for (int i = maxStops; i > 0; i--) {
            for (int j = 0; j < stops.get(i).size(); j++) {
                Edge e = stops.get(i).get(j);
                if ((e.w + G.totalWeight()) > max)
                    break;
                G.addEdge(e);
            }
        }
        return G;
    }
}