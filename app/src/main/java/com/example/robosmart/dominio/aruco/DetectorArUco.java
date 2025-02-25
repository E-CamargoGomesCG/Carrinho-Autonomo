package com.example.robosmart.dominio.aruco;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.util.Log;

import com.example.robosmart.dados.model.Marcador;

import org.opencv.android.Utils;
import org.opencv.aruco.Aruco;
import org.opencv.aruco.DetectorParameters;
import org.opencv.aruco.Dictionary;
import org.opencv.calib3d.Calib3d;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfDouble;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.MatOfPoint3f;
import org.opencv.core.Point;
import org.opencv.core.Point3;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

public class DetectorArUco {
    private Context context;

    public DetectorArUco() {
    }

    public DetectorArUco(Context context) {
        this.context = context;
    }

    public List<Marcador> detectarMarcadoresAruco(Bitmap bitmap) {
        List<Marcador> marcadores = new ArrayList<>();

        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        // Converter Bitmap para Mat
        Mat mat = new Mat(height,width, CvType.CV_8UC4);
        Utils.bitmapToMat(bitmap, mat);

        // Converter a imagem para escala de cinza
        Mat grayMat = new Mat();
        Imgproc.cvtColor(mat, grayMat, Imgproc.COLOR_BGR2GRAY);

        Dictionary dictionary = Aruco.getPredefinedDictionary(definirMarcadorEscolhido());
        Log.i("Dicionario", "Dicionario: "+ dictionary);

        DetectorParameters parameters = DetectorParameters.create();

        // Listas para armazenar os cantos e IDs detectados
        List<Mat> cantos = new ArrayList<>();
        Mat ids = new Mat();

        // Detectar os marcadores ArUco
        Aruco.detectMarkers(grayMat, dictionary, cantos, ids, parameters);

        if (ids.total() > 0) {
            // Parâmetros da câmera calibração
            Mat coeficientesDeCalibracao = new Mat(3, 3, CvType.CV_64FC1);
            coeficientesDeCalibracao.put(0, 0,
                    17986.8623, 0, 802.385638,  // Primeira linha
                    0, 12318.9415, 450.019516, // Segunda linha
                    0, 0, 1                   // Terceira linha
            );

            // Coeficientes de distorção
            MatOfDouble coeficientesDeDistorção = new MatOfDouble();
            coeficientesDeDistorção.fromArray(
                    15.7931101, 1195.26208, 0.159436379, 0.0258729627, 1.26520361 // Coeficientes
            );

            float tamanhoMarcador = 0.1f; // Ajuste para o tamanho real do marcador

            Mat rotacao = new Mat(); // Vetores de rotação
            Mat translacao = new Mat(); // Vetores de translação
            Aruco.estimatePoseSingleMarkers(cantos, tamanhoMarcador, coeficientesDeCalibracao, coeficientesDeDistorção, rotacao, translacao);


            for (int i = 0; i < ids.total(); i++) {
                // Acessar o ID de cada marcador
                int idMarcador = (int) ids.get(i, 0)[0];

                // Acessar o vetor de rotação e translação de cada marcador
                double[] rvec = rotacao.get(i, 0);
                double[] tvec = translacao.get(i, 0);

                // Projeção do centro do marcador (0, 0, 0) para coordenadas 2D
                MatOfPoint3f objectPoints = new MatOfPoint3f(new Point3(0.0, 0.0, 0.0));
                MatOfPoint2f imagePoints = new MatOfPoint2f();

                Calib3d.projectPoints(objectPoints, new MatOfDouble(rvec), new MatOfDouble(tvec),
                        coeficientesDeCalibracao, coeficientesDeDistorção, imagePoints);

                Point[] pontosProjetados = imagePoints.toArray();
                float x = (float) pontosProjetados[0].x;
                float y = (float) pontosProjetados[0].y;

                Mat rotationMatrix = new Mat();
                Calib3d.Rodrigues(new MatOfDouble(rvec), rotationMatrix);

                double thetaZ = Math.atan2(rotationMatrix.get(1, 0)[0], rotationMatrix.get(0, 0)[0]);
                thetaZ = Math.toDegrees(thetaZ);

                marcadores.add(new Marcador(idMarcador, x, y, (float) thetaZ));
            }
        }
        return marcadores;
    }


    private int definirMarcadorEscolhido() {
        SharedPreferences preferences = context.getSharedPreferences("DicionarioDeMarcadores", MODE_PRIVATE);
        String selectedItem = preferences.getString("dicionario", null);
        switch (selectedItem) {
            case "ARUCO_ORIGINAL":
                return Aruco.DICT_ARUCO_ORIGINAL;
            case "4X4_50":
                return Aruco.DICT_4X4_50;
            case "4X4_100":
                return Aruco.DICT_4X4_100;
            case "4X4_250":
                return Aruco.DICT_4X4_250;
            case "4X4_1000":
                return Aruco.DICT_4X4_1000;
            case "5X5_50":
                return Aruco.DICT_5X5_50;
            case "5X5_100":
                return Aruco.DICT_5X5_100;
            case "5X5_250":
                return Aruco.DICT_5X5_250;
            case "5X5_1000":
                return Aruco.DICT_5X5_1000;
            case "6X6_50":
                return Aruco.DICT_6X6_50;
            case "6X6_100":
                return Aruco.DICT_6X6_100;
            case "6X6_250":
                return Aruco.DICT_6X6_250;
            case "6X6_1000":
                return Aruco.DICT_6X6_1000;
            case "7X7_50":
                return Aruco.DICT_7X7_50;
            case "7X7_100":
                return Aruco.DICT_7X7_100;
            case "7X7_250":
                return Aruco.DICT_7X7_250;
            case "7X7_1000":
                return Aruco.DICT_7X7_1000;
            default:
                return -Aruco.DICT_ARUCO_ORIGINAL;}
    }
}