import java.util.ArrayList;
import java.util.Arrays;

public class Flow {
    private boolean[] visited;
    private int[] predecessor;
    private int[][] residual;  // gets updated
    private int[][] capacity;  // original state
    int vertexCt;

    public void findMinCut(){
        ArrayList<Integer> R = new ArrayList<>();
        for(int i = 1; i < vertexCt; i++){
            if(hasAugmentingPath(0, i)){
                R.add(i);
            }
        }

        // if nothing is in R, it means 0's edges are min cut edges
        if(R.size() == 0){
            R.add(0);
        }

        // remove the chance to find a path from an R item to another R item
        for(int r : R){
            for(int r2 : R){
                capacity[r][r2] = 0;
            }
        }

        // find what edges remain from R to somewhere else
        System.out.println("\nMIN CUT");
        for(int r : R){
            for(int i = 0; i < vertexCt; i++){
                if(capacity[r][i] != 0){
                    System.out.printf("Edge (%d, %d) transports %d\n", r, i, capacity[r][i]);
                }
            }
        }
        System.out.println("\n\n");
    }

    public void findMaxFlow(Graph graph){
        // set up lists we'll use later
        vertexCt = graph.getVertexCt();
        visited = new boolean[vertexCt];
        predecessor = new int[vertexCt];
        capacity = graph.getCapacity();
        residual = new int[vertexCt][vertexCt];
        for(int i = 0; i < vertexCt; i++){
            for(int j = 0; j < vertexCt; j++){
                residual[i][j] = capacity[i][j];
            }
        }
        int src = 0;
        int dest = vertexCt - 1;

        int totalFlow = 0;
        while(hasAugmentingPath(src, dest)){
            double availFlow = Double.POSITIVE_INFINITY;
            int prev;
            ArrayList<Integer> path = new ArrayList<>();

            // checks for the largest amount of flow we can send on the path
            for(int curr = dest; curr != src; curr = prev){
                prev = predecessor[curr];
                if(!visited[prev]){
                    availFlow = Math.min(availFlow, residual[curr][prev]);
                } else{
                    availFlow = Math.min(availFlow, residual[prev][curr]);
                }
            }

            // update residual plot
            for(int curr = dest; curr != src; curr = prev){
                path.add(curr);
                prev = predecessor[curr];
                if(!visited[prev]){
                    residual[prev][curr] += availFlow;
                    residual[curr][prev] -= availFlow;
                } else{
                    residual[prev][curr] -= availFlow;
                    residual[curr][prev] += availFlow;
                }
            }
            path.add(0);

            // print path
            System.out.printf("Path with flow (%.0f)", availFlow);
            for(int i = path.size() - 1; i >= 0; i--){
                System.out.printf(" %d", path.get(i));
            }
            System.out.println();

            totalFlow += availFlow;
        }
        printUsedFlow(totalFlow);
    }

    private boolean hasAugmentingPath(int src, int dest){
        Queue<Integer> queue = new Queue<>();

        queue.enqueue(src);
        clearVisited();
        visited[src] = true;

        while(!queue.isEmpty() && !visited[dest]){
            int curr = queue.dequeue();
            ArrayList<Integer> successors = getSuccessors(curr);

            // if there are no successors, we can use a back arrow
            if(successors.size() == 0){
                for(int i = 0; i < curr; i++){
                    if(residual[i][curr] < capacity[i][curr] && !visited[i]){
                        predecessor[i] = curr;
                        visited[dest] = false;
                        queue.enqueue(i);
                    }
                }
            }

            // if there are successors, continue normally
            for(int succ : successors){
                if(residual[curr][succ] > 0 && !visited[succ]){
                    predecessor[succ] = curr;
                    visited[succ] = true;
                    queue.enqueue(succ);
                    if(succ == dest){ return true; }
                }
            }
        }
        return false;
    }

    private void printUsedFlow(int maxFlow){
        System.out.printf("\nMax Flow %d\n", maxFlow);

        for(int src = 0; src < vertexCt; src++){
            for(int dest = 0; dest < vertexCt; dest++){
                if(residual[dest][src] != 0 && capacity[dest][src] == 0){
                    System.out.printf("Flow %d -> %d (%d)\n", src, dest, residual[dest][src]);
                }
            }
        }
    }

    private void clearVisited(){
        Arrays.fill(visited, false);
    }

    private ArrayList<Integer> getSuccessors(int src){
        ArrayList<Integer> successors = new ArrayList<>();
        for(int i = 0; i < vertexCt; i++){
            if(residual[src][i] != 0){
                successors.add(i);
            }
        }
        return successors;
    }

    public String printResiduals(){
        StringBuilder sb = new StringBuilder();
        sb.append("\n");
        for (int i = 0; i < vertexCt; i++) {
            for (int j = 0; j < vertexCt; j++) {
                sb.append(String.format("%5d", residual[i][j]));
            }
            sb.append("\n");
        }
        return sb.toString();
    }


}
