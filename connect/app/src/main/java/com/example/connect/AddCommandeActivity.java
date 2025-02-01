package com.example.connect;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.connect.interfaces.CommandeAPI;
import com.example.connect.models.Commande;
import com.example.connect.models.Etat;
import com.example.connect.models.ModePaiement;
import com.example.connect.models.Client;
import com.example.connect.controller.ClientApi;
import com.example.connect.repositories.CommandeService;
import com.example.connect.utils.RetrofitClientsaif;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddCommandeActivity extends AppCompatActivity {

    private EditText editMontant, editQuantite, editRemarque, editDate;
    private Spinner spinnerEtat, spinnerModePaiement, spinnerClients;
    private Button btnAddCommande;
    private List<Client> clientList;
    private List<String> clientNames;
    private List<Long> clientIds;
    private CommandeService commandeService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_commande);

        editMontant = findViewById(R.id.edit_montant);
        editQuantite = findViewById(R.id.edit_quantite);
        editRemarque = findViewById(R.id.edit_remarque);
        spinnerEtat = findViewById(R.id.spinner_etat);
        spinnerModePaiement = findViewById(R.id.spinner_modePaiement);
        spinnerClients = findViewById(R.id.spinner_clients);
        btnAddCommande = findViewById(R.id.btn_add_commande);
        editDate = findViewById(R.id.edit_date);

        clientList = new ArrayList<>();
        clientNames = new ArrayList<>();
        clientIds = new ArrayList<>();

        fetchClients();
        setupSpinner(spinnerEtat, Etat.values());
        setupSpinner(spinnerModePaiement, ModePaiement.values());
        setCurrentDate();

        btnAddCommande.setOnClickListener(v -> addCommande());
    }

    private <T extends Enum<T>> void setupSpinner(Spinner spinner, T[] values) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, getEnumNames(values));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    private <T extends Enum<T>> List<String> getEnumNames(T[] values) {
        List<String> list = new ArrayList<>();
        for (T value : values) {
            list.add(value.name());
        }
        return list;
    }

    private void fetchClients() {
        ClientApi apiService = RetrofitClientsaif.getInstance().create(ClientApi.class);
        Call<List<Client>> call = apiService.getAllClient();

        call.enqueue(new Callback<List<Client>>() {
            @Override
            public void onResponse(Call<List<Client>> call, Response<List<Client>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    clientList = response.body();
                    for (Client client : clientList) {
                        clientNames.add(client.getNom());
                        clientIds.add(client.getIdClient());
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(AddCommandeActivity.this, android.R.layout.simple_spinner_item, clientNames);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerClients.setAdapter(adapter);
                } else {
                    Toast.makeText(AddCommandeActivity.this, "Failed to load clients", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Client>> call, Throwable t) {
                Toast.makeText(AddCommandeActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setCurrentDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX", Locale.getDefault());
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        String currentDateStr = dateFormat.format(new Date());
        editDate.setText(currentDateStr);
    }

    private void addCommande() {
        String montantStr = editMontant.getText().toString().trim();
        String quantiteStr = editQuantite.getText().toString().trim();
        String remarque = editRemarque.getText().toString().trim();
        String etatStr = spinnerEtat.getSelectedItem().toString();
        String modePaiementStr = spinnerModePaiement.getSelectedItem().toString();

        if (montantStr.isEmpty() || quantiteStr.isEmpty()) {
            Toast.makeText(this, "Please fill in all required fields", Toast.LENGTH_SHORT).show();
            return;
        }

        double montant, quantite;
        try {
            montant = Double.parseDouble(montantStr);
            quantite = Double.parseDouble(quantiteStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid Montant or Quantité", Toast.LENGTH_SHORT).show();
            return;
        }

        Etat etat = Etat.valueOf(etatStr);
        ModePaiement modePaiement = ModePaiement.valueOf(modePaiementStr);

        int selectedClientIndex = spinnerClients.getSelectedItemPosition();
        Long idClient = clientIds.get(selectedClientIndex);

        // Get the current date
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date dateCommande = new Date();

        Commande newCommande = new Commande(
                null,
                quantite,
                dateCommande,
                remarque,
                montant,
                etat,
                modePaiement
        );

        CommandeService commandeService = new CommandeService();
        commandeService.addCommande(newCommande, idClient, new CommandeService.AddCommandeCallback() {
            @Override
            public void onSuccess(Commande commande) {
                Toast.makeText(AddCommandeActivity.this, "Commande added successfully!", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(AddCommandeActivity.this, "Failed to add Commande", Toast.LENGTH_SHORT).show();
            }
        });
    }


}