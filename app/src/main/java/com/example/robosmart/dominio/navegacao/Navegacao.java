package com.example.robosmart.dominio.navegacao;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.example.robosmart.dados.model.Marcador;
import com.example.robosmart.dados.repositorio.GetStatusTask;
import com.example.robosmart.dados.model.Objetivo;
import com.example.robosmart.dados.model.Obstaculo;
import com.example.robosmart.dados.model.Robo;
import com.example.robosmart.databinding.FragmentCapturaImagemBinding;
import com.example.robosmart.ui.viewmodel.ImagemViewModel;

import java.util.ArrayList;
import java.util.List;

public class Navegacao {
    private List<Marcador> marcadores;
    private Context context;
    private Robo robo;
    private Objetivo objetivo;

    private List<Obstaculo> obstaculos;
    private Bitmap bitmap;
    private float larguraReal;
    private float alturaReal;
    private FragmentCapturaImagemBinding binding;
    private boolean sucesso = false;
    private String status = "desocupado";
    private ImagemViewModel imagemViewModel;
    private int repeticao = 0;
    private boolean objetivoCalculado = false;


    public Navegacao (List<Marcador> marcadores, Context context, float x, float y, Bitmap bitmap, ImagemViewModel imagemViewModel, FragmentCapturaImagemBinding binding){
        this.marcadores = marcadores;
        this.context = context;
        this.objetivo = new Objetivo(x, y);
        this.bitmap = bitmap;
        this.imagemViewModel = imagemViewModel;
        this.obstaculos = new ArrayList<>();
        this.binding = binding;
        identificarMarcadores();
    }

    public boolean navegarParaObjetivo() {
        if(robo == null){
            Log.i("NAVEGAR", "NAO DETECTOU O ROBO");
            return false;
        }else{
            Log.i("NAVEGAR", " DETECTOU O ROBO");
            calcularCoordenadaReais();

            NavegacaoParaPonto navigate = new NavegacaoParaPonto();
            sucesso = navigate.navigateToPoint(robo, objetivo, obstaculos);

            if(sucesso){
                Log.i("SUCESSO", "CHEGOU NO OBJETIVO");
                Toast.makeText(context, "CHEGOU NO OBJETIVO", Toast.LENGTH_SHORT).show();
                return false;
            }else{
                status = "ocupado";
                checkStatusRepeatedly();
            }

            return true;
        }
    }

    private void identificarMarcadores(){
        for(Marcador marcador : marcadores){
            if(marcador.getId() == pegarIdRobo()){
                this.robo = new Robo(marcador.getId(), marcador.getX(), marcador.getY(), marcador.getThetaZ());
            }else{
                Obstaculo obstaculo = new Obstaculo(marcador.getId(), marcador.getX(), marcador.getY());
                this.obstaculos.add(obstaculo);
            }
        }

        if(robo == null){
            Log.i("IDENTIFICAR MARCADORES", "ROBO NAO ESTA NO POV DA CAMERA");
        }
    }
    private int pegarIdRobo() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("IdRobo", MODE_PRIVATE);
        String idRobo = sharedPreferences.getString("id", "");
        return  Integer.parseInt(idRobo);
    }

    private void checkStatusRepeatedly() {
        // Criar um Handler para o atraso
        Handler handler = new Handler();
        // Criar um Runnable para checar o status
        Runnable statusCheckRunnable = new Runnable() {
            @Override
            public void run() {
                // Chama o método para verificar o status
                checkStatus();
                if(!status.equals("desocupado")){
                    // Agendar a próxima verificação em 3 segundos
                    handler.postDelayed(this, 3000);
                }

            }
        };
        // Iniciar a primeira chamada
        handler.post(statusCheckRunnable); // Chama a primeira vez
    }
    private void checkStatus() {
        new GetStatusTask(imagemViewModel) {
            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                status = result;
                // Verifica se o status é "desocupado"
                if ("desocupado".equals(result.trim())) {
                    imagemViewModel.capturarImagem(); // Captura a imagem se desocupado

                    Log.i("STATUS", "Estado mudou para desocupado, capturando imagem.");
                } else {
                    Log.i("STATUS", "STATUS ESTÁ OCUPADO: " + result);
                }
            }
        }.execute(); // Executa a tarefa
    }

    private void calcularCoordenadaReais(){
        repeticao++;
        recuperarDimensoesAmbiente();

        //Atribuir valores do bitmap e do tamanho da imagem
        int imageViewLargura = binding.imageViewImagem.getWidth();
        int imageViewAltura = binding.imageViewImagem.getHeight();
        int bitmapLargura = bitmap.getWidth();
        int bitmapAltura = bitmap.getHeight();

        //Fazer a escala
        float escalaX = (float) bitmapLargura / imageViewLargura;
        float escalaY = (float) bitmapAltura / imageViewAltura;

        //Fazer lógica para nao mudar as coordenadas do objetivo
        //Calcular Coordenadas do Objetivo
        if(!objetivoCalculado){
            float objetivoRealX = ((objetivo.getX() * escalaX) / bitmapLargura) * larguraReal;
            float objetivoRealY = ((objetivo.getY() * escalaY) / bitmapAltura) * alturaReal;
            objetivo.setX(objetivoRealX);
            objetivo.setY(objetivoRealY);
            objetivoCalculado = true;


        }


        //Calcular coordenadas do robo
        float roboX = (robo.getX() / bitmapLargura) * larguraReal;
        float roboY = (robo.getY() / bitmapAltura) * alturaReal;
        robo.setX(roboX);
        robo.setY(roboY);

        //calcular coordenadas dos obstaculos
        for (int i = 0; i < obstaculos.size(); i++) {
            float xReal = (obstaculos.get(i).getX() / bitmapLargura) * larguraReal;
            float yReal = (obstaculos.get(i).getY() / bitmapAltura) * alturaReal;
            obstaculos.get(i).setX(xReal);
            obstaculos.get(i).setY(yReal);
        }


        Log.i("Touch", "COORDENADAS REAIS COMPLETA, objetivoX = "+ objetivo.getX()+ ", ObjetivoY : "+ objetivo.getY() + "RoboX: "+robo.getX()+", RoboY: "+robo.getY() + "RoboTheta: "+ robo.getTheta());
    }
    private void recuperarDimensoesAmbiente(){
        SharedPreferences sharedPreferences = context.getSharedPreferences("DimensoesAmbiente", MODE_PRIVATE);
        String largura = sharedPreferences.getString("comprimento", "");
        String altura = sharedPreferences.getString("largura", "");
        larguraReal = Float.parseFloat(largura);
        alturaReal = Float.parseFloat(altura);
    }
}
