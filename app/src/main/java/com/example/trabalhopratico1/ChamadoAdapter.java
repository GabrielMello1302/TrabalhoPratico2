package com.example.trabalhopratico1;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ChamadoAdapter extends RecyclerView.Adapter<ChamadoAdapter.ViewHolder> {

    //Interface para capturar o clique em um card
    public interface OnItemClickListener {
        void onItemClick(Chamado chamado);
    }

    private List<Chamado> listaChamados;
    private OnItemClickListener listener;

    public ChamadoAdapter(List<Chamado> listaChamados, OnItemClickListener listener) {
        this.listaChamados = listaChamados;
        this.listener = listener;
    }

    // Ao Criar a "view" de cada item — infla o item_chamado.xml
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_chamado, parent, false);
        return new ViewHolder(view);
    }

    // Preenche os dados de cada item
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Chamado chamado = listaChamados.get(position);

        holder.itemId.setText("#" + String.format("%03d", chamado.id));
        holder.itemTitulo.setText(chamado.titulo);
        holder.itemStatus.setText(chamado.status);
        holder.itemData.setText("Relatado em: " + chamado.dataCadastro);

        // Cor do badge muda conforme o status
        switch (chamado.status) {
            case "Em Andamento":
                holder.itemStatus.setBackgroundColor(0xFF1565C0);
                break;
            case "Concluído":
                holder.itemStatus.setBackgroundColor(0xFF2E7D32);
                break;
            default: // Aberto
                holder.itemStatus.setBackgroundColor(0xFF6750A4);
                break;
        }

        // Clique no card inteiro
        holder.itemView.setOnClickListener(v -> listener.onItemClick(chamado));
    }

    @Override
    public int getItemCount() {
        return listaChamados.size();
    }

    // ViewHolder — segura as referências dos campos do card
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView itemId, itemTitulo, itemStatus, itemData;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemId     = itemView.findViewById(R.id.item_id);
            itemTitulo = itemView.findViewById(R.id.item_titulo);
            itemStatus = itemView.findViewById(R.id.item_status);
            itemData   = itemView.findViewById(R.id.item_data);
        }
    }
}