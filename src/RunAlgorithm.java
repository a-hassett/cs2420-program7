public class RunAlgorithm {
    public static void main(String[] args){
        MaxFlowMinCut("demands1.txt");
        MaxFlowMinCut("demands2.txt");
        MaxFlowMinCut("demands3.txt");
        MaxFlowMinCut("demands4.txt");
        MaxFlowMinCut("demands5.txt");
        MaxFlowMinCut("demands6.txt");
        MaxFlowMinCut("demands7.txt");
    }

    private static void MaxFlowMinCut(String filename){
        Graph graph = new Graph();
        graph.makeGraph(filename);
        System.out.println(graph);

        Flow graphFlow = new Flow();
        graphFlow.findMaxFlow(graph);
        graphFlow.findMinCut();

        //System.out.println(graphFlow.printResiduals());
    }
}
