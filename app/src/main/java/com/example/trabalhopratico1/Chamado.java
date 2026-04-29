package com.example.trabalhopratico1;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "chamados")
public class Chamado {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public String titulo;
    public String descricao;
    public String dataCadastro;
    public String status;     // "Aberto", "Em Andamento", "Concluído"
}