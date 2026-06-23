package com.example.trabalhopratico1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class Filtros extends AppCompatActivity {

    private RecyclerView recyclerView;
    private Spinner statusSpinner;
    private TextView tvListaVazia; // 🌟 Texto amigável para quando a lista sumir

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filtros);

        Button inicioBotao   = findViewById(R.id.inicio_botao);
        Button filtrarBotao  = findViewById(R.id.filtrar_botao);
        statusSpinner        = findViewById(R.id.filtro_status_spinner);
        recyclerView         = findViewById(R.id.recycler_filtrados);

        // 🌟 Se você não tiver esse TextView no seu XML, comente essa linha abaixo:
        tvListaVazia         = findViewById(R.id.tv_lista_vazia);

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
    }

    // 🔄 O SEGREDO ESTÁ AQUI: Atualiza a lista automaticamente sempre que você VOLTAR para esta tela
    @Override
    protected void onResume() {
        super.onResume();
        if (statusSpinner != null && statusSpinner.getSelectedItem() != null) {
            String statusAtual = statusSpinner.getSelectedItem().toString();
            filtrar(statusAtual); // Recarrega os dados atualizados da nuvem
        }
    }

    private void filtrar(String status) {
        com.parse.ParseQuery<com.parse.ParseObject> query = com.parse.ParseQuery.getQuery("Chamado");
        query.whereEqualTo("status", status);
        query.orderByDescending("createdAt");

        query.findInBackground((chamadosNuvem, e) -> {
            if (e == null) {
                // 💡 Se a nuvem retornar uma lista vazia (porque o status mudou)
                if (chamadosNuvem == null || chamadosNuvem.isEmpty()) {
                    recyclerView.setAdapter(null); // Limpa o recycler

                    if (tvListaVazia != null) {
                        tvListaVazia.setVisibility(View.VISIBLE);
                        tvListaVazia.setText("Nenhum chamado encontrado como '" + status + "'");
                    } else {
                        Toast.makeText(this, "Nenhum chamado nesta categoria.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Se achou dados, esconde o texto de aviso e mostra a lista linda atualizada!
                    if (tvListaVazia != null) tvListaVazia.setVisibility(View.GONE);

                    ChamadoNuvemAdapter adapter = new ChamadoNuvemAdapter(chamadosNuvem);
                    recyclerView.setAdapter(adapter);
                }
            } else {
                Toast.makeText(Filtros.this, "Erro na Nuvem: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}