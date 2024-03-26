package com.shurygin.core.bodies;

public enum FilterCategory {

    WALL(0),
    PLAYER(1),
    SOLID(2),
    SENSOR(3),

    ALL(Short.MAX_VALUE);

    final short n;

    FilterCategory(int n) {
        this.n = (short) (1<<n);
    }

    public short getN() {
        return n;
    }
}
