package com.example.dasmeet;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class UsersList extends AppCompatActivity {
    DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReferenceFromUrl("https://mensajeriafcm-ea7c9-default-rtdb.europe-west1.firebasedatabase.app/");
    private String usermail="saragarcia@gmail.com";
    private String userExtmail="";
    private String chatKey="";
    String mailComprobar1="";
    String mailComprobar2="";
    private ArrayList<String> noms=new ArrayList<>();
    private ArrayList<String> imgs=new ArrayList<>();
    private ArrayList<String> mails=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_list);
        saveSession("sara@gmail.com");
        ListView lista=findViewById(R.id.users_list);
        /*noms.add("jowi");
        noms.add("diego");
        noms.add("vicent");
        noms.add("urko");
        mails.add("joel@gmail.com");
        mails.add("diego@gmail.com");
        mails.add("vicent@gmail.com");
        mails.add("urko@gmail.com");*/
        // Retrieve the image from the database
        FileUtils fileUtils = new FileUtils();

        String mail = fileUtils.readFile(this, "config.txt");

        String url = "http://192.168.1.74:3005/mailsChats";
        JSONObject requestBody = new JSONObject();

        try {
            requestBody.put("mail", mail);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url,
                requestBody, response -> {
            try {
                if (response.get("success").equals(true)) {
                    // The photo exists
                    JSONArray a = response.getJSONArray("lista");

                    for (int i = 0; i < a.length(); i++) {
                        String mail1 = a.getJSONObject(i).getString("Mail1");
                        String mail2 = a.getJSONObject(i).getString("Mail2");
                        if (mail1.equals(mail)){//el pimer mail es el del usuario
                            noms.add(a.getJSONObject(i).getString("Nombre2"));
                            mails.add(mail2);
                            imgs.add(a.getJSONObject(i).getString("Foto2"));
                        }else{
                            noms.add(a.getJSONObject(i).getString("Nombre1"));
                            mails.add(mail1);
                            imgs.add(a.getJSONObject(i).getString("Foto1"));
                        }
                    }
                    UserListAdapter eladap = new UserListAdapter(getApplicationContext(), noms, imgs);
                    lista.setAdapter(eladap);

                }
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }, error -> {
            Log.e("PA", "ERROR", error);
        });

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);

        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                obtenerClave(mails.get(i),i);
            }
        });
    }

    private void obtenerClave(String otroMail,int pos) {
        chatKey="";
        databaseReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild("chat")) {
                    for (DataSnapshot messagesnapshot : snapshot.child("chat").getChildren()) {
                        if (messagesnapshot.hasChild("user1") && messagesnapshot.hasChild("user2")) {
                            mailComprobar1=messagesnapshot.child("user1").getValue(String.class);
                            mailComprobar2=messagesnapshot.child("user2").getValue(String.class);
                            if((mailComprobar1.equals(otroMail) || mailComprobar1.equals(usermail)) && (mailComprobar2.equals(otroMail) || mailComprobar2.equals(usermail)))
                            {
                                //se asume que no existen mensajes contigo mismo
                                chatKey=messagesnapshot.getKey();
                            }
                        }
                    }
                }
                //se abre la ventana de chat
                Intent intent = new Intent(UsersList.this, Chat.class);
                intent.putExtra("nombre", noms.get(pos));
                intent.putExtra("mail1",mails.get(pos));
                intent.putExtra("fotoPerfil",imgs.get(pos));
                intent.putExtra("mailUser",usermail);
                intent.putExtra("chatKey", chatKey);
                //intent.putExtra("fotoPerfil",imgs.get(i));
                startActivity(intent);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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
}