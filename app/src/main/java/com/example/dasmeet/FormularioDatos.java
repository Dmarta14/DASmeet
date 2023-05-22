package com.example.dasmeet;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
    CheckBox gracioso,alegre,simpatico,borde,cabezon,humilde,fiel,impuntual,carinoso,leer,deporte,fiesta,cine,otro;
    int selectSexo,selectPelo,selectOjos;
    boolean bgracioso,balegre,bsimpatico,bborde,bcabezon,bhumilde,bfiel,bimpuntual,bcarinoso,bleer,bdeporte,bfiesta,bcine,botro = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formularios_datos);
        gracioso=findViewById(R.id.opcion11);
        alegre=findViewById(R.id.opcion12);
        simpatico=findViewById(R.id.opcion13);
        borde=findViewById(R.id.opcion14);
        cabezon=findViewById(R.id.opcion15);
        humilde=findViewById(R.id.opcion16);
        fiel=findViewById(R.id.opcion17);
        impuntual=findViewById(R.id.opcion18);
        carinoso=findViewById(R.id.opcion19);
        leer=findViewById(R.id.opcion20);
        deporte=findViewById(R.id.opcion21);
        fiesta=findViewById(R.id.opcion22);
        cine=findViewById(R.id.opcion23);
        otro=findViewById(R.id.opcion24);
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
                Log.d("Prueba", "" + selectSexo);
                FileUtils fUtils =new FileUtils();
                String mail = fUtils.readFile(getApplicationContext(), "config.txt");
                if(selectSexo != -1){
                    RadioButton sexoSelecionado= findViewById(selectSexo);
                    String respuestaSelecionadaSexo = sexoSelecionado.getText().toString();

                    ojos = findViewById(R.id.radio_group_respuestas2);
                    selectOjos = ojos.getCheckedRadioButtonId();
                    Log.d("Prueba Ojo" , "" + selectOjos);
                    if(selectOjos != -1){
                        RadioButton ojosSelecionado= findViewById(selectOjos);
                        Log.d("Prueba Ojo" , "" + ojosSelecionado);
                        String respuestaSelecionadaOjo = ojosSelecionado.getText().toString();
                        Log.d("Prueba Ojo" , "" + respuestaSelecionadaOjo);

                        pelo = findViewById(R.id.radio_group_respuestas3);
                        selectPelo = pelo.getCheckedRadioButtonId();

                        if(selectPelo != -1){
                            RadioButton peloSelecionado= findViewById(selectPelo);
                            String respuestaSelecionadaPelo = peloSelecionado.getText().toString();

                            anadirDatos(respuestaSelecionadaSexo,respuestaSelecionadaOjo,respuestaSelecionadaPelo,mail);
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

                if (gracioso.isChecked()){
                    bgracioso=true;
                }
                if(alegre.isChecked()){
                    balegre=true;
                }
                if (simpatico.isChecked()){
                    bsimpatico=true;
                }
                if(borde.isChecked()){
                    bborde=true;
                }
                if (cabezon.isChecked()){
                    bcabezon=true;
                }
                if(humilde.isChecked()){
                    bhumilde=true;
                }
                if(fiel.isChecked()){
                    bfiel=true;
                }
                if (impuntual.isChecked()){
                    bimpuntual=true;
                }
                if(carinoso.isChecked()){
                    bcarinoso=true;
                }

                anardirPersonalidad(bgracioso,balegre,bsimpatico,bborde,bcabezon,bhumilde,bfiel,bimpuntual,bcarinoso,mail);

                if (leer.isChecked()){
                    bleer=true;
                }
                if(deporte.isChecked()){
                    bdeporte=true;
                }
                if (fiesta.isChecked()){
                    bfiesta=true;
                }
                if(cine.isChecked()){
                    bcine=true;
                }
                if (otro.isChecked()){
                    botro=true;
                }

                anadirHobbies(bleer,bdeporte,bfiesta,bcine,botro,mail);







                Intent intent = new Intent(v.getContext(),FormularioGustos.class);
                startActivity(intent);
            }
        });
    }

    public void anadirDatos(String sexo, String ojo ,String pelo, String mail){
        Data param = new Data.Builder()
                .putString("param", "IntroducirDatos")
                .putString("sexo", sexo)
                .putString("ojo", ojo)
                .putString("pelo", pelo)
                .putString("mail",mail).build();
        Log.d("Prueba Datos", "" + param);

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
    public void anardirPersonalidad(Boolean gracioso,Boolean alegre,Boolean simpatico,Boolean borde,Boolean cabezon,Boolean humilde,Boolean fiel,Boolean impuntual,Boolean carinoso,String mail){
        Data param = new Data.Builder()
                .putString("param", "IntroducirPersonalidad")
                .putBoolean("gracioso", gracioso)
                .putBoolean("alegre", alegre)
                .putBoolean("simpatico", simpatico)
                .putBoolean("borde",borde)
                .putBoolean("cabezon", cabezon)
                .putBoolean("humilde", humilde)
                .putBoolean("fiel", fiel)
                .putBoolean("imputual",impuntual)
                .putBoolean("carinoso",carinoso)
                .putString("mail",mail).build();
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

    public void anadirHobbies(Boolean leer, Boolean deporte, Boolean fiesta, Boolean cine, Boolean otro, String mail){
        Data param = new Data.Builder()
                .putString("param", "IntroducirHobbies")
                .putBoolean("leer", leer)
                .putBoolean("deporte", deporte)
                .putBoolean("fiesta", fiesta)
                .putBoolean("cine", cine)
                .putBoolean("otro", otro)
                .putString("mail",mail).build();
        Log.d("Prueba Hobbies",""+ param);
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
