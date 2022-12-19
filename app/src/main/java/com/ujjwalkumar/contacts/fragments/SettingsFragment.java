package com.ujjwalkumar.contacts.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.fragment.app.Fragment;

import com.ujjwalkumar.contacts.R;
import com.ujjwalkumar.contacts.activities.GroupsActivity;
import com.ujjwalkumar.contacts.activities.HomeActivity;
import com.ujjwalkumar.contacts.databinding.FragmentSettingsBinding;

public class SettingsFragment extends Fragment {

    private FragmentSettingsBinding binding;
    private HomeActivity activity;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSettingsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        activity = (HomeActivity)getActivity();

        binding.setting1.setOnClickListener(view -> startActivity(new Intent(container.getContext(), GroupsActivity.class)));

        binding.setting2.setOnClickListener(view -> {
            LayoutInflater li = LayoutInflater.from(container.getContext());
            View promptsView = li.inflate(R.layout.dialog_add_group, null);
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(container.getContext());
            alertDialogBuilder.setView(promptsView);

            final EditText userInput1 = promptsView.findViewById(R.id.editTextDialogUserInput1);
            userInput1.setText(activity.mAuth.getCurrentUser().getPhoneNumber());

            alertDialogBuilder
                    .setCancelable(false)
                    .setPositiveButton("Add",
                            (dialog, id) -> {
                                String name = userInput1.getText().toString();
                                if(name.equals(""))
                                    Toast.makeText(container.getContext(), "Group name required", Toast.LENGTH_SHORT).show();
                                else {
//                                    activity.mAuth.getCurrentUser().updatePhoneNumber();
                                }
                            })
                    .setNegativeButton("Cancel",
                            (dialog, id) -> dialog.cancel());

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();

            alertDialog.getWindow().setBackgroundDrawable(AppCompatResources.getDrawable(container.getContext(), R.color.gray));
            alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(Color.WHITE);
            alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(Color.WHITE);
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}