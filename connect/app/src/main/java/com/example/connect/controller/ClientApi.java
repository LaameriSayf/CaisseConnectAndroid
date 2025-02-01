package com.example.connect.controller;

import com.example.connect.models.Client;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

public interface ClientApi {
    @GET("Client/getAll")
    Call<List<Client>> getAllClient();

    @POST("Client/add") // Assurez-vous que cette URL correspond à votre point d'entrée
    Call<Client> addClient(@Body Client client);

    @PUT("Client/update/{id}")
    Call<Client> updateClient(@Path("id") int id, @Body Client client);

    @DELETE("Client/delete/{id}")
    Call<Void> deleteClient(@Path("id") Long id);

    @GET("Client/getClientByRib/{rib}")
    Call<Client> getClientByRib(@Path("rib") String rib);
}
