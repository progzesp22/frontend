package com.progzesp22.scoutout.fragments.gm;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.progzesp22.scoutout.R;
import com.progzesp22.scoutout.TeamsExpandableListAdapter;
import com.progzesp22.scoutout.databinding.FragmentGmWaitForPlayersBinding;
import com.progzesp22.scoutout.domain.Game;
import com.progzesp22.scoutout.domain.GamesModel;
import com.progzesp22.scoutout.domain.Team;
import com.progzesp22.scoutout.domain.TeamsModel;

import java.util.List;
import java.util.Timer;


public class GMWaitForPlayersFragment extends Fragment {
    private FragmentGmWaitForPlayersBinding binding;
    TeamsExpandableListAdapter expandableListAdapter;
    Timer timer;
    TeamsModel teamsModel;
    GamesModel gamesModel;

    public GMWaitForPlayersFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        expandableListAdapter = new TeamsExpandableListAdapter(getContext());
        binding.teamsList.setAdapter(expandableListAdapter);
        teamsModel = new ViewModelProvider(requireActivity()).get(TeamsModel.class);
        gamesModel = new ViewModelProvider(requireActivity()).get(GamesModel.class);
        teamsModel.refresh(gamesModel.getActiveGame().getId());

        gamesModel.getGames().observe(getViewLifecycleOwner(), games -> {
            if (gamesModel.getActiveGame().getState() == Game.GameState.STARTED){
                NavHostFragment.findNavController(GMWaitForPlayersFragment.this)
                        .navigate(R.id.gameStart);
            }
        });

        teamsModel.getTeams(gamesModel.getActiveGame().getId()).observe(getViewLifecycleOwner(), this::displayTeams);



        binding.startButton.setOnClickListener((event)->startButtonPressed());
        binding.gameNameTextView.setText(gamesModel.getActiveGame().getName());
    }

    @Override
    public void onResume() {
        super.onResume();
        timer = new Timer();
        timer.schedule(new java.util.TimerTask() {
            @Override
            public void run() {
                teamsModel.refresh(gamesModel.getActiveGame().getId());
            }
        }, 0, 5000);
    }

    @Override
    public void onPause() {
        super.onPause();
        timer.cancel();
    }

    private void startButtonPressed(){
        Toast.makeText(getContext(), R.string.starting_game, Toast.LENGTH_SHORT).show();
        GamesModel gamesModel = new ViewModelProvider(requireActivity()).get(GamesModel.class);
        gamesModel.startGame(gamesModel.getActiveGame());
    }

    private void displayTeams(List<Team> teams){
        expandableListAdapter.getExpandableListTitle().clear();
        expandableListAdapter.getExpandableListDetail().clear();
        TeamsModel teamsModel = new ViewModelProvider(requireActivity()).get(TeamsModel.class);
        for (Team team : teams) {
            teamsModel.fetchTeamInfo(team.getId());
            expandableListAdapter.getExpandableListTitle().add(team.getName());
            expandableListAdapter.getExpandableListDetail().put(team.getName(), team.getMembers());
        }
        expandableListAdapter.notifyDataSetChanged();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentGmWaitForPlayersBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}