package com.example.veterinariaapp;

import android.content.Context;
import android.widget.Toast;

public class Utils {
    public static final String BASE_URL = "http://192.168.1.148/veterinaria/";
    public static final String URL_LOGIN = BASE_URL + "controller/cliente.controller.php";
    public static final String URL_CLIENTE = BASE_URL + "controller/cliente.controller.php";
    public static final String URL_MASCOTA = BASE_URL + "controller/mascota.controller.php";

    // Datos Globales
    public static  String idCliente = "";

    public static  String rol = "";
    public static  String dni = "";

    public static  final  String BASE_URL_IMG = BASE_URL + "img/";
    public static void showToast(Context context, String message){
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    public static String getRol() {
        return rol;
    }

    public static void setRol(String rol) {
        Utils.rol = rol;
    }

    public static String getDni() {
        return dni;
    }

    public static void setDni(String dni) {
        Utils.dni = dni;
    }

    public static String getIdCliente() {
        return idCliente;
    }

    public static void setIdCliente(String idCliente) {
        Utils.idCliente = idCliente;
    }
}
