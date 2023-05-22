package com.example.dasmeet;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import android.widget.ListView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";



    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SettingsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SettingsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SettingsFragment newInstance(String param1, String param2) {
        SettingsFragment fragment = new SettingsFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ArrayList<String> texto=new ArrayList<>();
        ArrayList<Drawable> imgs=new ArrayList<>();
        texto.add(getResources().getString(R.string.perfil));
        texto.add(getResources().getString(R.string.notificaciones));
        texto.add(getResources().getString(R.string.idioma));
        texto.add(getResources().getString(R.string.tema));
        texto.add(getResources().getString(R.string.cerrar));
        texto.add(getResources().getString(R.string.eliminar));
        imgs.add(getResources().getDrawable(R.drawable.perfil));
        imgs.add(getResources().getDrawable(R.drawable.notifi));
        imgs.add(getResources().getDrawable(R.drawable.idioma));
        imgs.add(getResources().getDrawable(R.drawable.themas));
        imgs.add(getResources().getDrawable(R.drawable.cerrar));
        imgs.add(getResources().getDrawable(R.drawable.eliminar));




        ListView lista=view.findViewById(R.id.lista);
        AdaptadorLista adap = new AdaptadorLista(getContext(), texto, imgs);
        lista.setAdapter(adap);

        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                System.out.println(texto.get(i));
                NavController navController = Navigation.findNavController(view);
                switch (i) {
                    case 0:
                        // Navegar al fragmento de ajustes
                        navController.navigate(R.id.action_SettingsFragment_to_PerfilFragment);
                        break;
                    case 1:
                        // Navegar al fragmento de idioma
                        navController.navigate(R.id.action_SettingsFragment_to_NotificacionesFragment);
                        break;
                    case 2:
                        // Navegar al fragmento de notificaciones
                        navController.navigate(R.id.action_SettingsFragment_to_IdiomaFragment);
                        break;
                    case 3:
                        navController.navigate(R.id.action_SettingsFragment_to_TemaFragment);
                        break;
                    case 4:
                        break;
                    case 5:
                        break;

                    default:
                        // Valor de i no válido, realizar una acción alternativa o mostrar un mensaje de error
                }


            }
        });


    }

}
