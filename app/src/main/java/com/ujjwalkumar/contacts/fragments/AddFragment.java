package com.ujjwalkumar.contacts.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.ujjwalkumar.contacts.activities.HomeActivity;
import com.ujjwalkumar.contacts.databinding.FragmentAddBinding;
import com.ujjwalkumar.contacts.models.Contact;
import com.ujjwalkumar.contacts.models.Group;

import java.util.ArrayList;

public class AddFragment extends Fragment {

    private FragmentAddBinding binding;
    private HomeActivity activity;
    private ArrayList<String> groups;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAddBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        activity = (HomeActivity)getActivity();

        binding.buttonAdd.setOnClickListener(view -> {
            if(validateFields()) {
                long number = Long.parseLong(binding.editTextNumber.getText().toString());
                String name = binding.editTextName.getText().toString();
                String group = (binding.spinnerGroup.getSelectedItemPosition()==0)?"NA":binding.spinnerGroup.getSelectedItem().toString();
                String id = activity.ref.push().getKey();
                long timestamp = System.currentTimeMillis();

                Contact contact = new Contact(id, name, number, group, timestamp);
                activity.ref.child("contacts").child(id).setValue(contact);
                Snackbar.make(root, "New contact saved successfully.", Snackbar.LENGTH_SHORT).show();

                binding.editTextNumber.setText("");
                binding.editTextName.setText("");
                binding.spinnerGroup.setSelection(0);
            }
        });

        activity.ref.child("groups").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                groups = new ArrayList<>();
                groups.add("-Select Group-");
                try {
                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                        groups.add(data.getValue(Group.class).getName());
                    }
                    // Display groups for filtering
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(container.getContext(), android.R.layout.simple_spinner_item, groups);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    binding.spinnerGroup.setAdapter(adapter);
                }
                catch (Exception e) {
                    Toast.makeText(container.getContext(), "An exception occurred", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(container.getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        return root;
    }

    private boolean validateFields() {
        if(binding.editTextNumber.getText().toString().length()!=10) {
            binding.editTextNumber.setError("Number should be of 10 digits");
            return false;
        }
        if(binding.editTextName.getText().toString().equals("")) {
            binding.editTextName.setError("Name required");
            return false;
        }
        return true;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}