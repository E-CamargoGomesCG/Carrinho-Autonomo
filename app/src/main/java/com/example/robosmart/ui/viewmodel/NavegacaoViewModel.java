package com.example.robosmart.ui.viewmodel;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.Toast;

import androidx.lifecycle.ViewModel;

import com.example.robosmart.dados.model.Marcador;
import com.example.robosmart.databinding.FragmentCapturaImagemBinding;
import com.example.robosmart.dominio.navegacao.Navegacao;

import java.util.List;

public class NavegacaoViewModel extends ViewModel {
    private boolean navegacaoAtiva = false;

    public boolean isNavegacaoAtiva() {
        return navegacaoAtiva;
    }

    public void iniciarNavegacao(List<Marcador> marcadores, Context context, float x, float y, Bitmap bitmap, ImagemViewModel imagemViewModel, FragmentCapturaImagemBinding binding) {
        if (navegacaoAtiva) {
            return;
        }
        navegacaoAtiva = true;
        Navegacao navegacao = new Navegacao(marcadores, context, x, y, bitmap, imagemViewModel, binding);
        if(!navegacao.navegarParaObjetivo()){
            Toast.makeText(context, "Robô não foi identificado. Certifique-se de que está no campo de visão.", Toast.LENGTH_LONG).show();
            navegacaoAtiva = false;
        }
    }
    public void atualizarNavegacao(List<Marcador> marcadores, Context context, float x, float y, Bitmap bitmap, ImagemViewModel imagemViewModel, FragmentCapturaImagemBinding binding) {
        if (!navegacaoAtiva) {
            return;
        }
        Navegacao navegacao = new Navegacao(marcadores, context, x, y, bitmap, imagemViewModel, binding);
        if(!navegacao.navegarParaObjetivo()){
            Toast.makeText(context, "Robô não foi identificado. Certifique-se de que está no campo de visão.", Toast.LENGTH_LONG).show();
            navegacaoAtiva = false;
        }
    }

    public void finalizarNavegacao(){

    }



}
