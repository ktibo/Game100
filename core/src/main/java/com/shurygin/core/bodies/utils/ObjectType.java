package com.shurygin.core.bodies.utils;

public enum ObjectType {
    COLLECTABLE(1),
    PLAYER(2),
    ENEMY(3),
    WALL(100);

    private final int depth;
    ObjectType(int depth) {
        this.depth = depth;
    }

    public int getDepth() {
        return depth;
    }
}
