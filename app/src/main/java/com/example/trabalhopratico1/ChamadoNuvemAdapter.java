package com.example.trabalhopratico1; // Confirme se o pacote está correto

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.parse.ParseFile;
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

        // Preenche os textos buscando as chaves exatas que você salvou no Back4App
        holder.txtTitulo.setText(chamado.getString("titulo"));
        holder.txtLocal.setText("Local: " + chamado.getString("local"));
        holder.txtStatus.setText("Status: " + chamado.getString("status"));

        // Carrega a imagem da nuvem, se existir
        ParseFile imagemFile = chamado.getParseFile("imagem");
        if (imagemFile != null) {
            imagemFile.getDataInBackground((data, e) -> {
                if (e == null && data != null) {
                    Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                    holder.imgMiniatura.setImageBitmap(bitmap);
                }
            });
        } else {
            // Se não tiver imagem, limpa o ImageView ou coloca uma imagem padrão
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
            // Mapeando os IDs do XML item_chamado.xml
            txtTitulo = itemView.findViewById(R.id.item_titulo);
            txtLocal = itemView.findViewById(R.id.txt_item_local);
            txtStatus = itemView.findViewById(R.id.item_status);
            imgMiniatura = itemView.findViewById(R.id.img_miniatura);
        }
    }
}