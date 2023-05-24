package com.example.dasmeet;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.dasmeet.databinding.ActivityMainBinding;
import com.example.dasmeet.home.Home;
import com.example.dasmeet.utils.FileUtils;

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
}