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
import android.view.View;
import android.widget.Button;

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
import java.util.UUID;

/**
 * A simple {@link Fragment} subclass.
 */
public class TaskEditFragment extends DialogFragment {

    private static final String ARG_TASK_ID = "taskId";
    private static final int REQUEST_CODE_DATE_PICKER = 0;
    private static final String DATE_PICKER_FRAGMENT_TAG = "datePickerFragment";
    private static final int REQUEST_CODE_TIME_PICKER = 1;
    private static final String TIME_PICKER_FRAGMENT_TAG = "timePickerFragment";

    private SimpleDateFormat formatterDate = new SimpleDateFormat("dd MMMM yyyy");
    private SimpleDateFormat formatterTime = new SimpleDateFormat("HH:mm");

    private TextInputLayout mInputLayoutTitle, mInputLayoutDescription;
    private TextInputEditText mEditTextTitle, mEditTextDescription;
    private MaterialButton mButtonDate, mButtonTime, mButtonState;

    private Task mTask;
    private Date mDateRes;
    private Date mDate;
    private Date mTime;

    private NoticeDialogListenerEdit listener;


    public static TaskEditFragment newInstance(UUID uuid) {
        Bundle args = new Bundle();
        TaskEditFragment fragment = new TaskEditFragment();
        args.putSerializable(ARG_TASK_ID, uuid);
        fragment.setArguments(args);
        return fragment;
    }

