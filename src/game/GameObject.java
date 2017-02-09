package game;

import java.awt.*;
import java.util.ArrayList;

abstract public class GameObject {

    KaluxGame game;
    // position
    int x, y, w, h;
    //map
    int map; // 1-5
    boolean dead = false;
    ArrayList<Background> room;
    boolean visited;

    public GameObject () {
        this.x = 0;
        this.y = 0;
        this.w = 0;
        this.h = 0;
    }

    public GameObject (KaluxGame game, int x, int y, int w, int h) {
        this.game = game;
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;

    }

    //returns distance in map squares between the current object and o
    public double dist (GameObject o) {
        return o.x > this.x ? o.x : this.x + o.y > this.y ? o.y : this.y;
    }

    abstract int size();
    abstract void draw(Graphics2D g);

}
