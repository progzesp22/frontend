package com.progzesp22.scoutout.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.progzesp22.scoutout.MainActivity;
import com.progzesp22.scoutout.R;
import com.progzesp22.scoutout.databinding.FragmentLoginBinding;


public class LoginFragment extends Fragment {
    private FragmentLoginBinding binding;


    public LoginFragment() {
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.loginButton.setOnClickListener(view1 -> {
            MainActivity.requestHandler.postUserLogin(
                    binding.username.getText().toString(),
                    binding.password.getText().toString(),
                    response -> {
                        Toast.makeText(getContext(), "Login successful", Toast.LENGTH_SHORT).show();
                        NavHostFragment.findNavController(this).navigate(R.id.action_loginFragment_to_selectUserTypeFragment);
                    },
                    error -> {
                        Toast.makeText(getContext(), "Login failed", Toast.LENGTH_SHORT).show();
                    }
            );
        });

        binding.registerButton.setOnClickListener(view1-> NavHostFragment.findNavController(this).navigate(R.id.action_loginFragment_to_registerFragment));
    }
}