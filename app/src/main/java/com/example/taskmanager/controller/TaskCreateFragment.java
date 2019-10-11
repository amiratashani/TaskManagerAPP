package com.example.taskmanager.controller;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.taskmanager.R;
import com.example.taskmanager.model.Repository;
import com.example.taskmanager.model.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * A simple {@link Fragment} subclass.
 */
public class TaskCreateFragment extends DialogFragment {

    private static final int REQUEST_CODE_DATE_PICKER = 0;
    private static final String DATE_PICKER_FRAGMENT_TAG = "datePickerFragment";
    private static final int REQUEST_CODE_TIME_PICKER = 1;
    private static final String TIME_PICKER_FRAGMENT_TAG = "timePickerFragment";

    private SimpleDateFormat formatterDate = new SimpleDateFormat("dd MMMM yyyy");
    private SimpleDateFormat formatterTime = new SimpleDateFormat("HH:mm");

    private TextInputLayout mInputLayoutTitle, mInputLayoutDescription;
    private TextInputEditText mEditTextTitle, mEditTextDescription;
    private MaterialButton mButtonDate, mButtonTime, mButtonState;
    private String mTitle, mDescription;
    private Task.State mState;

    private Date mDateRes = new Date();
    private Date mDate = new Date();
    private Date mTime = new Date();

    private NoticeDialogListenerCreate listener;

    public static TaskCreateFragment newInstance() {
        Bundle args = new Bundle();
        TaskCreateFragment fragment = new TaskCreateFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public TaskCreateFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListenerCreate so we can send events to the host
            listener = (NoticeDialogListenerCreate) context;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(getActivity().toString()
                    + " must implement NoticeDialogListenerCreate");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_task_create, null, false);

        initUi(view);
        editTextWatcher();


        mButtonDate.setOnClickListener(view12 -> {
            DatePickerFragment datePickerFragment = DatePickerFragment.newInstance(mDate);
            datePickerFragment.setTargetFragment(TaskCreateFragment.this, REQUEST_CODE_DATE_PICKER);
            datePickerFragment.show(getFragmentManager(), DATE_PICKER_FRAGMENT_TAG);
        });

        mButtonTime.setOnClickListener(view1 -> {
            TimePickerFragment timePickerFragment = TimePickerFragment.newInstance(mTime);
            timePickerFragment.setTargetFragment(TaskCreateFragment.this, REQUEST_CODE_TIME_PICKER);
            timePickerFragment.show(getFragmentManager(), TIME_PICKER_FRAGMENT_TAG);
        });

        mButtonState.setOnClickListener(view13 -> showMenu(view13));

        final AlertDialog alertDialog = new MaterialAlertDialogBuilder(getActivity())
                .setView(view)
                .setPositiveButton("Save", null)
                .setNeutralButton("Cancel", null)
                .create();

