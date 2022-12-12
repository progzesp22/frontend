package com.progzesp22.scoutout.fragments.gm;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.progzesp22.scoutout.databinding.FragmentGmGameBinding;

public class GMGameFragment extends Fragment {
    private FragmentGmGameBinding binding;

    public GMGameFragment() {
        // Required empty public constructor
    }

    public static GMGameFragment newInstance() {
        return new GMGameFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentGmGameBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

}