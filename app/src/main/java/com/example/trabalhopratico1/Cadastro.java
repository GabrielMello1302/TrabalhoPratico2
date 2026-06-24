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
    private String imagemBase64 = null;

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
                        // Pega a foto que voltou da camêra
                        android.os.Bundle extras = result.getData().getExtras();
                        android.graphics.Bitmap imageBitmap = (android.graphics.Bitmap) extras.get("data");

                        // Mostra na tela
                        imgViewFoto.setImageBitmap(imageBitmap);

                        // Converte a foto para salvar na Nuvem (Back4App)
                        // Dentro do seu cameraLauncher,depois de pegar o imageBitmap...
                        java.io.ByteArrayOutputStream stream = new java.io.ByteArrayOutputStream();
                        imageBitmap.compress(android.graphics.Bitmap.CompressFormat.PNG, 100, stream);
                        byte[] bytes = stream.toByteArray();

// Transforma os bytes no "stringão" (Base64)
                        imagemBase64 = android.util.Base64.encodeToString(bytes, android.util.Base64.DEFAULT);
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

        // Botão salvar
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

            // Salva no banco numa thread separada
            Executors.newSingleThreadExecutor().execute(() -> {
                AppDatabase.getInstancia(this).chamadoDao().inserir(chamado);

                // Volta para a UI thread para mostrar o Toast e fechar a tela
                runOnUiThread(() -> {
                    Toast.makeText(this, "Chamado registrado!", Toast.LENGTH_SHORT).show();
                    finish(); // fecha a tela e volta para a Home
                });
            });
            // 1. Cria o objeto do chamado com os textos
            com.parse.ParseObject chamadoNuvem = new com.parse.ParseObject("Chamado");
            chamadoNuvem.put("titulo", chamado.titulo);
            chamadoNuvem.put("descricao", chamado.descricao);
            chamadoNuvem.put("status", chamado.status);

            // 2. Salva a imagem como um texto gigante (se existir)
                if (imagemBase64 != null) {
                    chamadoNuvem.put("imagem_base64", imagemBase64);
                }

            // 3. Salva tudo de uma vez só!
                chamadoNuvem.saveInBackground(new com.parse.SaveCallback() {
                @Override
                public void done(com.parse.ParseException e) {
                    if (e == null) {
                        android.widget.Toast.makeText(Cadastro.this, "Chamado salvo com sucesso!", android.widget.Toast.LENGTH_SHORT).show();
                        imagemBase64 = null; // Limpa para o próximo
                        finish();
                    } else {
                        android.widget.Toast.makeText(Cadastro.this, "Erro: " + e.getMessage(), android.widget.Toast.LENGTH_LONG).show();
                    }
                }
            });
        });
    }
}