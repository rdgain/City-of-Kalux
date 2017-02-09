package game;

import utilities.Constants;

import static utilities.Constants.*;

import java.awt.*;
import java.util.ArrayList;

public abstract class WorldObject extends GameObject {

    public int size;
    //Image image = Constants.ASTEROID1;
    public int id;

    public WorldObject() {
        super();
    }

    public WorldObject(int x, int y, int w, int h, KaluxGame game) {
        super (game,x,y,w,h);
    }

    //random location
    public WorldObject(int w, int h, KaluxGame game) {
        super (game, (int) Math.random() * (FRAME_WIDTH - 2 * w), (int) Math.random() * (FRAME_HEIGHT - 2 * h), w, h);
    }

    @Override
    int size() {
        return h*w;
    }

}