package com.example.taskmanager.controller;


import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import com.example.taskmanager.R;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * A simple {@link Fragment} subclass.
 */
public class DatePickerFragment extends DialogFragment {
    public static final String ARG_TASK_DATE = "taskDate";
    public static final String EXTRA_TASK_DATE = "com.example.taskmanager.controller.taskDate";
    Date mDate;
    DatePicker mDatePicker;

    public static DatePickerFragment newInstance(Date date) {

        Bundle args = new Bundle();
        DatePickerFragment fragment = new DatePickerFragment();
        args.putSerializable(ARG_TASK_DATE, date);
        fragment.setArguments(args);
        return fragment;
    }

    public static String getExtraTaskDate() {
        return EXTRA_TASK_DATE;
    }

    public DatePickerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDate = (Date) getArguments().getSerializable(ARG_TASK_DATE);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_date_picker, null, false);
        mDatePicker = view.findViewById(R.id.date_picker);

        initDatePicker();


        return new MaterialAlertDialogBuilder(getActivity())
                .setPositiveButton("save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Date date = extractDate();
                        Fragment fragment = getTargetFragment();
                        Intent intent = new Intent();
                        intent.putExtra(EXTRA_TASK_DATE, date);
                        fragment.onActivityResult(getTargetRequestCode(), getActivity().RESULT_OK, intent);
                    }
                })
                .setView(view)
                .create();
    }

    private Date extractDate() {

        Calendar calendar = Calendar.getInstance();
        calendar.set(mDatePicker.getYear(), mDatePicker.getMonth(),mDatePicker.getDayOfMonth());
        return calendar.getTime();
    }

    private void initDatePicker() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(mDate);
        mDatePicker.init(calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH),
                null);
    }
}
