package com.eetac.proyecto_dsa;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.eetac.proyecto_dsa.model.User;
import com.eetac.proyecto_dsa.network.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    private EditText etUsername, etEmail, etPassword, etConfirmPassword;
    private Button btnRegister;
    private TextView tvGoToLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etUsername        = findViewById(R.id.etUsername);
        etEmail           = findViewById(R.id.etEmail);
        etPassword        = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btnRegister       = findViewById(R.id.btnRegister);
        tvGoToLogin       = findViewById(R.id.tvGoToLogin);

        btnRegister.setOnClickListener(v -> {
            String username = etUsername.getText().toString().trim();
            String email    = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            String confirm  = etConfirmPassword.getText().toString().trim();

            // Validaciones (igual que antes)
            if (username.isEmpty() || email.isEmpty() ||
                    password.isEmpty() || confirm.isEmpty()) {
                Toast.makeText(this, "⚠ Rellena todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, "⚠ Email no válido", Toast.LENGTH_SHORT).show();
                return;
            }
            if (password.length() < 6) {
                Toast.makeText(this, "⚠ Mínimo 6 caracteres", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!password.equals(confirm)) {
                Toast.makeText(this, "⚠ Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
                return;
            }

            // Llamada a la API con Retrofit
            User nuevoUsuario = new User(username, password, email);

            RetrofitClient.getService().registro(nuevoUsuario).enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(RegisterActivity.this,
                                "¡Héroe creado! Ya puedes iniciar sesión",
                                Toast.LENGTH_SHORT).show();
                        finish();
                    } else if (response.code() == 409) {
                        Toast.makeText(RegisterActivity.this,
                                "⚠ Ese nombre de usuario ya existe",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(RegisterActivity.this,
                                "⚠ Error al registrar: " + response.code(),
                                Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    Toast.makeText(RegisterActivity.this,
                            "⚠ Sin conexión con el servidor",
                            Toast.LENGTH_SHORT).show();
                }
            });
        });

        tvGoToLogin.setOnClickListener(v -> finish());
    }
}