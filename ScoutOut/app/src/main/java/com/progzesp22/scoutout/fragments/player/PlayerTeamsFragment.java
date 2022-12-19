package com.progzesp22.scoutout.fragments.player;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.progzesp22.scoutout.R;
import com.progzesp22.scoutout.databinding.FragmentPlayerTeamsBinding;
import com.progzesp22.scoutout.domain.Game;
import com.progzesp22.scoutout.domain.GamesModel;
import com.progzesp22.scoutout.domain.Team;
import com.progzesp22.scoutout.domain.TeamsModel;
import com.progzesp22.scoutout.domain.UserModel;
import com.progzesp22.scoutout.fragments.TeamsAdapter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class PlayerTeamsFragment extends Fragment {
    FragmentPlayerTeamsBinding binding;
    DateFormat df = new SimpleDateFormat("dd-MM-yy HH:mm", Locale.getDefault());

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
        GamesModel gamesModel = new ViewModelProvider(requireActivity()).get(GamesModel.class);
        TeamsModel teamsModel = new ViewModelProvider(requireActivity()).get(TeamsModel.class);
        UserModel userModel = new ViewModelProvider(requireActivity()).get(UserModel.class);

        Game activeGame = gamesModel.getActiveGame();

        binding.createTeamButton.setOnClickListener(view1 -> {
            if (binding.newTeamName.getText().toString().equals("")){
                Toast.makeText(getContext(), "Podaj nazwę drużyny", Toast.LENGTH_SHORT).show();
            } else {
                teamsModel.addTeam(activeGame.getId(), binding.newTeamName.getText().toString(), userModel.getUsername());
            }
        });

        binding.recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        teamsModel.getTeams(activeGame.getId()).observe(getViewLifecycleOwner(), this::displayTeams);
        teamsModel.refresh(activeGame.getId());

        binding.gameNameText.setText(activeGame.getName());
        binding.gameMasterText.setText(activeGame.getGameMaster());
        if (activeGame.getStartTime() != null) {
            binding.startTimeText.setText(df.format(activeGame.getStartTime()));
        } else {
            binding.startTimeText.setVisibility(View.INVISIBLE);
        }
        if (activeGame.getEndTime() != null) {
            binding.endTimeText.setText(df.format(activeGame.getEndTime()));
        } else {
            binding.endTimeText.setVisibility(View.INVISIBLE);
        }
    }

    public void displayTeams(List<Team> teams){
        GamesModel gamesModel = new ViewModelProvider(requireActivity()).get(GamesModel.class);
        long activeGameId = gamesModel.getActiveGame().getId();
        List<Team> filteredTeams = new ArrayList<>();
        for (Team team : teams) {
            if (team.getGameId() == activeGameId) {
                filteredTeams.add(team);
            }
        }

        TeamsModel model = new ViewModelProvider(requireActivity()).get(TeamsModel.class);
        NavController navController = NavHostFragment.findNavController(this);
        binding.recycler.setAdapter(new TeamsAdapter(filteredTeams, navController, model));
    }


}