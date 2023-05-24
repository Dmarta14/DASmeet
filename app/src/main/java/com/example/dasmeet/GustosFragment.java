package com.example.dasmeet;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import com.example.dasmeet.utils.FileUtils;

public class GustosFragment extends Fragment {
    CheckBox hombre, mujer, otroSexo, marrones, azules, verdes, rubio, castano, moreno,
            otroPelo, gracioso, alegre, simpatico, borde, cabezon, humilde, fiel,
            impuntual, carinoso, leer, deporte, fiesta, cine, otro;

    boolean bhombre, bmujer, botroSexo, bmarrones, bazules, bverdes, brubios, bcastano,
            bmoreno, botroPelo, bgracioso, balegre, bsimpatico, bborde, bcabezon,
            bhumilde, bfiel, bimpuntual, bcarinoso, bleer, bdeporte, bfiesta, bcine,
            botro = false;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gustos, container, false);

        ImageView imagenAtras = view.findViewById(R.id.Cancelar);
        imagenAtras.setOnClickListener(v -> {
            replaceFragment(new PerfilFragment());
        });
        hombre = view.findViewById(R.id.opcion1);
        mujer = view.findViewById(R.id.opcion2);
        otroSexo = view.findViewById(R.id.opcion3);
        marrones = view.findViewById(R.id.opcion4);
        azules = view.findViewById(R.id.opcion5);
        verdes = view.findViewById(R.id.opcion6);
        rubio = view.findViewById(R.id.opcion7);
        castano = view.findViewById(R.id.opcion8);
        moreno = view.findViewById(R.id.opcion9);
        otroPelo = view.findViewById(R.id.opcion10);
        gracioso = view.findViewById(R.id.opcion11);
        alegre = view.findViewById(R.id.opcion12);
        simpatico = view.findViewById(R.id.opcion13);
        borde = view.findViewById(R.id.opcion14);
        cabezon = view.findViewById(R.id.opcion15);
        humilde = view.findViewById(R.id.opcion16);
        fiel = view.findViewById(R.id.opcion17);
        impuntual = view.findViewById(R.id.opcion18);
        carinoso = view.findViewById(R.id.opcion19);
        leer = view.findViewById(R.id.opcion20);
        deporte = view.findViewById(R.id.opcion21);
        fiesta = view.findViewById(R.id.opcion22);
        cine = view.findViewById(R.id.opcion23);
        otro = view.findViewById(R.id.opcion24);


        FileUtils fUtils = new FileUtils();
        String mail = fUtils.readFile(getContext(), "config.txt");
        ImageView siguiente = view.findViewById(R.id.Guardar);
        siguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hombre.isChecked()) {
                    bhombre = true;
                }
                if (mujer.isChecked()) {
                    bmujer = true;
                }
                if (otroSexo.isChecked()) {
                    botroSexo = true;
                }

                anadirGustosSexo(bhombre, bmujer, botroSexo, mail);

                if (marrones.isChecked()) {
                    bmarrones = true;
                }
                if (azules.isChecked()) {
                    bazules = true;
                }
                if (verdes.isChecked()) {
                    bverdes = true;
                }
                anadirGustosOjo(bmarrones, bazules, bverdes, mail);

                if (rubio.isChecked()) {
                    brubios = true;
                }
                if (castano.isChecked()) {
                    bcastano = true;
                }
                if (moreno.isChecked()) {
                    bmoreno = true;
                }
                if (otroPelo.isChecked()) {
                    botroPelo = true;
                }
                anadirGustosPelo(brubios, bcastano, bmoreno, botroPelo, mail);

                if (gracioso.isChecked()) {
                    bgracioso = true;
                }
                if (alegre.isChecked()) {
                    balegre = true;
                }
                if (simpatico.isChecked()) {
                    bsimpatico = true;
                }
                if (borde.isChecked()) {
                    bborde = true;
                }
                if (cabezon.isChecked()) {
                    bcabezon = true;
                }
                if (humilde.isChecked()) {
                    bhumilde = true;
                }
                if (fiel.isChecked()) {
                    bfiel = true;
                }
                if (impuntual.isChecked()) {
                    bimpuntual = true;
                }
                if (carinoso.isChecked()) {
                    bcarinoso = true;
                }

                anardirPersonalidad(bgracioso, balegre, bsimpatico, bborde, bcabezon,
                        bhumilde, bfiel, bimpuntual, bcarinoso, mail);

                if (leer.isChecked()) {
                    bleer = true;
                }
                if (deporte.isChecked()) {
                    bdeporte = true;
                }
                if (fiesta.isChecked()) {
                    bfiesta = true;
                }
                if (cine.isChecked()) {
                    bcine = true;
                }
                if (otro.isChecked()) {
                    botro = true;
                }

                anadirHobbies(bleer, bdeporte, bfiesta, bcine, botro, mail);


                Toast.makeText(getContext(), "Regitro completado", Toast.LENGTH_LONG).show();

                replaceFragment(new PerfilFragment());
            }
        });

        return view;
    }

    public void anadirGustosOjo(Boolean marron, Boolean azul, Boolean verde,
                                String mail) {
        Data param = new Data.Builder()
                .putString("param", "ModificarGustoOjo")
                .putBoolean("marron", marron)
                .putBoolean("azul", azul)
                .putBoolean("verde", verde)
                .putString("mail", mail).build();
        OneTimeWorkRequest oneTimeWorkRequest =
                new OneTimeWorkRequest.Builder(BD.class).setInputData(param).build();
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

    public void anadirGustosSexo(Boolean hombre, Boolean mujer, Boolean otro,
                                 String mail) {
        Data param = new Data.Builder()
                .putString("param", "ModificarGustoSexo")
                .putBoolean("hombre", hombre)
                .putBoolean("mujer", mujer)
                .putBoolean("otroSexo", otro)
                .putString("mail", mail).build();
        OneTimeWorkRequest oneTimeWorkRequest =
                new OneTimeWorkRequest.Builder(BD.class).setInputData(param).build();
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

    public void anadirGustosPelo(Boolean rubio, Boolean castano, Boolean moreno,
                                 Boolean otro, String mail) {
        Data param = new Data.Builder()
                .putString("param", "ModificarGustoPelo")
                .putBoolean("rubio", rubio)
                .putBoolean("castano", castano)
                .putBoolean("moreno", moreno)
                .putBoolean("otroPelo", otro)
                .putString("mail", mail).build();
        OneTimeWorkRequest oneTimeWorkRequest =
                new OneTimeWorkRequest.Builder(BD.class).setInputData(param).build();
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

    public void anardirPersonalidad(Boolean gracioso, Boolean alegre, Boolean simpatico
            , Boolean borde, Boolean cabezon, Boolean humilde, Boolean fiel,
                                    Boolean impuntual, Boolean carinoso, String mail) {
        Data param = new Data.Builder()
                .putString("param", "ModificarPersonalidadGusto")
                .putBoolean("gracioso", gracioso)
                .putBoolean("alegre", alegre)
                .putBoolean("simpatico", simpatico)
                .putBoolean("borde", borde)
                .putBoolean("cabezon", cabezon)
                .putBoolean("humilde", humilde)
                .putBoolean("fiel", fiel)
                .putBoolean("imputual", impuntual)
                .putBoolean("carinoso", carinoso)
                .putString("mail", mail).build();
        OneTimeWorkRequest oneTimeWorkRequest =
                new OneTimeWorkRequest.Builder(BD.class).setInputData(param).build();
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

    public void anadirHobbies(Boolean leer, Boolean deporte, Boolean fiesta,
                              Boolean cine, Boolean otro, String mail) {
        Data param = new Data.Builder()
                .putString("param", "ModificarHobbiesGusto")
                .putBoolean("leer", leer)
                .putBoolean("deporte", deporte)
                .putBoolean("fiesta", fiesta)
                .putBoolean("cine", cine)
                .putBoolean("otro", otro)
                .putString("mail", mail).build();
        Log.d("Prueba Hobbies", "" + param);
        OneTimeWorkRequest oneTimeWorkRequest =
                new OneTimeWorkRequest.Builder(BD.class).setInputData(param).build();
        WorkManager.getInstance(getContext()).enqueue(oneTimeWorkRequest);
        WorkManager.getInstance(getContext()).getWorkInfoByIdLiveData(oneTimeWorkRequest.getId()).observe(this, workInfo -> {
            if (workInfo != null && workInfo.getState().isFinished()) {
                if (workInfo.getState() != WorkInfo.State.SUCCEEDED) {
                    Toast.makeText(getContext(), "Error", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.fragment_container, fragment);

        fragmentTransaction.commit();
    }
}
