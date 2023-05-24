package com.example.dasmeet;

import static android.Manifest.permission.CAMERA;
import static android.app.Activity.RESULT_OK;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.dasmeet.utils.FileUtils;

import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class PerfilFragment extends Fragment {

    private TextView etcorreo;
    private EditText etcontra,etdesc;
    private ActivityResultLauncher<Intent> imageCaptureLauncher;
    private ActivityResultLauncher<String> requestPermissionLauncher;

    private ImageView imageView;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_perfil, container, false);
        ((MainActivity) getActivity()).changeToolbar(true);
        imageCaptureLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        Intent data = result.getData();
                        Bundle extras = data.getExtras();
                        Bitmap bitmap = (Bitmap) extras.get("data");

                        // Rescale the image
                        Bitmap rescaledImage = adjustImageSize(bitmap);
                        imageView.setImageBitmap(rescaledImage);

                        // Set a name to the photo and save it to internal storage
                        String imageFileName =
                                "IMG_" + new SimpleDateFormat("yyyyMMdd_HHmmss")
                                        .format(new Date());
                        File directory =
                                getContext().getApplicationContext().getFilesDir();
                        File imageFile = new File(directory, imageFileName);

                        FileOutputStream outputStream = null;
                        try {
                            outputStream = new FileOutputStream(imageFile);
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100,
                                    outputStream);
                            outputStream.close();
                        } catch (FileNotFoundException e) {
                            throw new RuntimeException(e);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }


                        // Transform the photo to a Base64 String and compress it
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream);
                        byte[] byteArray = stream.toByteArray();
                        String photo64 = Base64.encodeToString(byteArray, Base64.DEFAULT);


                        // HTTP request to save the photo to database
                        String url = BD.getIp() + "/insertarFoto";
                        JSONObject requestBody = new JSONObject();
                        FileUtils fileUtils = new FileUtils();
                        String mail = fileUtils.readFile(getContext(), "config.txt");


                        try {
                            requestBody.put("mail", mail);
                            requestBody.put("Foto", photo64);

                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }

                        JsonObjectRequest request =
                                new JsonObjectRequest(Request.Method.POST,
                                url, requestBody, response -> {
                            Log.d("PA", "SUCCESS");
                        }, error -> {
                            Log.e("PA", "ERROR", error);
                        });

                        RequestQueue queue = Volley.newRequestQueue(getContext());
                        queue.add(request);
                    }
                });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ImageButton imageButton = view.findViewById(R.id.imagegustos);
        imageButton.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(view);
            navController.navigate(R.id.action_PerfilFragment_to_gustosFragment);

        });
        ImageButton imageButton2 = view.findViewById(R.id.imagedatos);
        imageButton2.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(view);
            navController.navigate(R.id.action_PerfilFragment_to_datosFragment);

        });
        FileUtils fileUtils = new FileUtils();
        String mail = fileUtils.readFile(getContext(), "config.txt");

        etcorreo = view.findViewById(R.id.textView1);
        etcorreo.setText(mail);
        obtenerDes(view, mail);

        requestPermissionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                isGranted -> {
                    if (isGranted) {
                        takeAPhoto();
                    } else {

                        Toast.makeText(getActivity(),
                                "mal",
                                Toast.LENGTH_SHORT).show();
                    }


                });
        Button photobutton = getView().findViewById(R.id.botonCamara);
        photobutton.setOnClickListener(c -> {
            if (ContextCompat.checkSelfPermission(getContext(), CAMERA) !=
                    PackageManager.PERMISSION_GRANTED) {
                requestPermissionLauncher.launch(CAMERA);
            } else {
                takeAPhoto();
            }
        });

        recogerFoto(view);
        ImageView guardar = getView().findViewById(R.id.imageViewguardar);
        guardar.setOnClickListener(v -> {
            etcontra = getView().findViewById(R.id.editTextcontra);
            String password = etcontra.getText().toString();
            etdesc = getView().findViewById(R.id.editTextdesc);
            String desc = etdesc.getText().toString();

            String url = "http://" + "192.168.1.116" + ":3005/modificarContra";
            JSONObject requestBody = new JSONObject();

                try {

                    requestBody.put("password", password);
                    requestBody.put("mail", mail);
                    requestBody.put("descripcion", desc);


                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

                JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url,
                        requestBody, response -> {
                    try {
                        //Log.e("titosss", "aaaa"+ response.toString());
                        if (response.get("success").equals(true)) {
                            Toast.makeText(getContext(), "Guardado", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }, error -> {
                    Log.e("PA", "ERROR", error);
                });

                RequestQueue queue = Volley.newRequestQueue(getContext());
                queue.add(request);

            });

    }

    public void recogerFoto(View view) {

        imageView = view.findViewById(R.id.imageView);
        // Create the registerForActivityResult to get the data after taking the photo


        FileUtils fileUtils = new FileUtils();
        String mail = fileUtils.readFile(getContext(), "config.txt");

        String url = BD.getIp() + "/buscarFoto";
        JSONObject requestBody = new JSONObject();

        try {

            requestBody.put("mail", mail);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url,
                requestBody, response -> {
            try {
                //Log.e("titosss", "aaaa"+ response.toString());
                if (response.get("success").equals(true)) {
                    // The photo exists
                    String image64 = response.getString("Foto");
                    byte[] b = Base64.decode(image64, Base64.DEFAULT);
                    Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0,
                            b.length);
                    Bitmap rescaledImage = adjustImageSize(bitmap);
                    imageView.setImageBitmap(rescaledImage);
                }
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }, error -> {
            Log.e("PA", "ERROR", error);
        });

        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(request);

    }


    // Adjust the image size to be bigger than the one taken
    private Bitmap adjustImageSize(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int length = bitmap.getHeight();

        int newSize = 800;
        float scaleWidth = ((float) newSize / width);
        float scaleLength = ((float) newSize / length);

        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleLength);

        return Bitmap.createBitmap(bitmap, 0, 0, width, length, matrix, true);
    }

    // Starts the camera to take a photo
    private void takeAPhoto() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            imageCaptureLauncher.launch(takePictureIntent);
        }
    }

    private void obtenerDes(View view, String mail) {
        etdesc = view.findViewById(R.id.editTextdesc);
        etcontra = view.findViewById(R.id.editTextcontra);
        String url = "http://" + "192.168.1.116" + ":3005/buscarcontraydesc";
        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("mail", mail);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, requestBody,
                response -> {
                    try {
                        if (response.getBoolean("success")) {
                            String desc = response.getString("Descripcion");
                            etdesc.setText(desc);


                        }
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                },
                error -> {
                    Log.e("PA", "ERROR", error);
                });

        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(request);
    }



}