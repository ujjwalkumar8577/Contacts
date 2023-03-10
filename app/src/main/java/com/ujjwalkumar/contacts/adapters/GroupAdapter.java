package com.ujjwalkumar.contacts.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.ujjwalkumar.contacts.R;
import com.ujjwalkumar.contacts.fragments.ContactsFragment;
import com.ujjwalkumar.contacts.models.Group;

import java.util.ArrayList;

public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.GroupViewHolder> {

    Context context;
    ContactsFragment fragment;
    ArrayList<Group> al;
    int selected;

    public GroupAdapter(Context context, ContactsFragment fragment, ArrayList<Group> al, int selected) {
        this.context = context;
        this.fragment = fragment;
        this.al = al;
        this.selected = selected;
    }

    @NonNull
    @Override
    public GroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recyclerview_groups, parent, false);
        return new GroupViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupViewHolder holder, int position) {
        Group obj = al.get(position);
        holder.textViewGroupName.setText(obj.getName());

        if(position==selected)
            holder.textViewGroupName.setBackground(AppCompatResources.getDrawable(context, R.drawable.bg_input_rounded_accent));
        else
            holder.textViewGroupName.setBackground(AppCompatResources.getDrawable(context, R.drawable.bg_input_rounded));

        if(fragment==null) {
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) holder.layout.getLayoutParams();
            params.width = ViewGroup.MarginLayoutParams.MATCH_PARENT;
            params.height = 80;
            params.rightMargin = 0;
            params.bottomMargin = 12;
            holder.layout.setLayoutParams(params);
        }
        else {
            holder.textViewGroupName.setOnClickListener(view -> {
                selected = holder.getAdapterPosition();
                notifyDataSetChanged();
                fragment.showData(context, obj.getName());
            });
        }
    }

    @Override
    public int getItemCount() {
        return al.size();
    }

    public void setSelected(int selected) {
        this.selected = selected;
    }

    public static class GroupViewHolder extends RecyclerView.ViewHolder {

        ConstraintLayout layout;
        TextView textViewGroupName;

        public GroupViewHolder(@NonNull View itemView) {
            super(itemView);
            layout = itemView.findViewById(R.id.layout);
            textViewGroupName = itemView.findViewById(R.id.textViewGroupName);
        }
    }
}