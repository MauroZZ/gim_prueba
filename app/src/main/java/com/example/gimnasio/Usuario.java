package com.example.gimnasio;

import android.content.Context;
import android.content.SharedPreferences;

public class Usuario {
    private String nombre;
    private String apellido;
    private String RUT;
    private double saldo;
    private String clave;
    private String numSecreto;

    public Usuario(String nombre, String apellido, String RUT, double saldo, String clave, String numSecreto) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.RUT = RUT;
        this.saldo = saldo;
        this.clave = clave;
        this.numSecreto = numSecreto;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public double getSaldo() {
        return saldo;
    }


    public boolean girar(double monto) {
        if (saldo >= monto && monto > 0) {
            saldo -= monto;
            return true;
        } else {
            return false;
        }
    }

    public boolean pagar(String rut, String claveIngresada, double monto) {
        if (rut.equals(this.RUT) && clave.equals(claveIngresada) && saldo >= monto && monto > 0) {
            saldo -= monto;
            return true;
        } else {
            return false;
        }
    }


    public boolean depositar(double monto) {
        if (monto > 0) {
            saldo += monto;
            return true;
        }
        return false;
    }

    public void guardarEnSharedPreferences(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("usuarios", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("RUT", RUT);
        editor.putString("nombre", nombre);
        editor.putString("apellido", apellido);
        editor.putString("clave", clave);
        editor.putString("numSecreto", numSecreto);
        editor.putFloat("saldo", (float) saldo);
        editor.apply();
    }

    public static Usuario cargarDesdeSharedPreferences(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("usuarios", Context.MODE_PRIVATE);
        String RUT = sharedPreferences.getString("RUT", "");
        String nombre = sharedPreferences.getString("nombre", "");
        String apellido = sharedPreferences.getString("apellido", "");
        String clave = sharedPreferences.getString("clave", "");
        String numSecreto = sharedPreferences.getString("numSecreto", "");
        float saldo = sharedPreferences.getFloat("saldo", 0.0f);

        // Inicializa un nuevo objeto Usuario con los valores obtenidos de las preferencias
        return new Usuario(nombre, apellido, RUT, saldo, clave, numSecreto);
    }

}


