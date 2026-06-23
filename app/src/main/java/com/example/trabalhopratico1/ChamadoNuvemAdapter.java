package com.example.trabalhopratico1; // Mantenha o seu pacote correto

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64; // Importante para decodificar o "stringão"
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.parse.ParseObject;

import java.util.List;

public class ChamadoNuvemAdapter extends RecyclerView.Adapter<ChamadoNuvemAdapter.ViewHolder> {

    private List<ParseObject> listaChamados;

    public ChamadoNuvemAdapter(List<ParseObject> listaChamados) {
        this.listaChamados = listaChamados;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chamado, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ParseObject chamado = listaChamados.get(position);

        // 🔥 SOLUÇÃO DO ID: Calcula o número sequencial com base na posição e tamanho da lista
        int numeroChamado = listaChamados.size() - position;
        String idFormatado = String.format("#%03d", numeroChamado);

        // Preenche os textos (concatenando o ID calculado com o título)
        holder.txtTitulo.setText(idFormatado + " " + chamado.getString("titulo"));
        holder.txtLocal.setText("Local: " + chamado.getString("local"));
        holder.txtStatus.setText("Status: " + chamado.getString("status"));

        // Carrega a imagem em Base64
        String imagemBase64 = chamado.getString("imagem_base64");
        if (imagemBase64 != null && !imagemBase64.isEmpty()) {
            try {
                byte[] decodedString = Base64.decode(imagemBase64, Base64.DEFAULT);
                Bitmap bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                holder.imgMiniatura.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
                holder.imgMiniatura.setImageDrawable(null);
            }
        } else {
            holder.imgMiniatura.setImageDrawable(null);
        }

        holder.itemView.setOnClickListener(v -> {
            android.content.Intent intent = new android.content.Intent(v.getContext(), Detalhes.class);

            intent.putExtra("origem", "nuvem");
            intent.putExtra("id_formatado", idFormatado);
            intent.putExtra("object_id", chamado.getObjectId());
            intent.putExtra("titulo", chamado.getString("titulo"));
            intent.putExtra("descricao", chamado.getString("descricao"));
            intent.putExtra("local", chamado.getString("local"));
            intent.putExtra("status", chamado.getString("status"));
            intent.putExtra("imagem_base64", imagemBase64);

            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return listaChamados.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtTitulo, txtLocal, txtStatus;
        ImageView imgMiniatura;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // Vinculando os componentes do item_chamado.xml
            txtTitulo = itemView.findViewById(R.id.item_titulo);
            txtLocal = itemView.findViewById(R.id.item_local);
            txtStatus = itemView.findViewById(R.id.item_status);
            imgMiniatura = itemView.findViewById(R.id.img_miniatura);
        }
    }
}