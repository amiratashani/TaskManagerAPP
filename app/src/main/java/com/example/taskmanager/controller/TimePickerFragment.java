package com.example.taskmanager.controller;


import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TimePicker;

import com.example.taskmanager.R;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.Calendar;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 */
public class TimePickerFragment extends DialogFragment {
    private static final String ARG_TASK_TIME = "com.example.taskmanager.controller.taskTime";
    private static final String EXTRA_TASK_TIME = "taskTime";
    private TimePicker mTimePicker;
    private Date mTime;

    public TimePickerFragment() {
        // Required empty public constructor
    }

    public static TimePickerFragment newInstance(Date date) {

        Bundle args = new Bundle();
        TimePickerFragment fragment = new TimePickerFragment();
        args.putSerializable(ARG_TASK_TIME, date);
        fragment.setArguments(args);
        return fragment;
    }

    public static String getExtraTaskTime() {
        return EXTRA_TASK_TIME;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTime = (Date) getArguments().getSerializable(ARG_TASK_TIME);

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_time_picker, null, false);

        mTimePicker = view.findViewById(R.id.time_picker);

        initTimePicker();


        return new MaterialAlertDialogBuilder(getActivity())
                .setPositiveButton("save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        Date date = extractTime();

                        Fragment fragment = getTargetFragment();
                        Intent intent = new Intent();
                        intent.putExtra(EXTRA_TASK_TIME, date);
                        fragment.onActivityResult(getTargetRequestCode(), getActivity().RESULT_OK, intent);

                    }
                })
                .setView(view)
                .create();
    }

    private Date extractTime() {

        Calendar calendar = Calendar.getInstance();
        int hour = mTimePicker.getCurrentHour();
        int min = mTimePicker.getCurrentMinute();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, min);
        return calendar.getTime();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void initTimePicker() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(mTime);
        mTimePicker.setHour(calendar.get(Calendar.HOUR));
        mTimePicker.setMinute(calendar.get(Calendar.MINUTE));
    }
}
