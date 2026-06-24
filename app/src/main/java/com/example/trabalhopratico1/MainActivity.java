package com.example.trabalhopratico1;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnNovoChamado = findViewById(R.id.chamado_botao);
        Button btnVerChamados = findViewById(R.id.ver_chamado_botao);
        Button btnFiltrar    = findViewById(R.id.filtrar_botao);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.nav_novo_chamado) {
                    startActivity(new Intent(MainActivity.this, Cadastro.class));
                } else if (id == R.id.nav_listagem) {
                    startActivity(new Intent(MainActivity.this, Listagem.class));
                } else if (id == R.id.nav_estatisticas) {
                    startActivity(new Intent(MainActivity.this, Estatisticas.class));
                } else if (id == R.id.nav_sobre) {
                    startActivity(new Intent(MainActivity.this, Sobre.class));
                }

                // Fecha o menu após clicar
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });

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
        // Forma moderna de lidar com o botão de voltar
        getOnBackPressedDispatcher().addCallback(this, new androidx.activity.OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    // Se o menu estiver fechado, permite que o botão voltar ha já normalmente
                    setEnabled(false);
                    getOnBackPressedDispatcher().onBackPressed();
                }
            }
        });
    }
}