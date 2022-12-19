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

import com.progzesp22.scoutout.MainActivity;
import com.progzesp22.scoutout.R;
import com.progzesp22.scoutout.TeamsExpandableListAdapter;
import com.progzesp22.scoutout.RequestInterface;
import com.progzesp22.scoutout.databinding.FragmentGmWaitForPlayersBinding;
import com.progzesp22.scoutout.domain.Game;
import com.progzesp22.scoutout.domain.GamesModel;
import com.progzesp22.scoutout.domain.Team;
import com.progzesp22.scoutout.domain.TeamsModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;


public class GMWaitForPlayersFragment extends Fragment {
    private FragmentGmWaitForPlayersBinding binding;
    TeamsExpandableListAdapter expandableListAdapter;

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
        TeamsModel teamsModel = new ViewModelProvider(requireActivity()).get(TeamsModel.class);
        GamesModel gamesModel = new ViewModelProvider(requireActivity()).get(GamesModel.class);
        teamsModel.refresh(gamesModel.getActiveGame().getId());

        gamesModel.getGames().observe(getViewLifecycleOwner(), games -> {
            if (gamesModel.getActiveGame().getState() == Game.GameState.STARTED){
                NavHostFragment.findNavController(GMWaitForPlayersFragment.this)
                        .navigate(R.id.gameStart);
            }
        });

        teamsModel.getTeams(gamesModel.getActiveGame().getId()).observe(getViewLifecycleOwner(), this::displayTeams);

        binding.refreshTeamsButton.setOnClickListener(view1 -> {
            teamsModel.refresh(gamesModel.getActiveGame().getId());
        });

        binding.startButton.setOnClickListener((event)->startButtonPressed());
        binding.gameNameTextView.setText(gamesModel.getActiveGame().getName());
    }

    private void startButtonPressed(){
        Toast.makeText(getContext(), "Starting game", Toast.LENGTH_SHORT).show();
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