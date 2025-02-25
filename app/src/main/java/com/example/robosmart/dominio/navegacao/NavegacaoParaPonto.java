package com.example.robosmart.dominio.navegacao;

import android.util.Log;

import com.example.robosmart.dados.repositorio.ComunicacaoEsp;
import com.example.robosmart.dados.model.Objetivo;
import com.example.robosmart.dados.model.Obstaculo;
import com.example.robosmart.dados.model.Robo;

import java.util.List;

public class NavegacaoParaPonto {

    float distancia = 0f;

    public boolean navigateToPoint(Robo robo, Objetivo objetivo, List<Obstaculo> obstaculos) {
        float deltaX = objetivo.getX() - robo.getX();
        float deltaY = objetivo.getY() - robo.getY();
        distancia = (float) Math.sqrt(deltaX * deltaX + deltaY * deltaY);
        float angleToTarget = (float) Math.toDegrees(Math.atan2(deltaY, deltaX));

        float angleToTurn = angleToTarget - robo.getTheta();

        float stepSize = 15.0f;

        // Atualiza as forças
        float[] forces = CamposPotenciaisArtificiais.atualizarForcas(robo,objetivo, obstaculos);
        float forceX = forces[0];
        float forceY = forces[1];

        // Calcula o ângulo resultante das forças
        float resultantAngle = (float) Math.toDegrees(Math.atan2(forceY, forceX));
        angleToTurn = resultantAngle - robo.getTheta();
        Log.d("ANGLE TO TURN", "ANGLE TO TURN: " + angleToTurn);

        if(distancia < 30){
            return true;
        }

        if (angleToTurn > 180) {
            angleToTurn -= 360;
        } else if (angleToTurn < -180) {
            angleToTurn += 360;
        }


        if (distancia < stepSize) {  // Se a distância for menor que o stepSize, anda a distância restante
        // chama o metodo que envia dados ao esp32
         ComunicacaoEsp comunicacaoEsp = new ComunicacaoEsp(angleToTurn, distancia);
         comunicacaoEsp.execute();
         // andarFrente(distance);

        } else {
            //chama o metodo que envia dados ao esp32
            ComunicacaoEsp comunicacaoEsp = new ComunicacaoEsp(angleToTurn, stepSize);
            comunicacaoEsp.execute();
            // andarFrente(stepSize);

            }

        // Recalcula as distâncias para o objetivo
        deltaX = objetivo.getX() - robo.getX();
        deltaY = objetivo.getY() - robo.getY();
        distancia = (float) Math.sqrt(deltaX * deltaX + deltaY * deltaY);
        return false;
    }

}
