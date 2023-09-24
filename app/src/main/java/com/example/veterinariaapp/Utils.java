package com.example.veterinariaapp;

import android.content.Context;
import android.widget.Toast;

public class Utils {
    public static final String BASE_URL = "http://192.168.100.6/veterinaria/";
    public static final String URL_LOGIN = BASE_URL + "controller/cliente.controller.php";
    public static final String URL_CLIENTE = BASE_URL + "controller/cliente.controller.php";
    public static final String URL_MASCOTA = BASE_URL + "controller/mascota.controller.php";

    public static  final  String BASE_URL_IMG = BASE_URL + "img/";
    public static void showToast(Context context, String message){
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }
}
