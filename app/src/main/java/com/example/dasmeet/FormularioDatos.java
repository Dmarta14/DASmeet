package com.example.dasmeet;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

public class FormularioDatos  extends AppCompatActivity {

    RadioGroup sexo,pelo,ojos;
    int selectSexo,selectPelo,selectOjos;
    CheckBox personalidad;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formularios_datos);
        Button volver = findViewById(R.id.Cancelar);
        volver.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),Registro.class);
                startActivity(intent);
            }
        });

        Button siguiente = findViewById(R.id.Siguiente);
        siguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sexo = findViewById(R.id.radio_group_respuestas1);
                selectSexo = sexo.getCheckedRadioButtonId();

                if(selectSexo != -1){
                    RadioButton sexoSelecionado= findViewById(selectSexo);
                    String respuestaSelecionadaSexo = sexoSelecionado.getText().toString();

                    ojos = findViewById(R.id.radio_group_respuestas2);
                    selectOjos = ojos.getCheckedRadioButtonId();
                    if(selectOjos != -1){
                        RadioButton ojosSelecionado= findViewById(selectOjos);
                        String respuestaSelecionadaOjo = ojosSelecionado.getText().toString();

                        pelo = findViewById(R.id.radio_group_respuestas2);
                        selectPelo = pelo.getCheckedRadioButtonId();

                        if(selectPelo != -1){
                            RadioButton peloSelecionado= findViewById(selectPelo);
                            String respuestaSelecionadaPelo = peloSelecionado.getText().toString();

                            anadirDatos(respuestaSelecionadaSexo,respuestaSelecionadaOjo,respuestaSelecionadaPelo);
                        }

                        else {
                            Toast.makeText(getApplicationContext(), "Debes seleccionar un color de pelo en la pregunta 3", Toast.LENGTH_LONG).show();
                        }
                    }

                    else {
                        Toast.makeText(getApplicationContext(), "Debes seleccionar un color de ojos en la pregunta 2", Toast.LENGTH_LONG).show();
                    }
                }
                else {
                    Toast.makeText(getApplicationContext(), "Debes seleccionar un sexo en la pregunta 1", Toast.LENGTH_LONG).show();
                }








                Intent intent = new Intent(v.getContext(),FormularioGustos.class);
                startActivity(intent);
            }
        });
    }

    public void anadirDatos(String sexo, String ojo ,String pelo){
        Data param = new Data.Builder()
                .putString("param", "IntroducirDatos")
                .putString("sexo", sexo)
                .putString("ojo", ojo)
                .putString("pelo", pelo).build();

        OneTimeWorkRequest oneTimeWorkRequest = new OneTimeWorkRequest.Builder(BD.class).setInputData(param).build();
        WorkManager.getInstance(FormularioDatos.this).enqueue(oneTimeWorkRequest);
        WorkManager.getInstance(FormularioDatos.this).getWorkInfoByIdLiveData(oneTimeWorkRequest.getId()).observe(FormularioDatos.this, new Observer<WorkInfo>() {
            @Override
            public void onChanged(WorkInfo workInfo) {
                if (workInfo != null && workInfo.getState().isFinished()) {
                    if (workInfo.getState() != WorkInfo.State.SUCCEEDED) {
                        Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }
}
