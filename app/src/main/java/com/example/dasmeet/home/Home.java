package com.example.dasmeet.home;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.dasmeet.BD;
import com.example.dasmeet.FileUtils;
import com.example.dasmeet.MainActivity;
import com.example.dasmeet.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

public class Home extends Fragment {

    private ImageView imageView1;
    private TextView textViewName1;
    private TextView textViewDesc1;
    private ImageView imageView2;
    private TextView textViewName2;
    private TextView textViewDesc2;
    private String mail1;
    private String mail2;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((MainActivity) getActivity()).changeToolbar(false);
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        imageView1 = view.findViewById(R.id.imageView1);
        textViewName1 = view.findViewById(R.id.textViewName1);
        textViewDesc1 = view.findViewById(R.id.textViewDesc1);
        imageView2 = view.findViewById(R.id.imageView2);
        textViewName2 = view.findViewById(R.id.textViewName2);
        textViewDesc2 = view.findViewById(R.id.textViewDesc2);

        getFirstElements();

        Button bStart = view.findViewById(R.id.b_start_chat);
        bStart.setOnClickListener(c -> {
            if (mail1 != null && mail2 != null) {
                sendNotification();
            }
        });

        return view;
    }

    private void sendNotification() {
        FileUtils f = new FileUtils();
        String mail = f.readFile(getContext(), "config.txt");

        JSONObject o = new JSONObject();
        try {
            o.put("mailSender", mail);
            o.put("mailP1", mail1);
            o.put("mailP2", mail2);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        String url = BD.getIp() + "/notification";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, o,
                response -> {
                    try {
                        if (response.getBoolean("success")) {
                            Toast.makeText(getContext(),
                                    getString(R.string.notification_sent),
                                    Toast.LENGTH_SHORT).show();

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
    }

    private void getFirstElements() {
        String url = BD.getIp() + "/user";

        FileUtils fu = new FileUtils();
        String mail = fu.readFile(getContext(), "config.txt");
        JSONObject o = new JSONObject();
        try {
            o.put("mail", mail);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, o,
                response -> {
                    try {
                        if (response.getJSONArray("userList").length() != 0) {
                            JSONArray a = response.getJSONArray("userList");
                            int maxSize = Math.min(a.length(), 2);
                            Log.d("HOME", "getFirstElements: " + a);
                            for (int i = 0; i < maxSize; i++) {
                                JSONObject obj = a.getJSONObject(i);
                                if (i % 2 == 0) {
                                    textViewName1.setText(obj.getString("Nombre"));
                                    textViewDesc1.setText(obj.getString("Descripcion"));
                                    String image64 = obj.getString("Foto");
                                    mail1 = obj.getString("Mail");
                                    byte[] b = Base64.decode(image64, Base64.DEFAULT);
                                    Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0,
                                            b.length);
                                    imageView1.setImageBitmap(bitmap);
                                } else {
                                    textViewName2.setText(obj.getString("Nombre"));
                                    textViewDesc2.setText(obj.getString("Descripcion"));
                                    String image64 = obj.getString("Foto");
                                    mail2 = obj.getString("Mail");
                                    byte[] b = Base64.decode(image64, Base64.DEFAULT);
                                    Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0,
                                            b.length);
                                    imageView2.setImageBitmap(bitmap);
                                }
                            }
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


    }
}