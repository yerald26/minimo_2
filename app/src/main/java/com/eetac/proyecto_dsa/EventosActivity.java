package com.eetac.proyecto_dsa;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.eetac.proyecto_dsa.model.Evento;
import com.eetac.proyecto_dsa.network.RetrofitClient;
import com.eetac.proyecto_dsa.utils.EventosAdapter;
import com.eetac.proyecto_dsa.utils.LocalUserManager;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventosActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private LocalUserManager userManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eventos);

        userManager = new LocalUserManager(this);
        recyclerView = findViewById(R.id.recyclerViewEventos);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        cargarEventos();
    }

    private void cargarEventos() {
        RetrofitClient.getService().getEventos().enqueue(new Callback<List<Evento>>() {
            @Override
            public void onResponse(Call<List<Evento>> call, Response<List<Evento>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Pasamos el username real guardado en el movil
                    String username = userManager.getLoggedUsername();
                    recyclerView.setAdapter(new EventosAdapter(response.body(), EventosActivity.this, username));
                }
            }
            @Override
            public void onFailure(Call<List<Evento>> call, Throwable t) {
                Toast.makeText(EventosActivity.this, "Error de red", Toast.LENGTH_SHORT).show();
            }
        });
    }
}