package com.example.trabalhopratico1;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.trabalhopratico1.Chamado;
import com.example.trabalhopratico1.ChamadoDao;
import com.example.trabalhopratico1.Historico;
import com.example.trabalhopratico1.HistoricoDao;

@Database(entities = {Chamado.class, Historico.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase instancia;

    public abstract ChamadoDao chamadoDao();
    public abstract HistoricoDao historicoDao();

    // Singleton — garante que só existe UMA conexão com o banco
    public static synchronized AppDatabase getInstancia(Context context) {
        if (instancia == null) {
            instancia = Room.databaseBuilder(
                    context.getApplicationContext(),
                    AppDatabase.class,
                    "banco_chamados"
            ).build();
        }
        return instancia;
    }
}