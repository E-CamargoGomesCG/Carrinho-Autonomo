package com.example.robosmart.ui.viewmodel;

import android.content.Context;
import android.graphics.Bitmap;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.robosmart.dados.model.Marcador;
import com.example.robosmart.dominio.aruco.DetectorArUco;

import java.util.List;

public class DetectaArUcoViewModel extends ViewModel {

    private final MutableLiveData<List<Marcador>> resultadoDeteccao = new MutableLiveData<>();

    public LiveData<List<Marcador>> getResultadoDeteccao() {
        return resultadoDeteccao;
    }

    public void detectarMarcadoresArUco(Bitmap imagemBitmap, Context context) {
        DetectorArUco detectaArUco = new DetectorArUco(context);

        List<Marcador> resultado = detectaArUco.detectarMarcadoresAruco(imagemBitmap);

        resultadoDeteccao.postValue(resultado);
    }
}
