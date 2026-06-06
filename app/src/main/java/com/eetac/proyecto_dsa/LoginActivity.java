package com.eetac.proyecto_dsa;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.eetac.proyecto_dsa.model.User;
import com.eetac.proyecto_dsa.network.RetrofitClient;
import com.eetac.proyecto_dsa.utils.LocalUserManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText etUsername, etPassword;
    private Button btnLogin;
    private Button btnBypass;
    private TextView tvGoToRegister;
    private LocalUserManager userManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userManager = new LocalUserManager(this);

        if (userManager.isLoggedIn()) {
            goToMain();
            return;
        }

        etUsername     = findViewById(R.id.etUsername);
        etPassword     = findViewById(R.id.etPassword);
        btnLogin       = findViewById(R.id.btnLogin);
        btnBypass      = findViewById(R.id.btnBypass);
        tvGoToRegister = findViewById(R.id.tvGoToRegister);

        btnBypass.setOnClickListener(v -> {
            userManager.saveSession("Tester");
            userManager.updateCoins(999);
            Toast.makeText(this, "Bypass: Iniciando como Tester", Toast.LENGTH_SHORT).show();
            goToMain();
        });

        btnLogin.setOnClickListener(v -> {
            String nombre   = etUsername.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (nombre.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Rellena todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            // Llamada a la API con Retrofit
            User credenciales = new User(nombre, password, null);

            RetrofitClient.getService().login(credenciales).enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        User user = response.body();
                        // Guardamos la sesión y actualizamos las monedas recibidas del servidor
                        userManager.saveSession(user.getNombre());
                        userManager.updateCoins(user.getMonedas());

                        Toast.makeText(LoginActivity.this,
                                "¡Bienvenido, " + user.getNombre() + "!",
                                Toast.LENGTH_SHORT).show();
                        goToMain();
                    } else {
                        Toast.makeText(LoginActivity.this,
                                " Usuario o contraseña incorrectos",
                                Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    Toast.makeText(LoginActivity.this,
                            " Sin conexión con el servidor",
                            Toast.LENGTH_SHORT).show();
                }
            });
        });

        tvGoToRegister.setOnClickListener(v -> startActivity(new Intent(this, RegisterActivity.class)));
    }

    private void goToMain() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}