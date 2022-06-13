package com.pgorski.photosapp.models;

import java.io.Serializable;

public class ImageData implements Serializable {
    private float x;
    private float y;
    private int w;
    private int h;

    public ImageData(float x, float y, float w, float h) {
        this.x = x;
        this.y = y;
        this.w = (int) w;
        this.h = (int) h;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public int getW() {
        return w;
    }

    public int getH() {
        return h;
    }

}
