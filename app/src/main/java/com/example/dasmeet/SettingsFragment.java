package com.example.dasmeet;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ListView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import com.example.dasmeet.utils.FileUtils;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;import org.json.JSONException;

import java.util.ArrayList;

public class SettingsFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((MainActivity)getActivity()).changeToolbar(false);
        ArrayList<String> texto=new ArrayList<>();
        ArrayList<Drawable> imgs=new ArrayList<>();
        texto.add(getResources().getString(R.string.perfil));

        texto.add(getResources().getString(R.string.idioma));
        texto.add(getResources().getString(R.string.tema));
        texto.add(getResources().getString(R.string.cerrar));
        texto.add(getResources().getString(R.string.eliminar));
        if (getContext() != null) {
            imgs.add(ContextCompat.getDrawable(getContext(),R.drawable.perfil));
            imgs.add(ContextCompat.getDrawable(getContext(),R.drawable.idioma));
            imgs.add(ContextCompat.getDrawable(getContext(),R.drawable.themas));
            imgs.add(ContextCompat.getDrawable(getContext(),R.drawable.cerrar));
            imgs.add(ContextCompat.getDrawable(getContext(),R.drawable.eliminar));
        }



        ListView lista=view.findViewById(R.id.lista);
        AdaptadorLista adap = new AdaptadorLista(getContext(), texto, imgs);
        lista.setAdapter(adap);

        lista.setOnItemClickListener((adapterView, view1, i, l) -> {
            System.out.println(texto.get(i));
            switch (i) {
                case 0:
                    // Navegar al fragmento de ajustes
                    replaceFragment(new PerfilFragment());
                    break;
                case 1:
                    // Navegar al fragmento de idioma
                    replaceFragment(new IdiomaFragment());
                    break;
                case 2:
                    replaceFragment(new TemaFragment());
                    break;
                case 3:
                    boolean success = getContext().deleteFile("config.txt");

                    if (success) {
                        Intent intent = new Intent(getContext(), InicioSesion.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
                                | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                    break;
                case 4:
                    FileUtils fileUtils = new FileUtils();
                    String mail = fileUtils.readFile(getContext(), "config.txt");

                        String url = BD.getIp() + "/eliminarTodoUsuario?mail="+mail;

						JsonObjectRequest request = new JsonObjectRequest(Request.Method.DELETE, url,null,
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


        });


    }

	private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.fragment_container, fragment);

        fragmentTransaction.commit();
    }}
