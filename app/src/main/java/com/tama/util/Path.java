package com.tama.util;

import com.tama.core.World;
import com.tama.thing.Pet;

import java.util.Comparator;
import java.util.PriorityQueue;

public class Path
{
    final Vec2[] step;
    PriorityQueue<Node> que;
    Node[][] visited;
    World m;
    int dist = 0;

    /**
     * @param aimDist The distance from the target to aim for. Eg 1 means the pathing will stop when
     *                a path to any space 1 square away is found
     */
    public Path(int aimDist)
    {

        step = Pet.steps;
        dist = aimDist;
    }

    /**
     * find path from position (ax, ay) to (bx, by) in the specified world
     * @param world
     * @param ax
     * @param ay
     * @param bx
     * @param by
     * @return
     */
    public Vec2<Integer>[] findPath(World world, Pet pet, int ax, int ay, int bx, int by)
    {

        this.m = world;
        que = new PriorityQueue<Node>(20, new VComp());
        visited = new Node[world.width()][world.height()];
        Node n = new Node(ax, ay, 0, Math.abs(bx - ax) + Math.abs(by - ay));
        que.add(n);
        visited[ax][ay] = n;

        int tx = 0, ty = 0;
        Node curr = null;
        while (que.size() > 0)
        {

            curr = que.poll();
            // println("polled " + curr.x, curr.y, " dist " + curr.dist);
            tx = curr.x;
            ty = curr.y;

            //if (tx == bx && ty == by)
            //  break;
            if (curr.dest == dist)
            {
                break;
            }

            for (Vec2<Integer> v : step)
            {
                if (pet.canMoveOnto(world.getTile(tx + v.x, ty + v.y)) &&
                        visited[tx + v.x][ty + v.y] == null)
                {
                    n = new Node(tx + v.x,
                                 ty + v.y,
                                 curr.dist + 1,
                                 Math.abs(bx - tx - v.x) + Math.abs(by - ty - v.y));
                    que.add(n);
                    visited[tx + v.x][ty + v.y] = n;
                }
            }
        }

        if (curr == null || curr.dest != dist)
        {
            return null;
        }

        Vec2<Integer>[] path = new Vec2[visited[curr.x][curr.y].dist];
        n = visited[curr.x][curr.y];

        for (int i = path.length - 1; i > -1; i--)
        {

            path[i] = new Vec2<Integer>(n.x, n.y);
            // println(n.x, n.y);
            tx = n.x;
            ty = n.y;

            for (Vec2<Integer> v : step)
            {
                if (A.inRange(visited, tx + v.x, ty + v.y) && visited[tx + v.x][ty + v.y] != null &&
                        visited[tx + v.x][ty + v.y].dist < n.dist)
                {
                    n = visited[tx + v.x][ty + v.y];
                    break;
                }
            }
        }

        return path;
    }
}

class Node
{

    int x, y, dist, dest;

    Node()
    {
        x = 0;
        y = 0;
        dist = 0;
        dest = 0;
    }

    Node(int X, int Y, int DIST, int DEST)
    {
        x = X;
        y = Y;
        dist = DIST;
        dest = DEST;
    }
}

class VComp implements Comparator<Node>
{
    public int compare(Node a, Node b)
    {
        return (a.dist + a.dest) - (b.dist + b.dest);
    }
}
