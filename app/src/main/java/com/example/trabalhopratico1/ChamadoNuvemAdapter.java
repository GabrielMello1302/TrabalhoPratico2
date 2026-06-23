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

        // Preenche os textos buscando as chaves exatas que salvamos no Back4App
        holder.txtTitulo.setText(chamado.getString("titulo"));
        holder.txtLocal.setText("Local: " + chamado.getString("local"));
        holder.txtStatus.setText("Status: " + chamado.getString("status"));

        // 1. Pegamos o "stringão" Base64 que representa a imagem
        String imagemBase64 = chamado.getString("imagem_base64");

        // 2. Se houver um texto de imagem salvo, transformamos de volta em foto
        if (imagemBase64 != null && !imagemBase64.isEmpty()) {
            try {
                // Decodifica a String de volta para um array de bytes
                byte[] decodedString = Base64.decode(imagemBase64, Base64.DEFAULT);

                // Converte o array de bytes para um Bitmap utilizável pelo Android
                Bitmap bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

                // Exibe a foto na miniatura do item
                holder.imgMiniatura.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
                holder.imgMiniatura.setImageDrawable(null); // Caso dê erro na conversão, limpa o espaço
            }
        } else {
            // Se o chamado não tiver foto associada, limpa a miniatura
            holder.imgMiniatura.setImageDrawable(null);
        }
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
            txtLocal = itemView.findViewById(R.id.txt_item_local);
            txtStatus = itemView.findViewById(R.id.item_status);
            imgMiniatura = itemView.findViewById(R.id.img_miniatura);
        }
    }
}