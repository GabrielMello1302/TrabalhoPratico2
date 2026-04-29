package com.example.trabalhopratico1;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnNovoChamado = findViewById(R.id.chamado_botao);
        Button btnVerChamados = findViewById(R.id.ver_chamado_botao);
        Button btnFiltrar    = findViewById(R.id.filtrar_botao);

        // Vai para a tela de Cadastro
        btnNovoChamado.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, Cadastro.class);
            startActivity(intent);
        });

        // Vai para a tela de Listagem
        btnVerChamados.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, Listagem.class);
            startActivity(intent);
        });

        // Vai para a tela de Filtros
        btnFiltrar.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, Filtros.class);
            startActivity(intent);
        });
    }
}