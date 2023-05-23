package com.example.dasmeet;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

public class FormularioGustos extends AppCompatActivity {


    CheckBox hombre,mujer,otroSexo,marrones,azules,verdes,rubio,castano,moreno,otroPelo,gracioso,alegre,simpatico,borde,cabezon,humilde,fiel,impuntual,carinoso,leer,deporte,fiesta,cine,otro;

    boolean bhombre,bmujer,botroSexo,bmarrones,bazules,bverdes,brubios,bcastano,bmoreno,botroPelo,bgracioso,balegre,bsimpatico,bborde,bcabezon,bhumilde,bfiel,bimpuntual,bcarinoso,bleer,bdeporte,bfiesta,bcine,botro = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario_gustos);
        hombre=findViewById(R.id.opcion1);
        mujer=findViewById(R.id.opcion2);
        otroSexo=findViewById(R.id.opcion3);
        marrones=findViewById(R.id.opcion4);
        azules=findViewById(R.id.opcion5);
        verdes=findViewById(R.id.opcion6);
        rubio=findViewById(R.id.opcion7);
        castano=findViewById(R.id.opcion8);
        moreno=findViewById(R.id.opcion9);
        otroPelo=findViewById(R.id.opcion10);
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

        FileUtils fUtils =new FileUtils();
        String mail = fUtils.readFile(getApplicationContext(), "config.txt");
        Button siguiente = findViewById(R.id.Siguiente);
        siguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hombre.isChecked()){
                    bhombre=true;
                }
                if(mujer.isChecked()){
                    bmujer=true;
                }
                if (otroSexo.isChecked()){
                    botroSexo=true;
                }

                anadirGustosSexo(bhombre,bmujer,botroSexo,mail);

                if (marrones.isChecked()){
                    bmarrones=true;
                }
                if(azules.isChecked()){
                    bazules=true;
                }
                if (verdes.isChecked()){
                    bverdes=true;
                }
                anadirGustosOjo(bmarrones,bazules,bverdes,mail);

                if (rubio.isChecked()){
                    brubios=true;
                }
                if(castano.isChecked()){
                    bcastano=true;
                }
                if (moreno.isChecked()){
                    bmoreno=true;
                }
                if (otroPelo.isChecked()){
                    botroPelo=true;
                }
                anadirGustosPelo(brubios,bcastano,bmoreno,botroPelo,mail);

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





                Toast.makeText(getApplicationContext(), "Regitro completado", Toast.LENGTH_LONG).show();

                Intent intent = new Intent(v.getContext(),inicio_sesion.class);
                startActivity(intent);
            }
        });
    }

   public void anadirGustosOjo(Boolean marron, Boolean azul, Boolean verde, String mail){
       Data param = new Data.Builder()
               .putString("param", "SeleccionarGustoOjo")
               .putBoolean("marron", marron)
               .putBoolean("azul", azul)
               .putBoolean("verde", verde)
               .putString("mail",mail).build();
       OneTimeWorkRequest oneTimeWorkRequest = new OneTimeWorkRequest.Builder(BD.class).setInputData(param).build();
       WorkManager.getInstance(FormularioGustos.this).enqueue(oneTimeWorkRequest);
       WorkManager.getInstance(FormularioGustos.this).getWorkInfoByIdLiveData(oneTimeWorkRequest.getId()).observe(FormularioGustos.this, new Observer<WorkInfo>() {
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
    public void anadirGustosSexo(Boolean hombre, Boolean mujer, Boolean otro, String mail){
        Data param = new Data.Builder()
                .putString("param", "SeleccionarGustoSexo")
                .putBoolean("hombre", hombre)
                .putBoolean("mujer", mujer)
                .putBoolean("otroSexo", otro)
                .putString("mail",mail).build();
        OneTimeWorkRequest oneTimeWorkRequest = new OneTimeWorkRequest.Builder(BD.class).setInputData(param).build();
        WorkManager.getInstance(FormularioGustos.this).enqueue(oneTimeWorkRequest);
        WorkManager.getInstance(FormularioGustos.this).getWorkInfoByIdLiveData(oneTimeWorkRequest.getId()).observe(FormularioGustos.this, new Observer<WorkInfo>() {
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
    public void anadirGustosPelo(Boolean rubio, Boolean castano,Boolean moreno, Boolean otro, String mail){
        Data param = new Data.Builder()
                .putString("param", "SeleccionarGustoPelo")
                .putBoolean("rubio", rubio)
                .putBoolean("castano", castano)
                .putBoolean("moreno",moreno)
                .putBoolean("otroPelo", otro)
                .putString("mail",mail).build();
        OneTimeWorkRequest oneTimeWorkRequest = new OneTimeWorkRequest.Builder(BD.class).setInputData(param).build();
        WorkManager.getInstance(FormularioGustos.this).enqueue(oneTimeWorkRequest);
        WorkManager.getInstance(FormularioGustos.this).getWorkInfoByIdLiveData(oneTimeWorkRequest.getId()).observe(FormularioGustos.this, new Observer<WorkInfo>() {
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
                .putString("param", "IntroducirPersonalidadGusto")
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
        WorkManager.getInstance(FormularioGustos.this).enqueue(oneTimeWorkRequest);
        WorkManager.getInstance(FormularioGustos.this).getWorkInfoByIdLiveData(oneTimeWorkRequest.getId()).observe(FormularioGustos.this, new Observer<WorkInfo>() {
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
                .putString("param", "IntroducirHobbiesGusto")
                .putBoolean("leer", leer)
                .putBoolean("deporte", deporte)
                .putBoolean("fiesta", fiesta)
                .putBoolean("cine", cine)
                .putBoolean("otro", otro)
                .putString("mail",mail).build();
        Log.d("Prueba Hobbies",""+ param);
        OneTimeWorkRequest oneTimeWorkRequest = new OneTimeWorkRequest.Builder(BD.class).setInputData(param).build();
        WorkManager.getInstance(FormularioGustos.this).enqueue(oneTimeWorkRequest);
        WorkManager.getInstance(FormularioGustos.this).getWorkInfoByIdLiveData(oneTimeWorkRequest.getId()).observe(FormularioGustos.this, new Observer<WorkInfo>() {
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

