package algo.dijkstra;

import java.util.*;

public class Dijkstra {

    class Cell {
        int id;
        int distanceFromOrigin;

        public Cell(int id, int weightFromOrigin) {
            this.id = id;
            this.distanceFromOrigin = weightFromOrigin;
        }
    }

    class Edge {
        Integer destination;
        int edgeWeight;

        public Edge(Integer destination, int edgeWeight) {
            this.destination = destination;
            this.edgeWeight = edgeWeight;
        }
    }

    Map<Integer, Integer> distanceTable; // a lookup table just for result display
    //Map<Cell, Boolean> cellStatusMap;
    Set<Cell> unvisited;
    Map<Integer, List<Edge>> graph;
    Map<Integer, Cell> cellMap;
    Map<Cell, Integer> cellIndexMap;

    public Dijkstra() {
        distanceTable = new HashMap<>();
        unvisited = new HashSet<>();
        graph = new HashMap<>();
        cellMap = new HashMap<>();
        cellIndexMap = new HashMap<>();
    }

    public void init(int[][] edgeTable, int n) {
        List<Cell> cellList = new ArrayList<>();
        for (int i = 1; i <= n; i++) {
            Cell cell = new Cell(i, Integer.MAX_VALUE);
            cellMap.put(i, cell);
            unvisited.add(cell);
            distanceTable.put(i, Integer.MAX_VALUE);
            graph.put(i, new ArrayList<>());
            cellIndexMap.put(cell, i);
        }
        for (int[] edge : edgeTable) {
            int origID = edge[0];
            int destID = edge[1];
            int weight = edge[2];
            graph.get(origID).add(new Edge(destID,weight));
            graph.get((destID)).add(new Edge(origID,weight));
        }


    }

    public  void getDistance(Integer start) {
        cellMap.get(start).distanceFromOrigin = 0;
        distanceTable.put(start, 0);
        Queue<Cell> minHeap = new PriorityQueue<>(
                (c1, c2) -> (c1.distanceFromOrigin - c2.distanceFromOrigin)
        );
        minHeap.offer(cellMap.get(start));
        //weighted bfs
        while (!minHeap.isEmpty()) {
            Cell cell = minHeap.poll();
            int cellIndex = cellIndexMap.get(cell);
            if (!unvisited.contains(cell)) {
                continue;
            }
            List<Edge> edges = graph.get(cellIndex);
            for (Edge edge : edges) {
                Integer destIndex = edge.destination;
                Cell dest = cellMap.get(destIndex);
                int newDistance = cell.distanceFromOrigin + edge.edgeWeight;
                if (newDistance < dest.distanceFromOrigin && unvisited.contains(dest)) { //update distance
                    //decreaseKey
                    Cell newCell = new Cell(dest.id, newDistance);
                    minHeap.offer(newCell);
                    unvisited.add(newCell);
                    unvisited.remove(dest);
                    distanceTable.put(newCell.id, newDistance);
                    cellMap.put(destIndex, newCell);
                    cellIndexMap.put(newCell, destIndex);
                }
            }
            unvisited.remove(cell);
        }
    }

    public static void main(String[] args) {
        int[][] edgeMatrix = {
                {1,2,3},
                {1,3,5},
                {2,3,1},
                {3,4,3},
                {2,3,3},
                {3,5,7},
                {4,5,2},
                {4,6,8},
                {2,6,5}
        };

        Dijkstra dijkstra = new Dijkstra();
        dijkstra.init(edgeMatrix, 6);
        dijkstra.getDistance(1);
        for (int nodeId : dijkstra.distanceTable.keySet()) {
            int dist = dijkstra.distanceTable.get(nodeId);
            System.out.println(nodeId + "  " + dist);
        }
    }

}
