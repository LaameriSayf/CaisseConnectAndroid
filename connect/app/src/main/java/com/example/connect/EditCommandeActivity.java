package com.example.connect;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.connect.models.Commande;
import com.example.connect.models.Etat;
import com.example.connect.models.ModePaiement;
import com.example.connect.repositories.CommandeService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class EditCommandeActivity extends AppCompatActivity {

    private EditText editMontant, editQuantite, editRemarque, editDate;
    private Spinner spinnerEtat, spinnerModePaiement;
    private Button btnUpdateCommande;
    private CommandeService commandeService;
    private Commande currentCommande;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_commande);

        // Initialize views
        editMontant = findViewById(R.id.edit_montant_commande);
        editQuantite = findViewById(R.id.edit_quantite_commande);
        editRemarque = findViewById(R.id.edit_remarque_commande);
        spinnerEtat = findViewById(R.id.spinner_etat_commande);
        spinnerModePaiement = findViewById(R.id.spinner_modePaiement_commande);
        btnUpdateCommande = findViewById(R.id.btn_edit_commande);
        editDate = findViewById(R.id.edit_date_commande);

        commandeService = new CommandeService();

        // Get the Commande object from the intent
        currentCommande = (Commande) getIntent().getSerializableExtra("commande");
        if (currentCommande == null) {
            Toast.makeText(this, "No Commande data provided", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Setup Spinners with Enum data
        setupSpinner(spinnerEtat, Etat.values());
        setupSpinner(spinnerModePaiement, ModePaiement.values());

        // Populate the fields with the current Commande data
        populateFields();

        // Update Commande button click listener
        btnUpdateCommande.setOnClickListener(v -> updateCommande());
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

    private void populateFields() {
        editMontant.setText(String.valueOf(currentCommande.getMontant()));
        editQuantite.setText(String.valueOf(currentCommande.getQuantite()));
        editRemarque.setText(currentCommande.getRemarque());

        // Set the date in the EditText field in ISO 8601 format
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault());
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        editDate.setText(dateFormat.format(currentCommande.getDateCommande()));

        // Set the selected items in the Spinners
        spinnerEtat.setSelection(((ArrayAdapter<String>) spinnerEtat.getAdapter()).getPosition(currentCommande.getEtat().name()));
        spinnerModePaiement.setSelection(((ArrayAdapter<String>) spinnerModePaiement.getAdapter()).getPosition(currentCommande.getModePaiement().name()));
    }

    private void updateCommande() {
        String montantStr = editMontant.getText().toString().trim();
        String quantiteStr = editQuantite.getText().toString().trim();
        String remarque = editRemarque.getText().toString().trim();
        String etatStr = spinnerEtat.getSelectedItem().toString();
        String modePaiementStr = spinnerModePaiement.getSelectedItem().toString();

        if (montantStr.isEmpty() || quantiteStr.isEmpty()) {
            Toast.makeText(this, "Please fill in all required fields", Toast.LENGTH_SHORT).show();
            return;
        }

        double montant;
        double quantite;

        try {
            montant = Double.parseDouble(montantStr);
            quantite = Double.parseDouble(quantiteStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid Montant or Quantité", Toast.LENGTH_SHORT).show();
            return;
        }

        Etat etat = Etat.valueOf(etatStr);
        ModePaiement modePaiement = ModePaiement.valueOf(modePaiementStr);

        // Get the date from the EditText field (ISO format with time and Z)
        String dateCommandeStr = editDate.getText().toString();

        // Ensure the date format is valid (ISO 8601)
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault());
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date dateCommande;

        try {
            dateCommande = dateFormat.parse(dateCommandeStr);
        } catch (ParseException e) {
            Toast.makeText(this, "Error parsing the date", Toast.LENGTH_SHORT).show();
            return;
        }

        // Update the current Commande object
        currentCommande.setMontant(montant);
        currentCommande.setQuantite(quantite);
        currentCommande.setRemarque(remarque);
        currentCommande.setEtat(etat);
        currentCommande.setModePaiement(modePaiement);
        currentCommande.setDateCommande(dateCommande);

        // Call the CommandeService to update the Commande
        commandeService.updateCommande(currentCommande.getIdCommande(), currentCommande, new CommandeService.UpdateCommandeCallback() {
            @Override
            public void onSuccess(Commande commande) {
                Toast.makeText(EditCommandeActivity.this, "Commande updated successfully!", Toast.LENGTH_SHORT).show();
                finish(); // Close the activity when successful
            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(EditCommandeActivity.this, "Failed to update Commande", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
