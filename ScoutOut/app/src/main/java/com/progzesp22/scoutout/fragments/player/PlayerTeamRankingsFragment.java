package com.progzesp22.scoutout.fragments.player;

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
import com.progzesp22.scoutout.databinding.FragmentPlayerTeamRankingsBinding;
import com.progzesp22.scoutout.domain.Game;
import com.progzesp22.scoutout.domain.GamesModel;
import com.progzesp22.scoutout.domain.Team;
import com.progzesp22.scoutout.domain.TeamsModel;

import java.util.List;
import java.util.Timer;
import java.util.stream.Collectors;


public class PlayerTeamRankingsFragment extends Fragment {
    private FragmentPlayerTeamRankingsBinding binding;
    TeamsExpandableListAdapter expandableListAdapter;
    Timer timer = new Timer();

    public PlayerTeamRankingsFragment() {
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
        TeamsModel teamsModel = new ViewModelProvider(requireActivity()).get(TeamsModel.class);
        GamesModel gamesModel = new ViewModelProvider(requireActivity()).get(GamesModel.class);
        teamsModel.refresh(gamesModel.getActiveGame().getId());

//        gamesModel.getGames().observe(getViewLifecycleOwner(), games -> {
//            if (gamesModel.getActiveGame().getState() == Game.GameState.STARTED){
//                NavHostFragment.findNavController(PlayerTeamRankingsFragment.this)
//                        .navigate(R.id.gameStart);
//            }
//        });

        teamsModel.getTeams(gamesModel.getActiveGame().getId()).observe(getViewLifecycleOwner(), this::displayTeams);

        timer.schedule(new java.util.TimerTask() {
            @Override
            public void run() {
                teamsModel.refresh(gamesModel.getActiveGame().getId());
                System.out.println("refreshing");
            }
        }, 0, 5000);

        binding.gameNameTextView.setText(gamesModel.getActiveGame().getName());
    }

    private void displayTeams(List<Team> teams){
        expandableListAdapter.getExpandableListTitle().clear();
        expandableListAdapter.getExpandableListDetail().clear();
        TeamsModel teamsModel = new ViewModelProvider(requireActivity()).get(TeamsModel.class);
        List<Team> teamsSorted = teams.stream().sorted((t1, t2) -> -Long.compare(t1.getScore(), t2.getScore())).collect(Collectors.toList());
        for (Team team : teamsSorted) {
            teamsModel.fetchTeamInfo(team.getId());
            expandableListAdapter.getExpandableListTitle().add(team.getName() + " (" + team.getScore() + ")");
            expandableListAdapter.getExpandableListDetail().put(team.getName() + " (" + team.getScore() + ")", team.getMembers());
        }
        expandableListAdapter.notifyDataSetChanged();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentPlayerTeamRankingsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        timer.cancel();
        super.onDestroyView();
    }
}