package game;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class KeyController extends KeyAdapter implements Controller {


    int inputDelay;
    Action action;
    //boolean fullScreen;

    public KeyController() {
        action = new Action();
        inputDelay = 5;
        //fullScreen = true;
    }

    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        switch (key) {
            case KeyEvent.VK_UP:
                action.moveUp = true;
                if (action.movement != action.MOVEDUP) {
                    action.rotate = true;
                }
                break;
            case KeyEvent.VK_DOWN:
                action.moveDown = true;
                if (action.movement != action.MOVEDDOWN) {
                    action.rotate = true;
                }
                break;
            case KeyEvent.VK_LEFT:
                action.prev = true;
                action.moveLeft = true;
                if (action.movement != action.MOVEDLEFT) {
                    action.rotate = true;
                }
                break;
            case KeyEvent.VK_RIGHT:
                action.n_next = true;
                action.moveRight = true;
                if (action.movement != action.MOVEDRIGHT) {
                    action.rotate = true;
                }
                break;
            case KeyEvent.VK_SPACE:
                action.talk = true;
                break;
            //case KeyEvent.VK_ESCAPE:
                //fullScreen = false;
                //break;
            case KeyEvent.VK_C:
                action.collect = true;
                break;
            case KeyEvent.VK_D:
                action.drop = true;
                break;
            case KeyEvent.VK_Q:
                action.line1 = true;
                break;
            case KeyEvent.VK_W:
                action.line2 = true;
                break;
            case KeyEvent.VK_M:
                action.map = true;
                break;
            case KeyEvent.VK_1:
                if (!action.map) {
                    action.use[0] = true;
                    action.title1 = true;
                    action.menu = false;
                }
                else
                    action.map1 = true;
                break;
            case KeyEvent.VK_2:
                if (!action.map) {
                    action.use[1] = true;
                    action.title2 = true;
                    action.menu = false;
                }
                else
                    action.map2 = true;
                break;
            case KeyEvent.VK_3:
                if (!action.map) {
                    action.use[2] = true;
                    action.title3 = true;
                    action.menu = false;
                }
                else
                    action.map3 = true;
                break;
            case KeyEvent.VK_4:
                if (!action.map) {
                    action.use[3] = true;
                    action.title4 = true;
                    action.menu = false;
                }
                else
                    action.map4 = true;
                break;
            case KeyEvent.VK_5:
                if (!action.map) {
                    action.use[4] = true;
                    action.title5 = true;
                    action.menu = false;
                }
                else
                    action.map5 = true;
                break;
            case KeyEvent.VK_6:
                action.use[5] = true;
                break;
            case KeyEvent.VK_7:
                action.use[6] = true;
                break;
            case KeyEvent.VK_8:
                action.use[7] = true;
                break;
            case KeyEvent.VK_9:
                action.use[8] = true;
                break;
            case KeyEvent.VK_ENTER:
                action.next = true;
                break;
            case KeyEvent.VK_BACK_SPACE:
                action.menu = true;
                action.title1 = false;
                action.title2 = false;
                action.title3 = false;
                action.title4 = false;
                action.title5 = false;
                break;
            case KeyEvent.VK_A:
                action.call = true;
                break;
            case KeyEvent.VK_S:
                action.steal = true;
                break;
            case KeyEvent.VK_N:
                action.notebook = true;
                action.menu = true;
                break;
            case KeyEvent.VK_Z:
                action.achievements = true;
                break;
        }
    }

    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        switch (key) {
            case KeyEvent.VK_UP:
                action.moveUp = false;
                action.rotate = false;
                break;
            case KeyEvent.VK_DOWN:
                action.moveDown = false;
                action.rotate = false;
                break;
            case KeyEvent.VK_LEFT:
                action.prev = false;
                action.moveLeft = false;
                action.rotate = false;
                break;
            case KeyEvent.VK_RIGHT:
                action.n_next = false;
                action.moveRight = false;
                action.rotate = false;
                break;
            case KeyEvent.VK_SPACE:
                action.talk = false;
                break;
           // case KeyEvent.VK_ESCAPE:
             //   fullScreen = true;
            //    break;
            case KeyEvent.VK_C:
                action.collect = false;
                break;
            case KeyEvent.VK_D:
                action.drop = false;
                break;
            case KeyEvent.VK_Q:
                action.line1 = false;
                break;
            case KeyEvent.VK_W:
                action.line2 = false;
                break;
            case KeyEvent.VK_M:
                action.map = false;
                break;
            case KeyEvent.VK_1:
                action.use[0] = false;
                action.map1 = false;
                action.title1 = false;
                break;
            case KeyEvent.VK_2:
                action.use[1] = false;
                action.map2 = false;
                action.title2 = false;
                break;
            case KeyEvent.VK_3:
                action.use[2] = false;
                action.map3 = false;
                action.title3 = false;
                break;
            case KeyEvent.VK_4:
                action.use[3] = false;
                action.map4 = false;
                action.title4 = false;
                break;
            case KeyEvent.VK_5:
                action.use[4] = false;
                action.map5 = false;
                action.title5 = false;
                break;
            case KeyEvent.VK_6:
                action.use[5] = false;
                break;
            case KeyEvent.VK_7:
                action.use[6] = false;
                break;
            case KeyEvent.VK_8:
                action.use[7] = false;
                break;
            case KeyEvent.VK_9:
                action.use[8] = false;
                break;
            case KeyEvent.VK_ENTER:
                action.next = false;
                break;
            case KeyEvent.VK_A:
                action.call = false;
                break;
            case KeyEvent.VK_S:
                action.steal = false;
                break;
            case KeyEvent.VK_N:
                action.notebook = false;
                break;
            case KeyEvent.VK_Z:
                action.achievements = false;
                break;
        }
    }

    @Override
    public Action action(KaluxGame game) {
        return action;
    }
}
