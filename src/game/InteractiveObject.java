package game;

import static utilities.Constants.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class InteractiveObject extends WorldObject {
    Player player;
    Color color = Color.WHITE;
    int type; // 1 - pickup // 2 - door
    boolean alive;
    int open; // if type = 1, open = -1 // if type = 2 and open, open = 1 // if type = 2 and closed, open = 0
    BufferedImage imgMap, imgBP, imgLarge;
    String content;

    public InteractiveObject (KaluxGame game, Player player, int x, int y, int w, int h, Color color, int id, int t, int st, int map) {
        super();
        this.game = game;
        this.player = player;
        this.x = x;
        this.y = y;
        if (t == 1) {
            this.w = 1;
            this.h = 1;
        }
        else {
            this.h = h;
            this.w = w;
        }
        this.color = color;
        this.id = id;
        this.type = t;
        this.open = st;
        alive = true;
        if (type == 1) {
            switch (id) {
                case 1: imgBP = CLOTHES; break;
                case 2: imgBP = KEY1; break;
                case 3: imgBP = NOTE; imgLarge = NOTE1; break;
                case 4: imgBP = KEY2; break;
                case 5: imgBP = NOTE; imgLarge = NOTE2; break;
                case 6: imgBP = NOTE; imgLarge = NOTE3; break;
                case 7: imgBP = NOTE; imgLarge = NOTE4; break;
                case 8: imgBP = FOOD; break;
                default: break;
            }
        }
        else {
            imgBP = null;
        }
        this.map = map;
        content = "";


        room = new ArrayList<Background>();
        visited = false;

        //assign rooms to objects
        ArrayList<GameObject> obj;
        switch (map) {
            case 1: obj = game.worldObjectsM1L1; break;
            case 2: obj = game.worldObjectsM2L1; break;
            case 3: obj = game.worldObjectsM3L1; break;
            case 4: obj = game.worldObjectsM4L1; break;
            default: obj = game.worldObjectsM5L1; break;
        }
        for (GameObject i: obj) {
            if (i instanceof Background) {
                Background o = (Background)i;
                if (this.x >= o.x && this.x <= (o.x + o.w) && this.y >= o.y && this.y <= (o.y + o.h)) {
                    room.add(o);
                }
            }
        }
    }

    public void execute () { //effect of object when used
        player = game.player;
        switch (id) {
            case 2:
                if (game.currentMap == 2 && (player.x >= 42 && player.x <= 44 && player.y == 54)) {
                    game.backpack.remove(this);
                    game.backpack.add(new InteractiveObject(game, player, player.x, player.y, 1, 1, Color.GREEN, 4, 1, -1, 2));
                    content = "";
                } else {
                    content = "This key could be used to open something ...";
                }
                break;
            case 4:
                content = "This key could be used to open something ...";
                break;
            case 8:
                content = "Food! Yum!";
                break;
            default: break;
        }
    }

    @Override
    void draw(Graphics2D g) {
        g.setColor(color);
        g.fillRect(x* SCALE+19, y*SCALE+19, w*SCALE, h*SCALE);
    }
}
