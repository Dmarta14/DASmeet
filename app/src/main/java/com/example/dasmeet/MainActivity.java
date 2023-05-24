package com.example.dasmeet;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStreamWriter;

public class MainActivity extends AppCompatActivity {

    DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReferenceFromUrl("https://mensajeriafcm-ea7c9-default-rtdb.europe-west1.firebasedatabase.app/");
    private String nom="Vicen";
    private String mail2="vic@gmail.com";
    private String frase="¿quieres ser mi princesa?";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        saveSession("sara@gmail.com");
        Button btnOpenDialog = findViewById(R.id.btnOpenDialog);
        btnOpenDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });
    }
    public void saveSession(String mail) {
        try {
            OutputStreamWriter outputStreamWriter =
                    new OutputStreamWriter(openFileOutput("config.txt",
                            Context.MODE_PRIVATE));
            outputStreamWriter.write(mail);
            outputStreamWriter.close();
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e);
        }
    }
    public void openDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(nom+" quiere conocerte");
        LayoutInflater inflater=getLayoutInflater();
        View elaspecto= inflater.inflate(R.layout.dialog_solicitud_chat,null);
        builder.setView(elaspecto);
        TextView nomusu= elaspecto.findViewById(R.id.nombreUsu);
        nomusu.setText(nom);
        TextView fraseLigar= elaspecto.findViewById(R.id.fraseLigar);
        fraseLigar.setText(frase);

        Button btnAccept = elaspecto.findViewById(R.id.btnAccept);
        Button btnReject = elaspecto.findViewById(R.id.btnReject);

        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //builder.create().dismiss();
                //enviarNotificacion();
                // Acción para aceptar
                AlertDialog.Builder builder2 = new AlertDialog.Builder(getApplicationContext());
                builder2.setTitle("Match!");
                builder2.setMessage("A partir de ahora teneis 24h para conoceros. Disfrutad!");

                builder2.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    builder2.create().dismiss();
                    FileUtils fileUtils = new FileUtils();

                    String mail = fileUtils.readFile(getApplicationContext(), "config.txt");

                    String url = "http://192.168.1.74:3005/aceparSolicitud";
                    JSONObject requestBody = new JSONObject();

                    try {
                        requestBody.put("mail", mail);
                        requestBody.put("mail2", mail2);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }

                    JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url,
                            requestBody, response -> {
                        try {
                            if (response.get("success").equals(true)) {
                                // The photo exists
                                Intent intent = new Intent(MainActivity.this, Chat.class);
                                intent.putExtra("nombre", nom);
                                intent.putExtra("mail1",mail2);
                                intent.putExtra("fotoPerfil","");
                                intent.putExtra("mailUser",mail);
                                intent.putExtra("chatKey", "");
                                //intent.putExtra("fotoPerfil",imgs.get(i));
                                startActivity(intent);
                                builder.create().dismiss();

                            }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }, error -> {
                        Log.e("PA", "ERROR", error);
                    });

                    RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                    queue.add(request);

                }


            });
            btnAccept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    builder2.create().dismiss();
                }
            });

                builder2.create().show();

            }
        });

        btnReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Acción para rechazar
                Toast.makeText(MainActivity.this, "", Toast.LENGTH_SHORT).show();
                builder.create().dismiss();
            }
        });

        builder.create().show();
    }
}