package com.example.amaraandroid.utils;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static Retrofit retrofit;

    private static final String BASE_URL ="http://192.168.1.102:8089/CaisseConnect/"; // URL du backend

    OkHttpClient client = new OkHttpClient.Builder()
            .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .build();

    // Méthode pour obtenir l'instance Retrofit
    public static Retrofit getInstance() {
        if (retrofit == null) {
            synchronized (RetrofitClient.class) { // Synchronisation pour éviter la création multiple dans des threads différents
                if (retrofit == null) {
                    retrofit = new Retrofit.Builder()
                            .baseUrl(BASE_URL)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();
                }
            }
        }
        return retrofit;
    }
}
