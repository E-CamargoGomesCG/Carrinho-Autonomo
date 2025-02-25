package com.example.robosmart.dados.repositorio;

import android.os.AsyncTask;
import android.util.Log;

import com.example.robosmart.ui.viewmodel.ImagemViewModel;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class GetStatusTask extends AsyncTask<Void, Void, String> {

    private final ImagemViewModel imageViewModel; // Referência para o ViewModel

    public GetStatusTask(ImagemViewModel imageViewModel) {
        this.imageViewModel = imageViewModel; // Inicializa a referência
    }

    @Override
    protected String doInBackground(Void... voids) {
        try {
            // URL do ESP32 (substitua pelo IP do seu ESP32)
            URL url = new URL("http://192.168.0.28/status");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            // Checando resposta
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                Log.i("STATUS", "STATUS NO METODO GETSTATUS: " + response.toString());
                return response.toString().trim(); // Retorna o status
            } else {
                return "Erro: Código de resposta " + responseCode;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Erro: " + e.getMessage();
        }
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        Log.i("STATUS", "Resultado da requisição: " + result);
        // O resultado pode ser processado onde a tarefa é chamada
    }
}
