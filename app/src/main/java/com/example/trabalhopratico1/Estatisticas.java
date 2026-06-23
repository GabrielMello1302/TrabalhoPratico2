package com.example.trabalhopratico1;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.concurrent.Executors;

public class Estatisticas extends AppCompatActivity {

    private TextView txtTotal, txtAberto, txtAndamento, txtConcluido;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_estatisticas);

        // Configuração segura do WindowInsets usando o ID "main"
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Mapeia os componentes do XML
        Button inicioBotao = findViewById(R.id.inicio_botao);
        txtTotal = findViewById(R.id.txt_total);
        txtAberto = findViewById(R.id.txt_aberto);
        txtAndamento = findViewById(R.id.txt_andamento);
        txtConcluido = findViewById(R.id.txt_concluido);

        // Ação do botão início para fechar a tela atual e voltar
        inicioBotao.setOnClickListener(v -> finish());

        // Executa a contagem dos dados vindos da Room
        carregarEstatisticas();
    }

    private void carregarEstatisticas() {
        Executors.newSingleThreadExecutor().execute(() -> {
            // Busca a quantidade correspondente ao tamanho de cada lista retornada do banco
            int abertos = AppDatabase.getInstancia(this).chamadoDao().filtrarPorStatus("Aberto").size();
            int andamento = AppDatabase.getInstancia(this).chamadoDao().filtrarPorStatus("Em Andamento").size();
            int concluidos = AppDatabase.getInstancia(this).chamadoDao().filtrarPorStatus("Concluído").size();
            int total = abertos + andamento + concluidos;

            // Devolve os valores para a thread principal atualizar a interface gráfica
            runOnUiThread(() -> {
                txtAberto.setText(String.valueOf(abertos));
                txtAndamento.setText(String.valueOf(andamento));
                txtConcluido.setText(String.valueOf(concluidos));
                txtTotal.setText(String.valueOf(total));
            });
        });
    }
}