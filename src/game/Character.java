package game;

import utilities.Constants;

import static utilities.Constants.*;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

public class Character extends MovingObject {

    KaluxGame game;
    Player player;
    WorldObject[][] mapL1; //layer 1 - background & decor
    InteractiveObject[][] mapL2; //layer 2 - pickups
    MovingObject[][] mapL3; //layer 3 - characters
    String name;
    int direction, lastD, side; //side: if 0, enemy; if 1, friendly

    ArrayList<String> playerLines; // what player can say to this character, depends on id and event
    int delay, turnDelay, navDelay, changeTargetDelay; // delay in movement
    Random r;
    boolean talked, rotate, rotated, chase;

    int money, stealRate, stealSuccess;
    int relationship, rGain;

    BufferedImage image, img, im, imgf, imge;

    int depth;

    NavMesh nm;
    ArrayList<Node> path;

    public Character (KaluxGame game, Player player, int x, int y, int id, int side, int map) {
        super();
        this.game = game;
        this.player = player;
        r = new Random();

        relationship = 0;
        rGain = r.nextInt(20) + 1; //relationship gain when talking to this character

        money = r.nextInt(200) + 5;
        stealRate = r.nextInt(40) + 1; //how much money the player can steal at one time
        stealSuccess = r.nextInt(9); //when trying to steal, the player will roll a number between 0-9; if it is >= than stealSuccess, the money is stolen successfully

        h = 2;
        w = 2;
        this.x = x;
        this.y = y;
        this.map = map;
        this.side = side;
        depth = 0;
        direction = 0;
        rotate = false;
        rotated = false;
        turnDelay = 5;
        chase = false;
        navDelay = 1;
        changeTargetDelay = 0;

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

        //decide which map to use for collision detection
        setMap(map);

        // set movement delay
        delay = r.nextInt(100) + 5;

        this.id = id;
        switch (id) {
            case 1:
                name = "Jayde Quilt";
                image = CHAR1;
                img = CH1;
                imgf = CH1F;
                imge = CH1E;
                break;
            case 3:
                name = "Tracy Gillis";
                image = CHAR3;
                img = CH3;
                imge = CH3E;
                imgf = CH5E;
                break;
            case 4:
                name = "Kay Martin";
                image = CHAR4;
                img = CH4;
                imge = CH4E;
                imgf = CH5E;
                break;
            case 5:
                name = "Gary Martin";
                image = CHAR5;
                img = CH5;
                imge = CH5E;
                imgf = CH5E;
                break;
            case 6:
                name = "Lloyd Wilks";
                image = CHAR6;
                img = CH6;
                imge = CH6E;
                imgf = CH6E;
                break;
            case 7:
                name = "Sebastian Grant";
                image = CHAR7;
                img = CH7;
                imge = CH7E;
                imgf = CH7F;
                break;
            case 8:
                name = "Charlie Paye";
                image = CHAR8;
                img = CH8;
                imge = CH8E;
                imgf = CH8F;
                break;
            case 9:
                name = "The Leader";
                img = CH9;
                imge = CH9;
                imgf = CH9;
                break;
            case 10:
                name = "Fatty";
                image = CHAR10;
                img = CH10;
                imge = CH10E;
                imgf = CH10F;
                break;
        }
        im = img;

        talking = false;
        talked = false;

        // what player can say to this character
        playerLines = new ArrayList<String>();
    }

    public Character() {
    }

    public void setMap(int m) {
        switch(m) {
            case 1:
                mapL1 = game.map1L1;
                mapL2 = game.map1L2;
                mapL3 = game.map1L3;
                break;
            case 2:
                mapL1 = game.map2L1;
                mapL2 = game.map2L2;
                mapL3 = game.map2L3;
                break;
            case 3:
                mapL1 = game.map3L1;
                mapL2 = game.map3L2;
                mapL3 = game.map3L3;
                break;
            case 4:
                mapL1 = game.map4L1;
                mapL2 = game.map4L2;
                mapL3 = game.map4L3;
                break;
            case 5:
                mapL1 = game.map5L1;
                mapL2 = game.map5L2;
                mapL3 = game.map5L3;
                break;
        }
    }


