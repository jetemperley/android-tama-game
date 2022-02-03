package com.tama.apptest;

import java.util.Comparator;
import java.util.PriorityQueue;

class Path {
    Vec2[] step;
    PriorityQueue<Node> que;
    Node[][] visited;
    World m;
    int dist = 0;

    Path(int nextTo){
        Vec2[] v = {new Vec2(0, 1), new Vec2(0, -1),new Vec2(1, 0),new Vec2(-1, 0)};
        step = v;
        dist = nextTo;
    }

    Path(Vec2[] steps) {
        step = steps;
    }

    Vec2[] findPath(World m, int ax, int ay, int bx, int by) {

        this.m = m;
        que = new PriorityQueue<Node>(20, new VComp());
        visited = new Node[m.width()][m.height()];
        Node n = new Node(ax, ay, 0, Math.abs(bx-ax)+ Math.abs(by-ay));
        que.add(n);
        visited[ax][ay] = n;

        int tx = 0, ty = 0;
        Node curr = null;
        while (que.size() > 0) {

            curr = que.poll();
            // println("polled " + curr.x, curr.y, " dist " + curr.dist);
            tx = curr.x;
            ty = curr.y;

            //if (tx == bx && ty == by)
            //  break;
            if (curr.dest == dist)
                break;

            for (Vec2 v : step) {
                if (m.canStepOnto(ax, ay, tx + v.x, ty+v.y) && visited[tx+v.x][ty+v.y] == null) {
                    n = new Node(tx+v.x, ty+v.y, curr.dist + 1, Math.abs(bx-tx-v.x) + Math.abs(by-ty-v.y));
                    que.add(n);
                    visited[tx+v.x][ty+v.y] = n;
                }
            }
        }

        if (curr == null || curr.dest != dist)
            return null;

        Vec2[] path = new Vec2[visited[curr.x][curr.y].dist];
        n = visited[curr.x][curr.y];

        for (int i = path.length-1; i > -1; i--) {

            path[i] = new Vec2(n.x, n.y);
            // println(n.x, n.y);
            tx = n.x;
            ty = n.y;

            for (Vec2 v : step) {
                if (A.inRange(visited, tx + v.x, ty+v.y) &&  visited[tx + v.x][ty+v.y] != null && visited[tx + v.x][ty+v.y].dist < n.dist) {
                    n = visited[tx + v.x][ty+v.y];
                    break;
                }
            }
        }

        return path;
    }
}

class Node {

    int x, y, dist, dest;

    Node() {
        x = 0;
        y = 0;
        dist = 0;
        dest = 0;
    }

    Node(int X, int Y, int DIST, int DEST) {
        x = X;
        y = Y;
        dist = DIST;
        dest = DEST;
    }
}

class VComp implements Comparator<Node> {
    public int compare(Node a, Node b) {
        return  (a.dist + a.dest) - (b.dist + b.dest);
    }
}
