package com.example.connect;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText editUsername, editPassword;
    private ImageView buttonLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login); // Assurez-vous que ce fichier XML existe

        // Initialiser les vues
        editUsername = findViewById(R.id.editUsername);
        editPassword = findViewById(R.id.editPassword);
        buttonLogin = findViewById(R.id.buttonLogin);

        // Gérer le clic sur le bouton de connexion
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = editUsername.getText().toString().trim();
                String password = editPassword.getText().toString().trim();

                if (username.isEmpty() || password.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
                } else {
                    // Appeler la méthode de connexion
                    performLogin(username, password);

                }
            }
        });
    }

    private void performLogin(String username, String password) {
        // Simuler une connexion réussie ou échouée
        if ("admin".equals(username) && "password".equals(password)) {
            // Redirection vers MainActivity
            Intent intent = new Intent(LoginActivity.this, AffichageClient.class);
            startActivity(intent);
            finish(); // Facultatif : pour empêcher de revenir à LoginActivity en appuyant sur Retour

            // Connexion réussie
            Toast.makeText(this, "Connexion réussie", Toast.LENGTH_SHORT).show();

            // Rediriger vers la page principale

        } else {
            // Connexion échouée
            Toast.makeText(this, "Nom d'utilisateur ou mot de passe incorrect", Toast.LENGTH_SHORT).show();
        }
    }
}
