package com.example.dasmeet;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.io.OutputStreamWriter;

public class MainActivity extends AppCompatActivity {

    DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReferenceFromUrl("https://mensajeriafcm-ea7c9-default-rtdb.europe-west1.firebasedatabase.app/");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        saveSession("sara@gmail.com");
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