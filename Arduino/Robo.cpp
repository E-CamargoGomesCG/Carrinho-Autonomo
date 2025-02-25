#include "Robo.h"
#include <Wire.h>
 
Robo::Robo(float x1, float y1, float theta1) : x(x1), y(y1), theta(theta1) {
  
}

float Robo::GetX() {
  return x;
}

float Robo::GetY() {
  return y;
}
float Robo::GetTheta(){
  return theta;
}
float Robo::GetAtualTheta() {
 // return Compass.GetHeadingDegrees();
 return 0;
}

void Robo::SetX(float x1) {
  x = x1;
}

void Robo::SetY(float y1) {
  y = y1;
}

void Robo::SetTheta(float theta1) {
  theta = theta1;
}
