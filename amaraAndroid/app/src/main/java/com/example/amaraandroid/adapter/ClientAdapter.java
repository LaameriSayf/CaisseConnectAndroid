package com.example.amaraandroid.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.amaraandroid.MainActivity;
import com.example.amaraandroid.R;
import com.example.amaraandroid.models.Client;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ClientAdapter extends RecyclerView.Adapter<ClientAdapter.ClientViewHolder> {

    private Context context;
    private List<Client> clientList;
    private List<Client> clientListFull; // Liste complète pour la recherche

    // Constructor
    public ClientAdapter(Context context, List<Client> clientList) {
        this.context = context;
        this.clientList = clientList != null ? clientList : new ArrayList<>();
        this.clientListFull = new ArrayList<>(clientList);  // Copy original list for search
    }

    @NonNull
    @Override
    public ClientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout for each client item (CardView)
        View view = LayoutInflater.from(context).inflate(R.layout.client_item, parent, false);
        return new ClientViewHolder(view);
    }

    public void updateData(List<Client> newClientList) {
        this.clientList = newClientList;
        this.clientListFull = new ArrayList<>(newClientList);  // Update full list for search
        notifyDataSetChanged();
    }

    // Search functionality
    public void filter(String query) {
        if (query.isEmpty()) {
            clientList = new ArrayList<>(clientListFull); // Reset to original list if query is empty
        } else {
            clientList = clientListFull.stream()
                    .filter(client -> client.getNom().toLowerCase().contains(query.toLowerCase()) ||
                            client.getPrenom().toLowerCase().contains(query.toLowerCase()))
                    .collect(Collectors.toList());
        }
        notifyDataSetChanged();
    }

    // Sorting functionality
    public void sortAsc() {
        clientList.sort((c1, c2) -> c1.getNom().compareTo(c2.getNom()));
        notifyDataSetChanged();
    }

    public void sortDesc() {
        clientList.sort((c1, c2) -> c2.getNom().compareTo(c1.getNom()));
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull ClientViewHolder holder, int position) {
        // Get the current client
        Client client = clientList.get(position);

        // Bind client attributes to the UI elements
        holder.clientName.setText("Nom: " + client.getNom());
        holder.clientFirstName.setText("Prénom: " + client.getPrenom());
        holder.clientPhone.setText("Téléphone: " + client.getTel());
        holder.clientAddress.setText("Adresse: " + client.getRib());

        // Edit button click listener
        holder.editClient.setOnClickListener(v -> {
            // Afficher un toast pour indiquer quel client est sélectionné
            Toast.makeText(context, "Edit " + client.getNom(), Toast.LENGTH_SHORT).show();

            // Créer une intention pour démarrer MainActivity avec l'objet client
            Intent intent = new Intent(context, MainActivity.class);

            // Passer l'objet client comme Serializable
            intent.putExtra("client", client); // Client est passé comme Serializable
            context.startActivity(intent);
        });

        // Delete button click listener
        holder.deleteClient.setOnClickListener(v -> {
            int positionToDelete = holder.getAdapterPosition();
            if (positionToDelete != RecyclerView.NO_POSITION) {
                clientList.remove(positionToDelete);
                notifyItemRemoved(positionToDelete);
                Toast.makeText(context, "Deleted " + client.getNom(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public int getItemCount() {
        return clientList.size();
    }

    // ViewHolder class for managing the UI elements of each list item
    public static class ClientViewHolder extends RecyclerView.ViewHolder {
        TextView clientName, clientFirstName, clientPhone, clientAddress;
        ImageView editClient, deleteClient;

        public ClientViewHolder(@NonNull View itemView) {
            super(itemView);
            clientName = itemView.findViewById(R.id.clientName);
            clientFirstName = itemView.findViewById(R.id.clientFirstName);
            clientPhone = itemView.findViewById(R.id.clientPhone);
            clientAddress = itemView.findViewById(R.id.clientAddress);
            editClient = itemView.findViewById(R.id.editClient);
            deleteClient = itemView.findViewById(R.id.deleteClient);
        }
    }
}
