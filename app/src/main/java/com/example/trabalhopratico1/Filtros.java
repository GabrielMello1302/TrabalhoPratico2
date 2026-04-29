package com.example.trabalhopratico1;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.concurrent.Executors;

public class Filtros extends AppCompatActivity {

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filtros);

        Button inicioBotao   = findViewById(R.id.inicio_botao);
        Button filtrarBotao  = findViewById(R.id.filtrar_botao);
        Spinner statusSpinner = findViewById(R.id.filtro_status_spinner);
        recyclerView         = findViewById(R.id.recycler_filtrados);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                new String[]{"Aberto", "Em Andamento", "Concluído"}
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        statusSpinner.setAdapter(adapter);

        inicioBotao.setOnClickListener(v -> finish());

        filtrarBotao.setOnClickListener(v -> {
            String statusSelecionado = statusSpinner.getSelectedItem().toString();
            filtrar(statusSelecionado);
        });

        // Carrega "Aberto" por padrão ao abrir a tela
        filtrar("Aberto");
    }

    private void filtrar(String status) {
        Executors.newSingleThreadExecutor().execute(() -> {
            List<Chamado> lista = AppDatabase.getInstancia(this)
                    .chamadoDao().filtrarPorStatus(status);

            runOnUiThread(() -> {
                ChamadoAdapter adapter = new ChamadoAdapter(lista, chamado -> {
                    Intent intent = new Intent(this, Detalhes.class);
                    intent.putExtra("chamado_id", chamado.id);
                    startActivity(intent);
                });
                recyclerView.setAdapter(adapter);
            });
        });
    }
}