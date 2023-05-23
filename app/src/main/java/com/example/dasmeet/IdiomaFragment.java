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


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link IdiomaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class IdiomaFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public IdiomaFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment IdiomaFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static IdiomaFragment newInstance(String param1, String param2) {
        IdiomaFragment fragment = new IdiomaFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
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

        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                System.out.println(texto.get(i));
                NavController navController = Navigation.findNavController(view);
                switch (i) {
                    case 0:
                        setAppLocale("es");
                        // Navegar al fragmento de ajustes
                        navController.navigate(R.id.action_IdiomaFragment_to_SettingsFragment);
                        break;
                    case 1:
                        // Navegar al fragmento de idioma
                        setAppLocale("en");
                        navController.navigate(R.id.action_IdiomaFragment_to_SettingsFragment);
                        break;


                    default:
                        // Valor de i no válido, realizar una acción alternativa o mostrar un mensaje de error
                }


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

        requireActivity().recreate(); // Reinicia la actividad para que se aplique el nuevo idioma
    }
}
