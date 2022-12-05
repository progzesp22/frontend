package com.progzesp22.scoutout.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.progzesp22.scoutout.MainActivity;
import com.progzesp22.scoutout.R;
import com.progzesp22.scoutout.databinding.FragmentPlayerTasksBinding;
import com.progzesp22.scoutout.databinding.FragmentRegisterBinding;


public class RegisterFragment extends Fragment {
    FragmentRegisterBinding binding;

    public RegisterFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentRegisterBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.registerButton.setOnClickListener(view1 -> {
            MainActivity.requestHandler.postRegister(
                    binding.username.getText().toString(),
                    binding.password.getText().toString(),
                    response -> {
                        Toast.makeText(getContext(), "Register successful", Toast.LENGTH_SHORT).show();
                        NavHostFragment.findNavController(this).navigateUp();
                    },
                    error -> {
                        Toast.makeText(getContext(), "Register error", Toast.LENGTH_SHORT).show();
                    }
            );
        });
    }
}