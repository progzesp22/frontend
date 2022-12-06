package com.progzesp22.scoutout.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavHostController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.progzesp22.scoutout.databinding.FragmentUserGamesBinding;
import com.progzesp22.scoutout.domain.Game;
import com.progzesp22.scoutout.domain.GamesModel;
import com.progzesp22.scoutout.domain.TasksModel;

import java.util.LinkedList;
import java.util.List;


public class UserGamesFragment extends Fragment {
    FragmentUserGamesBinding binding;

    public UserGamesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentUserGamesBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.recycler.setLayoutManager(new LinearLayoutManager(getContext()));

        GamesModel model = new ViewModelProvider(requireActivity()).get(GamesModel.class);
        model.getGames().observe(getViewLifecycleOwner(), this::displayGames);
        model.refresh();


    }

    public void displayGames(List<Game> games){
        GamesModel model = new ViewModelProvider(requireActivity()).get(GamesModel.class);
        NavController navController = NavHostFragment.findNavController(this);

        binding.recycler.setAdapter(new GamesAdapter(games, navController, model));
    }


}