package com.example.dasmeet;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.dasmeet.databinding.ActivityMainBinding;
import com.example.dasmeet.home.Home;
import com.example.dasmeet.utils.FileUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStreamWriter;

public class MainActivity extends AppCompatActivity {

    private static final int HOME_ID = R.id.home_bar;
    private static final int CHAT_ID = R.id.chat_bar;
    private static final int SETTINGS_ID = R.id.settings_bar;
    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FileUtils fu = new FileUtils();
        if (!fu.sessionExists(this, "config.txt")) {
            Intent intent = new Intent(this, InicioSesion.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
                    | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else {
            binding = ActivityMainBinding.inflate(getLayoutInflater());
            setContentView(binding.getRoot());

            Toolbar t = findViewById(R.id.toolbar);
            setSupportActionBar(t);

            binding.bottomNavigationView.setOnItemSelectedListener(i -> {
                if (i.getItemId() == HOME_ID)
                    replaceFragment(new Home());

                else if (i.getItemId() == CHAT_ID)
                    replaceFragment(new UsersListFragment());
                else if (i.getItemId() == SETTINGS_ID)
                    replaceFragment(new SettingsFragment());
                return true;
            });
            replaceFragment(new Home());
        }
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.fragment_container, fragment);

        fragmentTransaction.commit();
    }

    public void changeToolbar(boolean b) {
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(b);

        }

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        replaceFragment(new SettingsFragment());

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager = getSupportFragmentManager();

        Fragment fragmentById = fragmentManager.findFragmentById(R.id.fragment_container);

        if (fragmentById instanceof PerfilFragment ||
                fragmentById instanceof IdiomaFragment ||
                fragmentById instanceof TemaFragment) {
            this.replaceFragment(new SettingsFragment());
        } else {
            super.onBackPressed();
        }
    }

    public void openDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //builder.setTitle(nom+" quiere conocerte");
        LayoutInflater inflater=getLayoutInflater();
        View elaspecto= inflater.inflate(R.layout.dialog_solicitud_chat,null);
        builder.setView(elaspecto);
        TextView nomusu= elaspecto.findViewById(R.id.nombreUsu);
        //nomusu.setText(nom);
        TextView fraseLigar= elaspecto.findViewById(R.id.fraseLigar);
        //fraseLigar.setText(frase);

        Button btnAccept = elaspecto.findViewById(R.id.btnAccept);
        Button btnReject = elaspecto.findViewById(R.id.btnReject);

        btnAccept.setOnClickListener(v -> {
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
                        //requestBody.put("mail2", mail2);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }

                    JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url,
                            requestBody, response -> {
                        try {
                            if (response.get("success").equals(true)) {
                                // The photo exists
                                Intent intent = new Intent(MainActivity.this, Chat.class);
                                /*intent.putExtra("nombre", nom);
                                intent.putExtra("mail1",mail2);*/
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
            btnAccept.setOnClickListener(v1 -> builder2.create().dismiss());

            builder2.create().show();

        });

        btnReject.setOnClickListener(v -> {
            // Acción para rechazar
            Toast.makeText(MainActivity.this, "", Toast.LENGTH_SHORT).show();
            builder.create().dismiss();
        });

        builder.create().show();
    }
}
