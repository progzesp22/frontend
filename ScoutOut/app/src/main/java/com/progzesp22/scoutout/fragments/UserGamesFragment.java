package com.progzesp22.scoutout.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.progzesp22.scoutout.R;
import com.progzesp22.scoutout.databinding.FragmentUserGamesBinding;
import com.progzesp22.scoutout.domain.Game;
import com.progzesp22.scoutout.domain.GamesModel;
import com.progzesp22.scoutout.domain.TeamsModel;
import com.progzesp22.scoutout.domain.UserModel;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class UserGamesFragment extends Fragment {
    FragmentUserGamesBinding binding;
    GamesModel model;
    Timer timer;

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

        model = new ViewModelProvider(requireActivity()).get(GamesModel.class);

        binding.recycler.setLayoutManager(new LinearLayoutManager(getContext()));

        model.getGames().observe(getViewLifecycleOwner(), this::displayGames);
        model.refresh();

        binding.addGame.setOnClickListener(view1 -> {
                model.setActiveGame(null);
                NavHostFragment.findNavController(this).navigate(R.id.action_create_edit_game);
            }
        );
    }

    @Override
    public void onResume() {
        super.onResume();
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                model.refresh();
            }
        }, 0, 5000);
    }

    @Override
    public void onPause() {
        super.onPause();
        timer.cancel();
    }

    public void displayGames(List<Game> games){
        GamesModel model = new ViewModelProvider(requireActivity()).get(GamesModel.class);
        UserModel userModel = new ViewModelProvider(requireActivity()).get(UserModel.class);
        TeamsModel teamsModel = new ViewModelProvider(requireActivity()).get(TeamsModel.class);
        NavController navController = NavHostFragment.findNavController(this);

        binding.recycler.setAdapter(new GamesAdapter(games, navController, model, userModel, teamsModel));
//        int scrollY = binding.recycler.getScrollY();
//        binding.recycler.setScrollY(scrollY);
    }


}