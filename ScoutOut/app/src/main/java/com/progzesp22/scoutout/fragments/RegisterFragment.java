package com.progzesp22.scoutout.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.progzesp22.scoutout.MainActivity;
import com.progzesp22.scoutout.R;
import com.progzesp22.scoutout.databinding.FragmentRegisterBinding;


public class RegisterFragment extends Fragment {
    static final String TAG = "RegisterFragment";

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
                        Toast.makeText(getContext(), R.string.register_successful, Toast.LENGTH_SHORT).show();
                        NavHostFragment.findNavController(this).navigateUp();
                    },
                    error -> {
                        Log.e(TAG, error.toString());
                        Toast.makeText(getContext(), R.string.error_register, Toast.LENGTH_SHORT).show();
                    }
            );
        });
    }
}