package com.example.connect.interfaces;


import com.example.connect.models.Commande;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.*;

public interface CommandeAPI {

    @POST("commande/AjouterCommandeEtAffecterAclient/{idClient}")
    Call<Commande> addCommande(@Body Commande commande, @Path("idClient") Long idClient);


    @PUT("commande/UpdateCommande/{idCommande}")
    Call<Void> updateCommande(@Path("idCommande") Long idCommande, @Body Commande commande);

    @GET("commande/getCommande/{idCommande}")
    Call<Commande> getCommande(@Path("idCommande") Long idCommande);

    @GET("commande/getCommandes")
    Call<List<Commande>> getCommandes();

    @DELETE("commande/DeleteCommande/{idCommande}")
    Call<Void> deleteCommande(@Path("idCommande") Long idCommande);

    @GET("commande/getCommandebyCheque/{idCheque}")
    Call<Commande> getCommandeByCheque(@Path("idCheque") Long idCheque);

    @GET("commande/getCommandebyCheque/{idLettreEchange}")
    Call<Commande> getCommandeByLettre(@Path("idLettreEchange") Long idLettreEchange);

    @GET("commande/findCommandeByDate/{DateCommande}")
    Call<List<Commande>> findCommandeByDate(@Path("DateCommande") String dateCommande);

    @GET("/commande/findCommandeByEtat/{etat}")
    Call<List<Commande>> findCommandeByEtat(@Path("etat") String etat);



}
