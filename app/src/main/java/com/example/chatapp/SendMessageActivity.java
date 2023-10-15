package com.example.chatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class SendMessageActivity extends AppCompatActivity {


    Button buttonPosaljiPoruku;

    EditText emailEditText, porukaEditText;
    TextView welcomeText;
    static String rndID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_message);

        buttonPosaljiPoruku = findViewById(R.id.buttonNovaPoruka);

        emailEditText = findViewById(R.id.emailEditText);
        porukaEditText = findViewById(R.id.porukaEditText);
        buttonPosaljiPoruku.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                Random random = new Random();
                int randomID = random.nextInt(5) + 100;
                rndID = Integer.toString(randomID);
                DatabaseReference mainCollectionRef = FirebaseDatabase.getInstance().getReference("Poruke");


                String newMessageId = mainCollectionRef.push().getKey();
                String emailPosiljaoca = MainActivity.user.getEmail();
                String emailPrimaoca = emailEditText.getText().toString();
                String sadrzajPoruke = porukaEditText.getText().toString();

                DatabaseReference newMessageRef = mainCollectionRef.child(newMessageId);
                Map<String, Object> messageData = new HashMap<>();
                messageData.put("emailPosiljaoca", emailPosiljaoca);
                messageData.put("emailPrimaoca", emailPrimaoca);
                messageData.put("poruka", sadrzajPoruke);

                newMessageRef.updateChildren(messageData).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Uspesno ste poslali poruku", Toast.LENGTH_SHORT).show();
                            // Poruka je uspješno spremljena u bazu podataka
                        } else {
                            // Došlo je do pogreške pri spremanju poruke
                        }
                    }
                });

//                Poruka poruka = new Poruka(MainActivity.user.getEmail(),emailPrimaoca, sadrzajPoruke);
//                myRef.setValue(poruka);

            }
        });
    }
}