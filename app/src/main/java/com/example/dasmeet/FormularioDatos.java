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
}
