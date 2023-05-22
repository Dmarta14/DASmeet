package com.example.dasmeet;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
public class BD extends Worker{
    private static final String IP = "161.35.34.173";

    public static String getIp() {
        return IP;
    }

    public BD (@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    public Result doWork(){
        String action = getInputData().getString("param");
        assert action != null;
        switch (action){
            case "Registrar":{

                /*
                 *  HTTP Request to insert a user into Usuario table
                 */
                String dir = "http://192.168.0.22:3005/create";
                HttpURLConnection urlConnection;

                String usuario = getInputData().getString("nombre");
                String pass = getInputData().getString("password");
                String fecha = getInputData().getString("fechana");
                String mail =  getInputData().getString("mail");
                try {
                    URL dest =new URL(dir);
                    urlConnection = (HttpURLConnection) dest.openConnection();
                    urlConnection.setConnectTimeout(5000);
                    urlConnection.setReadTimeout(5000);
                    urlConnection.setRequestMethod("POST");
                    urlConnection.setRequestProperty("Content-Type","application/json");
                    JSONObject paramJson = new JSONObject();
                    paramJson.put("Usuario", usuario);
                    paramJson.put("Password", pass);
                    paramJson.put("FechaNacimiento",fecha);
                    paramJson.put("Mail",mail);
                    PrintWriter out = new PrintWriter(urlConnection.getOutputStream());
                    out.print(paramJson.toString());
                    out.close();
                    int statusCode = urlConnection.getResponseCode();
                    if (statusCode == 200) {
                        BufferedInputStream inputStream =
                                new BufferedInputStream(urlConnection.getInputStream());
                        BufferedReader bufferedReader =
                                new BufferedReader(new InputStreamReader(inputStream,
                                        "UTF-8"));
                        String line;
                        StringBuilder result = new StringBuilder();
                        while ((line = bufferedReader.readLine()) != null) {
                            result.append(line);
                        }
                        inputStream.close();

                        JSONParser parser = new JSONParser();
                        JSONObject json = (JSONObject) parser.parse(result.toString());
                        Log.i("JSON", "doWork: " + json);

                        Boolean success = (Boolean) json.get("success");
                        Data.Builder b = new Data.Builder();
                        return Result.success(b.putBoolean("exito", success).build());
                    }
                } catch (Exception e) {
                    Log.e("EXCEPTION", "doWork: ", e);
                    return Result.failure();
                }
                break;
            }
            case "ExisteUsuarioCorreo":{

                /*
                 *  HTTP Request to insert a user into Usuario table
                 */

                HttpURLConnection urlConnection;

                String mail =  getInputData().getString("mail");
                String dir = "http://192.168.0.22:3005/existeUsuarioCorreo?mail=" + mail;
                try {
                    URL dest =new URL(dir);
                    urlConnection = (HttpURLConnection) dest.openConnection();
                    urlConnection.setConnectTimeout(5000);
                    urlConnection.setReadTimeout(5000);
                    urlConnection.setRequestMethod("GET");
                    int statusCode = urlConnection.getResponseCode();
                    String code =String.valueOf(statusCode);
                    Log.d("Prueba",code);
                    if (statusCode == 200) {
                        BufferedInputStream inputStream = new BufferedInputStream(urlConnection.getInputStream());
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                        String line;
                        StringBuilder result = new StringBuilder();
                        while ((line = bufferedReader.readLine()) != null) {
                            result.append(line);
                        }
                        inputStream.close();
                        JSONParser parser = new JSONParser();
                        JSONObject json = (JSONObject) parser.parse(result.toString());
                        Log.i("JSON", "doWork: " + json);

                        Data.Builder b = new Data.Builder();
                        return Result.success(b.putBoolean("existe",(boolean) json.get("success")).build());
                    }
                } catch (Exception e) {
                    Log.e("EXCEPTION", "doWork: ", e);
                    return Result.failure();
                }
                break;
            }
        }
        return Result.success();
    }
}

