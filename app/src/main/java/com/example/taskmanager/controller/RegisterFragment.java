package com.example.taskmanager.controller;


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
import androidx.fragment.app.Fragment;
import androidx.transition.Slide;

import com.example.taskmanager.R;
import com.example.taskmanager.model.Repository;
import com.example.taskmanager.model.User;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;


public class RegisterFragment extends Fragment {

    private Button mButtonRegister;
    private TextInputEditText mEditTextUsername, mEditTextPassword;
    private TextInputLayout mTextInputUsername, mTextInputPassword;
    private TextView mTextViewAlreadyAcc;
    private SwitchMaterial mSwitchMaterialAdmin;

    static RegisterFragment newInstance() {
        Bundle args = new Bundle();
        RegisterFragment fragment = new RegisterFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public RegisterFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_register, container, false);

        initUi(view);

        editTextWatcher();

        mButtonRegister.setOnClickListener(v -> {

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
                    User user =new User(mEditTextUsername.getText().toString()
                            , mEditTextPassword.getText().toString()
                                    ,mSwitchMaterialAdmin.isChecked()
                    );

                    Repository.getInstance(getContext()).insertUser(user);

                } catch (Exception e) {
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        });

        mTextViewAlreadyAcc.setOnClickListener(v -> {

            LoginFragment loginFragment = LoginFragment.newInstance();

            Slide slideTransition = new Slide(Gravity.START);
            slideTransition.setDuration(getResources().getInteger(R.integer.anim_duration_medium));

            loginFragment.setEnterTransition(slideTransition);
            loginFragment.setAllowEnterTransitionOverlap(false);

            getFragmentManager().beginTransaction()
                    // .setCustomAnimations(R.anim.fragment_in_left, R.anim.fragment_out_right)
                    .replace(R.id.container_fragment_login_register, loginFragment)
//                    .addSharedElement(mTextInputUsername, getString(R.string.username_edit_text_transition))
//                    .addSharedElement(mTextInputPassword,getString(R.string.password_edit_text_transition))
                    .commit();
        });

        return view;
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

    private void initUi(View view) {
        mButtonRegister = view.findViewById(R.id.button_register);
        mEditTextUsername = view.findViewById(R.id.edit_text_username);
        mTextInputUsername = view.findViewById(R.id.text_input_username);
        mEditTextPassword = view.findViewById(R.id.edit_text_password);
        mTextInputPassword = view.findViewById(R.id.text_input_password);
        mTextViewAlreadyAcc = view.findViewById(R.id.text_view_already_acc);
        mSwitchMaterialAdmin=view.findViewById(R.id.switch_admin);

    }

    private boolean isValidUsername(@Nullable String text) {
        return text != null && !(text.isEmpty());
    }

    private boolean isValidPassword(@Nullable String text) {
        return text != null && text.length() >= 4 && !(text.isEmpty());
    }


}
