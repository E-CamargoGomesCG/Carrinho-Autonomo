package com.example.robosmart.ui.view;

import static java.security.AccessController.getContext;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.example.robosmart.R;
import com.example.robosmart.databinding.ActivityMainBinding;
import com.example.robosmart.utils.TipoFragment;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.opencv.android.OpenCVLoader;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        EdgeToEdge.enable(this);
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        if(OpenCVLoader.initDebug()){
            Log.e("OpenCV", "SUCESSO");
        }else{
            Log.e("OpenCV", "FALHA");
        }

        inicializarToolbar();
        inicializarBottomBar();
        alterarFragment(CapturaImagemFragment.newInstance(TipoFragment.DEFINIR_OBJETIVO));

        binding.bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId() == R.id.itemMenu_arUco){
                    //binding.bottomNavigationView.getMenu().findItem(R.id.itemMenu_arUco).setIcon(R.drawable.icon_marcador_preto);
                    alterarFragment(ArUcoFragment.newInstance(TipoFragment.ARUCO));
                    return true;
                } else if (item.getItemId() == R.id.itemMenu_definirObjetivo) {
                    alterarFragment(CapturaImagemFragment.newInstance(TipoFragment.DEFINIR_OBJETIVO));
                    return true;
                }else{
                    return false;
                }
            }
        });
    }


    private void inicializarBottomBar() {
        BottomAppBar bottomAppBar = findViewById(R.id.bottomAppBar);
        Drawable drawable = ContextCompat.getDrawable(this, R.drawable.bottom_app_bar_background);
        bottomAppBar.setBackground(drawable);
    }

    private void inicializarToolbar() {
        setSupportActionBar(binding.includeToolbar.toolbarPrincipal);
    }

    private void alterarFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, fragment).commit();
    }

}