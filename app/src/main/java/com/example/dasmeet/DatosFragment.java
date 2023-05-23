package com.example.dasmeet;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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

public class DatosFragment extends Fragment {
    RadioGroup sexo,pelo,ojos;
    CheckBox gracioso,alegre,simpatico,borde,cabezon,humilde,fiel,impuntual,carinoso,leer,deporte,fiesta,cine,otro;
    int selectSexo,selectPelo,selectOjos;
    boolean bgracioso,balegre,bsimpatico,bborde,bcabezon,bhumilde,bfiel,bimpuntual,bcarinoso,bleer,bdeporte,bfiesta,bcine,botro = false;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_datos, container, false);

        ImageView imagenAtras = view.findViewById(R.id.Cancelar);
        imagenAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(v);
                navController.navigate(R.id.action_datosFragment_to_perfilFragment);
            }
        });


        gracioso=view.findViewById(R.id.opcion11);
        alegre=view.findViewById(R.id.opcion12);
        simpatico=view.findViewById(R.id.opcion13);
        borde=view.findViewById(R.id.opcion14);
        cabezon=view.findViewById(R.id.opcion15);
        humilde=view.findViewById(R.id.opcion16);
        fiel=view.findViewById(R.id.opcion17);
        impuntual=view.findViewById(R.id.opcion18);
        carinoso=view.findViewById(R.id.opcion19);
        leer=view.findViewById(R.id.opcion20);
        deporte=view.findViewById(R.id.opcion21);
        fiesta=view.findViewById(R.id.opcion22);
        cine=view.findViewById(R.id.opcion23);
        otro=view.findViewById(R.id.opcion24);


        Button guardar = view.findViewById(R.id.Guardar);
        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sexo = view.findViewById(R.id.radio_group_respuestas1);
                selectSexo = sexo.getCheckedRadioButtonId();
                Log.d("Prueba", "" + selectSexo);
                FileUtils fUtils =new FileUtils();
                String mail = fUtils.readFile(getContext(), "config.txt");
                if(selectSexo != -1){
                    RadioButton sexoSelecionado=view.findViewById(selectSexo);
                    String respuestaSelecionadaSexo = sexoSelecionado.getText().toString();

                    ojos = view.findViewById(R.id.radio_group_respuestas2);
                    selectOjos = ojos.getCheckedRadioButtonId();
                    Log.d("Prueba Ojo" , "" + selectOjos);
                    if(selectOjos != -1){
                        RadioButton ojosSelecionado= view.findViewById(selectOjos);
                        Log.d("Prueba Ojo" , "" + ojosSelecionado);
                        String respuestaSelecionadaOjo = ojosSelecionado.getText().toString();
                        Log.d("Prueba Ojo" , "" + respuestaSelecionadaOjo);

                        pelo = view.findViewById(R.id.radio_group_respuestas3);
                        selectPelo = pelo.getCheckedRadioButtonId();

                        if(selectPelo != -1){
                            RadioButton peloSelecionado= view.findViewById(selectPelo);
                            String respuestaSelecionadaPelo = peloSelecionado.getText().toString();

                            anadirDatos(respuestaSelecionadaSexo,respuestaSelecionadaOjo,respuestaSelecionadaPelo,mail);
                        }

                        else {
                            Toast.makeText(getContext(), "Debes seleccionar un color de pelo en la pregunta 3", Toast.LENGTH_LONG).show();
                        }
                    }

                    else {
                        Toast.makeText(getContext(), "Debes seleccionar un color de ojos en la pregunta 2", Toast.LENGTH_LONG).show();
                    }
                }
                else {
                    Toast.makeText(getContext(), "Debes seleccionar un sexo en la pregunta 1", Toast.LENGTH_LONG).show();
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
                NavController navController = Navigation.findNavController(v);
                navController.navigate(R.id.action_datosFragment_to_perfilFragment);
            }
        });
        return view;
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
        WorkManager.getInstance(getContext()).enqueue(oneTimeWorkRequest);
        WorkManager.getInstance(getContext()).getWorkInfoByIdLiveData(oneTimeWorkRequest.getId()).observe(this, new Observer<WorkInfo>() {
            @Override
            public void onChanged(WorkInfo workInfo) {
                if (workInfo != null && workInfo.getState().isFinished()) {
                    if (workInfo.getState() != WorkInfo.State.SUCCEEDED) {
                        Toast.makeText(getContext(), "Error", Toast.LENGTH_LONG).show();
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
        WorkManager.getInstance(getContext()).enqueue(oneTimeWorkRequest);
        WorkManager.getInstance(getContext()).getWorkInfoByIdLiveData(oneTimeWorkRequest.getId()).observe(this, new Observer<WorkInfo>() {
            @Override
            public void onChanged(WorkInfo workInfo) {
                if (workInfo != null && workInfo.getState().isFinished()) {
                    if (workInfo.getState() != WorkInfo.State.SUCCEEDED) {
                        Toast.makeText(getContext(), "Error", Toast.LENGTH_LONG).show();
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
        WorkManager.getInstance(getContext()).enqueue(oneTimeWorkRequest);
        WorkManager.getInstance(getContext()).getWorkInfoByIdLiveData(oneTimeWorkRequest.getId()).observe(this, new Observer<WorkInfo>() {
            @Override
            public void onChanged(WorkInfo workInfo) {
                if (workInfo != null && workInfo.getState().isFinished()) {
                    if (workInfo.getState() != WorkInfo.State.SUCCEEDED) {
                        Toast.makeText(getContext(), "Error", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }
}



