import java.util.ArrayList;
public class PartBTest {
    public static void main(String[] args) {
	//Test 1
	int testNum = 1;
	int graphNum = 5;
	int numReg = 3;
	int firstReg = 1;
	String[] interReg = new String[] {"F", "G", "E", "C"};
	runTest(testNum, graphNum, numReg, firstReg, interReg);
	//Test 2
	testNum++;
	graphNum = 2;
	numReg = 4;
	firstReg = 4;
	runTest(testNum, graphNum, numReg, firstReg, null);
    }

    private static void runTest(int testNum, int graphNum, 
				int numReg, int firstReg, String[] interReg) {
	System.out.println("*****BEGIN TEST " + testNum + "*****");
	Graph O = new Graph("src/files/graph" + graphNum + ".txt");
	Graph[] regions = new Graph[numReg];
	int j = 0;
	while(j < numReg) {
	    regions[j] = new Graph("src/files/region" + (firstReg + j) + ".txt");
	    j++;
	}
	Graph G = GlobalNet.run(O, regions);
	Graph E = new Graph("src/files/B_test_" + testNum +".txt");

    ArrayList<Edge> act = G.sortedEdges();
	ArrayList<Edge> exp = E.sortedEdges();

	boolean match = true;
	System.out.println("Checking if graph is correct...");
	if(act.size() != exp.size()) {
	    printMsg1(exp.size(), act.size());
	    match = false;
	} else {
	    for(int i = 0; i < act.size(); i++) {
		if(!act.get(i).equals(exp.get(i))) {
		    printMsg4(i, act.get(i), exp.get(i));
		    match = false;
		    break;
		}
	    }
	}
	printMsg3(testNum, match);
    }

    //Failure message when number of edges don't match
    private static void printMsg1(int exp, int act) {
	System.out.println("Error: Number of edges in graphs do not match.");
	System.out.println("Expected:\n" + exp + "\n");
	System.out.println("Actual:\n" + act + "\n");
    }

    //Failure message when inter-regional airports do not match
    private static void printMsg2(String code, boolean exp, boolean act) {
	System.out.println("Inter-regional value for " + code + " is incorrect.");
	System.out.println("Expected: " + exp);
	System.out.println("Actual: " + act);
    }

    //Message about overall test result
    private static void printMsg3(int testNum, boolean passed) {
	System.out.print("Test " + testNum);
	if(passed)
	    System.out.println(" passed!");
	else
	    System.out.println(" failed.");
    }

    //Message when two edges don't match
    private static void printMsg4(int i, Edge exp, Edge act) {
	System.out.println("The " + i + "th edge in the sorted edge lists " +
			   "do not match.");
	System.out.println("Expected:\n" + exp.toString() + "\n");
	System.out.println("Actual:\n" + act.toString() + "\n");
    }
}
	    