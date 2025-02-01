package com.example.connect.adapters;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.connect.EditCommandeActivity;
import com.example.connect.R;
import com.example.connect.models.Commande;
import com.example.connect.repositories.CommandeService;
import java.util.ArrayList;
import java.util.List;

public class CommandeAdapter extends RecyclerView.Adapter<CommandeAdapter.CommandeViewHolder> {

    private List<Commande> commandes;
    private List<Commande> filteredCommandes;
    private CommandeService commandeService;

    public CommandeAdapter(List<Commande> commandes) {
        this.commandes = commandes;
        this.filteredCommandes = new ArrayList<>(commandes);
        this.commandeService = new CommandeService();
    }

    @Override
    public CommandeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_commande, parent, false);
        return new CommandeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CommandeViewHolder holder, int position) {
        Commande commande = filteredCommandes.get(position);

        // Hide idCommande
        holder.idCommande.setVisibility(View.INVISIBLE);

        // Set other fields
        holder.dateCommande.setText(commande.getDateCommande().toString());
        holder.montant.setText(String.valueOf(commande.getMontant()));
        holder.quantite.setText(String.valueOf(commande.getQuantite()));
        holder.modePaiement.setText(commande.getModePaiement().toString());
        holder.remarque.setText(commande.getRemarque());

        // Set etat dynamically with gradient background
        String etat = commande.getEtat().toString();
        holder.etat.setText(etat);

        switch (etat) {
            case "PAYE":
                holder.etat.setBackgroundResource(R.drawable.green_gradient);
                break;
            case "NON_PAYE":
                holder.etat.setBackgroundResource(R.drawable.red_gradient);
                break;
            case "SEMI_PAYE":
                holder.etat.setBackgroundResource(R.drawable.orange_gradient);
                break;
            default:
                holder.etat.setBackgroundResource(R.drawable.etat_background);
                break;
        }

        // Handle delete button click
        holder.deleteCommandeButton.setOnClickListener(v -> deleteCommande(commande.getIdCommande(), position));

        // Handle edit button click
        holder.editCommandeButton.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), EditCommandeActivity.class);
            intent.putExtra("commande", commande); // Pass the full Commande object
            v.getContext().startActivity(intent);
        });

    }

    private void deleteCommande(Long idCommande, int position) {
        commandeService.deleteCommande(idCommande, new CommandeService.DeleteCommandeCallback() {
            @Override
            public void onSuccess(List<Commande> commandes) {
                CommandeAdapter.this.commandes = commandes;
                filterByEtat("All");
                notifyItemRemoved(position);
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e("CommandeAdapter", "Failed to delete commande", t);
            }
        });
    }

    @Override
    public int getItemCount() {
        return filteredCommandes.size();
    }

    public void filterByEtat(String etatFilter) {
        filteredCommandes.clear();

        if (etatFilter == null || etatFilter.equals("All")) {
            filteredCommandes.addAll(commandes);
        } else {
            for (Commande commande : commandes) {
                if (commande.getEtat().toString().equals(etatFilter)) {
                    filteredCommandes.add(commande);
                }
            }
        }

        notifyDataSetChanged();
    }

    public class CommandeViewHolder extends RecyclerView.ViewHolder {

        TextView idCommande, dateCommande, quantite, etat, modePaiement, remarque, montant;
        Button deleteCommandeButton, editCommandeButton;

        public CommandeViewHolder(View itemView) {
            super(itemView);
            idCommande = itemView.findViewById(R.id.idCommande);
            dateCommande = itemView.findViewById(R.id.dateCommande);
            quantite = itemView.findViewById(R.id.quantite);
            etat = itemView.findViewById(R.id.etat);
            modePaiement = itemView.findViewById(R.id.modePaiement);
            remarque = itemView.findViewById(R.id.remarque);
            montant = itemView.findViewById(R.id.montant);
            deleteCommandeButton = itemView.findViewById(R.id.deleteCommandeButton);
            editCommandeButton = itemView.findViewById(R.id.editCommandeButton); // Initialize the button
        }
    }
}
