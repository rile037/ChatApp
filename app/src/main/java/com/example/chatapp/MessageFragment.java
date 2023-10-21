package com.example.chatapp;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MessageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MessageFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private DatabaseReference mDatabase;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    Button buttonNovaPoruka;
    TextView poruka;
    ProgressBar progressBar;
    private LinearLayout messageContainer;

    public MessageFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MessageFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MessageFragment newInstance(String param1, String param2) {
        MessageFragment fragment = new MessageFragment();
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
        View inf = inflater.inflate(R.layout.fragment_message, container, false);
        messageContainer = inf.findViewById(R.id.messageContainer);

        buttonNovaPoruka = (Button) inf.findViewById(R.id.buttonNovaPoruka);
        buttonNovaPoruka.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), SendMessageActivity.class);
                startActivity(intent);

            }
        });
        progressBar = inf.findViewById(R.id.progressBar);

//        poruka = (TextView) inf.findViewById(R.id.porukaTxtView);
        // Read from the database
        if (isAdded()) {
            progressBar.setVisibility(View.VISIBLE);
            // ...
        }
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        String userEmailPrimaoca = MainActivity.user.getEmail(); // Zamijenite sa stvarnim e-mailom primaoca

        DatabaseReference mainCollectionRef = FirebaseDatabase.getInstance().getReference("Poruke");

        // ... Ostatak koda fragmenta ...

// ... Ostatak koda fragmenta ...

        mainCollectionRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (isAdded()) {
                    for (DataSnapshot messageSnapshot : dataSnapshot.getChildren()) {
                        String emailPrimaoca = messageSnapshot.child("emailPrimaoca").getValue(String.class);

                        if (emailPrimaoca.equals(userEmailPrimaoca)) {
                            String emailPosiljaoca = messageSnapshot.child("emailPosiljaoca").getValue(String.class);
                            String porukaText = messageSnapshot.child("poruka").getValue(String.class);


                            // Kreiranje slike posiljaoca koristeći resurs iz drawable foldera
                            RoundedImage profileImageView = new RoundedImage(getContext());

                            int width = getResources().getDimensionPixelSize(R.dimen.profile_image_width);
                            int height = getResources().getDimensionPixelSize(R.dimen.profile_image_height);
                            profileImageView.setLayoutParams(new LinearLayout.LayoutParams(150, 150));

                            String imageUrl = messageSnapshot.child("imageUrl").getValue(String.class);

                            // Uzmi email posiljaoca iz baze podataka
                            String storagePath = "profile_images/" + emailPosiljaoca + "/profile.jpg";

                            // Create a StorageReference and load the image
                            StorageReference storageRef = FirebaseStorage.getInstance().getReference(storagePath);

                            storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    progressBar.setVisibility(View.VISIBLE);
                                    String imageUrl = uri.toString();

                                    // Use Glide to load the image
                                    Glide.with(getContext())
                                            .load(imageUrl)
                                            .into(profileImageView);
                                    progressBar.setVisibility(View.GONE);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // Handle the failure to get the download URL (e.g., log an error or show a placeholder image)
                                    Log.e("GlideError", "Failed to get image download URL: " + e.getMessage());
                                    progressBar.setVisibility(View.GONE);
                                }
                            });
// Create layout for each message with a vertical orientation
                            // Create a top-level horizontal layout
                            LinearLayout mainLayout = new LinearLayout(getContext());
                            mainLayout.setOrientation(LinearLayout.HORIZONTAL);

// Create layout for the message details (email and message) with vertical orientation
                            LinearLayout messageDetailsLayout = new LinearLayout(getContext());
                            messageDetailsLayout.setOrientation(LinearLayout.VERTICAL);


// Create a horizontal layout for email and name
                            LinearLayout emailNameLayout = new LinearLayout(getContext());
                            emailNameLayout.setOrientation(LinearLayout.HORIZONTAL);

// Create text views for email and name
                            TextView emailTextView = new TextView(getContext());
                            emailTextView.setText(emailPosiljaoca);
                            emailTextView.setTypeface(null, Typeface.BOLD); // Make text bold

// Create a new TextView for message
                            TextView messageTextView = new TextView(getContext());
                            messageTextView.setText(porukaText);

// Add the elements to the emailNameLayout (horizontally)
                            emailNameLayout.addView(emailTextView);

// Add the elements to the messageDetailsLayout (vertically)
                            messageDetailsLayout.addView(emailNameLayout);
                            messageDetailsLayout.addView(messageTextView);

// Add the profile image and message details to the mainLayout (horizontally)
                            mainLayout.addView(profileImageView);
                            mainLayout.addView(messageDetailsLayout);

// Add the mainLayout to the message container
                            messageContainer.addView(mainLayout);



                        }
                    }

                    progressBar.setVisibility(View.GONE);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Pogreška pri dohvaćanju podataka
            }
        });



//        myRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                // This method is called once with the initial value and again
//                // whenever data at this location is updated.
//                String value = dataSnapshot.getValue(String.class);
//                Toast.makeText(getContext(), value, Toast.LENGTH_SHORT).show();
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError error) {
//                Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();
//                // Failed to read value
//            }
//        });

        return inf;
    }
}