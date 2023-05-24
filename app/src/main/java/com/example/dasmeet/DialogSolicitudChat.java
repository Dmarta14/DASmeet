package com.example.dasmeet;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class DialogSolicitudChat extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("x quiere conocerte");
        LayoutInflater inflater=getActivity().getLayoutInflater();
        View elaspecto= inflater.inflate(R.layout.dialog_solicitud_chat,null);
        builder.setView(elaspecto);
        TextView fraseLigar= elaspecto.findViewById(R.id.fraseLigar);
        fraseLigar.setText("¿En serio vas a abandonar la aplicación?");
        return builder.create();
    }
}
