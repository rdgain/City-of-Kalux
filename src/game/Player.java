package game;

import utilities.Constants;

import static utilities.Constants.*;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Random;

public class Player extends MovingObject {

    KeyController ctrl;
    BufferedImage image = PLAYER;
    Action action;
    Character character;
    HashMap<Character, Integer> charTalkedTo; //characters the player talked to and how many times they spoke
    int delay, turnDelay; // rotational delay
    final static int x1 = 37, y1 = 41;
    final static int x2 = 40, y2 = 2;
    final static int x3 = 1, y3 = 28;
    final static int x4 = 34, y4 = 57;
    final static int x5 = 37, y5 = 57;
    int money;

    public Player(KaluxGame game, KeyController ctrl) {
        super();
        this.game = game;
        this.ctrl = ctrl;
        action = ctrl.action(game);
        h = 2;
        w = 2;
        id = 0;
        talking = false;
        blockedMovement = false;
        character = null;
        delay = 3;
        turnDelay = 3;
        charTalkedTo = new HashMap<Character, Integer>();

        money = 30;

        reset();
    }

    public void reset() {
        x = x1;
        y = y1;
    }

    @Override
    public void update() {
        //update x and y according to Action object
        //check collision [can't collide with Background, open doors and pickups] BEFORE update, check edge of screen
        if (delay > 0) {
            delay--;
        } else {
            if (action.moveDown) {
                if (action.movement != action.MOVEDDOWN) { //changed direction, introduce delay
                    action.movement = action.MOVEDDOWN;
                    delay = turnDelay;
                    return;
                }
                if (!blockedMovement && (y + h < WORLD_HEIGHT)) //otherwise just change direction character is facing
                    if (game.mapL3[y + h][x] == null && game.mapL3[y + h][x + 1] == null &&
                            (game.mapL1[y + h][x] instanceof Background ||
                                    game.mapL2[y + h][x] != null && game.mapL2[y + h][x].open == 1) &&
                            (game.mapL1[y + h][x + 1] instanceof Background ||
                                    game.mapL2[y + h][x + 1] != null && game.mapL2[y + h][x + 1].open == 1)) {
                        game.mapL3[y][x] = null;
                        game.mapL3[y][x + 1] = null;
                        y++;
                        game.mapL3[y + 1][x] = this;
                        game.mapL3[y + 1][x + 1] = this;
                    }
            } else if (action.moveUp) {
                if (action.movement != action.MOVEDUP) { //changed direction, introduce delay
                    action.movement = action.MOVEDUP;
                    delay = turnDelay;
                    return;
                }
                if (!blockedMovement && (y > 0))
                    if (game.mapL3[y - 1][x] == null && game.mapL3[y - 1][x + 1] == null &&
                            (game.mapL1[y - 1][x] instanceof Background ||
                                    game.mapL2[y - 1][x] != null && game.mapL2[y - 1][x].open == 1) &&
                            (game.mapL1[y - 1][x + 1] instanceof Background ||
                                    game.mapL2[y - 1][x + 1] != null && game.mapL2[y - 1][x + 1].open == 1)) {
                        game.mapL3[y + 1][x] = null;
                        game.mapL3[y + 1][x + 1] = null;
                        y--;
                        game.mapL3[y][x] = this;
                        game.mapL3[y][x + 1] = this;
                    }
            }
            if (action.moveLeft) {
                if (action.movement != action.MOVEDLEFT) { //changed direction, introduce delay
                    action.movement = action.MOVEDLEFT;
                    delay = turnDelay;
                    return;
                }
                if (!blockedMovement && (x > 0))
                    if (game.mapL3[y][x - 1] == null && game.mapL3[y + 1][x - 1] == null &&
                            (game.mapL1[y][x - 1] instanceof Background ||
                                    game.mapL2[y][x - 1] != null && game.mapL2[y][x - 1].open == 1) &&
                            (game.mapL1[y + 1][x - 1] instanceof Background ||
                                    game.mapL2[y + 1][x - 1] != null && game.mapL2[y + 1][x - 1].open == 1)) {
                        game.mapL3[y][x + 1] = null;
                        game.mapL3[y + 1][x + 1] = null;
                        x--;
                        game.mapL3[y][x] = this;
                        game.mapL3[y + 1][x] = this;
                    }
            } else if (action.moveRight) {
                if (action.movement != action.MOVEDRIGHT) { //changed direction, introduce delay
                    action.movement = action.MOVEDRIGHT;
                    delay = turnDelay;
                    return;
                }
                if (!blockedMovement && (x + w < WORLD_WIDTH))
                    if (game.mapL3[y][x + w] == null && game.mapL3[y + 1][x + w] == null &&
                            (game.mapL1[y][x + w] instanceof Background ||
                                    game.mapL2[y][x + w] != null && game.mapL2[y][x + w].open == 1) &&
                            (game.mapL1[y + 1][x + w] instanceof Background ||
                                    game.mapL2[y + 1][x + w] != null && game.mapL2[y + 1][x + w].open == 1)) {
                        game.mapL3[y][x] = null;
                        game.mapL3[y + 1][x] = null;
                        x++;
                        game.mapL3[y][x + 1] = this;
                        game.mapL3[y + 1][x + 1] = this;
                    }
            }

            // talk interaction
            if (action.talk || action.steal) {
                int p = -1, q = -1;
                if (action.movement == action.MOVEDDOWN && game.mapL3[y + h][x] != null && game.mapL3[y + h][x] instanceof Character) {
                    p = y + h;
                    q = x;
                }
                if (action.movement == action.MOVEDUP && game.mapL3[y - 1][x] != null && game.mapL3[y - 1][x] instanceof Character) {
                    p = y - h;
                    q = x;
                }
                if (action.movement == action.MOVEDLEFT && game.mapL3[y][x - 1] != null && game.mapL3[y][x - 1] instanceof Character) {
                    p = y;
                    q = x - w;
                }
                if (action.movement == action.MOVEDRIGHT && game.mapL3[y][x + w] != null && game.mapL3[y][x + w] instanceof Character) {
                    p = y;
                    q = x + w;
                }
                if (p > -1) {
                    character = (Character) game.mapL3[p][q];
                    if (action.talk) {
                        if (charTalkedTo.containsKey(character))
                            charTalkedTo.put(character, charTalkedTo.get(character) + 1);
                        else
                            charTalkedTo.put(character,1);
                        character.talking = true;
                        character.blockedMovement = true;
                        talking = true;
                        blockedMovement = true;
                        //improve relationship with the character
                        character.relationship += character.rGain * Math.pow(0.5, charTalkedTo.get(character));
                    } else if (action.steal) {
                        Random r = new Random();
                        int s = r.nextInt(9);
                        int sum = 0;
                        if (s >= character.stealSuccess)
                            sum = character.stealRate / character.stealSuccess;
                        money += sum;
                        character.money -= sum;
                    }
                }
            }
        }
    }

