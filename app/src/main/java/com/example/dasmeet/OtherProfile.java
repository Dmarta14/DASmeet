package com.example.dasmeet;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class OtherProfile extends AppCompatActivity {
    private TextView timer;
    private String getNombre,getFotoPerfil,user1mail,usermail,chatKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_profile);
        //obtenemos la informacion de la lista de usuarios
        getFotoPerfil = getIntent().getStringExtra("fotoPerfil");
        chatKey = getIntent().getStringExtra("chatKey");
        user1mail = getIntent().getStringExtra("mail1");
        usermail = getIntent().getStringExtra("mailUser");


        getNombre = getIntent().getStringExtra("nombreUser");
        String getFotoPer = getIntent().getStringExtra("fotoPer");
        TextView nombreUser=findViewById(R.id.nombre);
        ImageView fotoPerfil=findViewById(R.id.fotoPerfil);
        timer=findViewById(R.id.timer);
        ImageView atras=findViewById(R.id.atras_btn2);

        String image64 = getFotoPer;
        byte[] b = Base64.decode(image64, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(b,0,
                b.length);
        Bitmap rescaledImage = adjustImageSize(bitmap);
        fotoPerfil.setImageBitmap(rescaledImage);

        atras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OtherProfile.this, Chat.class);
                intent.putExtra("nombre",getNombre);
                intent.putExtra("mail1",user1mail);
                intent.putExtra("mailUser",usermail);
                intent.putExtra("chatKey", chatKey);
                intent.putExtra("fotoPer",getFotoPer);
                startActivity(intent);
            }
        });

        nombreUser.setText(getNombre);
        //Picasso.get().load(getFotoPer).into(fotoPerfil);
    }
    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onTimerEvent(TimerEvent event) {
        long millisUntilFinished = event.getMillisUntilFinished();
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

// Convertir el tiempo actual a formato de fecha y hora
        String formattedDateTime = dateFormat.format(new Date(millisUntilFinished));
        // Actualiza la interfaz de usuario con el tiempo restante
        timer.setText(formattedDateTime);
    }

    // Adjust the image size to be bigger than the one taken
    private Bitmap adjustImageSize(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int length = bitmap.getHeight();

        int newSize = 800;
        float scaleWidth = ((float) newSize/width);
        float scaleLength = ((float) newSize/length);

        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleLength);

        return Bitmap.createBitmap(bitmap, 0,0, width, length, matrix, true);
    }
}