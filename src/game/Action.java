package game;

public class Action {
    boolean moveUp, moveDown, moveRight, moveLeft, talk, collect, line1, line2, map1, map2, map3, map4, map5, map, rotate, call;
    boolean drop, next, notebook, steal, achievements, title1, title2, title3, title4, title5, menu, n_next, prev;
    boolean[] use;
    final int MOVEDDOWN = 1, MOVEDUP = 2, MOVEDLEFT = 3, MOVEDRIGHT = 4;
    int movement;

    public Action() {
        moveUp = false;
        moveDown = false;
        moveRight = false;
        moveLeft = false;
        rotate = false;
        talk = false;
        collect = false;
        drop = false; // drops first item
        movement = 2;
        map1 = false;
        map2 = false;
        map3 = false;
        map4 = false;
        map5 = false;
        map = false;
        next = false;
        call = false;
        notebook = false;
        steal = false;
        achievements = false;
        title1 = false;
        title2 = false;
        title3 = false;
        title4 = false;
        title5 = false;
        menu = false;
        n_next = false;
        prev = false;

        // drop item in inventory (1-9)
        use = new boolean[10];
        for (int i=0; i<9; i++) {
            use[i] = false;
        }
    }
}