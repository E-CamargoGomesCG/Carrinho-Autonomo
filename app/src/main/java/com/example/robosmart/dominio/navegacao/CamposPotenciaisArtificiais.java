package com.example.robosmart.dominio.navegacao;

import android.util.Log;

import com.example.robosmart.dados.model.Objetivo;
import com.example.robosmart.dados.model.Obstaculo;
import com.example.robosmart.dados.model.Robo;

import org.opencv.core.Mat;

import java.util.List;

public class CamposPotenciaisArtificiais {
    private static float k_rep = 10000f;
    private static float d0 = 90f; //zona de influencia obstaculos
    private static float dq = 1; //ao estar 1cm do objetivo o modo de aproximação passa de conico para quadratico
    private static float maxSpeed = 100;
    private static float k_att_quadratico = 5.0f;
    private static float k_att_conico = 1f;

    public static float[] atualizarForcas(Robo robo, Objetivo objetivo, List<Obstaculo> obstaculos ) {
        float distToGoal = (float) Math.sqrt(Math.pow(objetivo.getX() - robo.getX(), 2) + Math.pow(objetivo.getY() - robo.getY(), 2));


        float forceAttConicoX = 0;
        float forceAttConicoY = 0;

        float forceAttQuadraticoX = 0;
        float forceAttQuadraticoY = 0;

        if (distToGoal < dq) {
            forceAttQuadraticoX = k_att_quadratico * ( objetivo.getX() - robo.getX() ) * distToGoal;
            forceAttQuadraticoY = k_att_quadratico * (objetivo.getY() - robo.getY()) * distToGoal;

        } else {
            forceAttConicoX = k_att_conico * (objetivo.getX() - robo.getX()  ) / distToGoal;
            forceAttConicoY = k_att_conico * (objetivo.getY() - robo.getY()  ) / distToGoal;
            Log.d("AtualizarForcas", "forceAttConicoX: " + forceAttConicoX);
            Log.d("AtualizarForcas", "forceAttConicoY: " + forceAttConicoY);
        }

        float forceRepX = 0;
        float forceRepY = 0;

        if (obstaculos.size() > 0) {
            for (int i = 0; i < obstaculos.size(); i++) {
                float obstDistX =  obstaculos.get(i).getX() - robo.getX();
                float obstDistY =  obstaculos.get(i).getY() - robo.getY();
                float obstDist = (float) Math.sqrt(Math.pow(obstDistX, 2) + Math.pow(obstDistY, 2));
                Log.d("AtualizarForcas", "obstDist: " + obstDist);

                // Calcula a força de repulsão apenas se o obstáculo estiver dentro da zona de influência
                if (obstDist < d0) {
                    float repulsion = k_rep * (1.0f / obstDist - 1.0f / d0) * (float) Math.pow(1/obstDist, 2);
                    float theta = (float) Math.atan2(obstDistY, obstDistX) - (float) Math.PI / 2; // Adiciona OU subtrai 90 graus
                    float tangentialX = (float) Math.cos(theta);
                    float tangentialY = (float) Math.sin(theta);

                    forceRepX += repulsion * tangentialX;
                    forceRepY += repulsion * tangentialY;

                    Log.d("AtualizarForcas", "forceRepX: " + forceRepX);
                    Log.d("AtualizarForcas", "forceRepY: " + forceRepY);
                }
            }
        }

        float forceX = forceAttConicoX + forceAttQuadraticoX + forceRepX;
        float forceY = forceAttConicoY + forceAttQuadraticoY + forceRepY;



        float[] result = new float[2];
        result[0] = forceX ;
        result[1] = forceY ;
        //Log.d("AtualizarForcas", "Forca X2: " + result[0]);
        //Log.d("AtualizarForcas", "Forca Y2: " + result[1]);
        return result;
    }
}
