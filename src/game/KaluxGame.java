package game;

import java.awt.*;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import utilities.*;
import static utilities.Constants.*;

public class KaluxGame {

    ArrayList<GameObject> worldObjectsM1L1, worldObjectsM1L2, worldObjectsM1L3;
    ArrayList<GameObject> worldObjectsM2L1, worldObjectsM2L2, worldObjectsM2L3;
    ArrayList<GameObject> worldObjectsM3L1, worldObjectsM3L2, worldObjectsM3L3;
    ArrayList<GameObject> worldObjectsM4L1, worldObjectsM4L2, worldObjectsM4L3;
    ArrayList<GameObject> worldObjectsM5L1, worldObjectsM5L2, worldObjectsM5L3;
    Character[] characters; //collection of all characters
    ArrayList<GameObject> worldObjectsL1, worldObjectsL2, worldObjectsL3;
    WorldObject[][] map1L1, map2L1, map3L1, map4L1, map5L1, mapL1; //layer 1 - background & decor
    InteractiveObject[][] map1L2, map2L2, map3L2, map4L2, map5L2, mapL2; //layer 2 - pickups
    MovingObject[][] map1L3, map2L3, map3L3, map4L3, map5L3, mapL3; //layer 3 - characters
    NavMesh nm1, nm2, nm3, nm4, nm5;

    KeyController ctrl;
    boolean gameRunning, talking;
    int talkDelay;
    Player player;
    Sidekick sidekick;
    String answer;
    ArrayList<String> playerLines;
    ArrayList<String> notebook1, notebook2, notebook3, notebook4, notebook5;
    ArrayList<String> notebookT;
    int playerOption;
    int removeDelay;
    ArrayList<InteractiveObject> backpack;
    boolean gotBackpack, stageChanged;
    int event, stage, currentMap, rememberEvent, rememberStage;
    boolean eventStarted, freeze, huntedMode;
    boolean[] ach;
    String[] achN; //player achievements

    //vision range
    int[][] vis, visM1, visM2, visM3, visM4, visM5;
    int visW, visH;
    int visited2, visited3, visited4, visited5;

