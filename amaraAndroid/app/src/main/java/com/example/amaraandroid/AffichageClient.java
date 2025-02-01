package com.example.amaraandroid;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.amaraandroid.adapter.ClientAdapter;
import com.example.amaraandroid.controller.ClientApi;
import com.example.amaraandroid.models.Client;
import com.example.amaraandroid.utils.RetrofitClient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AffichageClient extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ClientAdapter clientAdapter;
    private List<Client> clientList;
    private EditText searchField;
    private Button btnAddClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_affichage_client);

        // Initialize components
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        searchField = findViewById(R.id.searchField);
        btnAddClient = findViewById(R.id.btnAddClient); // Move initialization here

        clientList = new ArrayList<>();
        clientAdapter = new ClientAdapter(this, clientList);
        recyclerView.setAdapter(clientAdapter);

        // Fetch clients from API
        fetchClients();



        // Dynamic search
        searchField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterClients(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Sort buttons
        findViewById(R.id.btnSortAsc).setOnClickListener(v -> sortClients(true));
        findViewById(R.id.btnSortDesc).setOnClickListener(v -> sortClients(false));
        // Set button click listener
        btnAddClient.setOnClickListener(view -> {
            Intent intent = new Intent(AffichageClient.this, MainActivity.class);
            startActivity(intent);
        });
    }

    // Fetch clients from the API
    private void fetchClients() {
        ClientApi apiService = RetrofitClient.getInstance().create(ClientApi.class);
        Call<List<Client>> call = apiService.getAllClient();

        call.enqueue(new Callback<List<Client>>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(Call<List<Client>> call, Response<List<Client>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    clientList.clear();
                    clientList.addAll(response.body());
                    clientAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(AffichageClient.this, "Erreur de réponse", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Client>> call, Throwable t) {
                Toast.makeText(AffichageClient.this, "Erreur réseau: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Filter clients based on search query
    private void filterClients(String query) {
        List<Client> filteredList = new ArrayList<>();
        for (Client client : clientList) {
            if (client.getNom().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(client);
            }
        }
        clientAdapter.updateData(filteredList);
    }

    // Sort clients alphabetically
    private void sortClients(boolean ascending) {
        if (ascending) {
            Collections.sort(clientList, Comparator.comparing(Client::getNom));
        } else {
            Collections.sort(clientList, (c1, c2) -> c2.getNom().compareTo(c1.getNom()));
        }
        clientAdapter.notifyDataSetChanged();
    }
}
