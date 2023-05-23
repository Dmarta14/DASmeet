package com.example.dasmeet;

import static android.Manifest.permission.CAMERA;
import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PerfilFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PerfilFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private EditText etUsername;
    private ActivityResultLauncher<Intent> imageCaptureLauncher;
    private ActivityResultLauncher<String> requestPermissionLauncher;

    private ImageView imageView;


    public PerfilFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PerfilFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PerfilFragment newInstance(String param1, String param2) {
        PerfilFragment fragment = new PerfilFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_perfil, container, false);


        return view;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



        ImageButton imageButton = view.findViewById(R.id.imagegustos);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(view);
                navController.navigate(R.id.action_PerfilFragment_to_gustosFragment);

            }
        });
        ImageButton imageButton2 = view.findViewById(R.id.imagedatos);
        imageButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(view);
                navController.navigate(R.id.action_PerfilFragment_to_datosFragment);

            }
        });
        requestPermissionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                isGranted -> {
                    if (isGranted) {
                        abrirCamara(view);
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
                abrirCamara(view);
            }
        });
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
                        MediaStore.Images.Media.insertImage(getActivity().getContentResolver(), bitmap,
                                imageFileName, null);

                        // Transform the photo to a Base64 String and compress it
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream);
                        byte[] byteArray = stream.toByteArray();
                        String photo64 = Base64.encodeToString(byteArray,Base64.DEFAULT);

                        FileUtils fileUtils = new FileUtils();
                        String mail = fileUtils.readFile(getContext(), "config.txt");

                        // HTTP request to save the photo to database
                        String url = "http://" + "192.168.1.116" + ":3005/insertarFoto";
                        JSONObject requestBody = new JSONObject();

                        try {
                            requestBody.put("mail", mail);
                            requestBody.put("image", photo64);

                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }

                        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,
                                url, requestBody, response -> {
                            Log.d("PA", "SUCCESS");
                        }, error -> {
                            Log.e("PA", "ERROR", error);
                        });

                        RequestQueue queue = Volley.newRequestQueue(getContext());
                        queue.add(request);
                    }
                });


    }

    public void abrirCamara(View view){
        imageView = view.findViewById(R.id.imageView);
        // Create the registerForActivityResult to get the data after taking the photo


        FileUtils fileUtils = new FileUtils();
        String mail = fileUtils.readFile(getContext(), "config.txt");

        String url = "http://" + "192.168.1.116" + ":3005/insertarFoto";
        JSONObject requestBody = new JSONObject();

        try {

            requestBody.put("mail", mail);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url,
                requestBody, response -> {
            try {
                if (!response.get("result").equals("null")) {
                    // The photo exists
                    String image64 = response.getString("result");
                    byte[] b = Base64.decode(image64, Base64.DEFAULT);
                    Bitmap bitmap = BitmapFactory.decodeByteArray(b,0,
                            b.length);
                    Bitmap rescaledImage = adjustImageSize(bitmap);
                    imageView.setImageBitmap(rescaledImage);
                } else {
                    // The photo doesn't exist
                    takeAPhoto();
                }
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }, error -> {
            Log.e("PA", "ERROR", error);
        });

        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(request);

        imageView.setOnClickListener(c -> {
            takeAPhoto();
        });
    }



    // Adjust the image size to be bigger than the one taken
    private Bitmap adjustImageSize(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int length = bitmap.getHeight();

        int newSize = 800;
        float scaleWidth = ((float) newSize/width);
        float scaleLength = ((float) newSize/length);

        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleLength);

        return Bitmap.createBitmap(bitmap, 0,0, width, length, matrix, true);
    }

    // Starts the camera to take a photo
    private void takeAPhoto() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            imageCaptureLauncher.launch(takePictureIntent);
        }
    }




}