    public KaluxGame() throws IOException {
        ach = new boolean[Constants.NO_ACHIEVEMENTS];
        achN = new String[Constants.NO_ACHIEVEMENTS];
        achN[0] = "Moneymaker: have more than 100$";
        achN[1] = "Troublemaker: reach event 3";
        for (int i=0; i<Constants.NO_ACHIEVEMENTS; i++)
            ach[i] = false;

        event = 1;
        SoundManager.loop(SoundManager.map1M);
        eventStarted = false;
        stage = 0;
        stageChanged = false;
        freeze = true;
        huntedMode = false;

        notebook1 = new ArrayList<String>();
        notebook2 = new ArrayList<String>();
        notebook3 = new ArrayList<String>();
        notebook4 = new ArrayList<String>();
        notebook5 = new ArrayList<String>();
        notebookT = new ArrayList<String>();
        notebookT.add("1. Introduction");
        notebookT.add("2. Tutorial");
        notebookT.add("3. Story p1.");
        notebookT.add("4. Story p2.");
        notebookT.add("5. Story p3.");
        notebook1.add("It’s year 4122. 715 days ago was Day 0. The day the old world ended and the new world began. " +
                "It was a rather slow Apocalypse. The sun just blinked out one day: no warning, no signs. " +
                "The scientists were as confused as everyone else, as it turned out everything they thought " +
                "they knew about the star was wrong. Complete chaos followed soon after for years, as the temperatures slowly started dropping." +
                "And when people panic, they break things ...");
        notebook1.add("This time, it was their civilization and order they broke. They stole, they killed, they burned, " +
                "they ran and hid to try and survive; but most couldn’t. The rulers of the world united in a desperate attempt " +
                "to save human kind from extinction. Even though they blamed the scientists for the disaster, as they were " +
                "the ones who should have seen it coming, they all worked together for once and gave birth to the New World: " +
                "a world of dome cities. ");
        notebook1.add("The most advanced technology was used to mimic the Old World inside the domes: daylight, warmth, normality. " +
                "However, the number of people they could house was limited and the Cities operated on a first come, " +
                "first served basis, shutting down contact with everything outside once they reached their capacity. " +
                "As the rest of the world froze over, each City built its new society and laws from scratch. ");
        notebook1.add("But it soon turned out the Cities were too small to fully function by themselves, so they specialized, " +
                "sharing products with each other using strictly monitored trading routes going through the Wilderness. " +
                "Nothing lived in the Wilderness anymore, nothing worth taking into consideration anyway. It was a cruel mission " +
                "for traders to travel through the dark frozen world, which is why in most cities they were selected by drafting.");
        notebook1.add("That is the case in the City of Kalux. Or, as known to the traders, the City of Paper. This is one of the " +
                "few cities that adopted an extreme set of laws and distinction between citizens on Day 0: the lower class " +
                "(which I am part of) is required to work 12 hours a day with minimum benefits; the higher class is in charge " +
                "of security and administration work, having the option of doing office jobs or slacking off. There is an 11pm curfew, " +
                "lifted at 5am.");
        notebook1.add("Anyone caught outside between these hours is arrested and punished accordingly. At least every citizen gets a house, " +
                "although even this depends on their rank in the society, so the lower class gets small identical houses on the edge of the City. " +
                "The Leader, Noah Primes, is the highest in command and has the last word (or so the rules say). ");
        notebook1.add("Then there are the Officials, a group of high ranks, each in charge of different aspects of the city, such as Trading, Tech, Security etc. " +
                "(they’re actually the ones ruling, off the record). A great system, as some say and most disagree.");
        notebook1.add("My name is Andrew King. I work 12 hours a day in the paper factory and I own a small home at " +
                "10 Chopper Way, where I live by myself. My friend and work colleague, Lilith Joy Wings, was murdered. " +
                "And this is my story.");
        notebook2.add("Lilith died last night. Daren, her boyfriend, called me 2 hours after my shift ended " +
                "and told me something was wrong, she was late. So I went back to work and found her in a pool " +
                "of her own blood. Before I could even understand what was going on, I got kicked out by security. " +
                "They said it was an accident. I don't believe it. Lilith wouldn't just 'have an accident'. " +
                "I'm going to find out what really happened. First stop: the factory.");
        talkDelay = 0;

        currentMap = 1;
        visited2 = 0;
        visited3 = 0;
        visited4 = 0;
        visited5 = 0;

        characters = new Character[11];
        for (int i=0; i<11; i++) {
            characters[i] = null;
        }

        //map1 object collections
        worldObjectsM1L1 = new ArrayList<GameObject>();
        worldObjectsM1L2 = new ArrayList<GameObject>();
        worldObjectsM1L3 = new ArrayList<GameObject>();
        //map2
        worldObjectsM2L1 = new ArrayList<GameObject>();
        worldObjectsM2L2 = new ArrayList<GameObject>();
        worldObjectsM2L3 = new ArrayList<GameObject>();
        //map3
        worldObjectsM3L1 = new ArrayList<GameObject>();
        worldObjectsM3L2 = new ArrayList<GameObject>();
        worldObjectsM3L3 = new ArrayList<GameObject>();
        //map4
        worldObjectsM4L1 = new ArrayList<GameObject>();
        worldObjectsM4L2 = new ArrayList<GameObject>();
        worldObjectsM4L3 = new ArrayList<GameObject>();
        //map5
        worldObjectsM5L1 = new ArrayList<GameObject>();
        worldObjectsM5L2 = new ArrayList<GameObject>();
        worldObjectsM5L3 = new ArrayList<GameObject>();

        //define navigation meshes for all maps
        nm1 = new NavMesh();
        nm2 = new NavMesh();
        nm3 = new NavMesh();
        nm4 = new NavMesh();
        nm5 = new NavMesh();

        ctrl = new KeyController();
        gameRunning = true;
        answer = "";
        playerLines = new ArrayList<String>();
        talking = false;
        playerOption = -1;
        removeDelay = 50;
        backpack = new ArrayList<InteractiveObject>(10);
        gotBackpack = false;

        // read maps from files
        map1L1 = new WorldObject[Constants.WORLD_HEIGHT + 1][Constants.WORLD_WIDTH + 1];
        map2L1 = new WorldObject[Constants.WORLD_HEIGHT + 1][Constants.WORLD_WIDTH + 1];
        map3L1 = new WorldObject[Constants.WORLD_HEIGHT + 1][Constants.WORLD_WIDTH + 1];
        map4L1 = new WorldObject[Constants.WORLD_HEIGHT + 1][Constants.WORLD_WIDTH + 1];
        map5L1 = new WorldObject[Constants.WORLD_HEIGHT + 1][Constants.WORLD_WIDTH + 1];

        map1L2 = new InteractiveObject[Constants.WORLD_HEIGHT + 1][Constants.WORLD_WIDTH + 1];
        map2L2 = new InteractiveObject[Constants.WORLD_HEIGHT + 1][Constants.WORLD_WIDTH + 1];
        map3L2 = new InteractiveObject[Constants.WORLD_HEIGHT + 1][Constants.WORLD_WIDTH + 1];
        map4L2 = new InteractiveObject[Constants.WORLD_HEIGHT + 1][Constants.WORLD_WIDTH + 1];
        map5L2 = new InteractiveObject[Constants.WORLD_HEIGHT + 1][Constants.WORLD_WIDTH + 1];

        map1L3 = new MovingObject[Constants.WORLD_HEIGHT + 1][Constants.WORLD_WIDTH + 1];
        map2L3 = new MovingObject[Constants.WORLD_HEIGHT + 1][Constants.WORLD_WIDTH + 1];
        map3L3 = new MovingObject[Constants.WORLD_HEIGHT + 1][Constants.WORLD_WIDTH + 1];
        map4L3 = new MovingObject[Constants.WORLD_HEIGHT + 1][Constants.WORLD_WIDTH + 1];
        map5L3 = new MovingObject[Constants.WORLD_HEIGHT + 1][Constants.WORLD_WIDTH + 1];

        for (int i = 0; i < Constants.WORLD_HEIGHT; i++)
            for (int j = 0; j < Constants.WORLD_WIDTH; j++) {
                map1L1[i][j] = null;
                map2L1[i][j] = null;
                map3L1[i][j] = null;
                map4L1[i][j] = null;
                map5L1[i][j] = null;

                map1L2[i][j] = null;
                map2L2[i][j] = null;
                map3L2[i][j] = null;
                map4L2[i][j] = null;
                map5L2[i][j] = null;

                map1L3[i][j] = null;
                map2L3[i][j] = null;
                map3L3[i][j] = null;
                map4L3[i][j] = null;
                map5L3[i][j] = null;
            }
        WorldObject o;
        Scanner in1 = new Scanner(new FileReader("maps/map1_ob.txt"));
        Scanner in2 = new Scanner(new FileReader("maps/map2_ob.txt"));
        Scanner in3 = new Scanner(new FileReader("maps/map3_ob.txt"));
        Scanner in4 = new Scanner(new FileReader("maps/map4_ob.txt"));
        Scanner in5 = new Scanner(new FileReader("maps/map5_ob.txt"));
        synchronized (this){
            Color color;
            //map1
            while (in1.hasNext()) {
                String line = in1.nextLine();
                String[] contents = line.split("\\s+");
                for (String content: contents) {
                    int x = Integer.parseInt(content.substring(2, 4));
                    int y = Integer.parseInt(content.substring(5, 7));
                    int w = 1, h = 1, id, st = -1;
                    char dir, state, or;
                    switch (content.charAt(0)) {
                        case 'a':
                            color = Color.BLACK;
                            w = Integer.parseInt(content.substring(8, 10));
                            h = Integer.parseInt(content.substring(11));
                            o = new Background(this, player, x, y, w, h, color, 0, 1, 1);
                            for (int a = y; a < y + h && a < 60; a++)
                                for (int b = x; b < x + w && b < 80; b++)
                                    map1L1[a][b] = o;
                            worldObjectsM1L1.add(o);
                            break;
                        case 'g':
                            color = Color.BLACK;
                            w = Integer.parseInt(content.substring(8, 10));
                            h = Integer.parseInt(content.substring(11));
                            o = new Background(this, player, x, y, w, h, color, 0, 2, 1);
                            for (int a = y; a < y + h && a < 60; a++)
                                for (int b = x; b < x + w && b < 80; b++)
                                    map1L1[a][b] = o;
                            worldObjectsM1L1.add(o);
                            break;
                        case 'o':
                            color = Color.RED;
                            w = Integer.parseInt(content.substring(8, 9));
                            h = Integer.parseInt(content.substring(10, 11));
                            id = Integer.parseInt(content.substring(12, 14));
                            or = content.charAt(14);
                            o = new Decor(this, player, x, y, w, h, color, id, 2, 1, or);
                            for (int a = y; a < y + h && a < 60; a++)
                                for (int b = x; b < x + w && b < 80; b++)
                                    map1L1[a][b] = o;
                            worldObjectsM1L1.add(o);
                            break;
                        case 's':
                            color = Color.GRAY;
                            w = Integer.parseInt(content.substring(8, 10));
                            h = Integer.parseInt(content.substring(11));
                            o = new Background(this, player, x, y, w, h, color, 0, 3, 1);
                            for (int a = y; a < y + h && a < 60; a++)
                                for (int b = x; b < x + w && b < 80; b++)
                                    map1L1[a][b] = o;
                            worldObjectsM1L1.add(o);
                            break;
                        case 'd':
                            color = Color.YELLOW;
                            dir = content.charAt(7); //vertical or horizontal
                            state = content.charAt(8); //open or closed
                            if (dir == 'h') {
                                w = 2;
                                h = 1;
                            } else if (dir == 'v') {
                                w = 1;
                                h = 2;
                            }
                            if (state == 'o') {
                                st = 1;
                            } else if (state == 'c') {
                                st = 0;
                            }
                            o = new InteractiveObject(this, player, x, y, w, h, color, 0, 2, st, 1);
                            for (int a = y; a < y + h && a < 60; a++)
                                for (int b = x; b < x + w && b < 80; b++)
                                    map1L2[a][b] = (InteractiveObject) o;
                            worldObjectsM1L2.add(o);
                            break;
                        case '#':
                            color = Color.GREEN;
                            id = Integer.parseInt(content.substring(8));
                            o = new InteractiveObject(this, player, x, y, 1, 1, color, id, 1, -1, 1);
                            map1L2[y][x] = (InteractiveObject) o;
                            worldObjectsM1L2.add(o);
                            break;
                        case 'c':
                            id = Integer.parseInt(content.substring(8, 10));
                            int s = Integer.parseInt(content.substring(10, 11));
                            if (id == 2) {
                                sidekick = new Sidekick(this, player, x, y, 1);
                                for (int a = y; a < y + 2; a++)
                                    for (int b = x; b < x + 2; b++)
                                        map1L3[a][b] = sidekick;
                                worldObjectsM1L3.add(sidekick);
                            } else {
                                Character c = new Character(this, player, x, y, id, s, 1);
                                worldObjectsM1L3.add(c);
                                characters[id] = c;
                                for (int a = y; a < y + 2; a++)
                                    for (int b = x; b < x + 2; b++)
                                        map1L3[a][b] = c;
                            }
                            break;
                    }
                }
            }
            // add walls in empty places
            for (int i = 0; i < Constants.WORLD_HEIGHT; i++)
                for (int j = 0; j < Constants.WORLD_WIDTH; j++)
                    if (map1L1[i][j] == null) {
                        o = new Decor(this, player, j, i, 1, 1, Color.WHITE, i+j, 1, 1, ' ');
                        worldObjectsM1L1.add(o);
                        map1L1[i][j] = o;
                    }
            //create navigation mesh after input
            createNavMesh(1);

            //map2
            while (in2.hasNext()) {
                String line = in2.nextLine();
                String[] contents = line.split("\\s+");
                for (String content: contents) {
                    int x = Integer.parseInt(content.substring(2, 4));
                    int y = Integer.parseInt(content.substring(5, 7));
                    int w = 1, h = 1, id, st = -1;
                    char dir, state, or;
                    switch (content.charAt(0)) {
                        case 'a':
                            color = Color.BLACK;
                            w = Integer.parseInt(content.substring(8, 10));
                            h = Integer.parseInt(content.substring(11));
                            o = new Background(this, player, x, y, w, h, color, 0, 1, 2);
                            for (int a = y; a < y + h && a < 60; a++)
                                for (int b = x; b < x + w && b < 80; b++)
                                    map2L1[a][b] = o;
                            worldObjectsM2L1.add(o);
                            break;
                        case 'g':
                            color = Color.BLACK;
                            w = Integer.parseInt(content.substring(8, 10));
                            h = Integer.parseInt(content.substring(11));
                            o = new Background(this, player, x, y, w, h, color, 0, 2, 2);
                            for (int a = y; a < y + h && a < 60; a++)
                                for (int b = x; b < x + w && b < 80; b++)
                                    map2L1[a][b] = o;
                            worldObjectsM2L1.add(o);
                            break;
                        case 'o':
                            color = Color.RED;
                            w = Integer.parseInt(content.substring(8, 9));
                            h = Integer.parseInt(content.substring(10, 11));
                            id = Integer.parseInt(content.substring(12, 14));
                            or = content.charAt(14);
                            o = new Decor(this, player, x, y, w, h, color, id, 2, 2, or);
                            for (int a = y; a < y + h && a < 60; a++)
                                for (int b = x; b < x + w && b < 80; b++)
                                    map2L1[a][b] = o;
                            worldObjectsM2L1.add(o);
                            break;
                        case 's':
                            color = Color.GRAY;
                            w = Integer.parseInt(content.substring(8, 10));
                            h = Integer.parseInt(content.substring(11));
                            o = new Background(this, player, x, y, w, h, color, 0, 3, 2);
                            for (int a = y; a < y + h && a < 60; a++)
                                for (int b = x; b < x + w && b < 80; b++)
                                    map2L1[a][b] = o;
                            worldObjectsM2L1.add(o);
                            break;
                        case 'd':
                            color = Color.YELLOW;
                            dir = content.charAt(7); //vertical or horizontal
                            state = content.charAt(8); //open or closed
                            if (dir == 'h') {
                                w = 2;
                                h = 1;
                            } else if (dir == 'v') {
                                w = 1;
                                h = 2;
                            }
                            if (state == 'o') {
                                st = 1;
                            } else if (state == 'c') {
                                st = 0;
                            }
                            o = new InteractiveObject(this, player, x, y, w, h, color, 0, 2, st, 2);
                            for (int a = y; a < y + h && a < 60; a++)
                                for (int b = x; b < x + w && b < 80; b++)
                                    map2L2[a][b] = (InteractiveObject) o;
                            worldObjectsM2L2.add(o);
                            break;
                        case '#':
                            color = Color.GREEN;
                            id = Integer.parseInt(content.substring(8));
                            o = new InteractiveObject(this, player, x, y, 1, 1, color, id, 1, -1, 2);
                            map2L2[y][x] = (InteractiveObject) o;
                            worldObjectsM2L2.add(o);
                            break;
                        case 'c':
                            id = Integer.parseInt(content.substring(8, 10));
                            int s = Integer.parseInt(content.substring(10, 11));
                            if (id == 2) {
                                sidekick = new Sidekick(this, player, x, y, 2);
                                for (int a = y; a < y + 2; a++)
                                    for (int b = x; b < x + 2; b++)
                                        map2L3[a][b] = sidekick;
                                worldObjectsM2L3.add(sidekick);
                            } else {
                                Character c = new Character(this, player, x, y, id, s, 2);
                                worldObjectsM2L3.add(c);
                                characters[id] = c;
                                for (int a = y; a < y + 2; a++)
                                    for (int b = x; b < x + 2; b++)
                                        map2L3[a][b] = c;
                            }
                            break;
                    }
                }
            }
            // add walls in empty places
            for (int i = 0; i < Constants.WORLD_HEIGHT; i++)
                for (int j = 0; j < Constants.WORLD_WIDTH; j++)
                    if (map2L1[i][j] == null) {
                        o = new Decor(this, player, j, i, 1, 1, Color.WHITE, i+j, 1, 2, ' ');
                        worldObjectsM2L1.add(o);
                        map2L1[i][j] = o;
                    }
            //create navigation mesh after input
            createNavMesh(2);

            //map3
            while (in3.hasNext()) {
                String line = in3.nextLine();
                String[] contents = line.split("\\s+");
                for (String content: contents) {
                    int x = Integer.parseInt(content.substring(2, 4));
                    int y = Integer.parseInt(content.substring(5, 7));
                    int w, h, id;
                    char or;
                    switch (content.charAt(0)) {
                        case 'a':
                            color = Color.BLACK;
                            w = Integer.parseInt(content.substring(8, 10));
                            h = Integer.parseInt(content.substring(11));
                            o = new Background(this, player, x, y, w, h, color, 0, 1, 3);
                            for (int a = y; a < y + h && a < 60; a++)
                                for (int b = x; b < x + w && b < 80; b++)
                                    map3L1[a][b] = o;
                            worldObjectsM3L1.add(o);
                            break;
                        case 'r':
                            color = Color.BLUE;
                            w = Integer.parseInt(content.substring(8, 10));
                            h = Integer.parseInt(content.substring(11));
                            o = new Decor(this, player, x, y, w, h, color, 0, 3, 3, ' ');
                            for (int a = y; a < y + h && a < 60; a++)
                                for (int b = x; b < x + w && b < 80; b++)
                                    map3L1[a][b] = o;
                            worldObjectsM3L1.add(o);
                            break;
                        case 'g':
                            color = Color.BLACK;
                            w = Integer.parseInt(content.substring(8, 10));
                            h = Integer.parseInt(content.substring(11));
                            o = new Background(this, player, x, y, w, h, color, 0, 2, 3);
                            for (int a = y; a < y + h && a < 60; a++)
                                for (int b = x; b < x + w && b < 80; b++)
                                    map3L1[a][b] = o;
                            worldObjectsM3L1.add(o);
                            break;
                        case 'o':
                            color = Color.RED;
                            w = Integer.parseInt(content.substring(8, 9));
                            h = Integer.parseInt(content.substring(10, 11));
                            id = Integer.parseInt(content.substring(12, 14));
                            or = content.charAt(14);
                            o = new Decor(this, player, x, y, w, h, color, id, 2, 3, or);
                            for (int a = y; a < y + h && a < 60; a++)
                                for (int b = x; b < x + w && b < 80; b++)
                                    map3L1[a][b] = o;
                            worldObjectsM3L1.add(o);
                            break;
                        case 's':
                            color = Color.GRAY;
                            w = Integer.parseInt(content.substring(8, 10));
                            h = Integer.parseInt(content.substring(11));
                            o = new Background(this, player, x, y, w, h, color, 0, 3, 3);
                            for (int a = y; a < y + h && a < 60; a++)
                                for (int b = x; b < x + w && b < 80; b++)
                                    map3L1[a][b] = o;
                            worldObjectsM3L1.add(o);
                            break;
                        case '#':
                            color = Color.GREEN;
                            id = Integer.parseInt(content.substring(8));
                            o = new InteractiveObject(this, player, x, y, 1, 1, color, id, 1, -1, 3);
                            map3L2[y][x] = (InteractiveObject) o;
                            worldObjectsM3L2.add(o);
                            break;
                        case 'c':
                            id = Integer.parseInt(content.substring(8, 10));
                            int s = Integer.parseInt(content.substring(10, 11));
                            if (id == 2) {
                                sidekick = new Sidekick(this, player, x, y, 3);
                                for (int a = y; a < y + 2; a++)
                                    for (int b = x; b < x + 2; b++)
                                        map3L3[a][b] = sidekick;
                                worldObjectsM3L3.add(sidekick);
                            } else {
                                Character c = new Character(this, player, x, y, id, s, 3);
                                worldObjectsM3L3.add(c);
                                characters[id] = c;
                                for (int a = y; a < y + 2; a++)
                                    for (int b = x; b < x + 2; b++)
                                        map3L3[a][b] = c;
                            }
                            break;
                    }
                }
            }
            //create navigation mesh after input
            createNavMesh(3);

            //map4
            while (in4.hasNext()) {
                String line = in4.nextLine();
                String[] contents = line.split("\\s+");
                for (String content: contents) {
                    int x = Integer.parseInt(content.substring(2, 4));
                    int y = Integer.parseInt(content.substring(5, 7));
                    int w = 1, h = 1, id, st = -1;
                    char dir, state, or;
                    switch (content.charAt(0)) {
                        case 'a':
                            color = Color.BLACK;
                            w = Integer.parseInt(content.substring(8, 10));
                            h = Integer.parseInt(content.substring(11));
                            o = new Background(this, player, x, y, w, h, color, 0, 1, 4);
                            for (int a = y; a < y + h && a < 60; a++)
                                for (int b = x; b < x + w && b < 80; b++)
                                    map4L1[a][b] = o;
                            worldObjectsM4L1.add(o);
                            break;
                        case 'g':
                            color = Color.BLACK;
                            w = Integer.parseInt(content.substring(8, 10));
                            h = Integer.parseInt(content.substring(11));
                            o = new Background(this, player, x, y, w, h, color, 0, 2, 4);
                            for (int a = y; a < y + h && a < 60; a++)
                                for (int b = x; b < x + w && b < 80; b++)
                                    map4L1[a][b] = o;
                            worldObjectsM4L1.add(o);
                            break;
                        case 'o':
                            color = Color.RED;
                            w = Integer.parseInt(content.substring(8, 9));
                            h = Integer.parseInt(content.substring(10, 11));
                            id = Integer.parseInt(content.substring(12, 14));
                            or = content.charAt(14);
                            o = new Decor(this, player, x, y, w, h, color, id, 2, 4, or);
                            for (int a = y; a < y + h && a < 60; a++)
                                for (int b = x; b < x + w && b < 80; b++)
                                    map4L1[a][b] = o;
                            worldObjectsM4L1.add(o);
                            break;
                        case 's':
                            color = Color.GRAY;
                            w = Integer.parseInt(content.substring(8, 10));
                            h = Integer.parseInt(content.substring(11));
                            o = new Background(this, player, x, y, w, h, color, 0, 3, 4);
                            for (int a = y; a < y + h && a < 60; a++)
                                for (int b = x; b < x + w && b < 80; b++)
                                    map4L1[a][b] = o;
                            worldObjectsM4L1.add(o);
                            break;
                        case 'd':
                            color = Color.YELLOW;
                            dir = content.charAt(7); //vertical or horizontal
                            state = content.charAt(8); //open or closed
                            if (dir == 'h') {
                                w = 2;
                                h = 1;
                            } else if (dir == 'v') {
                                w = 1;
                                h = 2;
                            }
                            if (state == 'o') {
                                st = 1;
                            } else if (state == 'c') {
                                st = 0;
                            }
                            o = new InteractiveObject(this, player, x, y, w, h, color, 0, 2, st, 4);
                            for (int a = y; a < y + h && a < 60; a++)
                                for (int b = x; b < x + w && b < 80; b++)
                                    map4L2[a][b] = (InteractiveObject) o;
                            worldObjectsM4L2.add(o);
                            break;
                        case '#':
                            color = Color.GREEN;
                            id = Integer.parseInt(content.substring(8));
                            o = new InteractiveObject(this, player, x, y, 1, 1, color, id, 1, -1, 4);
                            map4L2[y][x] = (InteractiveObject) o;
                            worldObjectsM4L2.add(o);
                            break;
                        case 'c':
                            id = Integer.parseInt(content.substring(8, 10));
                            int s = Integer.parseInt(content.substring(10, 11));
                            if (id == 2) {
                                sidekick = new Sidekick(this, player, x, y, 4);
                                for (int a = y; a < y + 2; a++)
                                    for (int b = x; b < x + 2; b++)
                                        map4L3[a][b] = sidekick;
                                worldObjectsM4L3.add(sidekick);
                            } else {
                                Character c = new Character(this, player, x, y, id, s, 4);
                                worldObjectsM4L3.add(c);
                                characters[id] = c;
                                for (int a = y; a < y + 2; a++)
                                    for (int b = x; b < x + 2; b++)
                                        map4L3[a][b] = c;
                            }
                            break;
                    }
                }
            }
            // add walls in empty places
            for (int i = 0; i < Constants.WORLD_HEIGHT; i++)
                for (int j = 0; j < Constants.WORLD_WIDTH; j++)
                    if (map4L1[i][j] == null) {
                        o = new Decor(this, player, j, i, 1, 1, Color.WHITE, i+j, 1, 4, ' ');
                        worldObjectsM4L1.add(o);
                        map4L1[i][j] = o;
                    }
            //create navigation mesh after input
            createNavMesh(4);

            //map5
            while (in5.hasNext()) {
                String line = in5.nextLine();
                String[] contents = line.split("\\s+");
                for (String content: contents) {
                    int x = Integer.parseInt(content.substring(2, 4));
                    int y = Integer.parseInt(content.substring(5, 7));
                    int w = 1, h = 1, id, st = -1;
                    char dir, state, or;
                    switch (content.charAt(0)) {
                        case 'f':
                            color = Color.BLACK;
                            w = Integer.parseInt(content.substring(8, 10));
                            h = Integer.parseInt(content.substring(11));
                            o = new Background(this, player, x, y, w, h, color, 0, 1, 5);
                            for (int a = y; a < y + h && a < 60; a++)
                                for (int b = x; b < x + w && b < 80; b++)
                                    map5L1[a][b] = o;
                            worldObjectsM5L1.add(o);
                            break;
                        case 'y':
                            color = Color.BLACK;
                            w = Integer.parseInt(content.substring(8, 10));
                            h = Integer.parseInt(content.substring(11));
                            o = new Background(this, player, x, y, w, h, color, 0, 2, 5);
                            for (int a = y; a < y + h && a < 60; a++)
                                for (int b = x; b < x + w && b < 80; b++)
                                    map5L1[a][b] = o;
                            worldObjectsM5L1.add(o);
                            break;
                        case 'o':
                            color = Color.RED;
                            w = Integer.parseInt(content.substring(8, 9));
                            h = Integer.parseInt(content.substring(10, 11));
                            id = Integer.parseInt(content.substring(12, 14));
                            or = content.charAt(14);
                            o = new Decor(this, player, x, y, w, h, color, id, 2, 5, or);
                            for (int a = y; a < y + h && a < 60; a++)
                                for (int b = x; b < x + w && b < 80; b++)
                                    map5L1[a][b] = o;
                            worldObjectsM5L1.add(o);
                            break;
                        case 'd':
                            color = Color.YELLOW;
                            dir = content.charAt(7); //vertical or horizontal
                            state = content.charAt(8); //open or closed
                            if (dir == 'h') {
                                w = 2;
                                h = 1;
                            } else if (dir == 'v') {
                                w = 1;
                                h = 2;
                            }
                            if (state == 'o') {
                                st = 1;
                            } else if (state == 'c') {
                                st = 0;
                            }
                            o = new InteractiveObject(this, player, x, y, w, h, color, 0, 2, st, 5);
                            for (int a = y; a < y + h && a < 60; a++)
                                for (int b = x; b < x + w && b < 80; b++)
                                    map5L2[a][b] = (InteractiveObject) o;
                            worldObjectsM5L2.add(o);
                            break;
                        case '#':
                            color = Color.GREEN;
                            id = Integer.parseInt(content.substring(8));
                            o = new InteractiveObject(this, player, x, y, 1, 1, color, id, 1, -1, 5);
                            map5L2[y][x] = (InteractiveObject) o;
                            worldObjectsM5L2.add(o);
                            break;
                        case 'c':
                            id = Integer.parseInt(content.substring(8, 10));
                            int s = Integer.parseInt(content.substring(10, 11));
                            if (id == 2) {
                                sidekick = new Sidekick(this, player, x, y, 5);
                                for (int a = y; a < y + 2; a++)
                                    for (int b = x; b < x + 2; b++)
                                        map5L3[a][b] = sidekick;
                                worldObjectsM5L3.add(sidekick);
                            } else {
                                Character c = new Character(this, player, x, y, id, s, 5);
                                worldObjectsM5L3.add(c);
                                characters[id] = c;
                                for (int a = y; a < y + 2; a++)
                                    for (int b = x; b < x + 2; b++)
                                        map5L3[a][b] = c;
                            }
                            break;
                    }
                }
            }
            // add walls in empty places
            for (int i = 0; i < Constants.WORLD_HEIGHT; i++)
                for (int j = 0; j < Constants.WORLD_WIDTH; j++)
                    if (map5L1[i][j] == null) {
                        o = new Decor(this, player, j, i, 1, 1, Color.GRAY, i+j, 4, 5, ' ');
                        worldObjectsM5L1.add(o);
                        map5L1[i][j] = o;
                    }
            //create navigation mesh after input
            createNavMesh(5);
        }

        // initialise references to map 1
        worldObjectsL1 = worldObjectsM1L1;
        worldObjectsL2 = worldObjectsM1L2;
        worldObjectsL3 = worldObjectsM1L3;
        mapL1 = map1L1;
        mapL2 = map1L2;
        mapL3 = map1L3;

        // add player
        player = new Player(this, ctrl);

        //add player to map 1 layer 3
        for (int a = player.y; a < player.y + 2; a++)
            for (int b = player.x; b < player.x + 2; b++)
                map1L3[a][b] = player;
        worldObjectsM1L3.add(player);

        //vision range
        visW = WORLD_WIDTH * SCALE / 2 + 1;
        visH = WORLD_HEIGHT * SCALE / 2 + 1;
        vis = new int[visW][visH];
        visM1 = new int[visW][visH];
        visM2 = new int[visW][visH];
        visM3 = new int[visW][visH];
        visM4 = new int[visW][visH];
        visM5 = new int[visW][visH];
        for (int i = 0; i < visW; i++) {
            for (int j = 0; j < visH; j++) {
                if (Math.sqrt((player.y*SCALE / 2 - j) * (player.y*SCALE / 2 - j) + (player.x*SCALE / 2 - i) * (player.x*SCALE / 2 - i)) <= FOG) {
                    visM1[i][j] = 0;
                }
                else {
                    visM1[i][j] = 1;
                }
            }
        }
        vis = visM1;
    }

