#ifndef ROBO_H
  #define ROBO_H

  class Robo {
    private:
      float x;
      float y;
      float theta;
    public:
      Robo() : x(0), y(0), theta(0) {}  // Construtor padrão
      Robo(float x, float y, float theta); // Declaração do construtor
      float GetX(); // Declaração dos métodos
      float GetY();
      float GetTheta();
      float GetAtualTheta();
      void SetX(float x1); // Adiciona parâmetros
      void SetY(float y1); // Adiciona parâmetros
      void SetTheta(float theta1); // Adiciona parâmetros
  };

  #endif // ROBO_H
