package com.ujjwalkumar.contacts.fragments;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.ujjwalkumar.contacts.activities.HomeActivity;
import com.ujjwalkumar.contacts.adapters.ContactAdapter;
import com.ujjwalkumar.contacts.adapters.GroupAdapter;
import com.ujjwalkumar.contacts.databinding.FragmentContactsBinding;
import com.ujjwalkumar.contacts.models.Contact;
import com.ujjwalkumar.contacts.models.Group;

import java.util.ArrayList;

public class ContactsFragment extends Fragment {

    private FragmentContactsBinding binding;
    private HomeActivity activity;
    private ArrayList<Contact> al;
    private ArrayList<Group> groups;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentContactsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        activity = (HomeActivity)getActivity();

        binding.imageViewClear.setOnClickListener(view -> {
            showData(container.getContext(), "");
            ((GroupAdapter)binding.recyclerViewHorizontal.getAdapter()).setSelected(-1);
            binding.recyclerViewHorizontal.getAdapter().notifyDataSetChanged();
            binding.editTextSearch.setText("");
        });

        binding.editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String filter = editable.toString();
                showData(container.getContext(), filter.trim());
            }
        });

        activity.ref.child("groups").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                groups = new ArrayList<>();
                try {
                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                        groups.add(data.getValue(Group.class));
                    }
                    GroupAdapter adapter = new GroupAdapter(container.getContext(), ContactsFragment.this, groups, -1);
                    binding.recyclerViewHorizontal.setLayoutManager(new LinearLayoutManager(
                            container.getContext(),
                            LinearLayoutManager.HORIZONTAL,
                            false));
                    binding.recyclerViewHorizontal.setAdapter(adapter);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("database error", error.getDetails());
            }
        });

        activity.ref.child("contacts").orderByChild("name").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                al = new ArrayList<>();
                try {
                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                        al.add(data.getValue(Contact.class));
                    }
                    showData(container.getContext(), "");
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("database error", error.getDetails());
            }
        });

        return root;
    }

    public void showData(Context context, String filter) {
        ContactAdapter adapter = new ContactAdapter(context, al);
        if(!filter.equals("")) {
            ArrayList<Contact> filtered = new ArrayList<>();
            filter = filter.toLowerCase();
            for(Contact contact: al) {
                if(contact.getName().toLowerCase().contains(filter) ||
                    contact.getGroup().toLowerCase().contains(filter) ||
                    String.valueOf(contact.getNumber()).contains(filter))
                    filtered.add(contact);
            }
            adapter = new ContactAdapter(context, filtered);
        }
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(context));
        binding.recyclerView.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}