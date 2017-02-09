package game;

import static utilities.Constants.*;

import java.util.ArrayList;
import java.util.Random;

public class Sidekick extends Character {

    int navDelay;
    boolean init;

    public Sidekick(KaluxGame game, Player player, int x, int y, int map) {
        this.game = game;
        this.player = player;
        r = new Random();
        h = 2;
        w = 2;
        this.x = x;
        this.y = y;
        this.map = map;
        depth = 0;

        init = false;
        //decide which map to use for collision detection
        setMap(map);
        init = true;
        path = new ArrayList<Node>();

        // set movement delay
        delay = r.nextInt(100) + 5;
        navDelay = 1;

        name = "Daren Cole";
        img = CH2;
        image = CHAR2;

        talking = false;
        talked = false;

        // what player can say to this character
        playerLines = new ArrayList<String>();
        switch (game.event) {
            case 1:
                if (game.stage <= 6) {
                    playerLines.clear();
                    playerLines.add("Q. Breathe. Tell me what happened.");
                    playerLines.add("W. I think I'll have a look around. Talk to you later.");
                }
                break;
            default:
                playerLines.clear();
                playerLines.add("Q. Give me a hint.");
                playerLines.add("W. Bye.");
        }
    }

    public void reset () {
        switch (map) {
            case 1:
                x = 41;
                y = 41;
                break;
            case 2:
                x = 44;
                y = 2;
                break;
            case 3:
                x = 1;
                y = 32;
                break;
            case 4:
                x = 38;
                y = 57;
                break;
            case 5:
                x = 41;
                y = 57;
                break;
            default:
                break;
        }
    }

