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

public class IniciarSesion extends AppCompatActivity {

    private EditText editTextRut;
    private EditText editTextClave;
    private Button btnIniciarSesion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iniciar_sesion);

        editTextRut = findViewById(R.id.editTextRutM); // Reemplaza con el ID real de tu EditText
        editTextClave = findViewById(R.id.editTextClaveM); // Reemplaza con el ID real de tu EditText
        btnIniciarSesion = findViewById(R.id.btnIniciarSesionM); // Reemplaza con el ID real de tu botón

        try {

        btnIniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String rut = editTextRut.getText().toString();
                String clave = editTextClave.getText().toString();

                // Validar que los campos no estén vacíos
                if (rut.isEmpty() || clave.isEmpty()) {
                    Toast.makeText(IniciarSesion.this, "RUT y clave son obligatorios", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Verificar si el RUT y la clave coinciden con un usuario existente
                if (validarCredenciales(rut, clave)) {
                    // Iniciar sesión exitosa
                    Toast.makeText(IniciarSesion.this, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show();

                    // Redirigir al usuario al menú o a la actividad deseada
                    Intent intent = new Intent(IniciarSesion.this, SesionGimnasio.class); // Reemplaza con el nombre de tu actividad de menú
                    startActivity(intent);

                    finish(); // Cierra la actividad de inicio de sesión
                } else {
                    // Credenciales incorrectas
                    Toast.makeText(IniciarSesion.this, "RUT o clave incorrectos", Toast.LENGTH_SHORT).show();
                }
            }
        });

        } catch (Exception e) {
            e.printStackTrace();
        }

        Button btnVolverS = findViewById(R.id.btnVolverS);

        btnVolverS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(IniciarSesion.this, MainActivity.class);

                startActivity(intent);
            }
        });
    }

    private boolean validarCredenciales(String rut, String clave) {
        SharedPreferences sharedPreferences = getSharedPreferences("usuarios", Context.MODE_PRIVATE);
        String rutGuardado = sharedPreferences.getString("RUT", ""); // Obtén el RUT almacenado
        String claveGuardada = sharedPreferences.getString("clave", ""); // Obtén la clave almacenada

        // Verifica si las credenciales coinciden con las almacenadas
        return rut.equals(rutGuardado) && clave.equals(claveGuardada);
    }
}

