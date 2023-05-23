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
                String token =  getInputData().getString("token");
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
                    paramJson.put("token",token);
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
                 *  HTTP Request to select a user to Usuario table
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
            case "IntroducirDatos":{

                /*
                 *  HTTP Request to insert a user into Datos table
                 */

                HttpURLConnection urlConnection;


                String dir = "http://192.168.0.22:3005/introducirDatos";
                String sexo =  getInputData().getString("sexo");
                String ojos =  getInputData().getString("ojo");
                String pelo =  getInputData().getString("pelo");
                String mail = getInputData().getString("mail");

                try {
                    URL dest =new URL(dir);
                    urlConnection = (HttpURLConnection) dest.openConnection();
                    urlConnection.setConnectTimeout(5000);
                    urlConnection.setReadTimeout(5000);
                    urlConnection.setRequestMethod("POST");
                    urlConnection.setRequestProperty("Content-Type","application/json");
                    JSONObject paramJson = new JSONObject();
                    paramJson.put("Sexo", sexo);
                    paramJson.put("Ojos", ojos);
                    paramJson.put("Pelo",pelo);
                    paramJson.put("cod",mail);
                    Log.d("Datos Prueba",""+ paramJson);
                    PrintWriter out = new PrintWriter(urlConnection.getOutputStream());
                    out.print(paramJson.toString());
                    out.close();
                    int statusCode = urlConnection.getResponseCode();
                    String code =String.valueOf(statusCode);
                    Log.d("Datos Prueba",code);
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
            case "IntroducirPersonalidad":{

                /*
                 *  HTTP Request to insert a user into Personalidad table
                 */

                HttpURLConnection urlConnection;


                String dir = "http://192.168.0.22:3005/introducirPersonalidad";
                boolean gracioso =  getInputData().getBoolean("gracioso",false);
                int intGracioso = gracioso ? 1 : 0;
                boolean alegre =  getInputData().getBoolean("alegre", false);
                int intAlegre= alegre ? 1 : 0;
                boolean simpatico =  getInputData().getBoolean("simpatico", false);
                int intSimpatico= simpatico ? 1 : 0;
                boolean borde = getInputData().getBoolean("borde",false);
                int intBorde = borde ? 1 : 0;
                boolean cabezon =  getInputData().getBoolean("cabezon",false);
                int intCabezon = cabezon ? 1 : 0;
                boolean humilde =  getInputData().getBoolean("humilde", false);
                int intHumilde = humilde ? 1 : 0;
                boolean fiel =  getInputData().getBoolean("fiel", false);
                int intFiel = fiel ? 1 : 0;
                boolean impuntual = getInputData().getBoolean("impuntual", false);
                int intImputual = impuntual ? 1 : 0;
                boolean carinoso =getInputData().getBoolean("carinoso", false);
                int intCarinoso = carinoso ? 1 : 0;
                String mail =getInputData().getString("mail");
                try {
                    URL dest =new URL(dir);
                    urlConnection = (HttpURLConnection) dest.openConnection();
                    urlConnection.setConnectTimeout(5000);
                    urlConnection.setReadTimeout(5000);
                    urlConnection.setRequestMethod("POST");
                    urlConnection.setRequestProperty("Content-Type","application/json");
                    JSONObject paramJson = new JSONObject();
                    paramJson.put("gracioso", intGracioso);
                    paramJson.put("alegre", intAlegre);
                    paramJson.put("simpatico",intSimpatico);
                    paramJson.put("borde",intBorde);
                    paramJson.put("cabezon", intCabezon);
                    paramJson.put("humilde", intHumilde);
                    paramJson.put("fiel",intFiel);
                    paramJson.put("impuntual",intImputual);
                    paramJson.put("carinoso",intCarinoso);
                    paramJson.put("cod",mail);
                    Log.d("Personalidad Prueba",""+ paramJson);
                    PrintWriter out = new PrintWriter(urlConnection.getOutputStream());
                    out.print(paramJson.toString());
                    out.close();
                    int statusCode = urlConnection.getResponseCode();
                    String code =String.valueOf(statusCode);
                    Log.d("Personalidad Prueba",code);
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

            case "IntroducirHobbies":{

                /*
                 *  HTTP Request to insert a user into Hobbies table
                 */

                HttpURLConnection urlConnection;


                String dir = "http://192.168.0.22:3005/introducirHobbies";
                boolean leer =  getInputData().getBoolean("leer",false);
                int intLeer = leer ? 1 : 0;
                boolean deporte =  getInputData().getBoolean("deporte", false);
                int intDeporte = deporte ? 1 : 0;
                boolean fiesta =  getInputData().getBoolean("fiesta", false);
                int intFieste = fiesta ? 1 : 0;
                boolean cine = getInputData().getBoolean("cine",false);
                int intCine = cine ? 1 : 0;
                boolean otro =  getInputData().getBoolean("otro",false);
                int intOtro = otro ? 1 : 0;
                String mail =getInputData().getString("mail");
                try {
                    URL dest =new URL(dir);
                    urlConnection = (HttpURLConnection) dest.openConnection();
                    urlConnection.setConnectTimeout(5000);
                    urlConnection.setReadTimeout(5000);
                    urlConnection.setRequestMethod("POST");
                    urlConnection.setRequestProperty("Content-Type","application/json");
                    JSONObject paramJson = new JSONObject();
                    paramJson.put("leer", intLeer);
                    paramJson.put("deporte", intDeporte);
                    paramJson.put("fiesta",intFieste);
                    paramJson.put("cine",intCine);
                    paramJson.put("otro", intOtro);
                    paramJson.put("cod",mail);
                    Log.d("Personalidad Hobbies",""+ paramJson);
                    PrintWriter out = new PrintWriter(urlConnection.getOutputStream());
                    out.print(paramJson.toString());
                    out.close();
                    int statusCode = urlConnection.getResponseCode();
                    String code =String.valueOf(statusCode);
                    Log.d("Hobbies Prueba",code);
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
            case "SeleccionarGustoSexo":{

                /*
                 *  HTTP Request to insert a user into Sexo table
                 */

                HttpURLConnection urlConnection;


                String dir = "http://192.168.0.22:3005/seleccionarGustoSexo";
                boolean hombre =  getInputData().getBoolean("hombre",false);
                int intHombre = hombre ? 1 : 0;
                boolean mujer =  getInputData().getBoolean("mujer", false);
                int intMujer = mujer ? 1 : 0;
                boolean otroSexo =  getInputData().getBoolean("otroSexo", false);
                int intOtroSexo = otroSexo ? 1 : 0;
                String mail =getInputData().getString("mail");
                try {
                    URL dest =new URL(dir);
                    urlConnection = (HttpURLConnection) dest.openConnection();
                    urlConnection.setConnectTimeout(5000);
                    urlConnection.setReadTimeout(5000);
                    urlConnection.setRequestMethod("POST");
                    urlConnection.setRequestProperty("Content-Type","application/json");
                    JSONObject paramJson = new JSONObject();
                    paramJson.put("hombre", intHombre);
                    paramJson.put("mujer", intMujer);
                    paramJson.put("otroSexo",intOtroSexo);
                    paramJson.put("cod",mail);
                    Log.d("Sexo",""+ paramJson);
                    PrintWriter out = new PrintWriter(urlConnection.getOutputStream());
                    out.print(paramJson.toString());
                    out.close();
                    int statusCode = urlConnection.getResponseCode();
                    String code =String.valueOf(statusCode);
                    Log.d("SEXO Prueba",code);
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

            case "SeleccionarGustoOjo":{

                /*
                 *  HTTP Request to insert a user into Sexo table
                 */

                HttpURLConnection urlConnection;


                String dir = "http://192.168.0.22:3005/seleccionarGustoOjo";
                boolean marron =  getInputData().getBoolean("marron",false);
                int intMarron = marron ? 1 : 0;
                boolean azul =  getInputData().getBoolean("azul", false);
                int intAzul = azul ? 1 : 0;
                boolean verde =  getInputData().getBoolean("verde", false);
                int intVerde = verde ? 1 : 0;
                String mail =getInputData().getString("mail");
                try {
                    URL dest =new URL(dir);
                    urlConnection = (HttpURLConnection) dest.openConnection();
                    urlConnection.setConnectTimeout(5000);
                    urlConnection.setReadTimeout(5000);
                    urlConnection.setRequestMethod("POST");
                    urlConnection.setRequestProperty("Content-Type","application/json");
                    JSONObject paramJson = new JSONObject();
                    paramJson.put("marron", intMarron);
                    paramJson.put("azul", intAzul);
                    paramJson.put("verde",intVerde);
                    paramJson.put("cod",mail);
                    Log.d("Ojos",""+ paramJson);
                    PrintWriter out = new PrintWriter(urlConnection.getOutputStream());
                    out.print(paramJson.toString());
                    out.close();
                    int statusCode = urlConnection.getResponseCode();
                    String code =String.valueOf(statusCode);
                    Log.d("Ojos Prueba",code);
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
            case "SeleccionarGustoPelo":{

                /*
                 *  HTTP Request to insert a user into Sexo table
                 */

                HttpURLConnection urlConnection;


                String dir = "http://192.168.0.22:3005/seleccionarGustoPelo";
                boolean rubio =  getInputData().getBoolean("rubio",false);
                int intRubio = rubio ? 1 : 0;
                boolean castano =  getInputData().getBoolean("castano", false);
                int intCastano = castano ? 1 : 0;
                boolean moreno =  getInputData().getBoolean("moreno", false);
                int intMoreno = moreno ? 1 : 0;
                boolean otro =  getInputData().getBoolean("otroPelo", false);
                int intOtro = otro ? 1 : 0;
                String mail =getInputData().getString("mail");
                try {
                    URL dest =new URL(dir);
                    urlConnection = (HttpURLConnection) dest.openConnection();
                    urlConnection.setConnectTimeout(5000);
                    urlConnection.setReadTimeout(5000);
                    urlConnection.setRequestMethod("POST");
                    urlConnection.setRequestProperty("Content-Type","application/json");
                    JSONObject paramJson = new JSONObject();
                    paramJson.put("rubio", intRubio);
                    paramJson.put("castano", intCastano);
                    paramJson.put("moreno",intMoreno);
                    paramJson.put("otro",intOtro);
                    paramJson.put("cod",mail);
                    Log.d("Pelo",""+ paramJson);
                    PrintWriter out = new PrintWriter(urlConnection.getOutputStream());
                    out.print(paramJson.toString());
                    out.close();
                    int statusCode = urlConnection.getResponseCode();
                    String code =String.valueOf(statusCode);
                    Log.d("Pelo Prueba",code);
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
            case "IntroducirHobbiesGusto":{

                /*
                 *  HTTP Request to insert a user into Hobbies table
                 */

                HttpURLConnection urlConnection;


                String dir = "http://192.168.0.22:3005/introducirHobbiesGusto";
                boolean leer =  getInputData().getBoolean("leer",false);
                int intLeer = leer ? 1 : 0;
                boolean deporte =  getInputData().getBoolean("deporte", false);
                int intDeporte = deporte ? 1 : 0;
                boolean fiesta =  getInputData().getBoolean("fiesta", false);
                int intFieste = fiesta ? 1 : 0;
                boolean cine = getInputData().getBoolean("cine",false);
                int intCine = cine ? 1 : 0;
                boolean otro =  getInputData().getBoolean("otro",false);
                int intOtro = otro ? 1 : 0;
                String mail =getInputData().getString("mail");
                try {
                    URL dest =new URL(dir);
                    urlConnection = (HttpURLConnection) dest.openConnection();
                    urlConnection.setConnectTimeout(5000);
                    urlConnection.setReadTimeout(5000);
                    urlConnection.setRequestMethod("POST");
                    urlConnection.setRequestProperty("Content-Type","application/json");
                    JSONObject paramJson = new JSONObject();
                    paramJson.put("leer", intLeer);
                    paramJson.put("deporte", intDeporte);
                    paramJson.put("fiesta",intFieste);
                    paramJson.put("cine",intCine);
                    paramJson.put("otro", intOtro);
                    paramJson.put("cod",mail);
                    Log.d("Personalidad Hobbies",""+ paramJson);
                    PrintWriter out = new PrintWriter(urlConnection.getOutputStream());
                    out.print(paramJson.toString());
                    out.close();
                    int statusCode = urlConnection.getResponseCode();
                    String code =String.valueOf(statusCode);
                    Log.d("Hobbies Prueba",code);
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

            case "IntroducirPersonalidadGusto":{

                /*
                 *  HTTP Request to insert a user into Personalidad table
                 */

                HttpURLConnection urlConnection;


                String dir = "http://192.168.0.22:3005/introducirPersonalidadGusto";
                boolean gracioso =  getInputData().getBoolean("gracioso",false);
                int intGracioso = gracioso ? 1 : 0;
                boolean alegre =  getInputData().getBoolean("alegre", false);
                int intAlegre= alegre ? 1 : 0;
                boolean simpatico =  getInputData().getBoolean("simpatico", false);
                int intSimpatico= simpatico ? 1 : 0;
                boolean borde = getInputData().getBoolean("borde",false);
                int intBorde = borde ? 1 : 0;
                boolean cabezon =  getInputData().getBoolean("cabezon",false);
                int intCabezon = cabezon ? 1 : 0;
                boolean humilde =  getInputData().getBoolean("humilde", false);
                int intHumilde = humilde ? 1 : 0;
                boolean fiel =  getInputData().getBoolean("fiel", false);
                int intFiel = fiel ? 1 : 0;
                boolean impuntual = getInputData().getBoolean("impuntual", false);
                int intImputual = impuntual ? 1 : 0;
                boolean carinoso =getInputData().getBoolean("carinoso", false);
                int intCarinoso = carinoso ? 1 : 0;
                String mail =getInputData().getString("mail");
                try {
                    URL dest =new URL(dir);
                    urlConnection = (HttpURLConnection) dest.openConnection();
                    urlConnection.setConnectTimeout(5000);
                    urlConnection.setReadTimeout(5000);
                    urlConnection.setRequestMethod("POST");
                    urlConnection.setRequestProperty("Content-Type","application/json");
                    JSONObject paramJson = new JSONObject();
                    paramJson.put("gracioso", intGracioso);
                    paramJson.put("alegre", intAlegre);
                    paramJson.put("simpatico",intSimpatico);
                    paramJson.put("borde",intBorde);
                    paramJson.put("cabezon", intCabezon);
                    paramJson.put("humilde", intHumilde);
                    paramJson.put("fiel",intFiel);
                    paramJson.put("impuntual",intImputual);
                    paramJson.put("carinoso",intCarinoso);
                    paramJson.put("cod",mail);
                    Log.d("Personalidad Prueba",""+ paramJson);
                    PrintWriter out = new PrintWriter(urlConnection.getOutputStream());
                    out.print(paramJson.toString());
                    out.close();
                    int statusCode = urlConnection.getResponseCode();
                    String code =String.valueOf(statusCode);
                    Log.d("Personalidad Prueba",code);
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
            case "ExisteUsuarioContra":{

                /*
                 *  HTTP Request to select a user Usuario table
                 */

                HttpURLConnection urlConnection;
                String contra =  getInputData().getString("contrasena");
                String mail =  getInputData().getString("mail");
                Log.d("Prueba inicio", "" + contra);
                Log.d("Prueba inicio", "" + mail);
                String dir = "http://192.168.0.22:3005/existeUsuarioContra?mail=" + mail+ "&password=" + contra;
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

