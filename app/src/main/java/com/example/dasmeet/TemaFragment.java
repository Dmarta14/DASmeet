package com.example.dasmeet;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

public class TemaFragment extends Fragment {

    private RadioGroup radioGroup;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflar el layout para el fragmento
        View view = inflater.inflate(R.layout.fragment_tema, container, false);
        ((MainActivity) getActivity()).changeToolbar(true);
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

        return view;
    }

    private void saveModo(String modo) {
        // Obtén una instancia de SharedPreferences
        SharedPreferences sharedPreferences =
                getActivity().getPreferences(getActivity().MODE_PRIVATE);

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