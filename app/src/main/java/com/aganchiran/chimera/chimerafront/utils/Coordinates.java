package com.aganchiran.chimera.chimerafront.utils;


import android.graphics.Point;

public class Coordinates extends Point {

    public int xOffset = 0;
    public int yOffset = 0;

    public Coordinates(int x, int y, int xOffset, int yOffset) {
        super(x, y);
        this.xOffset = xOffset;
        this.yOffset = yOffset;
    }

    public void setOffsets(int xOffset, int yOffset){
        this.xOffset = xOffset;
        this.yOffset = yOffset;
    }
}
