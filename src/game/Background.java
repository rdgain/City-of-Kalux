package game;

import utilities.Constants;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Background extends WorldObject {
    Player player;
    Color color = Color.WHITE;
    int type; // 1-indoor open ground // 2-outdoor open ground // 3-street

    public Background (KaluxGame game, Player player, int x, int y, int w, int h, Color color, int id, int t, int map) {
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
    }

    @Override
    void draw(Graphics2D g) {
        if (type == 3) { // street
            g.setPaint(new TexturePaint(Constants.STREET, new Rectangle(0, 0, 150, 108)));
            g.fillRect(x * Constants.SCALE + 19, y * Constants.SCALE + 19, w * Constants.SCALE, h * Constants.SCALE);
        }
        else if (type == 2){ // outdoor open ground
            if (map == 3) {
                g.drawImage(Constants.GROUND_PARK, x * Constants.SCALE + 19, y * Constants.SCALE + 19, null);
            }
            else if (map == 5) {
                g.setPaint(new TexturePaint(Constants.GROUND_5, new Rectangle(0, 0, 500, 500)));
                g.fillRect(x * Constants.SCALE + 19, y * Constants.SCALE + 19, w * Constants.SCALE, h * Constants.SCALE);
            }
            else {
                g.setPaint(new TexturePaint(Constants.GROUND, new Rectangle(0, 0, 150, 108)));
                g.fillRect(x * Constants.SCALE + 19, y * Constants.SCALE + 19, w * Constants.SCALE, h * Constants.SCALE);
            }
        }
        else { // indoor open ground
            if (map == 5) {
                g.setPaint(new TexturePaint(Constants.GROUND_CAVE, new Rectangle(0, 0, 300, 300)));
                g.fillRect(x * Constants.SCALE + 19, y * Constants.SCALE + 19, w * Constants.SCALE, h * Constants.SCALE);
            }
            else {
                g.setPaint(new TexturePaint(Constants.GROUND_1, new Rectangle(0, 0, 1000, 800)));
                g.fillRect(x * Constants.SCALE + 19, y * Constants.SCALE + 19, w * Constants.SCALE, h * Constants.SCALE);
            }
        }
    }

}