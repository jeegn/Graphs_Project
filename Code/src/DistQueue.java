public class DistQueue {
    private Dist[] queue;
    private int nextOpen;
    private int[] posInQueue;
    
    //constructor: set variables 
    public DistQueue(int n) {
	this.queue = new Dist[n];
	this.nextOpen = 0;
	this.posInQueue = new int[n];
	for(int i = 0; i < posInQueue.length; i++)
	    posInQueue[i] = -1;
    }
    
    //returns true if v is currently in the queue
    //and false otherwise
    public boolean inQueue(int v) {
	return posInQueue[v] > -1;
    }

    //insert Dist object into queue
    //v: the vertex number
    //d: the distance associated with that vertex
    public void insert(int v, int d) {
	Dist dist = new Dist(v, d);
	if(nextOpen == queue.length)
	    return;
	queue[nextOpen] = dist;
	swim(nextOpen);
	nextOpen++;
    }

    //remove and return the vertex with the current min distance
    public int delMin() {
	if(isEmpty())
	    return -1;
	Dist min = queue[0];
	nextOpen--;
	swap(0, nextOpen);
	queue[nextOpen] = null;
	sink(0);
	posInQueue[min.v] = -1;
	return min.v;
    }

    //return the number of elements currently in the queue
    public int size() {
	return nextOpen;
    }

    //return true if the queue is empty; false else
    public boolean isEmpty() {
	return size() == 0;
    }

    //return vertex v's position in the queue
    public int posInQueue(int v) {
	return posInQueue[v];
    }

    //set the distance value of vertex v
    //if v is not in the queue, insert it into the queue
    public void set(int v, int d) {
	int p = posInQueue[v];
	if(p == -1)
	    insert(v, d);
	else {
	    Dist dist = queue[p];
	    int old = dist.d;
	    dist.d = d;
	    if(old > d)
		swim(p);
	    else if(old < d)
		sink(p);
	}
    }
	
    //swim function
    private void swim(int i) {
	if(queue[i] == null)
	    return;
	int p = (int)((i-1)/2);
	while(i > 0) {
	    if(queue[i].d < queue[p].d) {
		swap(i, p);
		i = p;
		swim(i);
	    } else {
		return;
	    }
	}
    }

    //sink function
    private void sink(int i) {
	if(queue[i] == null)
	    return;
	int lc = 2*i + 1;
	int rc = 2*i + 2;
	if(lc > nextOpen - 1)
	    return;
	if(rc > nextOpen - 1) {
	    if(queue[i].d > queue[lc].d) {
		swap(i, lc);
		sink(lc);
	    }
	    return;
	}
	int s = lc;
	if(queue[rc].d < queue[lc].d)
	    s = rc;
	if(queue[i].d > queue[s].d) {
	    swap(i, s);
	    sink(s);
	}
    }    
      
    //swap function to swap two items in the queue
    private void swap(int i, int j) {
	if(i == j)
	    return;
	Dist temp = queue[i];
	int t = posInQueue[i];
	queue[i] = queue[j];
	posInQueue[i] = posInQueue[j];
	queue[j] = temp;
	posInQueue[j] = t;
    }

    //Dist class
    class Dist {
	int v;//vertex number
	int d;//distance

	public Dist(int v, int d) {
	    this.v = v;
	    this.d = d;
	}
    }
}
    