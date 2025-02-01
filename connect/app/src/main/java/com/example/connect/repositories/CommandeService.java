package com.example.connect.repositories;

import android.util.Log;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.example.connect.models.Commande;
import com.example.connect.utils.RetrofitClientdhia;
import com.example.connect.interfaces.CommandeAPI;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class CommandeService {

    private static final String TAG = "CommandeService"; // For logging
    private CommandeAPI commandeAPI;

    public CommandeService() {
        commandeAPI = RetrofitClientdhia.getRetrofitInstance().create(CommandeAPI.class);
    }

    // Fetch all commandes
    public void getCommandes(final CommandeCallback callback) {
        Log.d(TAG, "Starting API call to fetch commandes...");
        Call<List<Commande>> call = commandeAPI.getCommandes();

        call.enqueue(new Callback<List<Commande>>() {
            @Override
            public void onResponse(Call<List<Commande>> call, Response<List<Commande>> response) {
                Log.d(TAG, "API call completed.");
                if (response.isSuccessful()) {
                    Log.d(TAG, "Response is successful. Status code: " + response.code());
                    callback.onSuccess(response.body());
                } else {
                    String errorMessage = "Error: Response code " + response.code() + ", Message: " + response.message();
                    Log.e(TAG, errorMessage);
                    callback.onFailure(new Throwable(errorMessage));
                }
            }

            @Override
            public void onFailure(Call<List<Commande>> call, Throwable t) {
                Log.e(TAG, "API call failed. Error: " + t.getMessage(), t);
                callback.onFailure(t);
            }
        });
    }

    // Fetch a specific commande by ID
    public void getCommande(Long idCommande, final GetCommandeCallback callback) {
        Log.d(TAG, "Starting API call to fetch commande by ID...");
        Call<Commande> call = commandeAPI.getCommande(idCommande);

        call.enqueue(new Callback<Commande>() {
            @Override
            public void onResponse(Call<Commande> call, Response<Commande> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "Commande fetched successfully. Response: " + response.body());
                    callback.onSuccess(response.body());
                } else {
                    String errorMessage = "Error: Response code " + response.code() + ", Message: " + response.message();
                    Log.e(TAG, errorMessage);
                    callback.onFailure(new Throwable(errorMessage));
                }
            }

            @Override
            public void onFailure(Call<Commande> call, Throwable t) {
                Log.e(TAG, "API call to fetch commande by ID failed. Error: " + t.getMessage(), t);
                callback.onFailure(t);
            }
        });
    }

    public void addCommande(Commande commande, Long idClient, final AddCommandeCallback callback) {
        Log.d(TAG, "Starting API call to add commande...");

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX", Locale.getDefault());
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

        if (commande.getDateCommande() != null) {
            String formattedDate = dateFormat.format(commande.getDateCommande());
            try {
                commande.setDateCommande(dateFormat.parse(formattedDate));
            } catch (ParseException e) {
                Log.e(TAG, "Error parsing date for commande", e);
            }
        }

        Call<Commande> call = commandeAPI.addCommande(commande, idClient);

        call.enqueue(new Callback<Commande>() {
            @Override
            public void onResponse(Call<Commande> call, Response<Commande> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "Commande added successfully.");
                    callback.onSuccess(response.body());
                } else {
                    String errorMessage = "Error: Response code " + response.code() + ", Message: " + response.message();
                    Log.e(TAG, errorMessage);
                    callback.onFailure(new Throwable(errorMessage));
                }
            }

            @Override
            public void onFailure(Call<Commande> call, Throwable t) {
                Log.e(TAG, "API call failed. Error: " + t.getMessage(), t);
                callback.onFailure(t);
            }
        });
    }


    public void deleteCommande(Long idCommande, final DeleteCommandeCallback callback) {
        Log.d(TAG, "Starting API call to delete commande...");

        Call<Void> call = commandeAPI.deleteCommande(idCommande);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "Commande deleted successfully.");

                    getCommandes(new CommandeCallback() {
                        @Override
                        public void onSuccess(List<Commande> commandes) {
                            callback.onSuccess(commandes);
                        }

                        @Override
                        public void onFailure(Throwable t) {
                            callback.onFailure(t);
                        }
                    });
                } else {
                    String errorMessage = "Error: Response code " + response.code() + ", Message: " + response.message();
                    Log.e(TAG, errorMessage);
                    callback.onFailure(new Throwable(errorMessage));
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e(TAG, "API call to delete commande failed. Error: " + t.getMessage(), t);
                callback.onFailure(t);
            }
        });
    }

    public void updateCommande(Long idCommande, Commande commande, final UpdateCommandeCallback callback) {
        Log.d(TAG, "Starting API call to update commande...");

        // Format the date before sending to the backend
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX", Locale.getDefault());
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

        if (commande.getDateCommande() != null) {
            String formattedDate = dateFormat.format(commande.getDateCommande());
            try {
                commande.setDateCommande(dateFormat.parse(formattedDate));
            } catch (ParseException e) {
                Log.e(TAG, "Error parsing date for commande", e);
            }
        }

        Call<Void> call = commandeAPI.updateCommande(idCommande, commande); // Use Call<Void> for empty responses

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "Commande updated successfully.");
                    callback.onSuccess(null); // Adjust callback to handle success without a body
                } else {
                    String errorBody = "";
                    try {
                        if (response.errorBody() != null) {
                            errorBody = response.errorBody().string(); // Log the error body for debugging
                        }
                    } catch (IOException e) {
                        Log.e(TAG, "Failed to read errorBody", e);
                    }
                    String errorMessage = "Error: Response code " + response.code() +
                            ", Message: " + response.message() +
                            ", Details: " + errorBody;
                    Log.e(TAG, errorMessage);
                    callback.onFailure(new Throwable(errorMessage));
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e(TAG, "API call to update commande failed. Error: " + t.getMessage(), t);
                callback.onFailure(t);
            }
        });
    }




    public interface CommandeCallback {
        void onSuccess(List<Commande> commandes);
        void onFailure(Throwable t);
    }

    public interface AddCommandeCallback {
        void onSuccess(Commande commande);
        void onFailure(Throwable t);
    }

    public interface DeleteCommandeCallback {
        void onSuccess(List<Commande> commandes);
        void onFailure(Throwable t);
    }

    public interface UpdateCommandeCallback {
        void onSuccess(Commande commande);
        void onFailure(Throwable t);
    }

    public interface GetCommandeCallback {
        void onSuccess(Commande commande);
        void onFailure(Throwable t);
    }
}
