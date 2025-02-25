package com.example.robosmart.dados.repositorio;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ComunicacaoCam extends AsyncTask<Void, Void, Bitmap> {

    private static final String URL = "http://192.168.0.19:8080/photo.jpg";
    private OnDownloadBitmapListener listener;

    public ComunicacaoCam(OnDownloadBitmapListener listener) {
        this.listener = listener;
    }

    @Override
    protected Bitmap doInBackground(Void... voids) {
        Bitmap foto = null;
        try {
            URL url = new URL(URL);
            HttpURLConnection conexao = (HttpURLConnection) url.openConnection();

            conexao.setReadTimeout(5000);
            conexao.setConnectTimeout(5000);
            conexao.setRequestMethod("GET");
            conexao.setDoInput(true);
            conexao.connect();

            int responseCode = conexao.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                InputStream in = conexao.getInputStream();
                foto = BitmapFactory.decodeStream(in);
            } else {
                Log.e("ComunicacaoCAM", "Erro na conexão. Código de resposta: " + responseCode);
                return null;
            }
        } catch (Exception e) {
            Log.e("ComunicacaoCAM", "Erro ao baixar a foto: " + e.getMessage());
            return null;
        }
        return foto;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
        if (listener != null) {
            listener.aoBaixarBitmap(bitmap);
        }
    }

}