    public static void main(String[] args) throws InterruptedException, IOException {
        KaluxGame game = new KaluxGame();
        View view = new View(game);
        JEasyFrame f = new JEasyFrame(view, "game");
        f.addKeyListener(game.ctrl);
        f.setSize(Constants.FRAME_SIZE);
        int moneyGainDelay = 0;
        int moneyGain = 10;

        // game loop
        while (game.gameRunning) {
            if (!game.freeze) {
                game.updateEvent();
                game.updateMap();
                game.update();
                game.updateVision();
                game.collectDropObject();
                game.useObject();

                //check achievements
                if (game.player.money >= 100) {
                    game.ach[0] = true;
                }
                if (game.event == 3) {
                    game.ach[1] = true;
                }

                if (game.ach[0] && game.ach[1]) {
                    //add bonus character
                    Character c = new Character(game, game.player, 16, 50, 10, 1, 3);
                    game.worldObjectsM3L3.add(c);
                    game.characters[10] = c;
                    for (int a = 16; a < 16 + 2; a++)
                        for (int b = 50; b < 50 + 2; b++)
                            game.map3L3[a][b] = c;
                }

                //add money
                if (moneyGainDelay < 300)
                    moneyGainDelay++;
                else {
                    game.player.money += moneyGain;
                    moneyGainDelay = 0;
                }
            }
            view.repaint();
            game.talk();
            Thread.sleep(Constants.DELAY);
        }
    }

