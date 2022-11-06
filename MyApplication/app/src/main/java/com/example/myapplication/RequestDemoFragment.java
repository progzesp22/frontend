package com.example.myapplication;

import static java.lang.Math.round;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.myapplication.databinding.FragmentRequestDemoBinding;

import java.util.Random;

public class RequestDemoFragment extends Fragment {

    private FragmentRequestDemoBinding binding;
    private RequestHandler requestHandler;
    private Random random;


    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentRequestDemoBinding.inflate(inflater, container, false);
        requestHandler = RequestHandler.getInstance(getContext());
        random = new Random();
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.buttonPost.setOnClickListener(view1 -> {
            requestHandler.postGame(response -> {}, error -> {
                binding.textView.setText(error.toString());
            });
        });

        binding.buttonGet.setOnClickListener(view1 -> {
            requestHandler.getGames(response -> {
                binding.textView.setText(response.toString());
            }, error -> {
                binding.textView.setText(error.toString());
            });
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        requestHandler = null;
        random = null;
    }

}