    @Override
    public void update() {
        //update relationship
        if (relationship >= 100 && side == 0) {
            side = 1;
        }

        setMap(map);

        if (chase) { //chase the player
            if (navDelay > 0) {
                navDelay--;
            } else {
                //change position & rotation to follow player when called
                if (changeTargetDelay > 0) {
                    changeTargetDelay --;
                } else {
                    changeTargetDelay = 10;
                    int x1, y1; //end position
                    //calculate end position so that it's as close to the player as possible
                    //try to the left first
                    boolean ok = true;
                    for (int i = player.y; i <= player.y + 1; i++)
                        for (int j = player.x - 2; j <= player.x - 1; j++) {
                            if (!nm.findNode(j, i)) {
                                ok = false;
                            }
                        }
                    if (ok) {
                        x1 = player.x - 2;
                        y1 = player.y;
                    } else { // try right
                        ok = true;
                        for (int i = player.y; i <= player.y + 1; i++)
                            for (int j = player.x + 2; j <= player.x + 3; j++) {
                                if (!nm.findNode(j, i)) {
                                    ok = false;
                                }
                            }
                        if (ok) {
                            x1 = player.x + 2;
                            y1 = player.y;
                        } else { // try up
                            ok = true;
                            for (int i = player.y - 2; i <= player.y - 1; i++)
                                for (int j = player.x; j <= player.x + 1; j++) {
                                    if (!nm.findNode(j, i)) {
                                        ok = false;
                                    }
                                }
                            if (ok) {
                                x1 = player.x;
                                y1 = player.y - 2;
                            } else { // try down
                                ok = true;
                                for (int i = player.y + 2; i <= player.y + 3; i++)
                                    for (int j = player.x; j <= player.x + 1; j++) {
                                        if (!nm.findNode(j, i)) {
                                            ok = false;
                                        }
                                    }
                                if (ok) {
                                    x1 = player.x;
                                    y1 = player.y + 2;
                                } else {
                                    x1 = x;
                                    y1 = y;
                                }
                            }
                        }
                    }
                    // calculate path from (x, y) to (x1, y1)
                    path = nm.getRoute(nm.getNode(x, y), nm.getNode(x1, y1));
                }
                //move along the path
                if (!path.isEmpty()) {
                    Node n = path.get(0);
                    mapL3[y + 1][x] = null;
                    mapL3[y + 1][x + 1] = null;
                    mapL3[y][x + 1] = null;
                    mapL3[y][x] = null;
                    x = n.x;
                    y = n.y;
                    mapL3[y + 1][x] = this;
                    mapL3[y + 1][x + 1] = this;
                    mapL3[y][x + 1] = this;
                    mapL3[y][x] = this;
                    path.remove(n);
                    navDelay = 1;
                }
            }
        } else {
            //change position & rotation
            if (delay > 0) {
                delay--;
                rotate = false;
            }
            else {
                lastD = direction;
                if (!rotated) { //get new direction
                    direction = r.nextInt(4);
                }
                if (direction != lastD) { //changed direction
                    rotate = true;
                    rotated = true;
                    delay = turnDelay;
                    return;
                } else {
                    rotate = false;
                    rotated = false;
                }
                if (direction == 0 && !blockedMovement && y > 0) { // can move up, check for collision
                    if (mapL3[y - 1][x] == null && mapL3[y - 1][x + 1] == null &&
                            (mapL1[y - 1][x] instanceof Background ||
                                    mapL2[y - 1][x] != null && mapL2[y - 1][x].open == 1) &&
                            (mapL1[y - 1][x + 1] instanceof Background ||
                                    mapL2[y - 1][x + 1] != null && mapL2[y - 1][x + 1].open == 1)) {
                        mapL3[y + 1][x] = null;
                        mapL3[y + 1][x + 1] = null;
                        y--;
                        mapL3[y][x] = this;
                        mapL3[y][x + 1] = this;
                        delay = r.nextInt(50) + 50;
                    }

                } else if (direction == 1 && !blockedMovement && x + w < WORLD_WIDTH) { //right
                    if (mapL3[y][x + w] == null && mapL3[y + 1][x + w] == null &&
                            (mapL1[y][x + w] instanceof Background ||
                                    mapL2[y][x + w] != null && mapL2[y][x + w].open == 1) &&
                            (mapL1[y + 1][x + w] instanceof Background ||
                                    mapL2[y + 1][x + w] != null && mapL2[y + 1][x + w].open == 1)) {
                        mapL3[y][x] = null;
                        mapL3[y + 1][x] = null;
                        x++;
                        mapL3[y][x + 1] = this;
                        mapL3[y + 1][x + 1] = this;
                        delay = r.nextInt(50) + 50;
                    }
                } else if (direction == 2 && !blockedMovement && (y + h < WORLD_HEIGHT)) { //down
                    if (mapL3[y + h][x] == null && mapL3[y + h][x + 1] == null &&
                            (mapL1[y + h][x] instanceof Background ||
                                    mapL2[y + h][x] != null && mapL2[y + h][x].open == 1) &&
                            (mapL1[y + h][x + 1] instanceof Background ||
                                    mapL2[y + h][x + 1] != null && mapL2[y + h][x + 1].open == 1)) {
                        mapL3[y][x] = null;
                        mapL3[y][x + 1] = null;
                        y++;
                        mapL3[y + 1][x] = this;
                        mapL3[y + 1][x + 1] = this;
                        delay = r.nextInt(50) + 50;
                    }
                } else if (direction == 3 && !blockedMovement && (x > 0)) { //left
                    if (mapL3[y][x - 1] == null && mapL3[y + 1][x - 1] == null &&
                            (mapL1[y][x - 1] instanceof Background ||
                                    mapL2[y][x - 1] != null && mapL2[y][x - 1].open == 1) &&
                            (mapL1[y + 1][x - 1] instanceof Background ||
                                    mapL2[y + 1][x - 1] != null && mapL2[y + 1][x - 1].open == 1)) {
                        mapL3[y][x + 1] = null;
                        mapL3[y + 1][x + 1] = null;
                        x--;
                        mapL3[y][x] = this;
                        mapL3[y + 1][x] = this;
                        delay = r.nextInt(50) + 50;
                    }
                }
            }
        }
    }


