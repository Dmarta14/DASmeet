package com.example.dasmeet;

import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Chat extends AppCompatActivity {
    DatabaseReference databaseReference =
            FirebaseDatabase.getInstance().getReferenceFromUrl("https://mensajeriafcm" +
                    "-ea7c9-default-rtdb.europe-west1.firebasedatabase.app/");
    private String chatKey;
    private RecyclerView chatRecView;
    private ArrayList<String> nombres = new ArrayList<String>();
    private ArrayList<String> mensajes = new ArrayList<String>();
    private ArrayList<String> horas = new ArrayList<String>();


    private PendingIntent pendingIntent;
    private ChatAdapter chatAdapter;
    private String getNombre, getFotoPerfil, user1mail, usermail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        //prueba
        //databaseReference.child("chat").child("1").child("user1").setValue(user1mail);

        final ImageView atras = findViewById(R.id.atras_btn);
        final TextView nombre = findViewById(R.id.nombre);
        final EditText messageEditTxt = findViewById(R.id.messageEditTxt);
        final ImageView fotoPerfil = findViewById(R.id.fotoPerfil);
        final ImageView enviar = findViewById(R.id.enviar_btn);
        chatRecView = findViewById(R.id.chatRecyclerView);

        //obtenemos la informacion de la lista de usuarios
        getNombre = getIntent().getStringExtra("nombre");
        getFotoPerfil = getIntent().getStringExtra("fotoPerfil");
        chatKey = getIntent().getStringExtra("chatKey");
        user1mail = getIntent().getStringExtra("mail1");
        usermail = getIntent().getStringExtra("mailUser");

        Log.d("chat", "onCreate: " + user1mail + " " + usermail);


        nombre.setText(getNombre);
        if (!getFotoPerfil.equals("default")) {
            String image64 = getFotoPerfil;
            byte[] b = Base64.decode(image64, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0,
                    b.length);
            Bitmap rescaledImage = adjustImageSize(bitmap);
            fotoPerfil.setImageBitmap(rescaledImage);
        }
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                nombres.clear();
                mensajes.clear();
                horas.clear();
                if (chatKey.isEmpty()) {
                    //activarTemporizador(Integer.parseInt(chatKey));
                    chatKey = "1";
                    if (snapshot.hasChild("chat")) {
                        chatKey =
                                String.valueOf(snapshot.child("chat").getChildrenCount() + 1);
                    }
                } else {
                    if (snapshot.hasChild("chat")) {
                        for (DataSnapshot messagesnapshot :
                                snapshot.child("chat").child(chatKey).child("messages").getChildren()) {
                            if (messagesnapshot.hasChild("msg") && messagesnapshot.hasChild("emisor")) {
                                final String msgTimeStamp = messagesnapshot.getKey();
                                final String usermailrec = messagesnapshot.child(
                                        "emisor").getValue(String.class);
                                final String msg =
                                        messagesnapshot.child("msg").getValue(String.class);
                                nombres.add(usermailrec);
                                mensajes.add(msg);
                                horas.add(msgTimeStamp);
                                chatRecView.setHasFixedSize(true);
                                chatRecView.setLayoutManager(new LinearLayoutManager(Chat.this));
                                chatAdapter = new ChatAdapter(usermail,
                                        getApplicationContext(), nombres, mensajes,
                                        horas);
                                chatRecView.setAdapter(chatAdapter);
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        //enviar boton
        enviar.setOnClickListener(v -> {
            // Obtener el tiempo actual en milisegundos
            long currentTimeMillis = System.currentTimeMillis();

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

            String formattedDateTime = dateFormat.format(new Date(currentTimeMillis));

            String message = messageEditTxt.getText().toString();
            if (!message.isEmpty()) {
                databaseReference.child("chat").child(chatKey).child("user1").setValue(user1mail);
                databaseReference.child("chat").child(chatKey).child("user2").setValue(usermail);
                databaseReference.child("chat").child(chatKey).child("messages").child(formattedDateTime).child("msg").setValue(message);
                databaseReference.child("chat").child(chatKey).child("messages").child(formattedDateTime).child("emisor").setValue(usermail);
            } else {
                Toast.makeText(Chat.this, "No se puede enviar mensajes vacios",
                        Toast.LENGTH_SHORT).show();
            }
            messageEditTxt.setText("");

        });

        //atras boton
        atras.setOnClickListener(v -> {
            finish();
        });
    }

    public void onClickPerfil(View v) {
        Intent intent = new Intent(Chat.this, OtherProfile.class);
        intent.putExtra("nombreUser", getNombre);
        intent.putExtra("mail1", user1mail);
        intent.putExtra("mailUser", usermail);
        intent.putExtra("chatKey", chatKey);
        intent.putExtra("fotoPer", getFotoPerfil.equals("null") ? "default" :
                getFotoPerfil);
        startActivity(intent);
    }

    private Bitmap adjustImageSize(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int length = bitmap.getHeight();

        int newSize = 800;
        float scaleWidth = ((float) newSize / width);
        float scaleLength = ((float) newSize / length);

        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleLength);

        return Bitmap.createBitmap(bitmap, 0, 0, width, length, matrix, true);
    }


}