package com.example.dasmeet;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import java.util.Calendar;
import java.util.regex.Pattern;
public class Registro extends AppCompatActivity {

    EditText usu,contraseña1,contraseña2,fechanacimiento;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        usu =findViewById (R.id.Usuario);
        contraseña1 = findViewById (R.id.PasswordInicial);
        contraseña2 = findViewById (R.id.PasswordConfirmada);
        fechanacimiento= findViewById(R.id.et_nacimiento);
        Button volver = findViewById(R.id.Cancelar);
        volver.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),inicio_sesion.class);
                startActivity(intent);
            }
        });

        Button siguiente = findViewById(R.id.Siguiente);
        siguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usuario = usu.getText().toString();
                String contra1 = contraseña1.getText().toString ();
                String contraConfirmada = contraseña2.getText().toString ();
                String fechaNa = fechanacimiento.getText().toString();

                //validaciones de los campos
                if (usuario.isEmpty() || contra1.isEmpty() || contraConfirmada.isEmpty() || fechaNa.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Deben estar todos los campos rellenados", Toast.LENGTH_LONG).show();
                }else{
                    if (contra1.length () < 8) {
                        Toast.makeText(getApplicationContext(), "La contraseña debe tener 8 dígitos como mínimo", Toast.LENGTH_LONG).show();
                    }else {
                            if (validarpassword (contra1) == false){
                            Toast.makeText(getApplicationContext(), "La contraseña debe tener números y letras", Toast.LENGTH_LONG).show();
                            }else{
                                if (contra1.equals (contraConfirmada)) {
                                    Toast.makeText(getApplicationContext(), "Usuario valido", Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(v.getContext(),FormularioGustos.class);
                                    intent.putExtra("Usuario",usuario);
                                    intent.putExtra("Password",contra1);
                                    intent.putExtra("FechaNacimiento", fechaNa);
                                    startActivity(intent);
                                }else {
                                    Toast.makeText(getApplicationContext(), "Contraseñas incorrectas", Toast.LENGTH_LONG).show();
                                }
                            }

                    }
                }

            }
        });

        }
    public boolean validarpassword(String password) {
        boolean numeros = false;
        boolean letras = false;
        for (int x = 0; x < password.length(); x++) {
            char c = password.charAt(x);
            // Si no está entre a y z, ni entre A y Z, ni es un espacio
            if (((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z')  || c =='ñ' || c=='Ñ'
                    || c=='á' || c=='é' || c=='í' || c=='ó' || c=='ú'
                    || c=='Á' || c=='É' || c=='Í' || c=='Ó' || c=='Ú')) {
                letras = true;
            }
            if ((c >= '0' && c <= '9') ) {
                numeros = true;
            }

        }
        if (numeros == true && letras ==true){
            return true;
        }
        return false;
    }
    //Utilizamos un fragment para la eleccion de la fecha de nacimiento
    public void showDatePickerDialog(View v) {
        DatePickerFragment newFragment = DatePickerFragment.newInstance((datePicker, year, month, day) -> {
            // +1 ya que Enero es 0
            Calendar selectedDate = Calendar.getInstance();
            selectedDate.set(Calendar.YEAR, year);
            selectedDate.set(Calendar.MONTH, month);
            selectedDate.set(Calendar.DAY_OF_MONTH, day);

            // Crear un objeto Calendar con la fecha actual
            Calendar currentDate = Calendar.getInstance();

            // Calcular la diferencia entre la fecha actual y la fecha seleccionada
            int age = currentDate.get(Calendar.YEAR) - selectedDate.get(Calendar.YEAR);
            if (currentDate.get(Calendar.MONTH) < selectedDate.get(Calendar.MONTH)) {
                age--;
            } else if (currentDate.get(Calendar.MONTH) == selectedDate.get(Calendar.MONTH)
                    && currentDate.get(Calendar.DAY_OF_MONTH) < selectedDate.get(Calendar.DAY_OF_MONTH)) {
                age--;
            }

            // Comprobar si el usuario es mayor de edad
            if (age >= 18) {
                // El usuario es mayor de edad, hacer lo que sea necesario
                final String diaSeleccionado = twoDigits(day) + "/" + twoDigits(month + 1) + "/" + year;
                fechanacimiento.setText(diaSeleccionado);
            } else {
                Toast.makeText(getApplicationContext(), "El usuario debe ser mayor de edad", Toast.LENGTH_LONG).show();
            }


        });

        newFragment.show(getSupportFragmentManager(), "datePicker");
    }
    private String twoDigits(int n) {
        return (n<=9) ? ("0"+n) : String.valueOf(n);
    }
    }