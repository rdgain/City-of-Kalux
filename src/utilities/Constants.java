package utilities;

import utilities.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

public class Constants {


    /* full screen dimensions
    public final static GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
    public final static GraphicsDevice device = env.getScreenDevices()[0];
    public static final Rectangle RECTANGLE = device.getDefaultConfiguration().getBounds();
    public static final int WIDTH = RECTANGLE.width;
    public static final int HEIGHT = RECTANGLE.height;
    */

    // frame dimensions
    public static final int FRAME_HEIGHT = 830;
    public static final int FRAME_WIDTH = 855;
    //public static final int FRAME_HEIGHT = HEIGHT;
    //public static final int FRAME_WIDTH = WIDTH;
    public static final Dimension FRAME_SIZE = new Dimension(
            FRAME_WIDTH, FRAME_HEIGHT);
	
	// background color
	public static final Color BG_COLOR = Color.BLACK;

	// constants relating to frame rate
	public static final int DELAY = 50;

    public static final int TRAVEL_COST = 5;
    public static final int NO_ACHIEVEMENTS = 5;

    // matrix scale
    public static final int SCALE = 10;
    public static final int WORLD_HEIGHT = (FRAME_HEIGHT-230)/SCALE;
    public static final int WORLD_WIDTH = (FRAME_WIDTH-55)/SCALE;

    // fog of war
    public static final int FOG = 50;

