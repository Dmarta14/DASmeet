package com.example.dasmeet;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;

public class AdaptadorLista extends BaseAdapter {
    private Context contexto;

    private LayoutInflater inflater;
    private ArrayList<String> textos;
    private ArrayList<Drawable> imagenes;

    public AdaptadorLista( Context pcontext,ArrayList<String> ptextos, ArrayList<Drawable> pimagenes){
        contexto = pcontext;
        textos = ptextos;
        imagenes=pimagenes;
        inflater = (LayoutInflater) contexto.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return textos.size();
    }

    @Override
    public Object getItem(int i) {
        return textos.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view=inflater.inflate(R.layout.adaptador_lista,null);
        TextView texto= (TextView) view.findViewById(R.id.texto);
        ImageView imagen=(ImageView) view.findViewById(R.id.img);
        texto.setText(textos.get(i));
        imagen.setImageDrawable(imagenes.get(i));


        return view;
    }
}
