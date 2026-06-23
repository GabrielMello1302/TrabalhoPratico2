package com.example.trabalhopratico1; // Mantenha a primeira linha do seu pacote!

import android.app.Application;
import com.parse.Parse;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // Lembre-se de colocar as suas chaves do Back4App aqui
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("t96QKPqbLVx3Bk7Dz6VdisRqg1o2o7K9agwxpanh")
                .clientKey("Qg0MQoS1ocCQI0kkq4ov62mpwiclqNOtNhaRmNYw")
                .server("https://parseapi.back4app.com")
                .build()
        );
    }
}