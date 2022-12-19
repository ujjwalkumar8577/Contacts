package com.ujjwalkumar.contacts.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.ujjwalkumar.contacts.R;
import com.ujjwalkumar.contacts.databinding.ActivityAccountBinding;

public class AccountActivity extends AppCompatActivity {

    private ActivityAccountBinding binding;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAccountBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();

        binding.textViewName.setText(mAuth.getCurrentUser().getDisplayName());
        binding.textViewNumber.setText(mAuth.getCurrentUser().getPhoneNumber());
        binding.textViewEmail.setText(mAuth.getCurrentUser().getEmail());

        Glide.with(AccountActivity.this)
                .load(mAuth.getCurrentUser().getPhotoUrl().toString())
                .placeholder(R.drawable.ic_baseline_person_24)
                .circleCrop()
                .into(binding.imageView);

        binding.imageViewBack.setOnClickListener(view -> super.onBackPressed());

        binding.textViewLogout.setOnClickListener(view -> {
            mAuth.signOut();

            GoogleSignInOptions gso = new GoogleSignInOptions
                    .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getString(R.string.default_web_client_id))
                    .requestEmail()
                    .build();
            GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(this, gso);
            googleSignInClient.signOut();

            Intent in = new Intent(this, LoginActivity.class);
            in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(in);
        });
    }
}