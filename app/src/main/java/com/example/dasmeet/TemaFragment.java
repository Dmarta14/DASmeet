package com.example.dasmeet;

import androidx.fragment.app.Fragment;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;


public class TemaFragment extends Fragment {

    private static final String PREFS_NAME = "MyPrefsFile";
    private static final String KEY_MODE = "mode";

    private RadioGroup radioGroup;
    private SharedPreferences sharedPrefs;
    private SharedPreferences.Editor editor;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflar el layout para el fragmento
        View view = inflater.inflate(R.layout.fragment_tema, container, false);

        // Obtener la referencia del RadioGroup
        radioGroup = view.findViewById(R.id.radioGroup);

        // Establecer el listener para el RadioGroup
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // Verificar qué radio button ha sido seleccionado
                if (checkedId == R.id.radioButtonModoClaro) {
                    saveModo("claro");
                    setModo("claro");
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                } else if (checkedId == R.id.radioButtonModoOscuro) {
                    saveModo("oscuro");
                    setModo("oscuro");
                    // Establecer el modo oscuro
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                }
            }
        });

        ImageView imageView = view.findViewById(R.id.imageview4);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(view);
                navController.navigate(R.id.action_TemaFragment_to_SettingsFragment);
            }
        });

        return view;
    }
    private void saveModo(String modo) {
        // Obtén una instancia de SharedPreferences
        SharedPreferences sharedPreferences = getActivity().getPreferences(getActivity().MODE_PRIVATE);

        // Obtén un editor de SharedPreferences
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // Guarda el idioma seleccionado
        editor.putString("modo", modo);

        // Aplica los cambios
        editor.apply();
    }

    private void setModo(String modo) {
        // Configura el modo en la aplicación
        ((MainActivity) getActivity()).setModo(modo);
    }
}