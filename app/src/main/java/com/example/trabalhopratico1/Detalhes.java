package com.example.trabalhopratico1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
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

        // Configuração padrão inicial para o SQLite
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
        TextView tvId        = findViewById(R.id.detalhe_id);
        TextView tvTitulo    = findViewById(R.id.detalhe_titulo);
        TextView tvStatus    = findViewById(R.id.detalhe_status);
        TextView tvDescricao = findViewById(R.id.detalhe_descricao);
        TextView tvData      = findViewById(R.id.detalhe_data);
        carregarDados(tvId, tvTitulo, tvStatus, tvDescricao, tvData);
    }

    private void carregarDados(TextView tvId, TextView tvTitulo, TextView tvStatus,
                               TextView tvDescricao, TextView tvData) {

        Button atendimentoBotao = findViewById(R.id.atendimento_botao);

        // ☁️ FLUXO DA NUVEM
        if (getIntent().hasExtra("origem") && "nuvem".equals(getIntent().getStringExtra("origem"))) {

            String objectId = getIntent().getStringExtra("object_id");
            String idFormatado = getIntent().getStringExtra("id_formatado");
            String titulo = getIntent().getStringExtra("titulo");
            String status = getIntent().getStringExtra("status");
            String descricao = getIntent().getStringExtra("descricao");
            String local = getIntent().getStringExtra("local");

            tvId.setText(idFormatado != null ? idFormatado : "#---");
            tvTitulo.setText(titulo);
            tvStatus.setText(status);
            tvDescricao.setText(descricao + "\n\n📍 Local original: " + local);
            tvData.setText("Sincronizado via Nuvem");

            atualizarCorStatus(tvStatus, status);

            // 🔥 CONFIGURA O BOTÃO PARA ATUALIZAR O STATUS NA NUVEM
            if (atendimentoBotao != null) {
                atendimentoBotao.setVisibility(View.VISIBLE);
                atendimentoBotao.setText("Alterar Status (Nuvem)");

                atendimentoBotao.setOnClickListener(v -> {
                    String[] opcoes = {"Aberto", "Em Andamento", "Concluído"};

                    new AlertDialog.Builder(this)
                            .setTitle("Alterar Status na Nuvem")
                            .setItems(opcoes, (dialog, which) -> {
                                String novoStatus = opcoes[which];

                                // Cria a referência ao objeto do Back4App para atualizar
                                com.parse.ParseObject chamadoNuvem = com.parse.ParseObject.createWithoutData("Chamado", objectId);
                                chamadoNuvem.put("status", novoStatus);

                                // Salva em background de forma assíncrona
                                chamadoNuvem.saveInBackground(e -> {
                                    if (e == null) {
                                        Toast.makeText(this, "Nuvem atualizada com sucesso!", Toast.LENGTH_SHORT).show();
                                        tvStatus.setText(novoStatus);
                                        atualizarCorStatus(tvStatus, novoStatus);

                                        // Atualiza a intent para caso o usuário rotacione a tela ou reabra
                                        getIntent().putExtra("status", novoStatus);
                                    } else {
                                        Toast.makeText(this, "Erro ao atualizar nuvem: " + e.getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                });
                            })
                            .show();
                });
            }

            historicoContainer.removeAllViews();
            TextView aviso = new TextView(this);
            aviso.setText("Histórico de intervenções disponível apenas nos chamados locais.");
            aviso.setTextColor(0xFF888888);
            historicoContainer.addView(aviso);

            return; // Garante que o código do SQLite abaixo não seja executado
        }

        // 📱 FLUXO DO SQLITE (LOCAL)
        if (atendimentoBotao != null) {
            atendimentoBotao.setVisibility(View.VISIBLE);
            atendimentoBotao.setText("Atendimento");
            atendimentoBotao.setOnClickListener(v -> {
                Intent intent = new Intent(this, Atendimento.class);
                intent.putExtra("chamado_id", chamadoId);
                startActivity(intent);
            });
        }

        Executors.newSingleThreadExecutor().execute(() -> {
            Chamado chamado = AppDatabase.getInstancia(this)
                    .chamadoDao().buscarPorId(chamadoId);
            List<Historico> historicos = AppDatabase.getInstancia(this)
                    .historicoDao().listarPorChamado(chamadoId);

            runOnUiThread(() -> {
                if (chamado != null) {
                    tvId.setText("#" + String.format("%03d", chamado.id));
                    tvTitulo.setText(chamado.titulo);
                    tvStatus.setText(chamado.status);
                    tvDescricao.setText(chamado.descricao);
                    tvData.setText(chamado.dataCadastro);
                    atualizarCorStatus(tvStatus, chamado.status);
                }

                historicoContainer.removeAllViews();
                if (historicos == null || historicos.isEmpty()) {
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

    // Método auxiliar para evitar código duplicado ao colorir os status
    private void atualizarCorStatus(TextView tvStatus, String status) {
        if (status == null) return;
        switch (status) {
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
    }
}