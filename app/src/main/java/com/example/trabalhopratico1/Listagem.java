package com.example.trabalhopratico1;

import com.parse.ParseQuery;
import com.parse.ParseObject;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;
import java.util.concurrent.Executors;

public class Listagem extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ChamadoAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listagem);

        Button inicioBotao = findViewById(R.id.inicio_botao);
        FloatingActionButton cadastrarBotao = findViewById(R.id.cadastrar_botao);
        recyclerView = findViewById(R.id.recycler_chamados);
        recyclerView.setLayoutManager(new androidx.recyclerview.widget.LinearLayoutManager(this));

        // Fazendo a consulta no Back4App
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Chamado");
        query.orderByDescending("createdAt"); // Ordena para mostrar os mais recentes primeiro

        query.findInBackground((chamadosNuvem, e) -> {
            if (e == null) {
                // Se deu tudo certo, passa a lista para o nosso novo Adapter
                ChamadoNuvemAdapter adapter = new ChamadoNuvemAdapter(chamadosNuvem);
                recyclerView.setAdapter(adapter);
            } else {
                android.widget.Toast.makeText(Listagem.this, "Erro ao carregar da nuvem: " + e.getMessage(), android.widget.Toast.LENGTH_LONG).show();
            }
        });

        // 1. MAPEAMOS O NOVO BOTÃO DE FILTROS
        Button filtrosBotao = findViewById(R.id.filtros_botao);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        inicioBotao.setOnClickListener(v -> finish());

        cadastrarBotao.setOnClickListener(v -> {
            startActivity(new Intent(this, Cadastro.class));
        });

        // 2. QUANDO CLICAR EM FILTROS, ABRE A TELA ESPERANDO RESPOSTA (código 100)
        filtrosBotao.setOnClickListener(v -> {
            Intent intent = new Intent(this, Filtros.class);
            startActivityForResult(intent, 100);
        });

        carregarChamados();
    }

    // 3. A "CAIXA DE CORREIO" (RECEBE A RESPOSTA DA TELA DE FILTROS)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Se for o código 100 (Filtros) e a pessoa clicou em Aplicar (RESULT_OK)
        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {

            // Abrimos o "envelope" e pegamos a palavra que o Assistente anotou
            String statusQueOUsuarioQuer = data.getStringExtra("filtro_status");

            // Mandamos filtrar no banco
            filtrarNoBanco(statusQueOUsuarioQuer);

        } else {
            // Se ele clicou em "Exibir Todos" (RESULT_CANCELED), carregamos tudo de novo
            carregarChamados();
        }
    }

    // 4. MÉTODO NOVO: BUSCA SÓ OS FILTRADOS (Usa a query do ChamadoDao)
    private void filtrarNoBanco(String status) {
        Executors.newSingleThreadExecutor().execute(() -> {
            // Aqui ele não busca todos, busca só pelo status passado
            List<Chamado> listaFiltrada = AppDatabase.getInstancia(this)
                    .chamadoDao().listarPorStatus(status);

            runOnUiThread(() -> {
                adapter = new ChamadoAdapter(listaFiltrada, chamado -> {
                    Intent intent = new Intent(this, Detalhes.class);
                    intent.putExtra("chamado_id", chamado.id);
                    startActivity(intent);
                });
                recyclerView.setAdapter(adapter); // Atualiza a lista na tela
            });
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        carregarChamados();
    }

    // MÉTODO QUE VOCÊ JÁ TINHA: BUSCA TODOS OS CHAMADOS
    private void carregarChamados() {
        Executors.newSingleThreadExecutor().execute(() -> {
            List<Chamado> lista = AppDatabase.getInstancia(this)
                    .chamadoDao().listarTodos();

            runOnUiThread(() -> {
                adapter = new ChamadoAdapter(lista, chamado -> {
                    Intent intent = new Intent(this, Detalhes.class);
                    intent.putExtra("chamado_id", chamado.id);
                    startActivity(intent);
                });
                recyclerView.setAdapter(adapter); // Atualiza a lista na tela
            });
        });
    }
}