    //check for event change
    public void updateEvent() {
        //check for stage change for every event
        switch (event) {
            case 1:
                if (currentMap == 1 && getPlayerRoom().x == 31 && getPlayerRoom().y == 26 && stage == 1) {// living room
                    stage = 2;
                    stageChanged = true;
                    notebook2.add("It's all over the news: \"Poor Lilith Joy Wings was found dead late last night just " +
                            "after her shift was due to end. Official Kay Martin, who was amongst the first at the scene, " +
                            "informed us that it was all an unfortunate work accident. She waas a beloved and well known " +
                            "member of our comunity and she will be greatly missed. ...");
                    notebook2.add("The good thing is that our brilliant Officials are doing their job perfectly and we " +
                            "are still safe from murderers and vandals. In other news...\" This is ridiculous.");
                } else {
                    if (currentMap == 1 && getPlayerRoom().x == 31 && getPlayerRoom().y == 15 && stage == 2) {// bedroom
                        stage = 3;
                        stageChanged = true;
                        notebook2.add("This is interesting. If I stand on top of an item and press 'C' I can collect that item. " +
                                "If I press 'D' I can drop an item from the backpack.");
                    } else {
                        if (gotBackpack && backpack.isEmpty() && stage == 3) { // picked up backpack and dropped unwanted item
                            stage = 4;
                            stageChanged = true;
                            notebook2.add("I can travel now! Although it appears to cost 5$. But if I press 'M' and the number " +
                                    "of the location I want to travel to (1-5) at the same time, I'll be there in no time! " +
                                    "(M-1 for home, M-2 for factory, M-3 for park, M-4 for Officials HQ, M-5 for wilderness).");
                        } else {
                            if (currentMap == 2 && stage == 4) { //traveled to factory
                                stage = 5;
                                stageChanged = true;
                                notebook2.add("Oh look! If I move around this fog around me disappears and I can see all the objects. Noted.");
                            } else {
                                if (currentMap == 2 && getPlayerRoom().x == 44 && getPlayerRoom().y == 6 && stage == 5) { // Daren's location
                                    stage = 6;
                                    stageChanged = true;
                                    notebook2.add("I can talk to people! All I have to do is run up to someone and press the SPACE BAR, " +
                                            "although it looks like I have to be facing them for this to work. I'd be rude otherwise. " +
                                            "I can then choose my lines by pressing either 'Q' or 'W'. Useful.");
                                } else {
                                    if (sidekick.talked && stage == 6) { // talked to Daren
                                        stage = 7;
                                        stageChanged = true;
                                    } else {
                                        if (backpack.size() == 2 && stage == 7) { // picked up piece of note & key
                                            stage = 8;
                                            stageChanged = true;
                                            notebook2.add("I can use the items in my backpack, if I press the number corresponding to the " +
                                                    "position of that item in the backpack (1-9). Although it looks like I need to be in a " +
                                                    "specific location for some to work. I can always ask Daren, I'm sure he'll give me a clue.");
                                        } else {
                                            for (InteractiveObject o: backpack) {
                                                if (o.id == 4 && stage == 8) { // opened filing cabinet and found papers
                                                    stage = 9;
                                                    stageChanged = true;
                                                    break;
                                                }
                                            }
                                            if (characters[3].talked && stage == 9) { // talked to Tracy
                                                stage = 10;
                                                stageChanged = true;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                break;
            case 2:
                if (stage == 1 && characters[4] == null) {
                    //make characters 4,5,6 show up in a location close to the player
                    for (int i = 0; i < 3; i++) {
                        Random r = new Random();
                        boolean ok = false;
                        while (!ok) {
                            int a = r.nextInt(20) + 5;
                            int p = player.x + a;
                            if (p >= WORLD_WIDTH) p = WORLD_WIDTH - 1;
                            if (p < 0) p = 0;
                            int q = player.y + a;
                            if (q >= WORLD_HEIGHT) q = WORLD_HEIGHT - 1;
                            if (q < 0) q = 0;
                            boolean k = true;
                            for (int d = q; d < q + 2; d++)
                                for (int b = p; b < p + 2; b++)
                                    if (!(mapL3[d][b] == null && (mapL1[d][b] instanceof Background ||
                                            mapL2[d][b] != null && mapL2[d][b].open == 1))) {
                                        k = false;
                                    }
                            if (k) {
                                ok = true;
                                Character c = new Character(this, player, p, q, 4+i, 0, 2);
                                worldObjectsM2L3.add(c);
                                characters[4+i] = c;
                                for (int d = q; d < q + 2; d++)
                                    for (int b = p; b < p + 2; b++)
                                        map2L3[d][b] = c;
                            }
                        }
                    }
                } else if (stage == 1 && characters[4].talked) { // talked to the head Official
                    stage = 2;
                    stageChanged = true;
                    for (Character i: characters) {
                        if (i.side == 0) i.img = i.imge;
                        else i.img = i.imgf;
                    }
                    notebook2.add("These pesky Officials seem to always be on the lookout if I talk to the wrong person. " +
                            "I should keep in mind that the highlight around someone means they are either friendly " +
                            "(green) or enemies (red). If I talk to enemy characters, the Officials show up and chase me." +
                            " Bad things happen if they catch me, so I'd better run!");

                    //remove Officials from previous location
                    for (int i = 4; i<= 6; i++) {
                        int q = characters[i].y;
                        int p = characters[i].x;
                        for (int d = q; d < q + 2; d++) {
                            for (int b = p; b < p + 2; b++) {
                                worldObjectsM2L3.remove(map2L3[d][b]);
                                map2L3[d][b] = null;
                            }
                        }
                    }
                } else if (stage == 2 && characters[1].talked) { //talked to Jayde
                    stage = 3;
                    stageChanged = true;
                    notebook4.add("So Lilith has something to do with a rebellion. It seems like she's the leader of it " +
                            "and she was killed at the order of the Leader in order to kill the rebellion with her.");
                } else if (stage == 3 && characters[7].talked) { //talked to Sebastian
                    stage = 4;
                    stageChanged = true;
                    notebook4.add("Sebastian is a rebel. He knows all the ins and outs of the city and even though Lilith " +
                            "was indeed technically a part of their rebellion, he thought there was a lot more to it than she led on. ");
                    notebook3.add("These papers he gave me, they are crucial to my investigation. Lilith wass actually " +
                            "woking with a group of Officials who stand by the current structure of the City and she was " +
                            "pretending to lead and instigate a rebellion in order to find weak members of the society " +
                            "and eliminate them, usually making them disappear without a trace. ");
                } else {
                    if (stage == 4) { //all pieces of note collected
                        int count = 0;
                        for (InteractiveObject o: backpack) {
                            if (o.id == 3 || o.id == 5 || o.id == 6 || o.id ==7) { // opened filing cabinet and found papers
                                count++;
                            }
                        }
                        if (count==4) {
                            stage = 10;
                            stageChanged = true;
                            notebook3.add("The note says \"My cover was blown. I can't cotinue the mission any longer. " +
                                    "I fear my life is in danger as they are all turning against me now. I'm going outside " +
                                    "the city to meet Charlie, he's the only one that can save the city. If I don't succeed, " +
                                    "you have to send someone else or all will be lost.\" It appears this note was written by Lilith. " +
                                    "I'm not sure who it was addressed to, but I should go talk to Charlie.");
                        }
                    }
                }
                break;
            case 3:
                if (stage == 1) { //escaped cell
                    for (InteractiveObject o: backpack) {
                        if (o.id == 4) { // have key in inventory to escape the cell
                            stage = 2;
                            stageChanged = true;
                            break;
                        }
                    }
                } else if (stage == 2) { //last stage, return to previous event and stage before getting caught; escaped building
                    if (getPlayerRoom().type == 2 || getPlayerRoom().type == 3) {
                        event = rememberEvent;
                        stage = rememberStage;
                    }
                }
                break;
            case 4:
                if (stage == 1 && characters[8].talked) { //talked to Charlie
                    stage = 2;
                    stageChanged = true;
                    notebook5.add("The Leader is the actual head rebel, accordin to Charlie. He found that he doesn't " +
                            "have enough power to change the way things are run; a previous attempt got him a death threat " +
                            "from the Officials who try to use him as a puppet, so he runs things in secret instead. " +
                            "He might have head something to do with Lilith's death.");
                } else if (stage == 2 && characters[9].talked) { //talked to the Leader
                    stage = 10;
                    stageChanged = true;
                    notebook5.add("The Leader killed Lilith. He found out what her mission was and by ending her, " +
                            "he believed that he was fighting for freedom of the people and basic human rights." +
                            " Everyone covers the murder as an accident for different purposes, although they are all " +
                            "suspicious and the Officials secretly investigate it themselves, as they can't do it ppublicly " +
                            "because that would mean revealing the rebellion as an official important problem, which would start a civil war.");
                    notebook5.add("The Officials want to have me killed so I won't find out about the rebellion and their operation will remain a secret.\n" +
                            "The Leader gave me an ultimatum: join him and keep his secret or die.");
                }
                break;
            default: break;
        }

        if (stage == 10) { // end of event
            if (event == 2) event = 4; //skip event 3
            else event++;
            stage = 1;
            eventStarted = false;
        }

        if (huntedMode) {
            //spawn Officials randomly on the map
            for (int i = 0; i < 3; i++) {
                Random r = new Random();
                boolean ok = false;
                while (!ok) {
                    int a = r.nextInt(WORLD_WIDTH - 1);
                    int p = r.nextInt(WORLD_HEIGHT - 1);
                    boolean k = true;
                    for (int d = p; d < p + 2; d++)
                        for (int b = a; b < a + 2; b++)
                            if (!(mapL3[d][b] == null && (mapL1[d][b] instanceof Background ||
                                    mapL2[d][b] != null && mapL2[d][b].open == 1))) {
                                k = false;
                            }
                    if (k) {
                        ok = true;
                        Character c = new Character(this, player, a, p, 4+i, 0, 2);
                        worldObjectsL3.add(c);
                        characters[4+i] = c;
                        for (int d = p; d < p + 2; d++)
                            for (int b = a; b < a + 2; b++)
                                mapL3[d][b] = c;
                        c.chase = true;
                    }
                }
            }
        }
        //disable huntedMode
        if (huntedMode) {
            boolean ok = true;
            for (int i = player.y - 30; i <= player.y + 30; i++) {
                for (int j = player.x - 30; j <= player.y + 30; j++) {
                    if (mapL3[i][j] == characters[4] || mapL3[i][j] ==characters[5] || mapL3[i][j] == characters[6]) {
                        ok = false;
                        break;
                    }
                }
            }
            if (ok) {
                huntedMode = false;
                SoundManager.huntedMode.stop();
            }

            //check if player caught
            boolean ok1 = false, ok2 = false, ok3 = false;
            for (int i = player.y - 30; i <= player.y + 30; i++) {
                for (int j = player.x - 30; j <= player.y + 30; j++) {
                    if (mapL3[i][j] == characters [4]) ok1 = true;
                    if (mapL3[i][j] == characters[5]) ok2 = true;
                    if (mapL3[i][j] == characters[6]) ok3 = true;
                }
            }
            if (ok1 && ok2 && ok3) { //move to event 3 which deals with being caught when haunted and is skipped in normal storyline
                huntedMode = false;
                SoundManager.huntedMode.stop();
                rememberEvent = event;
                rememberStage = stage;
                event = 3;
                stage = 1;
                eventStarted = false;
                stageChanged = true;

                //update map (map 4)
                //remove player from previous location
                for (int a = player.y; a < player.y + 2; a++)
                    for (int b = player.x; b < player.x + 2; b++)
                        mapL3[a][b] = null;
                worldObjectsL3.remove(player);

                //update map references
                SoundManager.loop(SoundManager.map4M);
                worldObjectsL1 = worldObjectsM4L1;
                worldObjectsL2 = worldObjectsM4L2;
                worldObjectsL3 = worldObjectsM4L3;
                mapL1 = map4L1;
                mapL2 = map4L2;
                mapL3 = map4L3;
                //location in cell
                player.x = 30;
                player.y = 3;

                if (currentMap != 4) player.money -= Constants.TRAVEL_COST;
                currentMap = 4;

                //vision range
                if (visited4 == 0) {
                    for (int i = 0; i < visW; i++) {
                        for (int j = 0; j < visH; j++) {
                            if (Math.sqrt((player.y * SCALE / 2 - j) * (player.y * SCALE / 2 - j) + (player.x * SCALE / 2 - i) * (player.x * SCALE / 2 - i)) <= FOG) {
                                visM4[i][j] = 0;
                            } else {
                                visM4[i][j] = 1;
                            }
                        }
                    }
                    visited4 = 1;
                }
                vis = visM4;

                // re-add reset player it to new map
                for (int a = player.y; a < player.y + 2; a++)
                    for (int b = player.x; b < player.x + 2; b++)
                        mapL3[a][b] = player;
                worldObjectsL3.add(player);
            }
        }
    }

    public void updateVision() {
        int x1 = player.x*SCALE / 2 - FOG;
        int x2 = player.x*SCALE / 2 + FOG;
        int y1 = player.y*SCALE / 2 - FOG;
        int y2 = player.y*SCALE / 2 + FOG;
        if (x1 <= 0) x1 = 0;
        if (x2 >= WORLD_WIDTH*SCALE / 2) x2 = WORLD_WIDTH*SCALE / 2;
        if (y1 <= 0) y1 = 0;
        if (y2 >= WORLD_HEIGHT*SCALE / 2) y2 = WORLD_HEIGHT*SCALE / 2;
        for (int i = x1; i <= x2; i++) {
            for (int j = y1; j <= y2; j++) {
                if (vis[i][j]==1) {
                    if (Math.sqrt((player.y*SCALE / 2 - j) * (player.y*SCALE / 2 - j) + (player.x*SCALE / 2 - i) * (player.x*SCALE / 2 - i)) <= FOG) {
                        vis[i][j] = 0;
                    }
                }
            }
        }
    }

    public void update() {
        for (GameObject i : worldObjectsL3) {
            synchronized(this) {
                //update moving objects
                ((MovingObject)i).update();
            }
        }
    }

    public void updateMap() {
        if ((ctrl.action.map1 || ctrl.action.map2 || ctrl.action.map3 || ctrl.action.map4 || ctrl.action.map5) && !huntedMode) {
            //if map was changed, remove player from previous location
            for (int a = player.y; a < player.y + 2; a++)
                for (int b = player.x; b < player.x + 2; b++)
                    mapL3[a][b] = null;
            worldObjectsL3.remove(player);
            //change references to new map
            if (ctrl.action.map1) {
                SoundManager.loop(SoundManager.map1M);
                worldObjectsL1 = worldObjectsM1L1;
                worldObjectsL2 = worldObjectsM1L2;
                worldObjectsL3 = worldObjectsM1L3;
                mapL1 = map1L1;
                mapL2 = map1L2;
                mapL3 = map1L3;
                player.x = Player.x1;
                player.y = Player.y1;

                if (currentMap != 1) player.money -= Constants.TRAVEL_COST;
                currentMap = 1;

                //vision range
                vis = visM1;
            } else if (ctrl.action.map2) {
                SoundManager.loop(SoundManager.map2M);
                worldObjectsL1 = worldObjectsM2L1;
                worldObjectsL2 = worldObjectsM2L2;
                worldObjectsL3 = worldObjectsM2L3;
                mapL1 = map2L1;
                mapL2 = map2L2;
                mapL3 = map2L3;
                player.x = Player.x2;
                player.y = Player.y2;

                if (currentMap != 2) player.money -= Constants.TRAVEL_COST;
                currentMap = 2;

                //vision range
                if (visited2 == 0) {
                    for (int i = 0; i < visW; i++) {
                        for (int j = 0; j < visH; j++) {
                            if (Math.sqrt((player.y*SCALE / 2 - j) * (player.y*SCALE / 2 - j) + (player.x*SCALE / 2 - i) * (player.x*SCALE / 2 - i)) <= FOG) {
                                visM2[i][j] = 0;
                            }
                            else {
                                visM2[i][j] = 1;
                            }
                        }
                    }
                    visited2 = 1;
                }
                vis = visM2;
            } else if (ctrl.action.map3) {
                SoundManager.loop(SoundManager.map3M);
                worldObjectsL1 = worldObjectsM3L1;
                worldObjectsL2 = worldObjectsM3L2;
                worldObjectsL3 = worldObjectsM3L3;
                mapL1 = map3L1;
                mapL2 = map3L2;
                mapL3 = map3L3;
                player.x = Player.x3;
                player.y = Player.y3;

                if (currentMap != 3) player.money -= Constants.TRAVEL_COST;
                currentMap = 3;

                //vision range
                if (visited3 == 0) {
                    for (int i = 0; i < visW; i++) {
                        for (int j = 0; j < visH; j++) {
                            if (Math.sqrt((player.y*SCALE / 2 - j) * (player.y*SCALE / 2 - j) + (player.x*SCALE / 2 - i) * (player.x*SCALE / 2 - i)) <= FOG) {
                                visM3[i][j] = 0;
                            }
                            else {
                                visM3[i][j] = 1;
                            }
                        }
                    }
                    visited3 = 1;
                }
                vis = visM3;
            } else if (ctrl.action.map4) {
                SoundManager.loop(SoundManager.map4M);
                worldObjectsL1 = worldObjectsM4L1;
                worldObjectsL2 = worldObjectsM4L2;
                worldObjectsL3 = worldObjectsM4L3;
                mapL1 = map4L1;
                mapL2 = map4L2;
                mapL3 = map4L3;
                player.x = Player.x4;
                player.y = Player.y4;

                if (currentMap != 4) player.money -= Constants.TRAVEL_COST;
                currentMap = 4;

                //vision range
                if (visited4 == 0) {
                    for (int i = 0; i < visW; i++) {
                        for (int j = 0; j < visH; j++) {
                            if (Math.sqrt((player.y*SCALE / 2 - j) * (player.y*SCALE / 2 - j) + (player.x*SCALE / 2 - i) * (player.x*SCALE / 2 - i)) <= FOG) {
                                visM4[i][j] = 0;
                            }
                            else {
                                visM4[i][j] = 1;
                            }
                        }
                    }
                    visited4 = 1;
                }
                vis = visM4;
            } else if (ctrl.action.map5) {
                SoundManager.loop(SoundManager.map5M);
                worldObjectsL1 = worldObjectsM5L1;
                worldObjectsL2 = worldObjectsM5L2;
                worldObjectsL3 = worldObjectsM5L3;
                mapL1 = map5L1;
                mapL2 = map5L2;
                mapL3 = map5L3;
                player.x = Player.x5;
                player.y = Player.y5;

                if (currentMap != 5) player.money -= Constants.TRAVEL_COST;
                currentMap = 5;

                //vision range
                if (visited5 == 0) {
                    for (int i = 0; i < visW; i++) {
                        for (int j = 0; j < visH; j++) {
                            if (Math.sqrt((player.y*SCALE / 2 - j) * (player.y*SCALE / 2 - j) + (player.x*SCALE / 2 - i) * (player.x*SCALE / 2 - i)) <= FOG) {
                                visM5[i][j] = 0;
                            }
                            else {
                                visM5[i][j] = 1;
                            }
                        }
                    }
                    visited5 = 1;
                }
                vis = visM5;
            }
            // re-add reset player it to new map
            for (int a = player.y; a < player.y + 2; a++)
                for (int b = player.x; b < player.x + 2; b++)
                    mapL3[a][b] = player;
            worldObjectsL3.add(player);
        }
    }

    // manage talking interaction
    public void talk () {

        if (player.talking) {
            Character talkingChar = player.character;

            //enable hunted mode when player talks to an enemy character
            if (!huntedMode && event >= 2) {
                if (talkingChar.side == 0) {
                    huntedMode = true;
                    SoundManager.loop(SoundManager.huntedMode);
                }
            }

            if (talkDelay > 0) {
                talkDelay --;
            } else {
                talkDelay = 5;

                //get lines available to player
                playerLines = talkingChar.getPlayerLines();

                // get selected player option
                if (ctrl.action.line1) {
                    playerOption = 0;
                    // get reply from character
                    answer = talkingChar.getAnswer(playerOption);
                    talking = true;
                } else if (ctrl.action.line2) {
                    playerOption = 1;
                    // get reply from character
                    answer = talkingChar.getAnswer(playerOption);
                    talking = true;
                } /*else if (ctrl.action.line3) {
                playerOption = 2;
                talking = true;
            } else if (ctrl.action.line4) {
                playerOption = 3;
                talking = true;
            } */


                // if first time talking, give greeting instead of reply
                if (!talking)
                    answer = talkingChar.getGreeting();

                // check for end of talking
                if (playerOption == 1 || !talkingChar.talking) {
                    talkingChar.talking = false;
                    playerOption = -1;
                    player.talking = false;
                    player.blockedMovement = false;
                    player.character = null;
                    talkingChar.blockedMovement = false;
                    talking = false;
                    answer = "";
                }
            }
        }
    }

    public void collectDropObject() {
        if (removeDelay > 0) removeDelay--;
        // drop object
        if (ctrl.action.drop && !backpack.isEmpty() && removeDelay == 0) {
            backpack.remove(0);
            SoundManager.play(SoundManager.dropS);
            removeDelay = 30;
        }

        // collect object
        if (ctrl.action.collect)
            synchronized (this) {
                for (int i = player.y - 1; i < player.y + player.h + 1; i++)
                    for (int j = player.x - 1; j < player.x + player.w + 1; j++)
                        if (mapL2[i][j] != null && (mapL2[i][j]).type == 1 && backpack.size() < 9) {
                            if (!gotBackpack && mapL2[i][j].id == 1) {
                                gotBackpack = true;
                                backpack.add(mapL2[i][j]);
                                worldObjectsL2.remove(mapL2[i][j]);
                                mapL2[i][j] = null;
                                SoundManager.play(SoundManager.collectS);
                            }
                            else {
                                if (gotBackpack && mapL2[i][j].id != 4) { //things you can't pick up; 4 = key in filing cabinet
                                    backpack.add(mapL2[i][j]);
                                    // delete object off map and world
                                    worldObjectsL2.remove(mapL2[i][j]);
                                    mapL2[i][j] = null;
                                    SoundManager.play(SoundManager.collectS);
                                    break;
                                }
                            }
                        }
            }
    }

    public void useObject() {
        for (int i=0; i<9; i++) {
            if (ctrl.action.use[i]) {
                if (backpack.size() > i) {
                    backpack.get(i).execute();
                }
            }
        }
    }

    public Background getPlayerRoom() {
        Background r = null;
        for (GameObject i: worldObjectsL1) {
            if (i instanceof Background) {
                Background o = (Background)i;
                if (player.x >= o.x && player.x <= (o.x + o.w) && player.y >= o.y && player.y <= (o.y + o.h)) {
                    r = o;
                    break;
                }
            }
        }
        return r;
    }

    public void createNavMesh(int map) {
        NavMesh nm;
        WorldObject[][] mL1;
        InteractiveObject[][] mL2;
        MovingObject[][] mL3;
        switch(map) {
            case 1:
                mL1 = map1L1;
                mL2 = map1L2;
                mL3 = map1L3;
                nm = nm1;
                break;
            case 2:
                mL1 = map2L1;
                mL2 = map2L2;
                mL3 = map2L3;
                nm = nm2;
                break;
            case 3:
                mL1 = map3L1;
                mL2 = map3L2;
                mL3 = map3L3;
                nm = nm3;
                break;
            case 4:
                mL1 = map4L1;
                mL2 = map4L2;
                mL3 = map4L3;
                nm = nm4;
                break;
            default:
                mL1 = map5L1;
                mL2 = map5L2;
                mL3 = map5L3;
                nm = nm5;
                break;
        }
        for (int i = 0; i < WORLD_HEIGHT; i++) {
            for (int j = 0; j < WORLD_WIDTH; j++) {
                //check (i, j) possible node. since 2x2 characters will be using the navigation mesh, 3 more spots need to be checked
                if (((mL1[i][j] instanceof Background || mL2[i][j] != null && mL2[i][j].open == 1) && mL3[i][j] == null)) {
                    Node n;

                    if (!nm.findNode(j, i)) { // check if node is not already added
                        n = new Node(j, i);
                        nm.addNode(n);
                    } else {
                        n = nm.getNode(j, i);
                    }

                    //add neighbours top, down, left, right
                    if (i-1 >= 0)
                        if (((mL1[i-1][j] instanceof Background || mL2[i-1][j] != null && mL2[i-1][j].open == 1))) {
                            if (nm.findNode(j, i-1)) {
                                Node r = nm.getNode(j, i - 1);
                                if (!n.neighbours.contains(r))
                                    n.addNeighbour(r);
                                if (!r.neighbours.contains(n))
                                    r.addNeighbour(n);
                            }
                            else {
                                Node r = new Node(j, i - 1);
                                nm.addNode(r);
                                if (!n.neighbours.contains(r))
                                    n.addNeighbour(r);
                                if (!r.neighbours.contains(n))
                                    r.addNeighbour(n);
                            }
                        }
                    if (i+1 < WORLD_HEIGHT)
                        if (((mL1[i+1][j] instanceof Background || mL2[i+1][j] != null && mL2[i+1][j].open == 1))) {
                            if (nm.findNode(j, i + 1)) {
                                Node r = nm.getNode(j, i + 1);
                                if (!n.neighbours.contains(r))
                                    n.addNeighbour(r);
                                if (!r.neighbours.contains(n))
                                    r.addNeighbour(n);
                            }
                            else {
                                Node r = new Node(j, i + 1);
                                nm.addNode(r);
                                if (!n.neighbours.contains(r))
                                    n.addNeighbour(r);
                                if (!r.neighbours.contains(n))
                                    r.addNeighbour(n);
                            }
                        }
                    if (j-1 >= 0)
                        if (((mL1[i][j-1] instanceof Background || mL2[i][j-1] != null && mL2[i][j-1].open == 1))) {
                            if (nm.findNode(j - 1, i)) {
                                Node r = nm.getNode(j - 1, i);
                                if (!n.neighbours.contains(r))
                                    n.addNeighbour(r);
                                if (!r.neighbours.contains(n))
                                    r.addNeighbour(n);
                            }
                            else {
                                Node r = new Node(j - 1, i);
                                nm.addNode(r);
                                if (!n.neighbours.contains(r))
                                    n.addNeighbour(r);
                                if (!r.neighbours.contains(n))
                                    r.addNeighbour(n);
                            }
                        }
                    if (j+1 < WORLD_WIDTH)
                        if (((mL1[i][j+1] instanceof Background || mL2[i][j+1] != null && mL2[i][j+1].open == 1))) {
                            if (nm.findNode(j + 1, i)) {
                                Node r = nm.getNode(j + 1, i);
                                if (!n.neighbours.contains(r))
                                    n.addNeighbour(r);
                                if (!r.neighbours.contains(n))
                                    r.addNeighbour(n);
                            }
                            else {
                                Node r = new Node(j + 1, i);
                                nm.addNode(r);
                                if (!n.neighbours.contains(r))
                                    n.addNeighbour(r);
                                if (!r.neighbours.contains(n))
                                    r.addNeighbour(n);
                            }
                        }
                }
            }
        }
    }
}