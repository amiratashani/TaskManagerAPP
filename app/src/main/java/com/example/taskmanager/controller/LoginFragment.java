package com.example.taskmanager.controller;


import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.transition.Slide;

import com.example.taskmanager.R;
import com.example.taskmanager.model.Repository;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class LoginFragment extends Fragment {

    private Button mButtonLogin;
    private TextInputEditText mEditTextUsername, mEditTextPassword;
    private TextInputLayout mTextInputUsername, mTextInputPassword;
    private TextView mTextViewCreateAcc;

    public static LoginFragment newInstance() {

        Bundle args = new Bundle();
        LoginFragment fragment = new LoginFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public LoginFragment() {
        // Required empty public constructor
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_login, container, false);

        initUi(view);
        editTextWatcher();

        mButtonLogin.setOnClickListener(view1 -> {

            if (!isValidUsername(mEditTextUsername.getText().toString())) {
                mTextInputUsername.setError(getString(R.string.error_empty_username));
            }

            if (String.valueOf(mEditTextPassword.getText()).isEmpty()) {
                mTextInputPassword.setError(getString(R.string.error_empty_password));
            } else if (mEditTextPassword.getText().toString().length() < 4) {
                mTextInputPassword.setError(getString(R.string.error_length_password));
            }

            if (isValidUsername(mEditTextUsername.getText().toString()) && isValidPassword(mEditTextPassword.getText().toString())) {
                mTextInputUsername.setError(null); // Clear the error
                mTextInputPassword.setError(null);
                try {
                    Repository.getInstance(getContext()).createSession(mEditTextUsername.getText().toString(),
                            mEditTextPassword.getText().toString());
                    //Repository.getInstance(getContext()).createSession("amir", "1234");
                    startActivity(StatePagerActivity.newIntent(getActivity()));
                } catch (Exception e) {
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }

        });

        mTextViewCreateAcc.setOnClickListener(v -> {

            RegisterFragment registerFragment = RegisterFragment.newInstance();

            Slide slideTransition = new Slide(Gravity.END);
            slideTransition.setDuration(getResources().getInteger(R.integer.anim_duration_medium));

            registerFragment.setEnterTransition(slideTransition);
            registerFragment.setAllowEnterTransitionOverlap(false);

            getFragmentManager().beginTransaction()
                    //.setCustomAnimations(R.anim.fragment_in_right, R.anim.fragment_out_left)
                    .replace(R.id.container_fragment_login_register, registerFragment)
//                    .addSharedElement(mTextInputUsername, getString(R.string.username_edit_text_transition))
//                    .addSharedElement(mTextInputPassword,getString(R.string.password_edit_text_transition))
                    .commit();
        });

        return view;

    }

    public void initUi(View view) {
        mEditTextUsername = view.findViewById(R.id.edit_text_username);
        mTextInputUsername = view.findViewById(R.id.text_input_username);
        mEditTextPassword = view.findViewById(R.id.edit_text_password);
        mTextInputPassword = view.findViewById(R.id.text_input_password);
        mButtonLogin = view.findViewById(R.id.button_login);
        mTextViewCreateAcc = view.findViewById(R.id.text_view_create_acc);

    }

    private void editTextWatcher() {

        mEditTextUsername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (isValidUsername(mEditTextUsername.getText().toString())) {
                    mTextInputUsername.setError(null); //Clear the error
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mEditTextPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (isValidPassword(mEditTextPassword.getText().toString())) {
                    mTextInputPassword.setError(null); //Clear the error
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private boolean isValidUsername(@Nullable String text) {
        return text != null && !(text.isEmpty());
    }

    private boolean isValidPassword(@Nullable String text) {
        return text != null && text.length() >= 4 && !(text.isEmpty());
    }


}
