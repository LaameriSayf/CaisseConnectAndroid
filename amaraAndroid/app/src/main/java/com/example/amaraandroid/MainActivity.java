package com.example.amaraandroid;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.amaraandroid.controller.ClientApi;
import com.example.amaraandroid.models.Client;
import com.example.amaraandroid.utils.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private ClientApi clientApi;
    private EditText editName, editPrenom, editPhone, editRib;
    private Button buttonSubmit;
    private boolean isEditing = false;
    private int clientIdToEdit = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize views
        editName = findViewById(R.id.editName);
        editPrenom = findViewById(R.id.editPrenom);
        editPhone = findViewById(R.id.editPhone);
        editRib = findViewById(R.id.editRib);
        buttonSubmit = findViewById(R.id.buttonAddClient);

        // Retrofit API instance
        clientApi = RetrofitClient.getInstance().create(ClientApi.class);

        // Check for edit mode and retrieve the client object
        if (getIntent().hasExtra("client")) {
            Client clientToEdit = (Client) getIntent().getSerializableExtra("client");
            if (clientToEdit != null) {
                isEditing = true;
                clientIdToEdit = clientToEdit.getIdClient();  // Assuming this is a valid ID
                prefillForm(clientToEdit);
                buttonSubmit.setText("Modifier");  // Change the button text to "Modifier"
            }
        }

        buttonSubmit.setOnClickListener(v -> handleSubmit());
    }

    private void prefillForm(Client client) {
        editName.setText(client.getNom());
        editPrenom.setText(client.getPrenom());
        editPhone.setText(client.getTel());
        editRib.setText(client.getRib());
    }


    private void handleSubmit() {
        String name = editName.getText().toString().trim();
        String prenom = editPrenom.getText().toString().trim();
        String phone = editPhone.getText().toString().trim();
        String rib = editRib.getText().toString().trim();

        if (name.isEmpty() || prenom.isEmpty() || phone.isEmpty() || rib.isEmpty()) {
            Toast.makeText(this, "Tous les champs sont obligatoires", Toast.LENGTH_SHORT).show();
            return;
        }

        Client client = new Client(clientIdToEdit, name, prenom, phone, rib);

        if (isEditing) {
            updateClient(client);


        } else {
            addClient(client);

        }
    }

    private void updateClient(Client client) {
        clientApi.updateClient(client.getIdClient(), client).enqueue(new Callback<Client>() {
            @Override
            public void onResponse(Call<Client> call, Response<Client> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(MainActivity.this, "Client modifié avec succès", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity.this, AffichageClient.class);
                    startActivity(intent);
                    finish();
                    finish();
                } else {
                    Log.e("UPDATE_ERROR", "Code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Client> call, Throwable t) {
                Log.e("UPDATE_FAILURE", t.getMessage(), t);
            }
        });
    }

    private void addClient(Client client) {
        clientApi.addClient(client).enqueue(new Callback<Client>() {
            @Override
            public void onResponse(Call<Client> call, Response<Client> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(MainActivity.this, "Client ajouté avec succès", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity.this, AffichageClient.class);
                    startActivity(intent);
                    finish();
                } else {
                    Log.e("ADD_ERROR", "Code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Client> call, Throwable t) {
                Log.e("ADD_FAILURE", t.getMessage(), t);
            }
        });
    }
}
