package com.example.ca1_mainscreen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.view.LayoutInflater;

import com.example.ca1_mainscreen.Model.HabitModel;
import com.example.ca1_mainscreen.Utils.DatabaseHandler;

import java.util.ArrayList;
import java.util.List;

public class NewHabit extends DialogFragment {
    public static final String TAG = "NewHabitDialog";
    private EditText habitNameEditText;
    private CheckBox mondayCheckbox, tuesdayCheckbox, wednesdayCheckbox, thursdayCheckbox, fridayCheckbox, saturdayCheckbox, sundayCheckbox;
    private Button saveButton;
    private DatabaseHandler db;

    public static NewHabit newInstance() {
        return new NewHabit();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.DialogStyle);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_new_habit, container, false);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        habitNameEditText = getView().findViewById(R.id.habitName);
        mondayCheckbox = getView().findViewById(R.id.monday);
        tuesdayCheckbox = getView().findViewById(R.id.tuesday);
        wednesdayCheckbox = getView().findViewById(R.id.wednesday);
        thursdayCheckbox = getView().findViewById(R.id.thursday);
        fridayCheckbox = getView().findViewById(R.id.friday);
        saturdayCheckbox = getView().findViewById(R.id.saturday);
        sundayCheckbox = getView().findViewById(R.id.sunday);
        saveButton = getView().findViewById(R.id.saveHabit);

        db = new DatabaseHandler(getActivity());
        db.openDatabase();

        boolean isUpdate = false;
        final Bundle bundle = getArguments();
        if (bundle != null) {
            isUpdate = true;

            HabitModel habit = (HabitModel) bundle.getSerializable("habit");
            habitNameEditText.setText(habit.getHabit());
            updateSelectedDays(habit.getDays());

            if (habit.getHabit().length() > 0) {
                saveButton.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));
            }
        }

        habitNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().equals("")) {
                    saveButton.setEnabled(false);
                    saveButton.setTextColor(Color.GRAY);
                } else {
                    saveButton.setEnabled(true);
                    saveButton.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));
                }
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = habitNameEditText.getText().toString();
                List<String> days = new ArrayList<>();
                if (mondayCheckbox.isChecked()) {
                    days.add("Monday");
                }
                if (tuesdayCheckbox.isChecked()) {
                    days.add("Tuesday");
                }
                if (wednesdayCheckbox.isChecked()) {
                    days.add("Wednesday");
                }
                if (thursdayCheckbox.isChecked()) {
                    days.add("Thursday");
                }
                if (fridayCheckbox.isChecked()) {
                    days.add("Friday");
                }
                if (saturdayCheckbox.isChecked()) {
                    days.add("Saturday");
                }
                if (sundayCheckbox.isChecked()) {
                    days.add("Sunday");
                }

                boolean isUpdate = false;
                if (bundle != null) {
                    isUpdate = true;
                    HabitModel habit = (HabitModel) bundle.getSerializable("habit");
                    habit.setHabit(name);
                    habit.setDays(days);

                    int id = habit.getId();
                    db.updateHabit(id, habit);
                } else {
                    HabitModel habit = new HabitModel();
                    habit.setHabit(name);
                    habit.setDays(days);
                    db.insertHabit(habit);
                }

                Intent i = new Intent(getActivity(), WeeklyHabit.class);
                startActivity(i);
                dismiss();
            }
        });

        Button cancelButton = getView().findViewById(R.id.cancelbtn);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    private void updateSelectedDays(List<String> days) {
        if (days.contains("Monday")) {
            mondayCheckbox.setChecked(true);
        }
        if (days.contains("Tuesday")) {
            tuesdayCheckbox.setChecked(true);
        }
        if (days.contains("Wednesday")) {
            wednesdayCheckbox.setChecked(true);
        }
        if (days.contains("Thursday")) {
            thursdayCheckbox.setChecked(true);
        }
        if (days.contains("Friday")) {
            fridayCheckbox.setChecked(true);
        }
        if (days.contains("Saturday")) {
            saturdayCheckbox.setChecked(true);
        }
        if (days.contains("Sunday")) {
            sundayCheckbox.setChecked(true);
        }
    }

    public void update(HabitModel habit) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("habit", habit);
        setArguments(bundle);
    }
}