    @Override
    int size() {
        return 4;
    }

    @Override
    public void draw(Graphics2D g) {
        AffineTransform transform = new AffineTransform();
        AffineTransformOp op;
        if (direction == 2 && rotate) { // down
            im = img;
            transform.rotate(Math.toRadians(180), im.getWidth() / 2, im.getHeight() / 2);
            op = new AffineTransformOp(transform, AffineTransformOp.TYPE_BILINEAR);
            im = op.filter(im, null);
        } else {
            if (direction == 3 && rotate) { // left
                im = img;
                transform.rotate(Math.toRadians(270), im.getWidth() / 2, im.getHeight() / 2);
                op = new AffineTransformOp(transform, AffineTransformOp.TYPE_BILINEAR);
                im = op.filter(im, null);
            } else {
                if (direction == 1 && rotate) { // right
                    im = img;
                    transform.rotate(Math.toRadians(90), im.getWidth() / 2, im.getHeight() / 2);
                    op = new AffineTransformOp(transform, AffineTransformOp.TYPE_BILINEAR);
                    im = op.filter(im, null);
                } else {
                    if (direction == 0 && rotate) {
                        im = img;
                    }
                }
            }
        }
        g.drawImage(im, x*Constants.SCALE+19, y*Constants.SCALE+19, null);
    }

