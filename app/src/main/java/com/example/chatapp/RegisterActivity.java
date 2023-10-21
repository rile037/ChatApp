package com.example.chatapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.common.util.IOUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.io.InputStream;

public class RegisterActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    EditText emailEditText, lozinkaEditText;
    TextView textViewLogin;
    Button registerButton;
    ProgressBar progressBar;

    String email, lozinka;

    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();

    // Kao primjer, pretpostavimo da korisnik je prijavljen i imate referencu na korisnika

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();

        emailEditText = findViewById(R.id.korisnickoIme);
        lozinkaEditText = findViewById(R.id.lozinka);
        registerButton = findViewById(R.id.buttonRegister);
        progressBar = findViewById(R.id.progressBar);
        textViewLogin = findViewById(R.id.textViewLogin);

        textViewLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });


        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email, lozinka;
                if (emailEditText.getText().length() < 1 || lozinkaEditText.getText().length() < 1) {
                    Toast.makeText(getApplicationContext(), "Niste uneli sve podatke!", Toast.LENGTH_SHORT).show();
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                    email = String.valueOf(emailEditText.getText());
                    lozinka = String.valueOf(lozinkaEditText.getText());

                    mAuth.createUserWithEmailAndPassword(email, lozinka)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {

                                    FirebaseUser currentUser = mAuth.getCurrentUser();

                                    if (currentUser != null) {
                                        String uniqueImageName = "profile_images/" + currentUser.getEmail() + "/" + System.currentTimeMillis() + ".jpg";
                                        StorageReference imageRef = storageRef.child(uniqueImageName);
                                        String photo = "android.resource://com.example.chatapp/drawable/person";

                                        imageRef.putFile(Uri.parse(photo))
                                                .addOnSuccessListener(taskSnapshot -> {
                                                    // Slika je uspešno uploudana
                                                    // Možete ažurirati korisnički profil sa URL-om slike
                                                    String imageUrl = taskSnapshot.getMetadata().getReference().getDownloadUrl().toString();
                                                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                                            .setPhotoUri(Uri.parse(photo))
                                                            .build();

                                        currentUser.updateProfile(profileUpdates)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            // Profilna slika je postavljena
                                                            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                                            startActivity(intent);
                                                            finish();
                                                        }
                                                    }
                                                })
                                                .addOnFailureListener(e -> {
                                                    // Došlo je do greške prilikom uplouda slike
                                                    Toast.makeText(RegisterActivity.this, "Greška pri uploudu slike", Toast.LENGTH_SHORT).show();
                                                });
                                                });
//                                    if (task.isSuccessful()) {
//                                        progressBar.setVisibility(View.GONE);
//                                        Toast.makeText(RegisterActivity.this, "Uspesno ste napravili korisnicki racun",
//                                                Toast.LENGTH_SHORT).show();
//
//
//                                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
//                                        startActivity(intent);
//                                        finish();
                                    } else {
                                        progressBar.setVisibility(View.GONE);
                                        Toast.makeText(RegisterActivity.this, "Greska, proverite podatke",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });


    }
}