    public void draw(Graphics2D g) {
        AffineTransform transform = new AffineTransform();
        AffineTransformOp op;
        if (action.movement == action.MOVEDDOWN && action.rotate) {
            image = PLAYER;
            transform.rotate(Math.toRadians(180), image.getWidth() / 2, image.getHeight() / 2);
            op = new AffineTransformOp(transform, AffineTransformOp.TYPE_BILINEAR);
            image = op.filter(image, null);
        } else {
            if (action.movement == action.MOVEDLEFT && action.rotate) {
                image = PLAYER;
                transform.rotate(Math.toRadians(270), image.getWidth() / 2, image.getHeight() / 2);
                op = new AffineTransformOp(transform, AffineTransformOp.TYPE_BILINEAR);
                image = op.filter(image, null);
            } else {
                if (action.movement == action.MOVEDRIGHT && action.rotate) {
                    image = PLAYER;
                    transform.rotate(Math.toRadians(90), image.getWidth() / 2, image.getHeight() / 2);
                    op = new AffineTransformOp(transform, AffineTransformOp.TYPE_BILINEAR);
                    image = op.filter(image, null);
                } else {
                    if (action.movement == action.MOVEDUP && action.rotate) {
                        image = PLAYER;
                    }
                }
            }
        }
        g.drawImage(image, x*Constants.SCALE+19, y*Constants.SCALE+19, null);
    }


    @Override
    int size() {
        return 4;
    }

}