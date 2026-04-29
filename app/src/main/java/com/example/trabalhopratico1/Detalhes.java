package com.example.trabalhopratico1;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;
import java.util.concurrent.Executors;

public class Detalhes extends AppCompatActivity {

    private int chamadoId;
    private LinearLayout historicoContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes);

        chamadoId = getIntent().getIntExtra("chamado_id", -1);

        TextView tvId        = findViewById(R.id.detalhe_id);
        TextView tvTitulo    = findViewById(R.id.detalhe_titulo);
        TextView tvStatus    = findViewById(R.id.detalhe_status);
        TextView tvDescricao = findViewById(R.id.detalhe_descricao);
        TextView tvData      = findViewById(R.id.detalhe_data);
        historicoContainer   = findViewById(R.id.historico_container);
        Button voltarBotao   = findViewById(R.id.voltar_botao);
        Button atendimentoBotao = findViewById(R.id.atendimento_botao);

        voltarBotao.setOnClickListener(v -> finish());

        atendimentoBotao.setOnClickListener(v -> {
            Intent intent = new Intent(this, Atendimento.class);
            intent.putExtra("chamado_id", chamadoId);
            startActivity(intent);
        });

        carregarDados(tvId, tvTitulo, tvStatus, tvDescricao, tvData);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Recarrega ao voltar do Atendimento para refletir novo histórico
        TextView tvId        = findViewById(R.id.detalhe_id);
        TextView tvTitulo    = findViewById(R.id.detalhe_titulo);
        TextView tvStatus    = findViewById(R.id.detalhe_status);
        TextView tvDescricao = findViewById(R.id.detalhe_descricao);
        TextView tvData      = findViewById(R.id.detalhe_data);
        carregarDados(tvId, tvTitulo, tvStatus, tvDescricao, tvData);
    }

    private void carregarDados(TextView tvId, TextView tvTitulo, TextView tvStatus,
                               TextView tvDescricao, TextView tvData) {
        Executors.newSingleThreadExecutor().execute(() -> {
            Chamado chamado = AppDatabase.getInstancia(this)
                    .chamadoDao().buscarPorId(chamadoId);
            List<Historico> historicos = AppDatabase.getInstancia(this)
                    .historicoDao().listarPorChamado(chamadoId);

            runOnUiThread(() -> {
                tvId.setText("#" + String.format("%03d", chamado.id));
                tvTitulo.setText(chamado.titulo);
                tvStatus.setText(chamado.status);
                tvDescricao.setText(chamado.descricao);
                tvData.setText(chamado.dataCadastro);

                // Cor do status
                switch (chamado.status) {
                    case "Em Andamento":
                        tvStatus.setBackgroundColor(0xFF1565C0);
                        break;
                    case "Concluído":
                        tvStatus.setBackgroundColor(0xFF2E7D32);
                        break;
                    default:
                        tvStatus.setBackgroundColor(0xFF6750A4);
                        break;
                }

                // Limpa e reconstrói o histórico
                historicoContainer.removeAllViews();

                if (historicos.isEmpty()) {
                    TextView vazio = new TextView(this);
                    vazio.setText("Nenhuma intervenção registrada ainda.");
                    vazio.setTextColor(0xFF888888);
                    historicoContainer.addView(vazio);
                } else {
                    for (Historico h : historicos) {
                        TextView tv = new TextView(this);
                        tv.setText("📅 " + h.data + " — " + h.emailTecnico
                                + "\n" + h.texto
                                + "\nStatus definido: " + h.novoStatus);
                        tv.setBackgroundColor(0xFFFFFFFF);
                        tv.setPadding(16, 12, 16, 12);

                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                        );
                        params.setMargins(0, 0, 0, 8);
                        tv.setLayoutParams(params);
                        historicoContainer.addView(tv);
                    }
                }
            });
        });
    }
}