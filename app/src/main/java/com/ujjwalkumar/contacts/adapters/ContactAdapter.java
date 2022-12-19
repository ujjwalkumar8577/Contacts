package com.ujjwalkumar.contacts.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.ujjwalkumar.contacts.R;
import com.ujjwalkumar.contacts.activities.ContactActivity;
import com.ujjwalkumar.contacts.models.Contact;

import java.util.ArrayList;
import java.util.Random;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder> {

    Context context;
    ArrayList<Contact> al;
    Random rnd;

    public ContactAdapter(Context context, ArrayList<Contact> al) {
        this.context = context;
        this.al = al;
        this.rnd = new Random();
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recyclerview_contacts, parent, false);
        return new ContactViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
        Contact obj = al.get(position);
        holder.textViewName.setText(obj.getName());
        holder.textViewNumber.setText(String.valueOf(obj.getNumber()));
        holder.textViewInitial.setText(Character.toLowerCase(obj.getName().charAt(0)) + "");

        int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
        holder.textViewInitial.setTextColor(color);

        holder.layout.setOnLongClickListener(view -> {
            Gson gson = new Gson();
            Intent in = new Intent(context, ContactActivity.class);
            in.putExtra("obj", gson.toJson(obj));
            context.startActivity(in);
            return false;
        });

//        holder.layout.setOnTouchListener(new OnSwipeTouchListener(context) {
//            @Override
//            public void onSwipeRight() {
//                Toast.makeText(context, "swipe right", Toast.LENGTH_SHORT).show();
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                    holder.itemView.setBackgroundColor(context.getColor(R.color.colorAccent));
//                }
//            }
//            @Override
//            public void onSwipeLeft() {
//                Toast.makeText(context, "swipe left", Toast.LENGTH_SHORT).show();
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                    holder.itemView.setBackgroundColor(context.getColor(R.color.colorAccent));
//                }
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return al.size();
    }

    public static class ContactViewHolder extends RecyclerView.ViewHolder {

        ConstraintLayout layout;
        TextView textViewName, textViewNumber, textViewInitial;

        public ContactViewHolder(@NonNull View itemView) {
            super(itemView);
            layout = itemView.findViewById(R.id.layout);
            textViewName = itemView.findViewById(R.id.textViewName);
            textViewNumber = itemView.findViewById(R.id.textViewNumber);
            textViewInitial = itemView.findViewById(R.id.textViewInitial);
        }
    }
}
