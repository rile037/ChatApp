package com.example.chatapp;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import com.bumptech.glide.Glide;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    Button buttonLogout;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    ProgressBar progressBar;
    FirebaseStorage storage = FirebaseStorage.getInstance();

    StorageReference storageRef = storage.getReference();

    private TextView profil;
    private RoundedImage profilnaSlika;
    private Button changePhotoButton;

    private static final int PICK_IMAGE_REQUEST = 1;

    public ProfileFragment() {
        // Required empty public constructor
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            // Dobijte URI odabrane slike
            Uri selectedImageUri = data.getData();
            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();



            if (currentUser != null) {
                if (currentUser.getPhotoUrl() != null) {
                    // Korisnik već ima postavljenu sliku, tako da ćemo je zamijeniti
                    String uniqueImageName = "profile_images/" + currentUser.getEmail() + "/" + "profile" + ".jpg";
                    StorageReference newImageRef = storageRef.child(uniqueImageName);

                    newImageRef.putFile(selectedImageUri)
                            .addOnSuccessListener(taskSnapshot -> {
                                // Nova slika je uspješno učitana
                                // Dobijte URL nove slike
                                newImageRef.getDownloadUrl()
                                        .addOnSuccessListener(uri -> {
                                            String newImageUrl = uri.toString();

                                            // Ažurirajte korisnički profil sa URL-om nove slike
                                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                                    .setPhotoUri(Uri.parse(newImageUrl))
                                                    .build();

                                            currentUser.updateProfile(profileUpdates)
                                                    .addOnCompleteListener(task -> {
                                                        if (task.isSuccessful()) {
                                                            profilnaSlika.setImageURI(selectedImageUri);
                                                            progressBar.setVisibility(View.GONE);
                                                        }
                                                    });
                                        });
                            })
                            .addOnFailureListener(e -> {
                                // Došlo je do greške prilikom učitavanja nove slike
                                Toast.makeText(getContext(), "Greška pri učitavanju nove slike", Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                            });
                } else {
                    // Korisnik nema postavljenu sliku, pa ćemo postaviti novu sliku kao korisničku sliku
                    String uniqueImageName = "profile_images/" + currentUser.getEmail() + "/" + "profile" + ".jpg";
                    StorageReference newImageRef = storageRef.child(uniqueImageName);

                    newImageRef.putFile(selectedImageUri)
                            .addOnSuccessListener(taskSnapshot -> {
                                // Nova slika je uspješno učitana
                                // Dobijte URL nove slike
                                newImageRef.getDownloadUrl()
                                        .addOnSuccessListener(uri -> {
                                            String newImageUrl = uri.toString();

                                            // Ažurirajte korisnički profil sa URL-om nove slike
                                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                                    .setPhotoUri(Uri.parse(newImageUrl))
                                                    .build();

                                            currentUser.updateProfile(profileUpdates)
                                                    .addOnCompleteListener(task -> {
                                                        if (task.isSuccessful()) {
                                                            profilnaSlika.setImageURI(selectedImageUri);
                                                            progressBar.setVisibility(View.GONE);
                                                        }
                                                    });
                                        });
                            })
                            .addOnFailureListener(e -> {
                                // Došlo je do greške prilikom učitavanja nove slike
                                Toast.makeText(getContext(), "Greška pri učitavanju nove slike", Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                            });
                }
            }
        }
    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
//            // Dobijte URI odabrane slike
//            Uri selectedImageUri = data.getData();
//
//            // Sada možete postaviti odabranu sliku u 'profilnaSlika' ili obraditi je kako želite.
//            profilnaSlika.setImageURI(selectedImageUri);
//
//            // Ovdje možete dodatno obraditi odabranu sliku, npr. spremiti je ili ažurirati u bazi podataka.
//        }
//    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inf = inflater.inflate(R.layout.fragment_profile, container, false);
        progressBar = inf.findViewById(R.id.progressBar);

        profilnaSlika = inf.findViewById(R.id.imageView);
//        profilnaSlika.setImageResource(R.drawable.defaultprofile);
//        profilnaSlika.setVisibility(View.VISIBLE);

        profil = (TextView)inf.findViewById(R.id.username);
        profil.setText(MainActivity.user.getEmail());

        buttonLogout = inf.findViewById(R.id.logout);
        changePhotoButton = inf.findViewById(R.id.changePhotoButton);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            // Provjerite ima li postavljenu korisničku sliku
            if (currentUser.getPhotoUrl() != null) {
                // Korisnik ima postavljenu sliku
                Glide.with(this)  // Koristite Glide ili drugu knjižnicu za učitavanje slika
                        .load(currentUser.getPhotoUrl())
                        .into(profilnaSlika);
            } else {
                // Korisnik nema postavljenu sliku, možete prikazati zadani avatar
                profilnaSlika.setImageResource(R.drawable.person);
            }

            // Postavite korisnički e-mail
            profil.setText(currentUser.getEmail());
        }

        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            }
        });

        profilnaSlika.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);

                // Pozovi intent za odabir slika
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, PICK_IMAGE_REQUEST);

            }
        });

        changePhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);

                // Pozovi intent za odabir slika
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, PICK_IMAGE_REQUEST);

            }
        });



        return inf;
    }

}

