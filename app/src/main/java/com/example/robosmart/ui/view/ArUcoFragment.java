package com.example.robosmart.ui.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.robosmart.databinding.FragmentArucoBinding;
import com.example.robosmart.utils.TipoFragment;

public class ArUcoFragment extends Fragment {

    private FragmentArucoBinding binding;
    private TipoFragment tipoFragment;

    public static ArUcoFragment newInstance(TipoFragment tipoFragment) {
        ArUcoFragment fragment = new ArUcoFragment();
        fragment.tipoFragment = tipoFragment;
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentArucoBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }
}
