package com.eetac.proyecto_dsa.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.eetac.proyecto_dsa.R;
import com.bumptech.glide.Glide;
import com.eetac.proyecto_dsa.model.Evento;
import com.eetac.proyecto_dsa.model.InscripcionRequest;
import com.eetac.proyecto_dsa.network.RetrofitClient;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventosAdapter extends RecyclerView.Adapter<EventosAdapter.ViewHolder> {
    private List<Evento> listaEventos;
    private Context context;
    private String username;

    public EventosAdapter(List<Evento> listaEventos, Context context, String username) {
        this.listaEventos = listaEventos;
        this.context = context;
        this.username = username;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_evento, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Evento evento = listaEventos.get(position);
        holder.nombre.setText(evento.nombre);
        holder.descripcion.setText(evento.descripcion);
        holder.fechas.setText("Del " + evento.fecha_inicio + " al " + evento.fecha_fin);

        Glide.with(context)
                .load(evento.imagen_URL)
                .placeholder(android.R.drawable.ic_menu_gallery) // Imagen temporal mientras carga
                .error(android.R.drawable.stat_notify_error)      // Imagen si la URL falla
                .into(holder.imagen);

        holder.btnInscribirse.setOnClickListener(v -> {
            InscripcionRequest req = new InscripcionRequest(username, evento.id);
            RetrofitClient.getService().inscribirseEvento(req).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(context, "¡Inscrito con éxito!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "Error en la inscripción", Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(context, "Error de conexión", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    @Override
    public int getItemCount() { return listaEventos.size(); }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nombre, descripcion, fechas;
        ImageView imagen;
        Button btnInscribirse;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nombre = itemView.findViewById(R.id.nombreEvento);
            descripcion = itemView.findViewById(R.id.descripcionEvento);
            fechas = itemView.findViewById(R.id.fechasEvento);
            btnInscribirse = itemView.findViewById(R.id.btnInscribirse);
            imagen = itemView.findViewById(R.id.imagenEvento);
        }
    }
}