package game;

import static utilities.Constants.*;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Decor extends WorldObject {
    Player player;
    Color color = Color.WHITE;
    int type; // 1-wall // 2-object // 3 - river // 4 - cave wall
    char orientation; // U - UP // D - DOWN // L - LEFT // R - RIGHT // A - ABSOLUTE
    BufferedImage img;
    AffineTransform transform;
    AffineTransformOp op;
    ArrayList<Background> room;
    boolean visited;

    public Decor (KaluxGame game, Player player, int x, int y, int w, int h, Color color, int id, int t, int map, char or) {
        super();
        this.game = game;
        this.player = player;
        this.x = x;
        this.y = y;
        this.h = h;
        this.w = w;
        this.color = color;
        this.id = id;
        this.type = t;
        this.map = map;
        this.orientation = or;
        room = new ArrayList<Background>();
        visited = false;

        //assign rooms to objects
        if (type == 2) {
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
    }

    @Override
    void draw(Graphics2D g) {
        if (type == 1)
            g.drawImage(WALL, x * SCALE + 19, y * SCALE + 19, null);
        else if (type == 3) {
            g.setPaint(new TexturePaint(WATER, new Rectangle(0, 0, 150, 108)));
            g.fillRect(x * SCALE + 19, y * SCALE + 19, w * SCALE, h * SCALE);
        }
        else if (type == 4) {
            g.drawImage(WALLC, x * SCALE + 19, y * SCALE + 19, null);
        }
        else if (type == 2) {
            switch (id) {
                case 1:
                    g.drawImage(TABLE2, x * SCALE + 19, y * SCALE + 19, null);
                    break;
                case 2:
                    g.drawImage(BED1, x * SCALE + 19, y * SCALE + 19, null);
                    break;
                case 3:
                    img = TOILET;
                    transform = new AffineTransform();
                    switch (orientation) {
                        case 'U':
                            transform.rotate(Math.toRadians(180), img.getWidth()/2, img.getHeight()/2);
                            op = new AffineTransformOp(transform, AffineTransformOp.TYPE_BILINEAR);
                            img = op.filter(img, null);
                            break;
                        default: break;
                    }
                    g.drawImage(img, x * SCALE + 19, y * SCALE + 19, null);
                    break;
                case 4:
                    img = SINK;
                    transform = new AffineTransform();
                    switch (orientation) {
                        case 'U':
                            transform.rotate(Math.toRadians(180), img.getWidth()/2, img.getHeight()/2);
                            op = new AffineTransformOp(transform, AffineTransformOp.TYPE_BILINEAR);
                            img = op.filter(img, null);
                            break;
                        case 'L':
                            transform.rotate(Math.toRadians(90), img.getWidth()/2, img.getHeight()/2);
                            op = new AffineTransformOp(transform, AffineTransformOp.TYPE_BILINEAR);
                            img = op.filter(img, null);
                            break;
                        case 'R':
                            transform.rotate(Math.toRadians(270), img.getWidth()/2, img.getHeight()/2);
                            op = new AffineTransformOp(transform, AffineTransformOp.TYPE_BILINEAR);
                            img = op.filter(img, null);
                            break;
                        default: break;
                    }
                    g.drawImage(img, x * SCALE + 19, y * SCALE + 19, null);
                    break;
                case 5:
                    g.drawImage(TUB, x * SCALE + 19, y * SCALE + 19, null);
                    break;
                case 6:
                    img = MIRROR;
                    if (w>h)
                        img = MIRRORH;
                    g.drawImage(img, x * SCALE + 19, y * SCALE + 19, null);
                    break;
                case 7:
                    img = DRESSER;
                    if (w<h) {
                        img = DRESSERV;
                    }
                    g.drawImage(img, x * SCALE + 19, y * SCALE + 19, null);
                    break;
                case 8:
                    img = CHAIR;
                    transform = new AffineTransform();
                    switch (orientation) {
                        case 'U':
                            transform.rotate(Math.toRadians(180), img.getWidth()/2, img.getHeight()/2);
                            op = new AffineTransformOp(transform, AffineTransformOp.TYPE_BILINEAR);
                            img = op.filter(img, null);
                            break;
                        case 'L':
                            transform.rotate(Math.toRadians(90), img.getWidth()/2, img.getHeight()/2);
                            op = new AffineTransformOp(transform, AffineTransformOp.TYPE_BILINEAR);
                            img = op.filter(img, null);
                            break;
                        case 'R':
                            transform.rotate(Math.toRadians(270), img.getWidth()/2, img.getHeight()/2);
                            op = new AffineTransformOp(transform, AffineTransformOp.TYPE_BILINEAR);
                            img = op.filter(img, null);
                            break;
                        default: break;
                    }
                    g.drawImage(img, x * SCALE + 19, y * SCALE + 19, null);
                    break;
                case 9:
                    g.drawImage(TABLE3, x * SCALE + 19, y * SCALE + 19, null);
                    break;
                case 10:
                    g.drawImage(TVV, x * SCALE + 19, y * SCALE + 19, null);
                    break;
                case 11:
                    g.drawImage(BIN, x * SCALE + 19, y * SCALE + 19, null);
                    break;
                case 12:
                    img = ACHAIR;
                    transform = new AffineTransform();
                    switch (orientation) {
                        case 'U':
                            transform.rotate(Math.toRadians(180), img.getWidth()/2, img.getHeight()/2);
                            op = new AffineTransformOp(transform, AffineTransformOp.TYPE_BILINEAR);
                            img = op.filter(img, null);
                            break;
                        case 'L':
                            transform.rotate(Math.toRadians(90), img.getWidth()/2, img.getHeight()/2);
                            op = new AffineTransformOp(transform, AffineTransformOp.TYPE_BILINEAR);
                            img = op.filter(img, null);
                            break;
                        case 'R':
                            transform.rotate(Math.toRadians(270), img.getWidth()/2, img.getHeight()/2);
                            op = new AffineTransformOp(transform, AffineTransformOp.TYPE_BILINEAR);
                            img = op.filter(img, null);
                            break;
                        default: break;
                    }
                    g.drawImage(img, x * SCALE + 19, y * SCALE + 19, null);
                    break;
                case 13:
                    img = PAINTING;
                    if (w>h)
                        img = PAINTINGH;
                    g.drawImage(img, x * SCALE + 19, y * SCALE + 19, null);
                    break;
                case 14:
                    img = FRIDGE;
                    if (w<h)
                        img = FRIDGEV;
                    g.drawImage(img, x * SCALE + 19, y * SCALE + 19, null);
                    break;
                case 15:
                    g.drawImage(COUNTER, x * SCALE + 19, y * SCALE + 19, null);
                    break;
                case 16:
                    g.drawImage(COOKER, x * SCALE + 19, y * SCALE + 19, null);
                    break;
                case 17:
                    img = BOOKCASE;
                    if (w<h)
                        img = BOOKCASEV;
                    g.drawImage(img, x * SCALE + 19, y * SCALE + 19, null);
                    break;
                case 18:
                    g.drawImage(PLANT, x * SCALE + 19, y * SCALE + 19, null);
                    break;
                case 19:
                    img = BED2;
                    if (w>h)
                        img = BED2H;
                    g.drawImage(img, x * SCALE + 19, y * SCALE + 19, null);
                    break;
                case 20:
                    img = COUCH;
                    if (w<h) {
                        img = COUCHV;
                    }
                    g.drawImage(img, x * SCALE + 19, y * SCALE + 19, null);
                    break;
                case 21:
                    g.drawImage(LAMP, x * SCALE + 19, y * SCALE + 19, null);
                    break;
                case 22:
                    g.drawImage(BCOUNTER, x * SCALE + 19, y * SCALE + 19, null);
                    break;
                case 23:
                    img = CUBICLE;
                    transform = new AffineTransform();
                    switch (orientation) {
                        case 'U':
                            transform.rotate(Math.toRadians(90), img.getWidth()/2, img.getHeight()/2);
                            op = new AffineTransformOp(transform, AffineTransformOp.TYPE_BILINEAR);
                            img = op.filter(img, null);
                            break;
                        case 'D':
                            transform.rotate(Math.toRadians(270), img.getWidth()/2, img.getHeight()/2);
                            op = new AffineTransformOp(transform, AffineTransformOp.TYPE_BILINEAR);
                            img = op.filter(img, null);
                            break;
                        case 'R':
                            transform.rotate(Math.toRadians(180), img.getWidth()/2, img.getHeight()/2);
                            op = new AffineTransformOp(transform, AffineTransformOp.TYPE_BILINEAR);
                            img = op.filter(img, null);
                            break;
                        default: break;
                    }
                    g.drawImage(img, x * SCALE + 19, y * SCALE + 19, null);
                    break;
                case 24:
                    g.drawImage(TABLEP, x * SCALE + 19, y * SCALE + 19, null);
                    break;
                case 25:
                    img = BENCH;
                    if (w<h) {
                        img = BENCHV;
                        if (orientation == 'L') {
                            img = BENCHV1;
                        }
                    }
                    else
                    if (orientation == 'U') {
                        img = BENCH1;
                    }
                    g.drawImage(img, x * SCALE + 19, y * SCALE + 19, null);
                    break;
                case 26:
                    g.drawImage(FOUNTAIN, x * SCALE + 10, y * SCALE + 10, null);
                    break;
                case 27:
                    g.drawImage(TREE, x * SCALE + 10, y * SCALE + 10, null);
                    break;
                case 28:
                    g.drawImage(FLOWER1, x * SCALE + 19, y * SCALE + 19, null);
                    break;
                case 29:
                    img = DESK;
                    if (w<h) {
                        img = DESKV;
                    }
                    g.drawImage(img, x * SCALE + 19, y * SCALE + 19, null);
                    break;
                case 30:
                    g.drawImage(STAIRS, x * SCALE + 19, y * SCALE + 19, null);
                    break;
                case 31:
                    img = SHOWER;
                    if (map == 5)
                        img = SHOWER1;
                    g.drawImage(img, x * SCALE + 19, y * SCALE + 19, null);
                    break;
                case 32:
                    img = FCABINET;
                    transform = new AffineTransform();
                    switch (orientation) {
                        case 'U':
                            transform.rotate(Math.toRadians(180), img.getWidth()/2, img.getHeight()/2);
                            op = new AffineTransformOp(transform, AffineTransformOp.TYPE_BILINEAR);
                            img = op.filter(img, null);
                            break;
                        case 'L':
                            transform.rotate(Math.toRadians(90), img.getWidth()/2, img.getHeight()/2);
                            op = new AffineTransformOp(transform, AffineTransformOp.TYPE_BILINEAR);
                            img = op.filter(img, null);
                            break;
                        case 'R':
                            transform.rotate(Math.toRadians(270), img.getWidth()/2, img.getHeight()/2);
                            op = new AffineTransformOp(transform, AffineTransformOp.TYPE_BILINEAR);
                            img = op.filter(img, null);
                            break;
                        default: break;
                    }
                    g.drawImage(img, x * SCALE + 19, y * SCALE + 19, null);
                    break;
                case 33:
                    img = CDESK;
                    if (orientation == 'U')
                        img = CDESK1;
                    g.drawImage(img, x * SCALE + 19, y * SCALE + 19, null);
                    break;
                case 34:
                    g.drawImage(DUMMY, x * SCALE + 19, y * SCALE + 19, null);
                    break;
                case 35:
                    img = WEAPONR;
                    if (w<h)
                        img = WEAPONR1;
                    g.drawImage(img, x * SCALE + 19, y * SCALE + 19, null);
                    break;
                case 36:
                    g.drawImage(GAZEBO, x * SCALE + 19, y * SCALE + 19, null);
                    break;
                default:
                    g.setColor(color);
                    g.fillRect(x * SCALE + 19, y * SCALE + 19, w * SCALE, h * SCALE);
            }
        }
        else {
            g.setColor(color);
            g.fillRect(x * SCALE + 19, y * SCALE + 19, w * SCALE, h * SCALE);
        }
    }

}