    public TaskEditFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListenerCreate so we can send events to the host
            listener = (TaskEditFragment.NoticeDialogListenerEdit) context;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(getActivity().toString()
                    + " must implement NoticeDialogListenerCreate");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UUID taskID = (UUID) getArguments().getSerializable(ARG_TASK_ID);
        mTask = Repository.getInstance(getContext()).getTask(taskID);
        mDateRes = mTask.getDate();
        mDate = mTask.getDate();
        mTime = mTask.getDate();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_task_edit, null, false);

        initUi(view);
        editTextWatcher();

        mButtonDate.setOnClickListener(v -> {
            DatePickerFragment datePickerFragment = DatePickerFragment.newInstance(mDate);
            datePickerFragment.setTargetFragment(TaskEditFragment.this, REQUEST_CODE_DATE_PICKER);
            datePickerFragment.show(getFragmentManager(), DATE_PICKER_FRAGMENT_TAG);
        });

        mButtonTime.setOnClickListener(v -> {
            TimePickerFragment timePickerFragment = TimePickerFragment.newInstance(mTime);
            timePickerFragment.setTargetFragment(TaskEditFragment.this, REQUEST_CODE_TIME_PICKER);
            assert getFragmentManager() != null;
            timePickerFragment.show(getFragmentManager(), TIME_PICKER_FRAGMENT_TAG);
        });

        mButtonState.setOnClickListener(v -> showMenu(v));

        AlertDialog alertDialog = new MaterialAlertDialogBuilder(getActivity())
                .setView(view)
                .setPositiveButton("Save", null)
                .setNegativeButton("Edit", null)
                .setNeutralButton("Delete", null)
                .create();


        alertDialog.setOnShowListener(dialog -> {
            Button positiveButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
            Button negativeButton = alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE);
            Button neutralButton = alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL);

            positiveButton.setOnClickListener(v -> {

                if (!isValidTitle(mEditTextTitle.getText().toString())) {
                    mInputLayoutTitle.setError(getString(R.string.error_empty_title));
                }

                if (!isValidDescription(mEditTextDescription.getText().toString())) {
                    mInputLayoutDescription.setError(getString(R.string.error_empty_description));
                }

                if (isValidTitle(mEditTextTitle.getText().toString()) && isValidTitle(mEditTextDescription.getText().toString())) {

                    mInputLayoutTitle.setError(null);
                    mInputLayoutDescription.setError(null);

                    mTask.setTitle(mEditTextTitle.getText().toString());
                    mTask.setDescription(mEditTextDescription.getText().toString());
                    mTask.setDate(mDateRes);

                    Repository.getInstance(getContext()).updateTsk(mTask);
                    listener.onDialogPositiveClickEditFragment(TaskEditFragment.this);
                    alertDialog.dismiss();
                }
            });

            negativeButton.setOnClickListener(v -> {
                mEditTextTitle.setEnabled(true);//
                mEditTextDescription.setEnabled(true);
                mButtonDate.setEnabled(true);
                mButtonTime.setEnabled(true);
                mButtonState.setEnabled(true);
            });

            neutralButton.setOnClickListener(v -> {
                Repository.getInstance(getContext()).deleteTask(mTask);
                listener.onDialogPositiveClickEditFragment(TaskEditFragment.this);
                alertDialog.dismiss();
            });

        });


        alertDialog.setOnShowListener(dialog -> {
            Button positiveButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
            Button negativeButton = alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE);
            Button neutralButton = alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL);

            positiveButton.setOnClickListener(v -> {

                if (!isValidTitle(mEditTextTitle.getText().toString())) {
                    mInputLayoutTitle.setError(getString(R.string.error_empty_title));
                }

                if (!isValidDescription(mEditTextDescription.getText().toString())) {
                    mInputLayoutDescription.setError(getString(R.string.error_empty_description));
                }

                if (isValidTitle(mEditTextTitle.getText().toString()) && isValidTitle(mEditTextDescription.getText().toString())) {

                    mInputLayoutTitle.setError(null);
                    mInputLayoutDescription.setError(null);

                    mTask.setTitle(mEditTextTitle.getText().toString());
                    mTask.setDescription(mEditTextDescription.getText().toString());
                    mTask.setDate(mDateRes);

                    Repository.getInstance(getContext()).updateTsk(mTask);
                    listener.onDialogPositiveClickEditFragment(TaskEditFragment.this);
                    alertDialog.dismiss();
                }
            });

            negativeButton.setOnClickListener(v -> {
                mEditTextTitle.setEnabled(true);//
                mEditTextDescription.setEnabled(true);
                mButtonDate.setEnabled(true);
                mButtonTime.setEnabled(true);
                mButtonState.setEnabled(true);
            });

            neutralButton.setOnClickListener(v -> {
                Repository.getInstance(getContext()).deleteTask(mTask);
                listener.onDialogPositiveClickEditFragment(TaskEditFragment.this);
                alertDialog.dismiss();
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
        mButtonDate = view.findViewById(R.id.button_date);
        mButtonTime = view.findViewById(R.id.button_time);
        mButtonState = view.findViewById(R.id.button_state);
        mInputLayoutTitle = view.findViewById(R.id.text_input_title);
        mInputLayoutDescription = view.findViewById(R.id.text_input_description);

        mEditTextTitle.setText(mTask.getTitle());
        mEditTextDescription.setText(mTask.getDescription());
        mButtonDate.setText(formatterDate.format(mTask.getDate()));
        mButtonTime.setText(formatterTime.format(mTask.getDate()));
        mButtonState.setText(mTask.getState().toString());

        mEditTextTitle.setEnabled(false);
        mEditTextDescription.setEnabled(false);
        mButtonDate.setEnabled(false);
        mButtonTime.setEnabled(false);
        mButtonState.setEnabled(false);

    }

    private void showMenu(View view) {
        PopupMenu popup = new PopupMenu(getActivity(), view);
        popup.getMenuInflater().inflate(R.menu.state_popup_menu, popup.getMenu());

        popup.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.menu_item_todo:
                    mButtonState.setText(Task.State.Todo.toString());
                    mTask.setState(Task.State.Todo);
                    return true;
                case R.id.menu_item_doing:
                    mButtonState.setText(Task.State.Doing.toString());
                    mTask.setState(Task.State.Doing);
                    return true;
                case R.id.menu_item_done:
                    mButtonState.setText(Task.State.Done.toString());
                    mTask.setState(Task.State.Done);
                    return true;
                default:
                    return false;
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

    public interface NoticeDialogListenerEdit {
        void onDialogPositiveClickEditFragment(DialogFragment dialog);
    }


}