    public String getAnswer(int line) {
        String answer = "";
        depth++;
        switch (id) {
            case 1:
                if (game.event == 2) {
                    switch (depth) {
                        case 1:
                            switch (line) {
                                case 0:
                                    answer = "I did, poor thing.";
                                    playerLines.clear();
                                    playerLines.add("Q. I don't think it was an accident.");
                                    playerLines.add("W. I need to go.");
                                    break; // possibly change what player can say to character
                                case 1:
                                    answer = "Bye";
                                    depth = 0;
                                    break;
                                default:
                                    answer = "";
                                    break;
                            }
                            break;
                        case 2:
                            switch (line) {
                                case 0:
                                    answer = "That's a dangerous statement to make out in the open.";
                                    playerLines.clear();
                                    playerLines.add("Q. Oh, trust me, I know.");
                                    playerLines.add("W. I need to go.");
                                    break; // possibly change what player can say to character
                                case 1:
                                    answer = "Bye";
                                    depth = 0;
                                    break;
                                default:
                                    answer = "";
                                    break;
                            }
                            break;
                        case 3:
                            switch (line) {
                                case 0:
                                    answer = "Already got into trouble, have you?";
                                    playerLines.clear();
                                    playerLines.add("Q. More or less. Do you know anything more about last night?");
                                    playerLines.add("W. I need to go.");
                                    break; // possibly change what player can say to character
                                case 1:
                                    answer = "Bye";
                                    depth = 0;
                                    break;
                                default:
                                    answer = "";
                                    break;
                            }
                            break;
                        case 4:
                            switch (line) {
                                case 0:
                                    answer = "Why would I tell you, even if I did?";
                                    playerLines.clear();
                                    playerLines.add("Q. She was murdered, Jayde. They are covering it up.");
                                    playerLines.add("W. I need to go.");
                                    break; // possibly change what player can say to character
                                case 1:
                                    answer = "Bye";
                                    depth = 0;
                                    break;
                                default:
                                    answer = "";
                                    break;
                            }
                            break;
                        case 5:
                            switch (line) {
                                case 0:
                                    answer = "Even more of a reason to stay out of it. I have no desire to join her.";
                                    playerLines.clear();
                                    playerLines.add("Q. Please. I know you're one of the good ones. Help me out.");
                                    playerLines.add("W. I need to go.");
                                    break; // possibly change what player can say to character
                                case 1:
                                    answer = "Bye";
                                    depth = 0;
                                    break;
                                default:
                                    answer = "";
                                    break;
                            }
                            break;
                        case 6:
                            switch (line) {
                                case 0:
                                    answer = "You want to be a hero, I see. Fine. All I'm gonna say is Grant worked last night with her.";
                                    playerLines.clear();
                                    playerLines.add("Q. Sebastian? What does he have to do with anything?");
                                    playerLines.add("W. I need to go.");
                                    break; // possibly change what player can say to character
                                case 1:
                                    answer = "Bye";
                                    depth = 0;
                                    break;
                                default:
                                    answer = "";
                                    break;
                            }
                            break;
                        case 7:
                            switch (line) {
                                case 0:
                                    answer = "Do you live under a rock or something? Everyone knows he's openly declared his hate of the Officials.";
                                    playerLines.clear();
                                    playerLines.add("Q. And if they're covering this up, he might know something...?");
                                    playerLines.add("W. I need to go.");
                                    break; // possibly change what player can say to character
                                case 1:
                                    answer = "Bye";
                                    depth = 0;
                                    break;
                                default:
                                    answer = "";
                                    break;
                            }
                            break;
                        case 8:
                            switch (line) {
                                case 0:
                                    answer = "He makes it his business to know what they are up to, if just to keep himself alive.";
                                    playerLines.clear();
                                    playerLines.add("Q. Alright. Do you know where he is?");
                                    playerLines.add("W. I need to go.");
                                    break; // possibly change what player can say to character
                                case 1:
                                    answer = "Bye";
                                    depth = 0;
                                    break;
                                default:
                                    answer = "";
                                    break;
                            }
                            break;
                        case 9:
                            switch (line) {
                                case 0:
                                    answer = "No tracker, I'm afraid. Try at the park. And don't ask me any more questions, please.";
                                    playerLines.clear();
                                    playerLines.add("Q. I won't put you in danger more than I already have. I appreciate your help.");
                                    playerLines.add("W. I need to go.");
                                    //end of conversation
                                    talking = false;
                                    talked = true;
                                    depth = 0;
                                    break; // possibly change what player can say to character
                                case 1:
                                    answer = "Bye";
                                    depth = 0;
                                    break;
                                default:
                                    answer = "";
                                    break;
                            }
                            break;
                    }
                } else {
                    switch (line) {
                        case 0:
                            answer = "Get lost then.";
                            playerLines.clear();
                            playerLines.add("Q. Fine.");
                            playerLines.add("W. No need to be rude, lady.");
                            talking = false;
                            depth = 0;
                            break; // possibly change what player can say to character
                        case 1:
                            answer = "Bye";
                            depth = 0;
                            break;
                        default:
                            answer = "";
                            break;
                    }
                }
                break;
            case 3: // Tracy Gillis
                if (game.event == 1 && game.stage == 9) {
                    switch (depth) {
                        case 1:
                            switch (line) {
                                case 0:
                                    answer = "What do you want, boy?";
                                    playerLines.clear();
                                    playerLines.add("Q. Hey, no need to be rude. Why would you assume I want something?");
                                    playerLines.add("W. I need to go.");
                                    break; // possibly change what player can say to character
                                case 1:
                                    answer = "Bye";
                                    depth = 0;
                                    break;
                                default:
                                    answer = "";
                                    break;
                            }
                            break;
                        case 2:
                            switch (line) {
                                case 0:
                                    answer = "Because we don't talk. Like ever. Now spill.";
                                    playerLines.clear();
                                    playerLines.add("Q. Poor Lilith...you've heard what happened, right?");
                                    playerLines.add("W. I need to go.");
                                    break; // possibly change what player can say to character
                                case 1:
                                    answer = "Bye";
                                    depth = 0;
                                    break;
                                default:
                                    answer = "";
                                    break;
                            }
                            break;
                        case 3:
                            switch (line) {
                                case 0:
                                    answer = "And you think I had something to do with it?";
                                    playerLines.clear();
                                    playerLines.add("Q. No! Of course not! Although...did you?");
                                    playerLines.add("W. I need to go.");
                                    break; // possibly change what player can say to character
                                case 1:
                                    answer = "Bye";
                                    depth = 0;
                                    break;
                                default:
                                    answer = "";
                                    break;
                            }
                            break;
                        case 4:
                            switch (line) {
                                case 0:
                                    answer = "Do you not like your face anymore, hun?";
                                    playerLines.clear();
                                    playerLines.add("Q. I'm sorry. I had to ask. I think they're lying. I don't think it was an accident.");
                                    playerLines.add("W. I need to go.");
                                    break; // possibly change what player can say to character
                                case 1:
                                    answer = "Bye";
                                    depth = 0;
                                    break;
                                default:
                                    answer = "";
                                    break;
                            }
                            break;
                        case 5:
                            switch (line) {
                                case 0:
                                    answer = "You think too much for your own good.";
                                    playerLines.clear();
                                    playerLines.add("Q. I get told that a lot. Have you at least seen anything weird last night?");
                                    playerLines.add("W. I need to go.");
                                    break; // possibly change what player can say to character
                                case 1:
                                    answer = "Bye";
                                    depth = 0;
                                    break;
                                default:
                                    answer = "";
                                    break;
                            }
                            break;
                        case 6:
                            switch (line) {
                                case 0:
                                    answer = "Let me get back to you on that.";
                                    playerLines.clear();
                                    playerLines.add("Q. Is that a no or a yes?");
                                    playerLines.add("W. Okay.");
                                    //end of conversation
                                    talking = false;
                                    talked = true;
                                    depth = 0;
                                    break; // possibly change what player can say to character
                                case 1:
                                    answer = "Bye";
                                    depth = 0;
                                    break;
                                default:
                                    answer = "";
                                    break;
                            }
                            break;
                    }
                } else {
                    switch (line) {
                        case 0:
                            answer = "Don't speak to me.";
                            playerLines.clear();
                            playerLines.add("Q. Fine.");
                            playerLines.add("W. Someone woke up on the wrong side of the bed this morning.");
                            talking = false;
                            depth = 0;
                            break; // possibly change what player can say to character
                        case 1:
                            answer = "Bye";
                            depth = 0;
                            break;
                        default:
                            answer = "";
                            break;
                    }
                }
                break;
            case 4: // Kay Martin
                if (game.event == 2 && game.stage == 1) {
                    switch (depth) {
                        case 1:
                            answer = "I've been informed you're looking into things that don't concern you and spreading rumors.";
                            playerLines.clear();
                            playerLines.add("Q. May I know who informed you?");
                            playerLines.add("");
                            break;
                        case 2:
                            answer = "That's one more inappropriate question you've asked recently.";
                            playerLines.clear();
                            playerLines.add("Q. I apologize if I stepped out of line.");
                            playerLines.add("");
                            break;
                        case 3:
                            answer = "Oh, you certainly did. Here's a friendly warning: let Lilith rest in peace.";
                            playerLines.clear();
                            playerLines.add("Q. I feel like there's an 'Or else...' coming");
                            playerLines.add("");
                            break;
                        case 4:
                            answer = "Look, kid. The only reason I'm letting you off now is because you've served the community well.";
                            playerLines.clear();
                            playerLines.add("Q. 'Now'?");
                            playerLines.add("");
                            break;
                        case 5:
                            answer = "Now. One more wrong move and you'll find yourself in a tight cell.";
                            playerLines.clear();
                            playerLines.add("Q. Whoa! I was just talking about my friend with people. That's not against the law.");
                            playerLines.add("");
                            break;
                        case 6:
                            answer = "You are starting an unauthorized investigation. Put a stop to it.";
                            playerLines.clear();
                            playerLines.add("Q. Sure, so you'll look into her death instead, right?");
                            playerLines.add("");
                            break;
                        case 7:
                            answer = "There's nothing to look into. It was an accident. Last warning, kid. Stop looking.";
                            playerLines.clear();
                            playerLines.add("Q. Fine. Good day, Official Martin.");
                            playerLines.add("");
                            break;
                        case 8:
                            answer = "Good day, Mr. King.";
                            playerLines.clear();
                            playerLines.add("");
                            playerLines.add("");
                            //end of conversation
                            talking = false;
                            talked = true;
                            depth = 0;
                            break;
                    }
                } else {
                    switch (line) {
                        case 0:
                            answer = "Get lost, kid.";
                            playerLines.clear();
                            playerLines.add("Q. Say hi to Tracy for me.");
                            playerLines.add("W. See ya, old man.");
                            talking = false;
                            depth = 0;
                            break; // possibly change what player can say to character
                        case 1:
                            answer = "Bye";
                            depth = 0;
                            break;
                        default:
                            answer = "";
                            break;
                    }
                }
                break;
            case 5: //Gary
                switch (line) {
                    case 0:
                        answer = "You smell too.";
                        playerLines.clear();
                        playerLines.add("Q. Oh, you're taking it too far now.");
                        playerLines.add("W. I'll go wash up.");
                        talking = false;
                        depth = 0;
                        break;
                    case 1:
                        answer = "Bye";
                        depth = 0;
                        break;
                    default:
                        answer = "";
                        break;

                }
                break;
            case 6: //Lloyd
                switch (line) {
                    case 0:
                        answer = "Good day, King.";
                        playerLines.clear();
                        playerLines.add("Q. See you later, queen.");
                        playerLines.add("W. Buh-bye.");
                        talking = false;
                        depth = 0;
                        break;
                    case 1:
                        answer = "Bye";
                        depth = 0;
                        break;
                    default:
                        answer = "";
                        break;

                }
                break;
            case 7: // Sebastian
                if (game.event == 2 && game.stage == 3) {
                    switch (depth) {
                        case 1:
                            answer = "Shout it from the rooftops now won't ya. Yes she was. Or she pretended to be.";
                            playerLines.clear();
                            playerLines.add("Q. What do you mean, she pretended to be?");
                            playerLines.add("W. Need to go.");
                            break;
                        case 2:
                            answer = "I suspect she was a double agent. That's all I've got for you.";
                            playerLines.clear();
                            playerLines.add("Q. Interesting. But please tell me more.");
                            playerLines.add("W. Need to go.");
                            break;
                        case 3:
                            answer = "I don't trust you. Have these papers I found and Lilith's house, they'll help.";
                            playerLines.clear();
                            playerLines.add("Q. Oh.. thanks!");
                            playerLines.add("W. Need to go.");
                            break;
                        case 4:
                            answer = "Go tell everyone about the rebels, bro.";
                            playerLines.clear();
                            playerLines.add("Will do.");
                            playerLines.add("Anytime.");
                            //end of conversation
                            talking = false;
                            talked = true;
                            depth = 0;
                            break;
                    }
                } else {
                    switch (line) {
                        case 0:
                            answer = "That I am.";
                            playerLines.clear();
                            playerLines.add("Q. Right.");
                            playerLines.add("W. See you around.");
                            talking = false;
                            depth = 0;
                            break; // possibly change what player can say to character
                        case 1:
                            answer = "Bye";
                            depth = 0;
                            break;
                        default:
                            answer = "";
                            break;
                    }
                }
                break;
            case 8: // Charlie
                if (game.event == 4 && game.stage == 1) {
                    switch (depth) {
                        case 1:
                            answer = "News travel fast, so yes, I was.";
                            playerLines.clear();
                            playerLines.add("Q. Tell me about Lilith's death.");
                            playerLines.add("W. Need to go.");
                            break;
                        case 2:
                            answer = "I don't know much. But I know the Leader is the one you should be talking to.";
                            playerLines.clear();
                            playerLines.add("Q. Why him?");
                            playerLines.add("W. Need to go.");
                            break;
                        case 3:
                            answer = "Because he's not who he says he is. He rules the rebels, you see.";
                            playerLines.clear();
                            playerLines.add("Q. That makes no sense.");
                            playerLines.add("W. Need to go.");
                            break;
                        case 4:
                            answer = "Here, this should explain things better. [notebook updated]";
                            playerLines.clear();
                            playerLines.add("Q. Thanks, I guess...");
                            playerLines.add("W. Need to go.");
                            break;
                        case 5:
                            answer = "Don't get yourself killed, kid.";
                            playerLines.clear();
                            playerLines.add("I won't.");
                            playerLines.add("Not before you do.");
                            //end of conversation
                            talking = false;
                            talked = true;
                            depth = 0;
                            break;
                    }
                } else {
                    switch (line) {
                        case 0:
                            answer = "I don't care.";
                            playerLines.clear();
                            playerLines.add("Q. Me neither.");
                            playerLines.add("W. Cool.");
                            talking = false;
                            depth = 0;
                            break; // possibly change what player can say to character
                        case 1:
                            answer = "Bye";
                            depth = 0;
                            break;
                        default:
                            answer = "";
                            break;
                    }
                }
                break;
            case 9: // Leader
                if (game.event == 4 && game.stage == 2) {
                    switch (depth) {
                        case 1:
                            answer = "You made quite a name for yourself, looking into that poor girl's death.";
                            playerLines.clear();
                            playerLines.add("Q. Do you know who killed her?");
                            playerLines.add("W. Need to go.");
                            break;
                        case 2:
                            answer = "Of course I do. I did. She was a nasty little thing, that Lilith.";
                            playerLines.clear();
                            playerLines.add("Q. Why would you say that?");
                            playerLines.add("W. Need to go.");
                            break;
                        case 3:
                            answer = "She was working for the Officials and pretending to be in my rebellion.";
                            playerLines.clear();
                            playerLines.add("Q. YOUR rebellion?");
                            playerLines.add("W. Need to go.");
                            break;
                        case 4:
                            answer = "You haven't figured out I'm behind the rebel movement? Some detetive you are...";
                            playerLines.clear();
                            playerLines.add("Q. I did suspect it, but..");
                            playerLines.add("W. Need to go.");
                            break;
                        case 5:
                            answer = "Anyway, as I was saying. She was working to get rebels snuffed out. She had to go.";
                            playerLines.clear();
                            playerLines.add("Q. That makes no sense. Lilith wouldn't do that...");
                            playerLines.add("W. Need to go.");
                            break;
                        case 6:
                            answer = "She would have and she did. She was more than you thought. And now it's time for you to choose.";
                            playerLines.clear();
                            playerLines.add("Q. Choose...?");
                            playerLines.add("W. Need to go.");
                            break;
                        case 7:
                            answer = "Whether you join me or die.";
                            playerLines.clear();
                            playerLines.add("Q. Oh. Um. Well. Easy choice there, isn't it.");
                            playerLines.add("W. Need to go.");
                            break;
                        case 8:
                            answer = "Let's take the city then!";
                            playerLines.clear();
                            playerLines.add("Q. Woo! Let's!");
                            playerLines.add("W. Need to go.");
                            break;
                        case 9:
                            answer = "I've got my eyes on you, Andrew King.";
                            playerLines.clear();
                            playerLines.add("Don't stare for too long.");
                            playerLines.add("Watch me run away.");
                            //end of conversation
                            talking = false;
                            talked = true;
                            depth = 0;
                            break;
                    }
                } else {
                    switch (line) {
                        case 0:
                            answer = "Then move along. I don't have time for you.";
                            playerLines.clear();
                            playerLines.add("Q. Understood.");
                            playerLines.add("W. Yes, sir.");
                            talking = false;
                            depth = 0;
                            break; // possibly change what player can say to character
                        case 1:
                            answer = "Bye";
                            depth = 0;
                            break;
                        default:
                            answer = "";
                            break;
                    }
                }
                break;
            case 10: //Fatty
                switch (line) {
                    case 0:
                        answer = "You're mean.";
                        playerLines.clear();
                        playerLines.add("Q. I also have food. See ya!");
                        playerLines.add("W. Buh-bye.");
                        talking = false;
                        depth = 0;
                        break;
                    case 1:
                        answer = "Bye";
                        depth = 0;
                        break;
                    default:
                        answer = "";
                        break;

                }
                break;
            default:
                switch (line) {
                    case 0:
                        answer = "Hello";
                        break; // possibly change what player can say to character
                    case 1:
                        answer = "Bye";
                        depth = 0; break;
                    default: answer = ""; break;
                }
        }
        return answer;
    }

