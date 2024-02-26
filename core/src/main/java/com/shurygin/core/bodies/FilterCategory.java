package com.shurygin.core.bodies;

public enum FilterCategory {

    WALL(1),
    PLAYER(2),
    SOLID(4),
    SENSOR(8),

    ALL(Short.MAX_VALUE);

    final short n;

    FilterCategory(int n) {
        this.n = (short) n;
    }

    public short getN() {
        return n;
    }
}
