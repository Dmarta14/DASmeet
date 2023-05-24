package com.example.dasmeet;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
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
import androidx.appcompat.app.AppCompatDelegate;
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
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private static final int HOME_ID = R.id.home_bar;
    private static final int CHAT_ID = R.id.chat_bar;
    private static final int SETTINGS_ID = R.id.settings_bar;

    private static final String DEFAULT_LANGUAGE = "default";
    private static final String DEFAULT_MODE = "default";
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
            SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);

            String idioma = sharedPreferences.getString("idioma", DEFAULT_LANGUAGE);
            String modo = sharedPreferences.getString("modo", DEFAULT_MODE);
            setLanguage(idioma);
            setModo(modo);
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
