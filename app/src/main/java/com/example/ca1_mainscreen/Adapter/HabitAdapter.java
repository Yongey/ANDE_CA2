package com.example.ca1_mainscreen.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ca1_mainscreen.Model.ItemViewHolder;
import com.example.ca1_mainscreen.R;

import java.util.List;

public class HabitAdapter extends RecyclerView.Adapter<ItemViewHolder> {

    private List<String> items;

    public HabitAdapter(List<String> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.habit, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        String item = items.get(position);
        holder.textView.setText(item);
    }

    @Override
    public int getItemCount() {
        return items != null ? items.size() : 0;
    }
}
