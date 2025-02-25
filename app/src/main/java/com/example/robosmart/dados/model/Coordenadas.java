package com.example.robosmart.dados.model;

public abstract class Coordenadas {
    protected int id;
    protected float x;
    protected float y;

    public Coordenadas(int id, float x, float y) {
        this.x = x;
        this.y = y;
        this.id = id;
    }
    public Coordenadas( float x, float y) {
        this.x = x;
        this.y = y;
    }

    public Coordenadas() {
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
