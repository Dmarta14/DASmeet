package com.example.dasmeet;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import android.content.res.Configuration;
import android.content.res.Resources;

import android.util.DisplayMetrics;

import java.util.Locale;

public class IdiomaFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((MainActivity)getActivity()).changeToolbar(true);
        return inflater.inflate(R.layout.fragment_idioma, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ArrayList<String> texto=new ArrayList<>();
        ArrayList<Drawable> imgs=new ArrayList<>();
        texto.add(getResources().getString(R.string.esp));
        texto.add(getResources().getString(R.string.ing));

        imgs.add(getResources().getDrawable(R.drawable.espa));
        imgs.add(getResources().getDrawable(R.drawable.ing));






        ListView lista=view.findViewById(R.id.lista2);
        AdaptadorLista adap = new AdaptadorLista(getContext(), texto, imgs);
        lista.setAdapter(adap);

        lista.setOnItemClickListener((adapterView, view1, i, l) -> {

            switch (i) {
                case 0:
                    setAppLocale("es");
                    break;
                case 1:
                    // Navegar al fragmento de idioma
                    setAppLocale("en");
                    break;


                default:
                    // Valor de i no válido, realizar una acción alternativa o mostrar un mensaje de error
            }


        });


    }
    private void setAppLocale(String languageCode) {
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);

        Resources resources = getResources();
        Configuration configuration = resources.getConfiguration();
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();

        configuration.setLocale(locale);

        resources.updateConfiguration(configuration, displayMetrics);

        requireActivity().recreate();
    }
}
