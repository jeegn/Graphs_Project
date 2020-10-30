public class UnionFind {
    private int[] id;
    private int[] sizes;
    private int c;
    private int cc;
    
    //constructor: n is the number of vertices
    public UnionFind(int n) {
	id = new int[n];
	sizes = new int[n];
	this.c = n;
	this.cc = n;
	for(int i = 0; i < n; i++) {
	    id[i] = i;
	    sizes[i] = 1;
	}
    }

    //unions nodes p & q if they are not
    //already connected
    public void union(int p, int q) {
	int id_p = find(p);
	int id_q = find(q);
	if(id_p == id_q)
	    return;
	if(sizes[p] <= sizes[q]) {
	    id[id_p] = q;
	    sizes[q] += sizes[p];
	} else {
	    id[id_q] = p;
	    sizes[p] += sizes[q];
	}
	cc--;
    }

    //returns the connected component
    //containing p
    public int find(int p) {
	int t = p;
	while(t != id[t]) 
	    t = id[t];
	return t;
    }

    //returns true if p and q are 
    //in the same component
    //and false otherwise
    public boolean connected(int p, int q) {
	return find(p) == find(q);
    }
    
    //returns the number of components
    public int count() {
	return c;
    }

    //returns the number of connected components
	public int connectedCount() { return cc;}
}
	