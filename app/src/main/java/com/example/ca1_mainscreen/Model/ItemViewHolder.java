package com.example.ca1_mainscreen.Model;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ca1_mainscreen.R;

public class ItemViewHolder extends RecyclerView.ViewHolder {
    public TextView textView;

    public ItemViewHolder(@NonNull View itemView) {
        super(itemView);
        textView = itemView.findViewById(R.id.textViewHabit);
    }
}
