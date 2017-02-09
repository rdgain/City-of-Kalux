package game;

public abstract class MovingObject extends GameObject {
    int id;
    boolean talking;
    boolean blockedMovement;

    public MovingObject() {
        super();
    }

    @Override
    int size() {
        return 1;
    }

    void update() {}
}
