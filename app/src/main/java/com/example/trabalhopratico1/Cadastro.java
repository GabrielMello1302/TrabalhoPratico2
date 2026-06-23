package com.example.trabalhopratico1;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.Executors;

public class Cadastro extends AppCompatActivity {
    private ImageView imgViewFoto;
    private byte[] imagemBytes = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        imgViewFoto = findViewById(R.id.img_foto);
        Button btnCamera = findViewById(R.id.btn_camera);

        // Liga as variáveis aos campos do XML
        EditText tituloInput    = findViewById(R.id.titulo_input);
        EditText descricaoInput = findViewById(R.id.descricao_input);
        Button   salvarBotao    = findViewById(R.id.salvar_botao);
        Button   inicioBotao    = findViewById(R.id.inicio_botao);

        androidx.activity.result.ActivityResultLauncher<Intent> cameraLauncher = registerForActivityResult(
                new androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        // Pega a foto que voltou da câmera
                        android.os.Bundle extras = result.getData().getExtras();
                        android.graphics.Bitmap imageBitmap = (android.graphics.Bitmap) extras.get("data");

                        // Mostra na tela
                        imgViewFoto.setImageBitmap(imageBitmap);

                        // Converte a foto para salvar na Nuvem (Back4App)
                        java.io.ByteArrayOutputStream stream = new java.io.ByteArrayOutputStream();
                        imageBitmap.compress(android.graphics.Bitmap.CompressFormat.PNG, 100, stream);
                        imagemBytes = stream.toByteArray();
                    }
                }
        );

        btnCamera.setOnClickListener(v -> {
            Intent takePictureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            cameraLauncher.launch(takePictureIntent);
        });

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


            //2. AQUI COMEÇA O SALVAMENTO NA NUVEM (Back4App)
            com.parse.ParseObject chamadoNuvem = new com.parse.ParseObject("Chamado");
            chamadoNuvem.put("titulo", titulo);
            chamadoNuvem.put("descricao", descricao);
            chamadoNuvem.put("status", chamado.status);

            // Se o usuário tirou uma foto, anexa ao documento da nuvem
            if (imagemBytes != null) {
                com.parse.ParseFile parseFile = new com.parse.ParseFile("foto.png", imagemBytes);
                chamadoNuvem.put("imagem", parseFile);
            }

            // Salva em segundo plano para não travar a tela
            chamadoNuvem.saveInBackground(e -> {
                if (e == null) {
                    android.widget.Toast.makeText(Cadastro.this, "Salvo localmente e na Nuvem!", android.widget.Toast.LENGTH_SHORT).show();
                    finish(); // Fecha a tela e volta pra listagem
                } else {
                    android.widget.Toast.makeText(Cadastro.this, "Erro na nuvem: " + e.getMessage(), android.widget.Toast.LENGTH_LONG).show();
                }
            });
        });
    }
}