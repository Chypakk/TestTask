package org.chypakk;

public class DSU {
    private final int[] parent;
    private final int[] rank;

    public DSU(int n){
        parent = new int[n];
        rank = new int[n];
        for (int i = 0; i < n; i++) {
            parent[i] = i;
        }
    }

    public int find(int x){
        if (parent[x] != x) parent[x] = find(parent[x]);
        return parent[x];
    }

    public void union(int x, int y){
        int rankX = find(x);
        int rankY = find(y);
        if (rankX == rankY) return;

        if (rank[rankX] < rank[rankY]) parent[rankX] = rankY;
        else if (rank[rankY] < rank[rankX]) parent[rankY] = rankX;
        else {
            parent[rankY] = rankX;
            rank[rankX]++;
        }
    }
}
