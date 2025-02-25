package com.example.robosmart.ui.view;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.robosmart.R;
import com.example.robosmart.dados.model.Marcador;
import com.example.robosmart.databinding.FragmentCapturaImagemBinding;
import com.example.robosmart.ui.viewmodel.DetectaArUcoViewModel;
import com.example.robosmart.ui.viewmodel.ImagemViewModel;
import com.example.robosmart.ui.viewmodel.NavegacaoViewModel;
import com.example.robosmart.utils.TipoFragment;
import com.google.android.material.textfield.TextInputEditText;

import java.util.List;

public class CapturaImagemFragment extends Fragment {
    private TextInputEditText editTextIdRobo;
    private TextInputEditText editTextComprimento;
    private TextInputEditText editTextLargura;
    private Spinner spinner;
    private Bitmap bitmap;
    private float x = 0;
    private float y = 0;
    private FragmentCapturaImagemBinding binding;
    private TipoFragment tipoFragment;
    private ImagemViewModel imagemViewModel;
    private DetectaArUcoViewModel detectaArUcoViewModel;
    private NavegacaoViewModel navegacaoViewModel;

    public static CapturaImagemFragment newInstance(TipoFragment tipo){
        CapturaImagemFragment fragment = new CapturaImagemFragment();
        fragment.tipoFragment = tipo;
        return fragment;
    }

    @Override
    public final View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentCapturaImagemBinding.inflate(inflater, container, false);

        imagemViewModel = new ViewModelProvider(requireActivity()).get(ImagemViewModel.class);
        detectaArUcoViewModel = new ViewModelProvider(requireActivity()).get(DetectaArUcoViewModel.class);
        navegacaoViewModel = new ViewModelProvider(requireActivity()).get(NavegacaoViewModel.class);

