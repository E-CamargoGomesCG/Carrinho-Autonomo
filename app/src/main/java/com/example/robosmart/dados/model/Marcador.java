package com.example.robosmart.dados.model;

public class Marcador {
    private int id;
    private float x;
    private float y;
    private float thetaZ;

    public Marcador(int id, float x, float y, float thetaZ) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.thetaZ = thetaZ;
    }

    public int getId() {
        return id;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getThetaZ() {
        return thetaZ;
    }
}