        alertDialog.setOnShowListener(dialog -> {
            Button positiveButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);

            positiveButton.setOnClickListener(v -> {

                if (!isValidTitle(mEditTextTitle.getText().toString())) {
                    mInputLayoutTitle.setError(getString(R.string.error_empty_title));
                }

                if (!isValidDescription(mEditTextDescription.getText().toString())) {
                    mInputLayoutDescription.setError(getString(R.string.error_empty_description));
                }

                if (isValidState()) {
                    Toast.makeText(getActivity(), "Pleas pick state", Toast.LENGTH_SHORT).show();
                }


                if (isValidTitle(mEditTextTitle.getText().toString()) &&
                        isValidTitle(mEditTextDescription.getText().toString()) &&
                        !(isValidState())) {

                    mInputLayoutTitle.setError(null);
                    mInputLayoutDescription.setError(null);

                    mTitle = mEditTextTitle.getText().toString();
                    mDescription = mEditTextDescription.getText().toString();
                    Task task = new Task(mTitle, mDescription, mDateRes, mState, getContext());
                    Repository.getInstance(getContext()).insertTask(task);
                    listener.onDialogPositiveClickCreateFragment(TaskCreateFragment.this);
                    alertDialog.dismiss();
                }

            });

        });

        return alertDialog;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != Activity.RESULT_OK || data == null)
            return;
        if (requestCode == REQUEST_CODE_DATE_PICKER) {
            Date date = (Date) data.getSerializableExtra(DatePickerFragment.getExtraTaskDate());
            int year, month, day, hour, minute;

            GregorianCalendar calendarDate = new GregorianCalendar();
            calendarDate.setTime(date);
            year = calendarDate.get(Calendar.YEAR);
            month = calendarDate.get(Calendar.MONTH);
            day = calendarDate.get(Calendar.DAY_OF_MONTH);
            mDate = calendarDate.getTime();


            GregorianCalendar calendarTime = new GregorianCalendar();
            calendarTime.setTime(mTime);
            hour = calendarTime.get(Calendar.HOUR_OF_DAY);
            minute = calendarTime.get(Calendar.MINUTE);


            GregorianCalendar calendarRes = new GregorianCalendar();
            calendarRes.set(Calendar.HOUR, hour);
            calendarRes.set(Calendar.MINUTE, minute);
            calendarRes.set(Calendar.YEAR, year);
            calendarRes.set(Calendar.MONTH, month);
            calendarRes.set(Calendar.DAY_OF_MONTH, day);
            mDateRes = calendarRes.getTime();

            String currentDate = formatterDate.format(mDateRes);
            mButtonDate.setText(currentDate);

        }
        if (requestCode == REQUEST_CODE_TIME_PICKER) {
            Date time = (Date) data.getSerializableExtra(TimePickerFragment.getExtraTaskTime());
            int year, month, day, hour, minute;

            Calendar calendarTime = Calendar.getInstance();
            calendarTime.setTime(time);
            hour = calendarTime.get(Calendar.HOUR_OF_DAY);
            minute = calendarTime.get(Calendar.MINUTE);
            mTime = calendarTime.getTime();

            Calendar calendarDate = Calendar.getInstance();
            calendarDate.setTime(mDate);
            year = calendarDate.get(Calendar.YEAR);
            month = calendarDate.get(Calendar.MONTH);
            day = calendarDate.get(Calendar.DAY_OF_MONTH);


            GregorianCalendar calendarRes = new GregorianCalendar();
            calendarRes.set(Calendar.HOUR, hour);
            calendarRes.set(Calendar.MINUTE, minute);
            calendarRes.set(Calendar.YEAR, year);
            calendarRes.set(Calendar.MONTH, month);
            calendarRes.set(Calendar.DAY_OF_MONTH, day);
            mDateRes = calendarRes.getTime();


            String currentTime = formatterTime.format(mDateRes);
            mButtonTime.setText(currentTime);
        }
    }

    private void initUi(View view) {
        mEditTextTitle = view.findViewById(R.id.edit_text_title);
        mEditTextDescription = view.findViewById(R.id.edit_text_description);
        mInputLayoutTitle = view.findViewById(R.id.text_input_title);
        mInputLayoutDescription = view.findViewById(R.id.text_input_description);
        mButtonDate = view.findViewById(R.id.button_date);
        mButtonTime = view.findViewById(R.id.button_time);
        mButtonState = view.findViewById(R.id.button_state);

        //Calendar calendar = Calendar.getInstance();
        //Date date = calendar.getTime();

        //String currentDate = DateFormat.getDateInstance(DateFormat.FULL).format(mDate);
        String currentDate = formatterDate.format(mDate);
        String currentTime = formatterTime.format(mTime);

        mButtonDate.setText(currentDate);
        mButtonTime.setText(currentTime);
    }

    public void showMenu(View view) {
        PopupMenu popup = new PopupMenu(getActivity(), view);
        popup.getMenuInflater().inflate(R.menu.state_popup_menu, popup.getMenu());

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_item_todo:
                        mButtonState.setText(Task.State.Todo.toString());
                        mState = Task.State.Todo;
                        return true;
                    case R.id.menu_item_doing:
                        mButtonState.setText(Task.State.Doing.toString());
                        mState = Task.State.Doing;
                        return true;
                    case R.id.menu_item_done:
                        mButtonState.setText(Task.State.Done.toString());
                        mState = Task.State.Done;
                        return true;
                    default:
                        return false;
                }

            }
        });
        popup.show();
    }

    private boolean isValidTitle(@Nullable String text) {
        return text != null && !(text.isEmpty());
    }

    private boolean isValidDescription(@Nullable String text) {
        return text != null && !(text.isEmpty());
    }

    private boolean isValidState() {
        return mButtonState.getText().toString().equals(getString(R.string.pick_state));
    }

    private void editTextWatcher() {

        mEditTextTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (isValidTitle(mEditTextTitle.getText().toString())) {
                    mInputLayoutTitle.setError(null); //Clear the error
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mEditTextDescription.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (isValidDescription(mEditTextDescription.getText().toString())) {
                    mInputLayoutDescription.setError(null); //Clear the error
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }


    public interface NoticeDialogListenerCreate {
        void onDialogPositiveClickCreateFragment(DialogFragment dialog);
    }

}
