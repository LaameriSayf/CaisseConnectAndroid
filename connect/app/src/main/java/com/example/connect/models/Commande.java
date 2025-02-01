package com.example.connect.models;

import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.internal.bind.DateTypeAdapter;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Commande implements Serializable {

    @SerializedName("idCommande")
    private Long idCommande;

    @SerializedName("quantite")
    private double quantite;

    @SerializedName("dateCommande")
    private Date dateCommande;

    @SerializedName("remarque")
    private String remarque;

    @SerializedName("montant")
    private double montant;

    @SerializedName("etat")
    private Etat etat;

    @SerializedName("modePaiement")
    private ModePaiement modePaiement;



    public Commande() {}

    public Commande(Long idCommande, double quantite, Date dateCommande, String remarque, double montant, Etat etat, ModePaiement modePaiement) {
        this.idCommande = idCommande;
        this.quantite = quantite;
        this.dateCommande = dateCommande;
        this.remarque = remarque;
        this.montant = montant;
        this.etat = etat;
        this.modePaiement = modePaiement;
    }



    // Getters and Setters
    public Long getIdCommande() {
        return idCommande;
    }

    public void setIdCommande(Long idCommande) {
        this.idCommande = idCommande;
    }

    public double getQuantite() {
        return quantite;
    }

    public void setQuantite(double quantite) {
        this.quantite = quantite;
    }

    public Date getDateCommande() {
        return dateCommande;
    }

    public void setDateCommande(Date dateCommande) {
        this.dateCommande = dateCommande;
    }

    public String getRemarque() {
        return remarque;
    }

    public void setRemarque(String remarque) {
        this.remarque = remarque;
    }

    public double getMontant() {
        return montant;
    }

    public void setMontant(double montant) {
        this.montant = montant;
    }

    public Etat getEtat() {
        return etat;
    }

    public void setEtat(Etat etat) {
        this.etat = etat;
    }

    public ModePaiement getModePaiement() {
        return modePaiement;
    }

    public void setModePaiement(ModePaiement modePaiement) {
        this.modePaiement = modePaiement;
    }
    // Enum for ModePaiement


    @Override
    public String toString() {
        return
                "idCommande=" + getIdCommande() +
                ", quantite=" + getQuantite() +
                ", dateCommande=" + getDateCommande() +
                ", remarque='" + getRemarque() + '\'' +
                ", montant=" + getMontant() +
                ", etat='" + getEtat() + '\'' +
                ", modePaiement=" + getModePaiement() +
                '}';
    }


}
