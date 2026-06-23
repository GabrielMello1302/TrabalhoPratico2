package com.example.trabalhopratico1;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

public class Estatisticas extends AppCompatActivity {

    private TextView txtTotal, txtAberto, txtAndamento, txtConcluido;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_estatisticas);

        // Configuração do WindowInsets usando o ID "main"
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
    }

    // 🔄 O SEGREDO: Atualiza o painel vindo da Nuvem toda vez que a tela reaparecer
    @Override
    protected void onResume() {
        super.onResume();
        carregarEstatisticasNuvem();
    }

    private void carregarEstatisticasNuvem() {
        // Cria a query apontando para a classe "Chamado" na nuvem
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Chamado");

        query.findInBackground((chamadosNuvem, e) -> {
            if (e == null) {
                int abertos = 0;
                int andamento = 0;
                int concluidos = 0;

                if (chamadosNuvem != null) {
                    // Percorre a lista vinda da nuvem e incrementa o contador correto
                    for (ParseObject chamado : chamadosNuvem) {
                        String status = chamado.getString("status"); // Pega o campo 'status' do Parse

                        if (status != null) {
                            if (status.equalsIgnoreCase("Aberto")) {
                                abertos++;
                            } else if (status.equalsIgnoreCase("Em Andamento")) {
                                andamento++;
                            } else if (status.equalsIgnoreCase("Concluído")) {
                                concluidos++;
                            }
                        }
                    }
                }

                // Calcula o total com base na soma dos status mapeados
                int total = abertos + andamento + concluidos;

                // Atualiza a interface gráfica (o Parse já roda o callback na UI Thread)
                txtAberto.setText(String.valueOf(abertos));
                txtAndamento.setText(String.valueOf(andamento));
                txtConcluido.setText(String.valueOf(concluidos));
                txtTotal.setText(String.valueOf(total));

            } else {
                // Exibe erro caso a comunicação com o Parse falhe
                Toast.makeText(Estatisticas.this, "Erro ao calcular estatísticas da nuvem: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}