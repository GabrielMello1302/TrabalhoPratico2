package com.example.trabalhopratico1;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface HistoricoDao {

    @Insert
    void inserir(Historico historico);

    @Query("SELECT * FROM historico WHERE chamadoId = :chamadoId ORDER BY id ASC")
    List<Historico> listarPorChamado(int chamadoId);
}