package com.example.dasmeet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private static final String DEFAULT_LANGUAGE = "default";
    private static final String DEFAULT_MODE = "default";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Fragment fragInfo = getSupportFragmentManager().findFragmentById(R.id.fragmentContainerView2);
        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);

        // Obtiene el idioma almacenado
        String idiomaSeleccionado = sharedPreferences.getString("idioma", DEFAULT_LANGUAGE);
        String modoSeleccionado = sharedPreferences.getString("modo", DEFAULT_MODE);
        // Configura el idioma en la aplicación
        setLanguage(idiomaSeleccionado);
        setModo(modoSeleccionado);
        saveSession("jbra001@gmail.com");
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
    public void setLanguage(String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);

        Resources resources = getResources();
        Configuration configuration = resources.getConfiguration();
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();

        configuration.setLocale(locale);

        resources.updateConfiguration(configuration, displayMetrics);

       // requireActivity().recreate(); // Reinicia la actividad para que se aplique el nuevo idioma
    }
    public void setModo(String modo){
        if(modo.equals("oscuro")){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else if (modo.equals("claro")) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        }

    }


}