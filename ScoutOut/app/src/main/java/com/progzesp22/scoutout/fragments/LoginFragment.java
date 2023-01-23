package com.progzesp22.scoutout.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.progzesp22.scoutout.MainActivity;
import com.progzesp22.scoutout.R;
import com.progzesp22.scoutout.databinding.FragmentLoginBinding;
import com.progzesp22.scoutout.domain.UserModel;

import org.json.JSONException;


public class LoginFragment extends Fragment {
    private FragmentLoginBinding binding;
    static final String TAG = "LoginFragment";
    boolean awaitingResponse = false;

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

        UserModel model = new ViewModelProvider(requireActivity()).get(UserModel.class);


        binding.loginButton.setOnClickListener(view1 -> {
            if (awaitingResponse) {
                return;
            }
            final String username =  binding.username.getText().toString();
            MainActivity.requestHandler.postUserLogin(
                    username,
                    binding.password.getText().toString(),
                    response -> {
                        String token;
                        try {
                            token = response.getString("sessionToken");
                        } catch (JSONException e) {
                            Log.e(TAG, "No session token in response");
                            return;
                        }

                        MainActivity.requestHandler.setSessionToken(token);
                        Toast.makeText(getContext(), R.string.login_successful, Toast.LENGTH_SHORT).show();
                        NavHostFragment.findNavController(this).navigate(R.id.loginSuccesfull);
                        model.setUsername(username);
                        awaitingResponse = false;
                    },
                    error -> {
                        Toast.makeText(getContext(), R.string.error_login, Toast.LENGTH_SHORT).show();
                        model.setUsername("");
                        awaitingResponse = false;
                    }
            );
            awaitingResponse = true;
        });

        binding.registerButton.setOnClickListener(view1-> NavHostFragment.findNavController(this).navigate(R.id.action_loginFragment_to_registerFragment));
    }
}