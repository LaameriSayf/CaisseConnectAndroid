package com.example.connect.models;


import java.io.Serializable;

public class Client implements Serializable {
    private long idClient; // Id du client
    private String nom;
    private String prenom;// Nom du client


    private String tel;  // Email du client
    private String rib;    // RIB du client

    // Constructeurs
    public Client() {
    }

    public Client(int idClient, String nom, String tel, String rib) {
        this.idClient = idClient;
        this.nom = nom;

        this.tel = tel;
        this.rib = rib;
    }
    public Client(String nom, String tel) {

        this.nom = nom;
        this.tel = tel;
    }

    public Client(int idClient, String nom, String prenom, String tel, String rib) {
        this.idClient = idClient;
        this.nom = nom;
        this.prenom = prenom;
        this.tel = tel;
        this.rib = rib;
    }

    public Client(String nom, String prenom, String tel, String rib) {
        this.nom = nom;
        this.prenom=prenom;
        this.tel = tel;
        this.rib = rib;
    }


    // Getters et Setters
    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public long getIdClient() {
        return idClient;
    }


    public void setIdClient(int idClient) {
        this.idClient = idClient;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getRib() {
        return rib;
    }

    public void setRib(String rib) {
        this.rib = rib;
    }

    @Override
    public String toString() {
        return "Client{" +
                "name='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", tel='" + tel + '\'' +
                ", rib='" + rib + '\'' +
                '}';
    }

}
