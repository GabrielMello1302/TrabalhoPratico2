package com.example.trabalhopratico1; // Mantenha a primeira linha do seu pacote!

import android.app.Application;
import com.parse.Parse;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // Lembre-se de colocar as suas chaves do Back4App aqui
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("COLE_SEU_APP_ID_AQUI")
                .clientKey("COLE_SEU_CLIENT_KEY_AQUI")
                .server("https://parseapi.back4app.com")
                .build()
        );
    }
}