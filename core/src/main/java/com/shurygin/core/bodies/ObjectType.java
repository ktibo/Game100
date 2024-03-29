package com.shurygin.core.bodies;

public enum ObjectType {
    PLAYER(2),
    ENEMY(3),
    WALL(100),
    COLLECTABLE(1);

    private final int depth;
    ObjectType(int depth) {
        this.depth = depth;
    }

    public int getDepth() {
        return depth;
    }
}