    public ArrayList<String> getPlayerLines() {
        return playerLines;
    }

    public String getGreeting() {
        String str;

        switch (id) {
            case 1: // Jayde
                if (game.event == 2) {
                    playerLines.clear();
                    playerLines.add("Q. Hey, Jayde. Have you heard about what happened to Lilith?");
                    playerLines.add("W. Apparently. See you later.");
                    str = "Are you lost, Andrew?";
                    break;
                }
                else {
                    playerLines.clear();
                    playerLines.add("Q. I don't even want to know what you're talking about...");
                    playerLines.add("W. Nevermind.");
                    str ="The Leader is at it again. Making his Officials do his dirty work for him...";
                    break;
                }
            case 3: // Tracy
                if (game.event == 1 && game.stage == 9) {
                    playerLines.clear();
                    playerLines.add("Q. How's work, Trace?");
                    playerLines.add("W. You're not in the mood. Talk to you later.");
                    str = "Andrew.";
                    break;
                }
                else {
                    playerLines.clear();
                    playerLines.add("Q. Long hours again, huh?");
                    playerLines.add("W. I was thinking the same thing. See you around.");
                    str = "I can't wait for my shift to be over...";
                    break;
                }
            case 4: // Kay
                if (game.event == 2 && game.stage == 1) {
                    playerLines.clear();
                    playerLines.add("Q. You may, what's this about?");
                    playerLines.add("W. Do I get a choice here?");
                    str = "Mr. Andrew King. May I have a word?";
                    break;
                }
                else {
                    playerLines.clear();
                    playerLines.add("Q. Pressing City business, I see.");
                    playerLines.add("W. I wonder how you keep your job.");
                    str = "I wonder how many gummy bears Tracy can fit in her mouth.";
                    break;
                }
            case 5: // Gary
                playerLines.clear();
                playerLines.add("Q. So do you.");
                playerLines.add("W. Whatever.");
                str = "You look dirty, son.";
                break;
            case 6: // Lloyd
                playerLines.clear();
                playerLines.add("Q. Alrighty then.");
                playerLines.add("W. Carry on the great work, Official Wilks.");
                str = "Hi! I love candy and arresting people!";
                break;
            case 7: // Seb
                if (game.event == 2 && game.stage == 3) {
                    playerLines.clear();
                    playerLines.add("Q. Was Lilith really a rebel?");
                    playerLines.add("W. Bye.");
                    str = "Hi.";
                    break;
                }
                else {
                    playerLines.clear();
                    playerLines.add("Q. Classy.");
                    playerLines.add("W. You can do better than that.");
                    str = "Do you always look so...frowny?";
                    break;
                }
            case 8: // Charlie
                if (game.event == 4 && game.stage == 1) {
                    playerLines.clear();
                    playerLines.add("Q. Were you expecting me?");
                    playerLines.add("W. Now see me leave.");
                    str = "I see you found me.";
                    break;
                }
                else {
                    playerLines.clear();
                    playerLines.add("Q. Me too.");
                    playerLines.add("W. I'll go look for some food.");
                    str = "I'm hungry.";
                    break;
                }
            case 9: // Leader
                if (game.event == 4 && game.stage == 2) {
                    playerLines.clear();
                    playerLines.add("Q. I see everyone knows me these days.");
                    playerLines.add("W. Let's meet later.");
                    str = "So, Andrew King. We finally meet.";
                    break;
                }
                else {
                    playerLines.clear();
                    playerLines.add("Q. Nothing, sir.");
                    playerLines.add("W. Pretend you never saw me.");
                    str = "What do you want boy?";
                    break;
                }
            case 10: // Fatty
                boolean ok = false;
                for (InteractiveObject o: game.backpack) {
                    if (o.id == 8) { // have food in inventory
                        playerLines.clear();
                        playerLines.add("Q. No, it's mine.");
                        playerLines.add("W. No way! Go away!");
                        str = "GIVE ME YOUR FOOD.";
                        ok = true;
                        break;
                    }
                }
                if (!ok) {
                    playerLines.clear();
                    str = "";
                    talking = false;
                    depth = 0;
                    break;
                }
            default: str = "Hi there."; break;
        }

        return str;
    }

}