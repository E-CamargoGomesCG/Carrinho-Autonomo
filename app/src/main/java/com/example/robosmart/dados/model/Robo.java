package com.example.robosmart.dados.model;

public class Robo extends Coordenadas {
    private float theta; // Ângulo de orientação do robô

    public Robo(int id, float x, float y, float theta) {
        super(id, x, y);
        this.theta =  theta;
    }

    public Robo() {
    }
    public float getTheta() {
        return theta;
    }

    public void setTheta(float theta) {
        this.theta = theta;
    }
}
