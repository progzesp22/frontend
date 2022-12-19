package com.progzesp22.scoutout.fragments.player;

import android.os.Bundle;
import android.util.Log;
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

import com.progzesp22.scoutout.MainActivity;
import com.progzesp22.scoutout.R;
import com.progzesp22.scoutout.databinding.FragmentPlayerTeamsBinding;
import com.progzesp22.scoutout.domain.Entity;
import com.progzesp22.scoutout.domain.Game;
import com.progzesp22.scoutout.domain.GamesModel;
import com.progzesp22.scoutout.domain.Team;
import com.progzesp22.scoutout.domain.TeamsModel;
import com.progzesp22.scoutout.domain.UserModel;
import com.progzesp22.scoutout.fragments.TeamsAdapter;

import org.json.JSONException;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class PlayerTeamsFragment extends Fragment {
    FragmentPlayerTeamsBinding binding;
    NavController navController;

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
        binding.recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        TeamsModel teamsModel = new ViewModelProvider(requireActivity()).get(TeamsModel.class);
        UserModel userModel = new ViewModelProvider(requireActivity()).get(UserModel.class);
        GamesModel gamesModel = new ViewModelProvider(requireActivity()).get(GamesModel.class);
        Game activeGame = gamesModel.getActiveGame();

        teamsModel.getTeams(activeGame.getId()).observe(getViewLifecycleOwner(), this::displayTeams);
        teamsModel.refresh(activeGame.getId());
        setGameInfoTexts(gamesModel);
        binding.createTeamButton.setOnClickListener(view1 -> {
            List<String> members = new ArrayList<>();
            members.add(userModel.getUsername());
            Team newTeam = new Team(
                    Entity.UNKNOWN_ID,
                    gamesModel.getActiveGame().getId(),
                    String.valueOf(binding.newTeamName.getText()),
                    userModel.getUsername(),
                    members
            );
            MainActivity.requestHandler.postTeams(newTeam, response -> {
                Toast.makeText(view1.getContext(), "Team created successfully", Toast.LENGTH_SHORT).show();
            }, error -> {
                Toast.makeText(view1.getContext(), "Error, team not created", Toast.LENGTH_SHORT).show();
            } );
            binding.newTeamName.setText("");
            teamsModel.setActiveTeam(newTeam);
            NavHostFragment.findNavController(this).navigate(R.id.action_playerTeamsFragment_to_userGamesFragment);
            });

    }

    public void displayTeams(List<Team> teams){
        TeamsModel model = new ViewModelProvider(requireActivity()).get(TeamsModel.class);
        NavController navController = NavHostFragment.findNavController(this);
        binding.recycler.setAdapter(new TeamsAdapter(teams, navController, model));
    }

    private void setGameInfoTexts(GamesModel gamesModel) {
        binding.gameNameText.setText(gamesModel.getActiveGame().getName());
        binding.gameMasterText.setText(gamesModel.getActiveGame().getGameMaster());
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'\n'HH:mm", Locale.getDefault());
        Date startTime = gamesModel.getActiveGame().getStartTime();
        Date endTime = gamesModel.getActiveGame().getEndTime();
        if(startTime != null){
            binding.startTimeText.setText(dateFormat.format(startTime));
        } else {
            binding.startTimeText.setText("");
        }

        if(endTime != null){
            binding.endTimeText.setText(dateFormat.format(endTime));
        } else{
            binding.endTimeText.setText("");
        }
    }


}