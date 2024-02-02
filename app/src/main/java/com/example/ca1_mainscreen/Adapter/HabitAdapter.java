package com.example.ca1_mainscreen.Adapter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ca1_mainscreen.DailyTask;
import com.example.ca1_mainscreen.Model.HabitModel;
import com.example.ca1_mainscreen.Model.ToDoModel;
import com.example.ca1_mainscreen.NewHabit;
import com.example.ca1_mainscreen.R;
import com.example.ca1_mainscreen.Utils.DatabaseHandler;
import com.google.android.material.chip.ChipGroup;

import java.util.List;

public class HabitAdapter extends RecyclerView.Adapter<HabitAdapter.ViewHolder> {
    private List<HabitModel> habitList;
    private DatabaseHandler db;

    public HabitAdapter(DatabaseHandler db) {
        this.db = db;
        this.habitList = db.getAllHabits();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.habit, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        HabitModel item = habitList.get(position);
        holder.habitName.setText(item.getName());

        holder.monday.setChecked(item.isMondaySelected());
        holder.tuesday.setChecked(item.isTuesdaySelected());
        holder.wednesday.setChecked(item.isWednesdaySelected());
        holder.thursday.setChecked(item.isThursdaySelected());
        holder.friday.setChecked(item.isFridaySelected());
        holder.saturday.setChecked(item.isSaturdaySelected());
        holder.sunday.setChecked(item.isSundaySelected());
    }

    @Override
    public int getItemCount() {
        return habitList.size();
    }

    public void setHabit(List<HabitModel> habitList) {
        this.habitList = habitList;
        notifyDataSetChanged();
    }

    public void deleteHabit(int position) {
        HabitModel item = habitList.get(position);
        db.deleteHabit(item.getId());
        habitList.remove(position);
        notifyItemRemoved(position);
    }

    public void editHabit(int position) {
        HabitModel item = habitList.get(position);
        Bundle bundle = new Bundle();
        bundle.putInt("id", item.getId());
        bundle.putString("habit", item.getHabit());
        NewHabit fragment = new NewHabit();
        fragment.setArguments(bundle);
        fragment.show(plan.getSupportFragmentManager(), NewHabit.TAG);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView habitName;
        public ChipGroup daypicker;
        public CheckBox monday, tuesday, wednesday, thursday, friday, saturday, sunday;

        public ViewHolder(View view) {
            super(view);
            habitName = view.findViewById(R.id.habitName);
            daypicker = view.findViewById(R.id.daypicker);
            monday = view.findViewById(R.id.monday);
            tuesday = view.findViewById(R.id.tuesday);
            wednesday = view.findViewById(R.id.wednesday);
            thursday = view.findViewById(R.id.thursday);
            friday = view.findViewById(R.id.friday);
            saturday = view.findViewById(R.id.saturday);
            sunday = view.findViewById(R.id.sunday);
        }
    }
}