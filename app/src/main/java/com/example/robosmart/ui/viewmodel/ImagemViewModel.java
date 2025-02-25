package com.example.robosmart.ui.viewmodel;

import android.graphics.Bitmap;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.robosmart.dados.repositorio.ComunicacaoCam;
import com.example.robosmart.dados.repositorio.OnDownloadBitmapListener;

public class ImagemViewModel extends ViewModel{
    private final MutableLiveData<Bitmap> imagemBitmap = new MutableLiveData<>();

    public LiveData<Bitmap> getImagemBitmap() {
        return imagemBitmap;
    }

    public void capturarImagem() {
        ComunicacaoCam comunicacaoCam = new ComunicacaoCam(new OnDownloadBitmapListener() {
            @Override
            public void aoBaixarBitmap(Bitmap bitmap) {
                imagemBitmap.postValue(bitmap);
            }
        });
        comunicacaoCam.execute();
    }
}
