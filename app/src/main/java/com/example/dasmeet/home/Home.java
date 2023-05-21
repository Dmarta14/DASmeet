package com.example.dasmeet.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.dasmeet.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Home extends Fragment {

    private ImageView imageView1;
    private TextView textViewName1;
    private TextView textViewDesc1;
    private ImageView imageView2;
    private TextView textViewName2;
    private TextView textViewDesc2;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        imageView1 = view.findViewById(R.id.imageView1);
        textViewName1 = view.findViewById(R.id.textViewName1);
        textViewDesc1 = view.findViewById(R.id.textViewDesc1);
        imageView2 = view.findViewById(R.id.imageView2);
        textViewName2 = view.findViewById(R.id.textViewName2);
        textViewDesc2 = view.findViewById(R.id.textViewDesc2);

        getFirstElements();

        return view;
    }

    private void getFirstElements() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.1.150:3005/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        MainInterface mainInterface = retrofit.create(MainInterface.class);
        Call<UserList> call = mainInterface.getUsers();

        call.enqueue(new Callback<>() {

            @Override
            public void onResponse(@NonNull Call<UserList> call,
                                   @NonNull Response<UserList> response) {
                if (response.isSuccessful() && response.body() != null) {

                    UserList userList = response.body();
                    for (int i = 0; i < userList.size(); i++) {
                        if (i % 2 == 0) {
                            textViewName1.setText(userList.get(i).getName());
                            textViewDesc1.setText(userList.get(i).getDesc());
                        } else {
                            textViewName2.setText(userList.get(i).getName());
                            textViewDesc2.setText(userList.get(i).getDesc());
                        }

                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<UserList> call, @NonNull Throwable t) {
                Log.i("Home", "onFailure: " + t + " " + call);
            }
        });
    }
}