package com.example.trabalhopratico1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class Listagem extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TextView tvListaVazia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listagem);

        Button inicioBotao = findViewById(R.id.inicio_botao);
        Button filtrosBotao = findViewById(R.id.filtros_botao);
        FloatingActionButton cadastrarBotao = findViewById(R.id.cadastrar_botao);

        recyclerView = findViewById(R.id.recycler_chamados);
        tvListaVazia = findViewById(R.id.tv_lista_vazia);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        inicioBotao.setOnClickListener(v -> finish());

        cadastrarBotao.setOnClickListener(v -> {
            startActivity(new Intent(this, Cadastro.class));
        });

        // Abre a tela de filtros independente
        filtrosBotao.setOnClickListener(v -> {
            Intent intent = new Intent(this, Filtros.class);
            startActivity(intent);
        });

        // Não precisamos mais chamar o carregamento no onCreate,
        // pois o onResume() já é executado logo em seguida quando a tela abre!
    }

    // 🔄 O SEGREDO: Atualiza a lista da Nuvem automaticamente toda vez que a tela aparecer
    @Override
    protected void onResume() {
        super.onResume();
        carregarChamadosNuvem();
    }

    private void carregarChamadosNuvem() {
        // Busca todos os chamados da nuvem
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Chamado");
        query.orderByDescending("createdAt"); // Mais recentes primeiro

        query.findInBackground((chamadosNuvem, e) -> {
            if (e == null) {
                // Se a lista vier vazia
                if (chamadosNuvem == null || chamadosNuvem.isEmpty()) {
                    recyclerView.setAdapter(null); // Limpa o RecyclerView
                    if (tvListaVazia != null) {
                        tvListaVazia.setVisibility(View.VISIBLE);
                    }
                } else {
                    // Se achou dados
                    if (tvListaVazia != null) {
                        tvListaVazia.setVisibility(View.GONE);
                    }
                    ChamadoNuvemAdapter adapter = new ChamadoNuvemAdapter(chamadosNuvem);
                    recyclerView.setAdapter(adapter);
                }
            } else {
                Toast.makeText(Listagem.this, "Erro ao carregar da nuvem: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}