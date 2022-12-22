package com.ujjwalkumar.contacts.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ujjwalkumar.contacts.R;
import com.ujjwalkumar.contacts.adapters.GroupAdapter;
import com.ujjwalkumar.contacts.databinding.ActivityGroupsBinding;
import com.ujjwalkumar.contacts.models.Group;

import java.util.ArrayList;

public class GroupsActivity extends AppCompatActivity {

    private ActivityGroupsBinding binding;
    public FirebaseAuth mAuth;
    public FirebaseDatabase db;
    public DatabaseReference ref;
    private ArrayList<Group> groups;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGroupsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        ref = db.getReference(mAuth.getUid());

        ref.child("groups").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                groups = new ArrayList<>();
                try {
                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                        groups.add(data.getValue(Group.class));
                    }
                    GroupAdapter adapter = new GroupAdapter(GroupsActivity.this, null, groups, -100);
                    binding.recyclerView.setLayoutManager(new LinearLayoutManager(GroupsActivity.this));
                    binding.recyclerView.setAdapter(adapter);
                } catch (Exception e) {
                    Toast.makeText(GroupsActivity.this, "An exception occurred", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(GroupsActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        binding.imageViewBack.setOnClickListener(view -> super.onBackPressed());

        binding.buttonAdd.setOnClickListener(view -> {
            LayoutInflater li = LayoutInflater.from(GroupsActivity.this);
            View promptsView = li.inflate(R.layout.dialog_add_group, null);
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(GroupsActivity.this);
            alertDialogBuilder.setView(promptsView);

            final EditText userInput1 = promptsView.findViewById(R.id.editTextDialogUserInput1);

            alertDialogBuilder
                    .setCancelable(false)
                    .setPositiveButton("Add",
                            (dialog, id) -> {
                                String name = userInput1.getText().toString();
                                if(name.equals(""))
                                    Toast.makeText(GroupsActivity.this, "Group name required", Toast.LENGTH_SHORT).show();
                                else {
                                    addGroup(name);
                                }
                            })
                    .setNegativeButton("Cancel",
                            (dialog, id) -> dialog.cancel());

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();

            alertDialog.getWindow().setBackgroundDrawable(AppCompatResources.getDrawable(GroupsActivity.this, R.color.gray));
            alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(Color.WHITE);
            alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(Color.WHITE);
        });
    }

    private void addGroup(String name) {
        String id = ref.push().getKey();
        Group group = new Group(id, name);
        ref.child("groups").child(id).setValue(group);
        Snackbar.make(binding.getRoot(), "New group saved successfully.", Snackbar.LENGTH_SHORT).show();
    }
}