package com.example.trabalhopratico1;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ChamadoDao {

    @Insert
    void inserir(Chamado chamado);

    @Update
    void atualizar(Chamado chamado);

    @Query("SELECT * FROM chamados ORDER BY id DESC")
    List<Chamado> listarTodos();

    @Query("SELECT * FROM chamados WHERE id = :id")
    Chamado buscarPorId(int id);

    @Query("SELECT * FROM chamados WHERE status = :status ORDER BY id DESC")
    List<Chamado> filtrarPorStatus(String status);

    @Query("SELECT * FROM chamados WHERE status = :statusSolicitado")
    List<Chamado> listarPorStatus(String statusSolicitado);
}