package com.eetac.proyecto_dsa;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import com.eetac.proyecto_dsa.utils.LocalUserManager;

public class MainActivity extends AppCompatActivity {

    private TextView tvWelcome;
    private Button btnLogout;
    private LocalUserManager userManager;

    // Reemplaza startActivityForResult — forma moderna
    private final ActivityResultLauncher<Intent> unityLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    result -> {
                        if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                            String input2 = result.getData().getStringExtra("input2");
                            Toast.makeText(this, "Resultado de Unity: " + input2, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(this, "Regresaste del juego", Toast.LENGTH_SHORT).show();
                        }
                    }
            );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userManager = new LocalUserManager(this);

        tvWelcome = findViewById(R.id.tvWelcome);
        Button btnTienda  = findViewById(R.id.btnTienda);
        Button btnMochila = findViewById(R.id.btnMochila);
        Button btnJugar   = findViewById(R.id.btnJugar);
        btnLogout         = findViewById(R.id.btnLogout);

        tvWelcome.setText("¡Bienvenido, " + userManager.getLoggedUsername() + "!");

        btnTienda.setOnClickListener(v ->
                startActivity(new Intent(this, TiendaActivity.class))
        );

        btnMochila.setOnClickListener(v ->
                startActivity(new Intent(this, MochilaActivity.class))
        );

        btnJugar.setOnClickListener(v -> {
            Intent i = new Intent();
            i.setComponent(new ComponentName(
                    "dsa.JuegoMazmorras",
                    "com.unity3d.player.UnityPlayerActivity"
            ));
            i.putExtra("input", userManager.getLoggedUsername());
            unityLauncher.launch(i);
        });

        btnLogout.setOnClickListener(v -> {
            userManager.logout();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });
    }
}