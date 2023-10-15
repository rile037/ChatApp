package com.example.chatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chatapp.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth auth;

    // creating a variable for our Database
    // Reference for Firebase.

    Button buttonLogout, buttonPosaljiPoruku;

    EditText emailEditText, porukaEditText;
    TextView welcomeText;
    static FirebaseUser user;


    ActivityMainBinding binding;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        if (user == null) {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            finish();
        } else {
            binding = ActivityMainBinding.inflate(getLayoutInflater());

            setContentView(binding.getRoot());
            replaceFragment(new HomeFragment());

            binding.bottomNavigationView.setOnItemSelectedListener(item -> {

                if (item.getItemId() == R.id.home) {
                    replaceFragment(new HomeFragment());
                } else if (item.getItemId() == R.id.message) {
                    replaceFragment(new MessageFragment());
                } else if (item.getItemId() == R.id.profile) {
                    replaceFragment(new ProfileFragment());
                }


                return true;
            });
        }
//        buttonLogout = findViewById(R.id.logout);
//        buttonPosaljiPoruku = findViewById(R.id.buttonPosaljiPoruku);
//        textView = findViewById(R.id.userDetails);
//        user = auth.getCurrentUser();
//
//        emailEditText = findViewById(R.id.emailEditText);
//        porukaEditText = findViewById(R.id.porukaEditText);


//        if (user == null) {
//            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
//            startActivity(intent);
//            finish();
//        } else {
//            textView.setText("Dobrodosli, " + user.getEmail());
//        }

//        buttonLogout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                FirebaseAuth.getInstance().signOut();
//                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
//                startActivity(intent);
//                finish();
//            }
//        });

//        buttonPosaljiPoruku.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                FirebaseDatabase database = FirebaseDatabase.getInstance();
//                Random random = new Random();
//                int randomID = random.nextInt(5) + 100;
//                String rndID = Integer.toString(randomID);
//                DatabaseReference myRef = database.getReference(rndID);
//
//                String emailPrimaoca = emailEditText.getText().toString();
//                String sadrzajPoruke = porukaEditText.getText().toString();
//
//                Poruka poruka = new Poruka(user.getEmail(),emailPrimaoca, sadrzajPoruke);
//                myRef.setValue(poruka);
//                Toast.makeText(MainActivity.this, "Uspesno ste poslali poruku", Toast.LENGTH_SHORT).show();
//
//            }
//        });
    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout,fragment);
        fragmentTransaction.commit();
    }

}