        //Acionar a captura da imagem
        binding.buttonCapturarImagem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imagemViewModel.capturarImagem();
            }
        });

        //Observar a captura da imagem para trocar o imageView assim que for capturada uma nova imagem
        imagemViewModel.getImagemBitmap().observe(getViewLifecycleOwner(), new Observer<Bitmap>() {
            @Override
            public void onChanged(Bitmap bitmapImagem) {
                if(bitmapImagem != null){
                    bitmap = bitmapImagem;
                    binding.imageViewImagem.setImageBitmap(bitmap);

                    //Acionar o processamento da imagem (Detectar marcadores ArUco)
                    detectaArUcoViewModel.detectarMarcadoresArUco(bitmap, getContext());
                }else{
                    binding.imageViewImagem.setImageResource(R.drawable.nenhuma_imagem_capturada);
                    Toast.makeText(getContext(), "Erro ao capturar imagem, verifique o ip ou a conexao de rede", Toast.LENGTH_LONG).show();
                }
            }
        });

        //Observar a detecção de marcadores
        detectaArUcoViewModel.getResultadoDeteccao().observe(getViewLifecycleOwner(), new Observer<List<Marcador>>() {
            @Override
            public void onChanged(List<Marcador> marcadores) {
                if(marcadores.size() > 0){
                    Log.i("TESTE MARCADOR", "DETECTOU, TAMANHO: " + marcadores.size());
                    if(!navegacaoViewModel.isNavegacaoAtiva()){
                        binding.imageViewImagem.setOnTouchListener(new View.OnTouchListener() {
                            @Override
                            public boolean onTouch(View v, MotionEvent event) {
                                if(event.getAction() == MotionEvent.ACTION_DOWN){
                                     x = event.getX();
                                     y = event.getY();

                                    //Acionar Navegação
                                    navegacaoViewModel.iniciarNavegacao(marcadores, getContext(), x, y, bitmap, imagemViewModel, binding);

                                    v.performClick();
                                    return true;
                                }
                                return false;
                            }
                        });
                    }else{
                        //atualizar navegação
                        navegacaoViewModel.atualizarNavegacao(marcadores, getContext(), x, y, bitmap, imagemViewModel, binding);
                    }
                }else{
                    Toast.makeText(getContext(), "Nenhum marcador ArUco Detectado", Toast.LENGTH_LONG).show();
                    Log.i("Marcadores", "NÃO DETECTOU");
                }
            }
        });

        binding.imageViewConfiguracoes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarDialog(tipoFragment);
            }
        });

        binding.fabFullScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(getContext(), ImagemFullScreenActivity.class);
                startActivity(it);
            }
        });

        return binding.getRoot();
    }

    private void mostrarDialog(TipoFragment tipoFragment) {
        LayoutInflater inflater = this.getLayoutInflater();
        View viewDialogCustomizada = null;
        //if(tipoFragment == TipoFragment.TRACAR_ROTA){
           // viewDialogCustomizada = inflater.inflate(R.layout.dialog_tracar_rota, null);
       // } else if (tipoFragment == TipoFragment.DEFINIR_OBJETIVO) {
            viewDialogCustomizada = inflater.inflate(R.layout.dialog_definir_objetivo, null);
            editTextIdRobo = viewDialogCustomizada.findViewById(R.id.textInputEditText_idMarcador);
            configurarIdRobo(editTextIdRobo);
        //}

        editTextComprimento = viewDialogCustomizada.findViewById(R.id.textInputEditText_comprimento);
        editTextLargura = viewDialogCustomizada.findViewById(R.id.textInputEditText_largura);
        configurarEditText(editTextComprimento, editTextLargura);
        spinner = viewDialogCustomizada.findViewById(R.id.spinner_dicionarios);
        configurarSpinner(spinner);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.DialogStyleCustomizado);
        builder.setView(viewDialogCustomizada);
        AlertDialog dialog = builder.create();
        dialog.setCancelable(false);
        dialog.show();


        editTextComprimento.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    Drawable endDrawable = editTextComprimento.getCompoundDrawablesRelative()[2];
                    if (endDrawable != null && event.getRawX() >= (editTextComprimento.getRight() - endDrawable.getBounds().width())) {
                        habilitarEditText(editTextComprimento);
                        return true;
                    }
                }
                return false;
            }
        });

        editTextLargura.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    Drawable endDrawable = editTextLargura.getCompoundDrawablesRelative()[2];
                    if (endDrawable != null && event.getRawX() >= (editTextLargura.getRight() - endDrawable.getBounds().width())) {
                        habilitarEditText(editTextLargura);
                        return true;
                    }
                }
                return false;
            }
        });

        if(editTextIdRobo!=null){
            editTextIdRobo.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        Drawable endDrawable = editTextIdRobo.getCompoundDrawablesRelative()[2];
                        if (endDrawable != null && event.getRawX() >= (editTextIdRobo.getRight() - endDrawable.getBounds().width())) {
                            habilitarEditText(editTextIdRobo);
                            return true;
                        }
                    }
                    return false;
                }
            });
        }

        Button buttonOk = viewDialogCustomizada.findViewById(R.id.button_ok);
        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Você escolheu OK!", Toast.LENGTH_LONG).show();
                if(String.valueOf(editTextComprimento.getText()).isEmpty() && String.valueOf(editTextLargura.getText()).isEmpty()){
                    editTextComprimento.setError("Por favor, insira um valor");
                    editTextLargura.setError("Por favor, insira um valor");
                }else if(String.valueOf(editTextComprimento.getText()).isEmpty()){
                    editTextComprimento.setError("Por favor, insira um valor");
                } else if (String.valueOf(editTextLargura.getText()).isEmpty()) {
                    editTextLargura.setError("Por favor, insira um valor");
                } else if (editTextIdRobo != null && String.valueOf(editTextIdRobo.getText()).isEmpty()) {
                    editTextIdRobo.setError("Por favor, insira um valor");
                } else{
                    String comprimento = String.valueOf(editTextComprimento.getText());
                    String largura = String.valueOf(editTextLargura.getText());
                    if(editTextIdRobo != null){
                        String idRobo = String.valueOf(editTextIdRobo.getText());
                        salvarIdRobo(idRobo);
                    }
                    salvarDicionarioMarcador(spinner);
                    salvarDimensoesAmbiente(comprimento, largura);
                    dialog.dismiss();
                }
            }
        });

        Button buttonCancelar = viewDialogCustomizada.findViewById(R.id.button_cancelar);
        buttonCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Você escolheu CANCELAR!", Toast.LENGTH_LONG).show();
                dialog.dismiss();
            }
        });
    }

    private void salvarDicionarioMarcador(Spinner spinner) {
        String selected = spinner.getSelectedItem().toString();
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("DicionarioDeMarcadores", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("dicionario", selected);
        editor.apply();
    }

    private void salvarIdRobo(String idRobo) {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("IdRobo", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("id", idRobo);
        editor.apply();
    }

    private void configurarIdRobo(TextInputEditText editTextIdRobo) {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("IdRobo", MODE_PRIVATE);
        String idRobo = sharedPreferences.getString("id", "");
        editTextIdRobo.setText(idRobo);
        if (!idRobo.isEmpty()) {
            desabilitarEditText(editTextIdRobo);
        }
    }

    private void configurarSpinner(Spinner spinner) {
        String[] dicionarios = adicionarDicionarios();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, dicionarios);
        adapter.setDropDownViewResource(R.layout.spinner_item_background);
        spinner.setAdapter(adapter);

        SharedPreferences preferences = getContext().getSharedPreferences("DicionarioDeMarcadores", MODE_PRIVATE);
        String selectedItem = preferences.getString("dicionario", null);
        if (selectedItem != null) {
            int spinnerPosition = adapter.getPosition(selectedItem);
            spinner.setSelection(spinnerPosition);
        }
    }

    private String[] adicionarDicionarios() {
        return new String[]{
                "ARUCO_ORIGINAL",
                "4X4_50",
                "4X4_100",
                "4X4_250",
                "4X4_1000",
                "5X5_50",
                "5X5_100",
                "5X5_250",
                "5X5_1000",
                "6X6_50",
                "6X6_100",
                "6X6_250",
                "6X6_1000",
                "7X7_50",
                "7X7_100",
                "7X7_250",
                "7X7_1000"
        };
    }

    private void salvarDimensoesAmbiente(String comprimento, String largura) {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("DimensoesAmbiente", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("comprimento", comprimento);
        editor.putString("largura", largura);
        editor.apply();
    }

    private void configurarEditText(TextInputEditText editTextComprimento, TextInputEditText editTextLargura) {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("DimensoesAmbiente", MODE_PRIVATE);
        String comprimento = sharedPreferences.getString("comprimento", "");
        String largura = sharedPreferences.getString("largura", "");

        editTextComprimento.setText(comprimento);
        editTextLargura.setText(largura);

        if (!comprimento.isEmpty()) {
            desabilitarEditText(editTextComprimento);
        }
        if (!largura.isEmpty()) {
            desabilitarEditText(editTextLargura);
        }
    }


    private void desabilitarEditText(TextInputEditText editText) {
        editText.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0, R.drawable.icon_edit,0);
        editText.setFocusable(false);
        editText.setClickable(false);
        editText.setCursorVisible(false);
    }

    private void habilitarEditText(TextInputEditText editText) {
        editText.setEnabled(true);
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.setCursorVisible(true);
        editText.requestFocus();
    }
}

