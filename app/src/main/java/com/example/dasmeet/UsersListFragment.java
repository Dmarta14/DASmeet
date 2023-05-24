package com.example.dasmeet;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.dasmeet.FileUtils;
import com.example.dasmeet.R;
import com.example.dasmeet.UserListAdapter;
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

public class UsersListFragment extends Fragment {

    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://mensajeriafcm-ea7c9-default-rtdb.europe-west1.firebasedatabase.app/");
    private String usermail;
    private String userExtmail = "";
    private String chatKey = "";
    String mailComprobar1 = "";
    String mailComprobar2 = "";
    private ArrayList<String> noms = new ArrayList<>();
    private ArrayList<String> imgs = new ArrayList<>();
    private ArrayList<String> mails = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_users_list, container, false);

        ListView lista = view.findViewById(R.id.users_list);

        FileUtils fileUtils = new FileUtils();
        String mail = fileUtils.readFile(requireContext(), "config.txt");

        String url = BD.getIp() + "/mailsChats";
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
                    UserListAdapter eladap = new UserListAdapter(requireContext(), noms, imgs);
                    lista.setAdapter(eladap);

                }
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }, error -> {
            Log.e("PA", "ERROR", error);
        });

        RequestQueue queue = Volley.newRequestQueue(requireContext());
        queue.add(request);

        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                obtenerClave(mails.get(i),i);
            }
        });

        return view;
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
                Intent intent = new Intent(requireActivity(), Chat.class);
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
                    new OutputStreamWriter(requireContext().openFileOutput("config.txt",
                            Context.MODE_PRIVATE));
            outputStreamWriter.write(mail);
            outputStreamWriter.close();
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e);
        }
    }
}
