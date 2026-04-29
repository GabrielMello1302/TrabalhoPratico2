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

public class Cadastro extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        // Liga as variáveis aos campos do XML
        EditText tituloInput    = findViewById(R.id.titulo_input);
        EditText descricaoInput = findViewById(R.id.descricao_input);
        Button   salvarBotao    = findViewById(R.id.salvar_botao);
        Button   inicioBotao    = findViewById(R.id.inicio_botao);

        // Preenche o Spinner com as opções de tipo
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                new String[]{"Infraestrutura", "TI"}
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Botão Voltar
        inicioBotao.setOnClickListener(v -> finish());

        // Botão Salvar
        salvarBotao.setOnClickListener(v -> {

            String titulo    = tituloInput.getText().toString().trim();
            String data      = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(new Date());
            String descricao = descricaoInput.getText().toString().trim();

            // Validação básica
            if (titulo.isEmpty() || data.isEmpty() || descricao.isEmpty()) {
                Toast.makeText(this, "Preencha os campos obrigatórios.", Toast.LENGTH_SHORT).show();
                return;
            }

            // Monta o objeto Chamado
            Chamado chamado    = new Chamado();
            chamado.titulo     = titulo;
            chamado.dataCadastro = data;
            chamado.descricao  = descricao;
            chamado.status     = "Aberto"; // sempre começa como Aberto

            // Salva no banco em uma thread separada
            Executors.newSingleThreadExecutor().execute(() -> {
                AppDatabase.getInstancia(this).chamadoDao().inserir(chamado);

                // Volta para a UI thread para mostrar o Toast e fechar a tela
                runOnUiThread(() -> {
                    Toast.makeText(this, "Chamado registrado!", Toast.LENGTH_SHORT).show();
                    finish(); // fecha a tela e volta para a Home
                });
            });
        });
    }
}