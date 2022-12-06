package com.progzesp22.scoutout.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.progzesp22.scoutout.databinding.FragmentUserGamesBinding;
import com.progzesp22.scoutout.domain.Game;

import java.util.LinkedList;


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

        LinkedList<Game> games = new LinkedList<>();
        games.add(new Game(1001, "Zbieramy szyszki", "kris", Game.GameState.PENDING));
        games.add(new Game(1000, "Druga gra", "mizoz", Game.GameState.STARTED));

        binding.recycler.setAdapter(new GamesAdapter(games));
        binding.recycler.setLayoutManager(new LinearLayoutManager(getContext()));
    }


}