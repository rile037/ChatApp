package com.example.chatapp;

import android.content.Intent;
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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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

        mainCollectionRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (isAdded()) {
                        for (DataSnapshot messageSnapshot : dataSnapshot.getChildren()) {
                            String emailPrimaoca = messageSnapshot.child("emailPrimaoca").getValue(String.class);

                            if (emailPrimaoca.equals(userEmailPrimaoca)) {
                                // Poruka odgovara e-mailu primaoca, pa je prikažite
                                String emailPosiljaoca = messageSnapshot.child("emailPosiljaoca").getValue(String.class);
                                String porukaText = messageSnapshot.child("poruka").getValue(String.class);

                                TextView messageTextView = new TextView(getContext());
                                messageTextView.setText("Od: " + emailPosiljaoca + "\nPoruka: " + porukaText + "\n");
                                messageContainer.addView(messageTextView);
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