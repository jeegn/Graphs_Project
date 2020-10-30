public class LargeGraphTest {
    public static void main(String[] args) {
	//create the original graph
	Graph O = new Graph("largeGraph1.txt");

	//create the regional graphs
	String[] codes = O.getCodes();
	Graph[] regions = new Graph[7];
	//Region 1
	int numV = 23;
	String[] reg1Codes = new String[numV];
	int r = 0;
	int s = 0;
	copyCodes(codes, s, reg1Codes);
	regions[r] = O.subgraph(reg1Codes);
	//Region 2
	r++;
	s += numV;
	s += 4;
	numV = 14;
	String[] reg2Codes = new String[numV];
	copyCodes(codes, s, reg2Codes);
	regions[r] = O.subgraph(reg2Codes);
	//Region 3
	r++;
	s += numV;
	s+= 5;
	numV = 9;
	String[] reg3Codes = new String[numV];
	copyCodes(codes, s, reg3Codes);
	regions[r] = O.subgraph(reg3Codes);
	//Region 4
	r++;
        s += numV;
        s+= 3;
        numV = 13;
        String[] reg4Codes = new String[numV];
        copyCodes(codes, s, reg4Codes);
	regions[r] = O.subgraph(reg4Codes);
	//Region 5
	r++;
        s += numV;
        s+= 1;
        numV = 25;
        String[] reg5Codes = new String[numV];
        copyCodes(codes, s, reg5Codes);
        regions[r] = O.subgraph(reg5Codes);
	//Region 6
	r++;
        s += numV;
        s+= 7;
        numV = 19;
        String[] reg6Codes = new String[numV];
        copyCodes(codes, s, reg6Codes);
        regions[r] = O.subgraph(reg6Codes);
	//Region 7
	r++;
        s += numV;
        s+= 5;
        numV = 16;
        String[] reg7Codes = new String[numV];
        copyCodes(codes, s, reg7Codes);
        regions[r] = O.subgraph(reg7Codes);

	boolean passed = true;

	//Build Regional Networks
	for(int i = 0; i < regions.length; i++) {
	    Graph R = regions[i];
	    regions[i] = RegNet.run(R, R.V()*500);
	    Graph E = new Graph("largeReg" + (i + 1) + ".txt");
	    if(!regions[i].equals(E)) {
		passed = false;
		printFailureMsg(i, E.toString(), regions[i].toString());
	    }
	}

	//Build Global Network
	Graph G = GlobalNet.run(O, regions);
	Graph E = new Graph("largeGlobalNet.txt");
	if(!G.equals(E)) {
	    passed = false;
	    printFailureMsg(-1, E.toString(), G.toString());
	}

	printMsg(passed);
    }

    private static void printMsg(boolean passed) {
	if(passed) 
	    System.out.println("Passed Robustness tests!");
	else
	    System.out.println("Did not pass all parts of the robustness tests.");
    }

    private static void printFailureMsg(int i, String exp, String act) {
	if(i < 0) 
	    System.out.println("Global Network does not match expected.");
	else
	    System.out.println("Regional Network " + (i + 1) + " does not match.");
	System.out.println("\nExpected:\n" + exp);
	System.out.println("\nActual:\n" + act);
    }

    private static void copyCodes(String[] orig, int start, String[] reg) {
	for(int i = 0; i < reg.length; i++) 
	    reg[i] = orig[i + start];
    }
}