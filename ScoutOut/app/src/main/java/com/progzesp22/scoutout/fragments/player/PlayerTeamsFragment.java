package com.progzesp22.scoutout.fragments.player;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.progzesp22.scoutout.R;
import com.progzesp22.scoutout.databinding.FragmentPlayerTeamsBinding;
import com.progzesp22.scoutout.domain.GamesModel;
import com.progzesp22.scoutout.domain.Team;
import com.progzesp22.scoutout.domain.TeamsModel;
import com.progzesp22.scoutout.fragments.TeamsAdapter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;


public class PlayerTeamsFragment extends Fragment {
    FragmentPlayerTeamsBinding binding;

    public PlayerTeamsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPlayerTeamsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.createTeamButton.setOnClickListener(view1 -> {
            binding.newTeamName.setText("");
            Toast.makeText(view1.getContext(), "Team created successfully", Toast.LENGTH_SHORT).show();});
        binding.recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        TeamsModel model = new ViewModelProvider(requireActivity()).get(TeamsModel.class);
        model.getTeams().observe(getViewLifecycleOwner(), this::displayTeams);
        model.refresh();
        GamesModel gamesModel = new ViewModelProvider(requireActivity()).get(GamesModel.class);
        binding.gameNameText.setText(gamesModel.getActiveGame().getName());
        binding.gameMasterText.setText(gamesModel.getActiveGame().getGameMaster());
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'\n'HH:mm", Locale.getDefault());
        binding.startTimeText.setText(dateFormat.format(gamesModel.getActiveGame().getStartTime()));
        binding.endTimeText.setText(dateFormat.format(gamesModel.getActiveGame().getEndTime()));

    }

    public void displayTeams(List<Team> teams){
        TeamsModel model = new ViewModelProvider(requireActivity()).get(TeamsModel.class);
        NavController navController = NavHostFragment.findNavController(this);
        binding.recycler.setAdapter(new TeamsAdapter(teams, navController, model));
    }


}