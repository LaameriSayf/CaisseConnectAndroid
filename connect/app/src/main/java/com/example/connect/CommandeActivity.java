package com.example.connect;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.connect.adapters.CommandeAdapter;
import com.example.connect.models.Commande;
import com.example.connect.repositories.CommandeService;
import java.util.List;

public class CommandeActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CommandeAdapter commandeAdapter;
    private CommandeService commandeService;
    private Spinner spinnerEtat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commande);

        recyclerView = findViewById(R.id.recyclerViewCommandes);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        commandeService = new CommandeService();

        spinnerEtat = findViewById(R.id.spinnerEtat);

        spinnerEtat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selectedEtat = parentView.getItemAtPosition(position).toString();
                filterCommandesByEtat(selectedEtat);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                filterCommandesByEtat("All");
            }
        });

        Button btnAddCommande = findViewById(R.id.btnAddCommande);
        btnAddCommande.setOnClickListener(v -> {
            Intent intent = new Intent(CommandeActivity.this, AddCommandeActivity.class);
            startActivity(intent);
        });

        fetchAndDisplayCommandes();
    }
    private void fetchAndDisplayCommandes() {
        commandeService.getCommandes(new CommandeService.CommandeCallback() {
            @Override
            public void onSuccess(List<Commande> commandes) {
                if (commandes != null && !commandes.isEmpty()) {
                    commandeAdapter = new CommandeAdapter(commandes);
                    recyclerView.setAdapter(commandeAdapter);
                } else {
                    Toast.makeText(CommandeActivity.this, "No commandes available", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(CommandeActivity.this, "Failed to load commandes", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void filterCommandesByEtat(String etatFilter) {
        if (commandeAdapter != null) {
            commandeAdapter.filterByEtat(etatFilter);
        }
    }
}
