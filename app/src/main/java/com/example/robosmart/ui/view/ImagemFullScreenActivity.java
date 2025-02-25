package com.example.robosmart.ui.view;

import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.robosmart.R;
import com.example.robosmart.databinding.ActivityImagemFullScreenBinding;
import com.example.robosmart.ui.viewmodel.ImagemViewModel;

public class ImagemFullScreenActivity extends AppCompatActivity {
    ImagemViewModel imagemViewModel;

    ActivityImagemFullScreenBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityImagemFullScreenBinding.inflate(getLayoutInflater());
        EdgeToEdge.enable(this);
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        imagemViewModel = new ViewModelProvider(this).get(ImagemViewModel.class);
        imagemViewModel.getImagemBitmap().observe(ImagemFullScreenActivity.this, new Observer<Bitmap>() {
            @Override
            public void onChanged(Bitmap bitmap) {
                if(bitmap != null){
                    binding.imageViewImagem.setImageBitmap(bitmap);
                }else{
                    binding.imageViewImagem.setImageResource(R.drawable.nenhuma_imagem_capturada);
                }
            }
        });
    }
}