package com.example.trabalhopratico1;


import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "historico")
public class Historico {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public int chamadoId;     // chave estrangeira (qual chamado pertence)
    public String emailTecnico;
    public String texto;
    public String data;
    public String novoStatus; // status que foi definido nessa intervenção
}