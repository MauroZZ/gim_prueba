package com.example.gimnasio;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;

public class Crear_Cuenta extends AppCompatActivity {

    private EditText editTextNombre;
    private EditText editTextApellido;
    private EditText editTextRUT;
    private EditText editTextSaldo;
    private EditText editTextClave;
    private EditText editTextRepetirClave;
    private Button btnCrear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_cuenta);

        editTextNombre = findViewById(R.id.editTextNombre);
        editTextApellido = findViewById(R.id.editTextApellido);
        editTextRUT = findViewById(R.id.editTextRUT);
        editTextSaldo = findViewById(R.id.editTextSaldo);
        editTextClave = findViewById(R.id.editTextClave);
        editTextRepetirClave = findViewById(R.id.editTextRepetirClave);
        btnCrear = findViewById(R.id.btnCrear);

        try {

            btnCrear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String nombre = editTextNombre.getText().toString();
                    String apellido = editTextApellido.getText().toString();
                    String rut = editTextRUT.getText().toString();
                    String saldoText = editTextSaldo.getText().toString();
                    String clave = editTextClave.getText().toString();
                    String repetirClave = editTextRepetirClave.getText().toString();

                    if (nombre.isEmpty() || apellido.isEmpty() || rut.isEmpty() || saldoText.isEmpty()) {
                        Toast.makeText(Crear_Cuenta.this, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    double saldoInicial = 0.0; // Valor predeterminado si el campo está vacío
                    try {
                        saldoInicial = Double.parseDouble(saldoText);
                    } catch (NumberFormatException e) {
                        Toast.makeText(Crear_Cuenta.this, "El saldo inicial debe ser un número válido", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (saldoInicial <= 0) {
                        Toast.makeText(Crear_Cuenta.this, "El saldo inicial debe ser mayor que $ 0", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (clave.length() != 6) {
                        Toast.makeText(Crear_Cuenta.this, "La clave debe tener exactamente 6 dígitos", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (!clave.equals(repetirClave)) {
                        Toast.makeText(Crear_Cuenta.this, "Las claves no coinciden", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (!validarRutChileno(rut)) {
                        Toast.makeText(Crear_Cuenta.this, "El RUT debe tener punto y guion", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    Usuario nuevoUsuario = new Usuario(nombre, apellido, rut, saldoInicial, clave, "");

                    borrarHistorialGiros();
                    borrarHistorialDepositos();
                    borrarHistorialPagos();

                    guardarHistorialDepositos(saldoInicial, "Depósito inicial");

                    nuevoUsuario.guardarEnSharedPreferences(getApplicationContext());

                    Toast.makeText(Crear_Cuenta.this, "Cuenta creada con éxito", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(Crear_Cuenta.this, SesionGimnasio.class);
                    startActivity(intent);

                    finish();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

        Button btnVolverC = findViewById(R.id.btnVolverC);

        btnVolverC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Crear_Cuenta.this, MainActivity.class);

                startActivity(intent);
            }
        });
    }

    private boolean validarRutChileno(String rut) {

        String regex = "\\d{2}\\.\\d{3}\\.\\d{3}-\\d{1}";
        return rut.matches(regex);

    }

    private void guardarHistorialDepositos(double monto, String descripcion) {
        SharedPreferences sharedPreferences = getSharedPreferences("mi_preferencia", Context.MODE_PRIVATE);

        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        String horaActual = dateFormat.format(new Date());

        String entradaHistorial =" Depósito inicial " + monto + " (" + horaActual + ")";

        ArrayList<String> historialList = new ArrayList<>(sharedPreferences.getStringSet("historial_depositos", new HashSet<>()));

        historialList.add(entradaHistorial);

        Collections.sort(historialList, new Comparator<String>() {
            @Override
            public int compare(String s1, String s2) {
                return s2.compareTo(s1);
            }
        });

        int maxHistorialSize = 10;
        if (historialList.size() > maxHistorialSize) {
            historialList.subList(maxHistorialSize, historialList.size()).clear();
        }

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putStringSet("historial_depositos", new HashSet<>(historialList));
        editor.apply();
    }

    private void borrarHistorialGiros() {
        SharedPreferences sharedPreferences = getSharedPreferences("mi_preferencia", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("historial_giros");
        editor.apply();
    }

    private void borrarHistorialDepositos() {
        SharedPreferences sharedPreferences = getSharedPreferences("mi_preferencia", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("historial_depositos");
        editor.apply();
    }

    private void borrarHistorialPagos() {
        SharedPreferences sharedPreferences = getSharedPreferences("mi_preferencia", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("historial_pagos");
        editor.apply();
    }
}

