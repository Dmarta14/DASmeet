package com.example.dasmeet;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Locale;

public class inicio_sesion extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_inicio_sesion);

        TextView mail =findViewById (R.id.UsuarioIni);
        TextView contrasena1 = findViewById (R.id.PasswordIni);
        TextView registrar = findViewById(R.id.Registrarse);


        Button iniciar_sesion = findViewById(R.id.Acceder);
        iniciar_sesion.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String email = mail.getText().toString();
                String contra1 = contrasena1.getText().toString ();
                obtenerUsuario (email, contra1);
            }
        });

        registrar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent intent = new Intent(v.getContext(),Registro.class);
                startActivity(intent);

            }
        });
    }


    public void obtenerUsuario(String usuario, String contra){
        Data param =new Data .Builder ()
                .putString ("param","ExisteUsuarioContra")
                .putString ("mail", usuario)
                .putString ("contrasena",contra).build ();
        Log.d("Prueba inicio",""+  param);
        OneTimeWorkRequest oneTimeWorkRequest = new OneTimeWorkRequest.Builder(BD.class).setInputData(param).build();
        WorkManager.getInstance(inicio_sesion.this).enqueue(oneTimeWorkRequest);
        WorkManager.getInstance(inicio_sesion.this).getWorkInfoByIdLiveData(oneTimeWorkRequest.getId())
                .observe (inicio_sesion.this, new Observer<WorkInfo> () {
                    @Override
                    public void onChanged(WorkInfo workInfo) {
                        if (workInfo != null && workInfo.getState().isFinished()) {
                            if (workInfo.getState() != WorkInfo.State.SUCCEEDED) {
                                Toast.makeText (getApplicationContext (),"ERROR",Toast.LENGTH_LONG).show ();
                            }else{
                                Data d = workInfo.getOutputData();
                                boolean b = d.getBoolean("existe",false);
                                Log.d("Prueba inicio", "" + b);
                                if(b){
                                    Toast.makeText(getApplicationContext(), "existe un usuario", Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(getApplicationContext(),Menu_Principal.class);
                                    saveSession(usuario);
                                    startActivity(intent);
                                    finish();
                                }
                                else {
                                    AlertDialog.Builder builder = new AlertDialog.Builder (inicio_sesion.this);
                                    builder.setTitle("Usuario o contraseña incorrectos");
                                    builder.setMessage("Introduce el usuario o contraseña correctamente o registrese en caso de no tener un usuario creado");
                                    builder.setPositiveButton("Volver", new DialogInterface.OnClickListener () {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            Intent intent = new Intent(getApplicationContext(), inicio_sesion.class);
                                            startActivity(intent);
                                        }
                                    });
                                    AlertDialog alert = builder.create();
                                    alert.show();


                                }
                            }
                        }
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