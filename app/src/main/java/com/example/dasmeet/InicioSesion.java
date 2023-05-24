package com.example.dasmeet;


import static android.Manifest.permission.POST_NOTIFICATIONS;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import java.io.IOException;
import java.io.OutputStreamWriter;

public class InicioSesion extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio_sesion);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, POST_NOTIFICATIONS) !=
                    PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new
                        String[]{POST_NOTIFICATIONS}, 11);
            }
        }

        TextView mail = findViewById(R.id.UsuarioIni);
        TextView contrasena1 = findViewById(R.id.PasswordIni);
        TextView registrar = findViewById(R.id.Registrarse);


        Button iniciarSesion = findViewById(R.id.Acceder);
        iniciarSesion.setOnClickListener(v -> {
            String email = mail.getText().toString();
            String contra1 = contrasena1.getText().toString();
            obtenerUsuario(email, contra1);
        });

        registrar.setOnClickListener(v -> {

            Intent intent = new Intent(v.getContext(), Registro.class);
            startActivity(intent);

        });
    }

    public void obtenerUsuario(String usuario, String contra) {
        Data param = new Data.Builder()
                .putString("param", "ExisteUsuarioContra")
                .putString("mail", usuario)
                .putString("contrasena", contra).build();
        Log.d("Prueba inicio", "" + param);
        OneTimeWorkRequest oneTimeWorkRequest =
                new OneTimeWorkRequest.Builder(BD.class).setInputData(param).build();
        WorkManager.getInstance(InicioSesion.this).enqueue(oneTimeWorkRequest);
        WorkManager.getInstance(InicioSesion.this)
                .getWorkInfoByIdLiveData(oneTimeWorkRequest.getId())
                .observe(InicioSesion.this, workInfo -> {
                    if (workInfo != null && workInfo.getState().isFinished()) {
                        if (workInfo.getState() != WorkInfo.State.SUCCEEDED) {
                            Toast.makeText(getApplicationContext(), "ERROR",
                                    Toast.LENGTH_LONG).show();
                        } else {
                            Data d = workInfo.getOutputData();
                            boolean b = d.getBoolean("existe", false);

                            if (b) {
                                Toast.makeText(getApplicationContext(), "existe un " +
                                        "usuario", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(getApplicationContext(),
                                        MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
                                        | Intent.FLAG_ACTIVITY_NEW_TASK);
                                saveSession(usuario);
                                startActivity(intent);
                            } else {
                                AlertDialog.Builder builder =
                                        new AlertDialog.Builder(InicioSesion.this);
                                builder.setTitle("Usuario o contraseña incorrectos");
                                builder.setMessage("Introduce el usuario o contraseña " +
                                        "correctamente o registrese en caso de no tener" +
                                        " un usuario creado");
                                builder.setPositiveButton("Volver", (dialogInterface,
                                                                     i) -> {
                                    Intent intent = new Intent(getApplicationContext(),
                                            InicioSesion.class);
                                    startActivity(intent);
                                });
                                AlertDialog alert = builder.create();
                                alert.show();


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