package com.example.dasmeet;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class TimerReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // Aquí puedes implementar la lógica para finalizar la conversación
        // Puedes mostrar una notificación, guardar el estado de la conversación, etc.

        Toast.makeText(context, "La conversación ha terminado", Toast.LENGTH_SHORT).show();
    }
}