    @Override
    public void update() {
        player = game.player;

        //change map
        if (game.event > 1 || (game.event == 1 && game.stage > 6)) {
            /*if (game.ctrl.action.map1 || game.ctrl.action.map2 || game.ctrl.action.map3 || game.ctrl.action.map4 || game.ctrl.action.map5) { //changed map
                map = game.currentMap;
                setMap(map);
                reset();
            }*/
            if (navDelay > 0) {
                navDelay --;
            } else {
                //change position & rotation to follow player when called
                if (game.ctrl.action.call) {
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
                    }
                    else { // try right
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
                        }
                        else { // try up
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
                            }
                            else { // try down
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
                                }
                                else {
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
        } else { //act like normal character before E1S6

            //change position & rotation
            if (delay > 0) {
                delay--;
                rotate = false;
            } else {
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
    public String getAnswer(int line) {
        String answer;
        depth++;
        if (game.event == 1 && game.stage <= 6) {
            switch (depth) {
                case 1:
                    switch (line) {
                        case 0:
                            answer = "I'd been waiting at her house for her to finish her shift last night. She was late. She's never late.";
                            playerLines.clear();
                            playerLines.add("Q. Yeah, she always crashes into bed after work, doesn't she? Or, she used to...");
                            playerLines.add("W. Hold that thought, I'll be back in a bit.");
                            break;
                        case 1:
                            answer = "Don't be too long, I need to talk to you!";
                            break;
                        default:
                            answer = "";
                            break;
                    }
                    break; // possibly change what player can say to character
                case 2:
                    switch (line) {
                        case 0:
                            answer = "Please don't say that, I'm barely keeping it together. They say it was an accident. It can't have been!";
                            playerLines.clear();
                            playerLines.add("Q. I was there last night, I saw her. No accident would've made her look like that.");
                            playerLines.add("W. Hold that thought, I'll be back in a bit.");
                            break;
                        case 1:
                            answer = "Don't be too long, I need to talk to you!";
                            break;
                        default:
                            answer = "";
                            break;
                    }
                    break;
                case 3:
                    switch (line) {
                        case 0:
                            answer = "I don't even want to imagine. Did they tell you anything? They wouldn't even speak to me!";
                            playerLines.clear();
                            playerLines.add("Q. No, they kicked me out. Honestly, I think it's all an effort to cover something big.");
                            playerLines.add("W. Hold that thought, I'll be back in a bit.");
                            break;
                        case 1:
                            answer = "Don't be too long, I need to talk to you!";
                            break;
                        default:
                            answer = "";
                            break;
                    }
                    break;
                case 4:
                    switch (line) {
                        case 0:
                            answer = "You think she was killed, don't you?";
                            playerLines.clear();
                            playerLines.add("Q. I do.");
                            playerLines.add("W. Hold that thought, I'll be back in a bit.");
                            break;
                        case 1:
                            answer = "Don't be too long, I need to talk to you!";
                            break;
                        default:
                            answer = "";
                            break;
                    }
                    break;
                case 5:
                    switch (line) {
                        case 0:
                            answer = "I feared that! But who could hurt sweet Lilly?";
                            playerLines.clear();
                            playerLines.add("Q. I couldn't say. Maybe we should ask around, somebody might know more.");
                            playerLines.add("W. Hold that thought, I'll be back in a bit.");
                            break;
                        case 1:
                            answer = "Don't be too long, I need to talk to you!";
                            break;
                        default:
                            answer = "";
                            break;
                    }
                    break;
                case 6:
                    switch (line) {
                        case 0:
                            answer = "Tracy worked with her last night. She must have seen something if we are right.";
                            playerLines.clear();
                            playerLines.add("Q. Alright, let's talk to her.");
                            playerLines.add("W. Hold that thought, I'll be back in a bit.");
                            break;
                        case 1:
                            answer = "Don't be too long, I need to talk to you!";
                            break;
                        default:
                            answer = "";
                            break;
                    }
                    break;
                case 7:
                    switch (line) {
                        case 0:
                            answer = "I think she's in a workroom in the south right now. Oh, my lovely Lilly, what did they do to you...";
                            playerLines.clear();
                            playerLines.add("Q. We'll find out, Daren. Let's look for some clues before we find Tracy though.");
                            playerLines.add("W. Hold that thought, I'll be back in a bit.");
                            //end of conversation
                            talking = false;
                            talked = true;
                            depth = 0;
                            break;
                        case 1:
                            answer = "Don't be too long, I need to talk to you!";
                            break;
                        default:
                            answer = "";
                            break;
                    }
                    break;
                default:
                    answer = "";
                    break;
            }
        } else {
            if (game.event == 1 && game.stage == 7) {
                switch (line) {
                    case 0:
                        answer = "Then get on with it! No time to lose.";
                        depth = 0;
                        talking = false;
                        break;
                    case 1:
                        answer = "Then get on with it! No time to lose.";
                        depth = 0;
                        break;
                    default:
                        answer = "";
                        break;
                }
            } else {
                if (game.event == 1 && game.stage == 8) {
                    switch (line) {
                        case 0:
                            answer = "Use that key you found.";
                            depth = 0;
                            talking = false;
                            break;
                        case 1:
                            answer = "Let's go.";
                            depth = 0;
                            break;
                        default:
                            answer = "";
                            break;
                    }
                } else {
                    if (game.event == 1 && game.stage == 9) {
                        switch (line) {
                            case 0:
                                answer = "Tracy worked with Lilith last night, she must know what happened.";
                                depth = 0;
                                talking = false;
                                break;
                            case 1:
                                answer = "Let's go.";
                                depth = 0;
                                break;
                            default:
                                answer = "";
                                break;
                        }
                    } else {
                        answer = "";
                    }
                }
            }
        }
        return answer;
    }

    @Override
    public String getGreeting() {
        if (game.event == 1 && game.stage <= 6) {
            playerLines.clear();
            if (depth > 0) {
                playerLines.clear();
                playerLines.add("Q. Hey, Daren, I'm back.");
                playerLines.add("W. Hold that thought, I'll be back in a bit.");
            } else {
                playerLines.clear();
                playerLines.add("Q. Breathe. Tell me what happened.");
                playerLines.add("W. I think I'll have a look around. Talk to you later.");
            }
            return "Thank God you're finally here! Please help me, this is all wrong!";
        } else {
            if (game.event == 1 && game.stage == 7) {
                playerLines.clear();
                playerLines.add("Q. Okay.");
                playerLines.add("W. Got it.");
                return "We should look around for clues.";
            } else {
                if (game.event == 1 && game.stage == 8) {
                    playerLines.clear();
                    playerLines.add("Q. Fancy. Thanks.");
                    playerLines.add("W. Got it.");
                    return "That key you just picked up, I think it opens a drawer in one of the tables in the small deposit room in the south.";
                } else {
                    if (game.event == 1 && game.stage == 9) {
                        playerLines.clear();
                        playerLines.add("Q. I really don't like Tracy, but you're right. Let's find her.");
                        playerLines.add("W. Got it.");
                        return "We should go find Tracy. Try looking in the rooms in the south.";
                    } else {
                        return "";
                    }
                }
            }
        }
    }

    @Override
    public void setMap(int m) {
        if (init) {
            for (int a = y; a < y + 2; a++)
                for (int b = x; b < x + 2; b++)
                    mapL3[a][b] = null;
            game.worldObjectsL3.remove(this);
            reset();
        }
        switch(m) {
            case 1:
                mapL1 = game.map1L1;
                mapL2 = game.map1L2;
                mapL3 = game.map1L3;
                nm = game.nm1;
                break;
            case 2:
                mapL1 = game.map2L1;
                mapL2 = game.map2L2;
                mapL3 = game.map2L3;
                nm = game.nm2;
                break;
            case 3:
                mapL1 = game.map3L1;
                mapL2 = game.map3L2;
                mapL3 = game.map3L3;
                nm = game.nm3;
                break;
            case 4:
                mapL1 = game.map4L1;
                mapL2 = game.map4L2;
                mapL3 = game.map4L3;
                nm = game.nm4;
                break;
            case 5:
                mapL1 = game.map5L1;
                mapL2 = game.map5L2;
                mapL3 = game.map5L3;
                nm = game.nm5;
                break;
        }
        // re-add reset sidekick to new map
        if (init) {
            for (int a = y; a < y + 2; a++)
                for (int b = x; b < x + 2; b++)
                    mapL3[a][b] = this;
            game.worldObjectsL3.add(this);
        }
    }
}

