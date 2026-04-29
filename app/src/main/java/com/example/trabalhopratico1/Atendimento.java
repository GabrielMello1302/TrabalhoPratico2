package com.example.trabalhopratico1;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.Executors;

public class Atendimento extends AppCompatActivity {

    private int chamadoId;
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atendimento);

        db = AppDatabase.getInstancia(this);
        // Recebe o ID vindo da tela de Detalhes
        chamadoId = getIntent().getIntExtra("chamado_id", -1);
        Button voltarBotao = findViewById(R.id.voltar_botao3);
        voltarBotao.setOnClickListener(v -> finish());

        Spinner spinner = findViewById(R.id.status_spinner);
        String[] opcoesStatus = {"Aberto", "Em Andamento", "Concluído"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, opcoesStatus);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        findViewById(R.id.cancelar_botao).setOnClickListener(v -> finish());

        findViewById(R.id.salvar_atendimento_botao).setOnClickListener(v -> {
            salvarIntervencao();
        });
    }

    private void salvarIntervencao() {
        String tecnico = ((EditText) findViewById(R.id.tecnico_email_input)).getText().toString();
        String texto = ((EditText) findViewById(R.id.intervencao_input)).getText().toString();
        String novoStatus = ((Spinner) findViewById(R.id.status_spinner)).getSelectedItem().toString();
        String dataAtual = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(new Date());

        if (tecnico.isEmpty() || texto.isEmpty()) {
            Toast.makeText(this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show();
            return;
        }

        Executors.newSingleThreadExecutor().execute(() -> {
            // 1. Criar e inserir o histórico
            Historico h = new Historico();
            h.chamadoId = chamadoId;
            h.emailTecnico = tecnico;
            h.texto = texto;
            h.data = dataAtual;
            h.novoStatus = novoStatus;
            db.historicoDao().inserir(h);

            // 2. Atualizar o status no chamado principal
            Chamado chamado = db.chamadoDao().buscarPorId(chamadoId);
            if (chamado != null) {
                chamado.status = novoStatus;
                db.chamadoDao().atualizar(chamado);
            }

            runOnUiThread(() -> {
                Toast.makeText(this, "Intervenção registrada!", Toast.LENGTH_SHORT).show();
                finish(); // Volta para a tela de Detalhes
            });
        });
    }
}