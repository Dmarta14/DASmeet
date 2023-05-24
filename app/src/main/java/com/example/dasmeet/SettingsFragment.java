package com.example.dasmeet;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import android.widget.ListView;
import android.widget.Toast;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

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

        texto.add(getResources().getString(R.string.idioma));
        texto.add(getResources().getString(R.string.tema));
        texto.add(getResources().getString(R.string.cerrar));
        texto.add(getResources().getString(R.string.eliminar));
        imgs.add(getResources().getDrawable(R.drawable.perfil));
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
                        navController.navigate(R.id.action_SettingsFragment_to_IdiomaFragment);
                        break;
                    case 2:
                        navController.navigate(R.id.action_SettingsFragment_to_TemaFragment);

                        break;
                    case 3:
                        //ir al inicio
                        break;
                    case 4:
                        FileUtils fileUtils = new FileUtils();
                        String mail = fileUtils.readFile(getContext(), "config.txt");
                        String url = "http://" + "192.168.1.116" + ":3005/eliminarTodoUsuario";
                        JSONObject requestBody = new JSONObject();
                        try {
                            requestBody.put("mail", mail);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                        JsonObjectRequest request = new JsonObjectRequest(Request.Method.DELETE, url, requestBody,
                                response -> {
                                    try {
                                        if (response.getBoolean("success")) {
                                            Log.d("titoss","aaa");


                                        }
                                    } catch (JSONException e) {
                                        throw new RuntimeException(e);
                                    }
                                },
                                error -> {
                                    Log.e("PA", "ERROR", error);
                                });

                        RequestQueue queue = Volley.newRequestQueue(getContext());
                        queue.add(request);
                        break;


                    default:
                        // Valor de i no válido, realizar una acción alternativa o mostrar un mensaje de error
                }


            }
        });


    }




}
