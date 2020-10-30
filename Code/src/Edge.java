public class Edge {
    public final String u;//the airport code for vertex u
    public final  String v;//the airport code for vertex v
    private int ui;//the int value for vertex u
    private int vi;//the int value for vertex v
    public final int w;//the weight of the edge

    //constructor
    public Edge(String u, String v, int w) {
	this.u = u;
	this.v = v;
	this.w = w;
    }

    //returns true if the two edges connect the same vertices
    //Note: We are assuming simple graphs in this project.
    public boolean equals(Object o) {
	if(o == null)
	    return false;
	Edge e = (Edge) o;
	if(this.w == e.w) {
	    if((this.u.equals(e.u) && this.v.equals(e.v)) ||
	       (this.v.equals(e.u) && this.u.equals(e.v)))
		return true;
	}
	return false;
    }

    //sets the index vi
    public void setVIndex(int vi) {
	this.vi = vi;
    }

    //sets the ui index
    public void setUIndex(int ui) {
	this.ui = ui;
    }

    //returns vi
    public int vi() {
	return vi;
    }

    //returns ui
    public int ui() {
	return ui;
    }

    //returns a String in the form (u, v, w) 
    public String toString() {
	return "(" + u + ", " + v + ", " + w + ")";
    }
}