    // import images
    public static BufferedImage INTERFACE, INTERFACENOB, BOX, N_BOX, STREET, WALL, WALLC, GROUND, PLAYER, GROUND_PARK, GROUND_CAVE, GROUND_5, GROUND_1, WATER, D_BOX;
    public static BufferedImage TABLE2, BED1, TOILET, SINK, TUB, MIRROR, MIRRORH, DRESSER, DRESSERV, CHAIR, TABLE3, TVV, BIN;
    public static BufferedImage ACHAIR, PAINTING, PAINTINGH, FRIDGE, FRIDGEV, COUNTER, COOKER, BOOKCASE, BOOKCASEV, PLANT, BED2, BED2H, COUCH, COUCHV, LAMP;
    public static BufferedImage BCOUNTER, CUBICLE, TABLEP, BENCH, BENCHV, BENCH1, BENCHV1, FOUNTAIN, TREE, FLOWER1, DESK, DESKV, STAIRS;
    public static BufferedImage SHOWER, SHOWER1, FCABINET, CDESK, CDESK1, DUMMY, WEAPONR, WEAPONR1, GAZEBO, NOCHAR, MAP;
    public static BufferedImage CHAR1, CHAR2, CH1, CH1E, CH1F, CH2, CH2F, CH3, CH3E, CH4, CH4E, CH5, CH5E, CH6, CH6E, CH7, CH7E, CH7F, CH8, CH8E, CH8F;
    public static BufferedImage CH9, CHAR3, CHAR4, CHAR5, CHAR6, CHAR7, CHAR8, CHAR10, CH10, CH10E, CH10F;
    public static BufferedImage EV1S1, EV1S2, EV1S3, EV1S4, EV1S5, EV1S6, EV1S8, EV2S2;
    //pickups
    public static BufferedImage KEY1, KEY2, CLOTHES, NOTE, NOTE1, NOTE2, NOTE3, NOTE4, FOOD;
    public static ArrayList<BufferedImage> EV1 = new ArrayList<BufferedImage>();
    static {
        try {
            INTERFACE = ImageManager.loadImage("interfacev3");
            INTERFACENOB = ImageManager.loadImage("interfacev4");
            D_BOX = ImageManager.loadImage("dialogue_box");
            N_BOX = ImageManager.loadImage("notebook_box");
            BOX = ImageManager.loadImage("box");
            NOCHAR = ImageManager.loadImage("nochar");
            MAP = ImageManager.loadImage("map");

            STREET = ImageManager.loadImage("textures/st");
            GROUND = ImageManager.loadImage("textures/gr");
            GROUND_PARK = ImageManager.loadImage("textures/park_g");
            GROUND_CAVE = ImageManager.loadImage("textures/cave_floor");
            GROUND_5 = ImageManager.loadImage("textures/gr5");
            GROUND_1 = ImageManager.loadImage("textures/gr1");
            WATER = ImageManager.loadImage("textures/water1");

            PLAYER = ImageManager.loadImage("characters/pl1");
            CHAR1 = ImageManager.loadImage("characters/char1");
            CHAR2 = ImageManager.loadImage("characters/char2");
            CHAR3 = ImageManager.loadImage("characters/char3");
            CHAR4 = ImageManager.loadImage("characters/char4");
            CHAR5 = ImageManager.loadImage("characters/char5");
            CHAR6 = ImageManager.loadImage("characters/char6");
            CHAR7 = ImageManager.loadImage("characters/char7");
            CHAR8 = ImageManager.loadImage("characters/char8");
            CHAR10 = ImageManager.loadImage("characters/char10");
            CH1 = ImageManager.loadImage("characters/ch1");
            CH1E = ImageManager.loadImage("characters/ch1e");
            CH1F = ImageManager.loadImage("characters/ch1f");
            CH2 = ImageManager.loadImage("characters/ch2");
            CH2F = ImageManager.loadImage("characters/ch2f");
            CH3 = ImageManager.loadImage("characters/ch3");
            CH3E = ImageManager.loadImage("characters/ch3e");
            CH4 = ImageManager.loadImage("characters/ch4");
            CH4E = ImageManager.loadImage("characters/ch4e");
            CH5 = ImageManager.loadImage("characters/ch5");
            CH5E = ImageManager.loadImage("characters/ch5e");
            CH6 = ImageManager.loadImage("characters/ch6");
            CH6E = ImageManager.loadImage("characters/ch6e");
            CH8 = ImageManager.loadImage("characters/ch8");
            CH8E = ImageManager.loadImage("characters/ch8e");
            CH8F = ImageManager.loadImage("characters/ch8f");
            CH7 = ImageManager.loadImage("characters/ch7");
            CH7E = ImageManager.loadImage("characters/ch7e");
            CH7F = ImageManager.loadImage("characters/ch7f");
            CH9 = ImageManager.loadImage("characters/ch9");
            CH10 = ImageManager.loadImage("characters/ch10");
            CH10E = ImageManager.loadImage("characters/ch10e");
            CH10F = ImageManager.loadImage("characters/ch10f");

            KEY1 = ImageManager.loadImage("pickups/key1");
            KEY2 = ImageManager.loadImage("pickups/key2");
            NOTE = ImageManager.loadImage("pickups/note");
            NOTE1 = ImageManager.loadImage("pickups/note1");
            NOTE2 = ImageManager.loadImage("pickups/note2");
            NOTE3 = ImageManager.loadImage("pickups/note3");
            NOTE4 = ImageManager.loadImage("pickups/note4");
            CLOTHES = ImageManager.loadImage("pickups/clothes");
            FOOD = ImageManager.loadImage("pickups/food");

            WALL = ImageManager.loadImage("objects/wall");
            WALLC = ImageManager.loadImage("objects/wallc");
            TABLE2 = ImageManager.loadImage("objects/t2");
            BED1 = ImageManager.loadImage("objects/bed1");
            TOILET = ImageManager.loadImage("objects/toilet");
            SINK = ImageManager.loadImage("objects/sink");
            TUB = ImageManager.loadImage("objects/btub");
            MIRROR = ImageManager.loadImage("objects/mir");
            MIRRORH = ImageManager.loadImage("objects/mirH");
            DRESSER = ImageManager.loadImage("objects/dresser");
            DRESSERV = ImageManager.loadImage("objects/dresserv");
            CHAIR = ImageManager.loadImage("objects/chair");
            TABLE3 = ImageManager.loadImage("objects/t3");
            TVV = ImageManager.loadImage("objects/tv");
            BIN = ImageManager.loadImage("objects/bin");
            ACHAIR = ImageManager.loadImage("objects/ach");
            PAINTING = ImageManager.loadImage("objects/paint");
            PAINTINGH = ImageManager.loadImage("objects/painth");
            FRIDGE = ImageManager.loadImage("objects/fridge");
            FRIDGEV = ImageManager.loadImage("objects/fridgeV");
            COUNTER = ImageManager.loadImage("objects/counter");
            COOKER = ImageManager.loadImage("objects/cooker");
            BOOKCASE = ImageManager.loadImage("objects/bcase");
            BOOKCASEV = ImageManager.loadImage("objects/bcaseV");
            PLANT = ImageManager.loadImage("objects/plant");
            BED2 = ImageManager.loadImage("objects/bed2");
            BED2H = ImageManager.loadImage("objects/bed2H");
            COUCH = ImageManager.loadImage("objects/couch");
            COUCHV = ImageManager.loadImage("objects/couchV");
            LAMP = ImageManager.loadImage("objects/lamp");
            BCOUNTER = ImageManager.loadImage("objects/bcounter");
            CUBICLE = ImageManager.loadImage("objects/cub");
            TABLEP = ImageManager.loadImage("objects/ptable");
            BENCH = ImageManager.loadImage("objects/bench");
            BENCHV = ImageManager.loadImage("objects/benchV");
            BENCHV1 = ImageManager.loadImage("objects/benchV1");
            BENCH1 = ImageManager.loadImage("objects/bench1");
            FOUNTAIN = ImageManager.loadImage("objects/fountain");
            TREE = ImageManager.loadImage("objects/tree");
            FLOWER1 = ImageManager.loadImage("objects/shrub");
            DESK = ImageManager.loadImage("objects/desk");
            DESKV = ImageManager.loadImage("objects/deskV");
            STAIRS = ImageManager.loadImage("objects/stairs");
            SHOWER = ImageManager.loadImage("objects/shower");
            SHOWER1 = ImageManager.loadImage("objects/shower1");
            FCABINET = ImageManager.loadImage("objects/fcab");
            CDESK = ImageManager.loadImage("objects/cdesk");
            CDESK1 = ImageManager.loadImage("objects/cdesk1");
            DUMMY = ImageManager.loadImage("objects/dummy");
            WEAPONR = ImageManager.loadImage("objects/wpr");
            WEAPONR1 = ImageManager.loadImage("objects/wpr1");
            GAZEBO = ImageManager.loadImage("objects/gz");

            //add event intro 1
            EV1.add(ImageManager.loadImage("intro/ev11"));
            EV1.add(ImageManager.loadImage("intro/ev12"));
            EV1.add(ImageManager.loadImage("intro/ev13"));
            EV1.add(ImageManager.loadImage("intro/ev14"));
            EV1.add(ImageManager.loadImage("intro/ev15"));
            EV1.add(ImageManager.loadImage("intro/ev16"));

            //event 1 stages
            EV1S1 = ImageManager.loadImage("intro/ev1s1");
            EV1S2 = ImageManager.loadImage("intro/ev1s2");
            EV1S3 = ImageManager.loadImage("intro/ev1s3");
            EV1S4 = ImageManager.loadImage("intro/ev1s4");
            EV1S5 = ImageManager.loadImage("intro/ev1s5");
            EV1S6 = ImageManager.loadImage("intro/ev1s6");
            EV1S8 = ImageManager.loadImage("intro/ev1s8");

            //event 2 stages
            EV2S2 = ImageManager.loadImage("intro/ev2s2");
        } catch (IOException e) { System.exit(1); }
    }



}