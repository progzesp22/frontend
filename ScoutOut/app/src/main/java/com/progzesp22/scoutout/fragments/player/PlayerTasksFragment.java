package com.progzesp22.scoutout.fragments.player;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.progzesp22.scoutout.R;
import com.progzesp22.scoutout.TaskView;
import com.progzesp22.scoutout.databinding.FragmentPlayerTasksBinding;
import com.progzesp22.scoutout.domain.Game;
import com.progzesp22.scoutout.domain.GamesModel;
import com.progzesp22.scoutout.domain.Task;
import com.progzesp22.scoutout.domain.TasksModel;
import com.progzesp22.scoutout.domain.Team;
import com.progzesp22.scoutout.domain.TeamsModel;
import com.progzesp22.scoutout.domain.UserModel;

import java.util.List;


public class PlayerTasksFragment extends Fragment {
    FragmentPlayerTasksBinding binding;

    public PlayerTasksFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentPlayerTasksBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        GamesModel gamesModel = new ViewModelProvider(requireActivity()).get(GamesModel.class);
        TeamsModel teamsModel = new ViewModelProvider(requireActivity()).get(TeamsModel.class);
        TasksModel model = new ViewModelProvider(requireActivity()).get(TasksModel.class);

        model.getTasks(gamesModel.getActiveGame().getId()).observe(getViewLifecycleOwner(), this::displayTasks);
        model.refresh(gamesModel.getActiveGame().getId());

        gamesModel.getGames().observe(getViewLifecycleOwner(), games -> {
            if (gamesModel.getActiveGame().getState() == Game.GameState.FINISHED) {
                NavHostFragment.findNavController(this).navigate(R.id.action_listTasksFragment_to_playerTeamRankingsFragment);
            } else if (gamesModel.getActiveGame().getState() != Game.GameState.STARTED){
                NavHostFragment.findNavController(this).navigate(R.id.userGamesFragment);
            }
        });

        teamsModel.getTeams(gamesModel.getActiveGame().getId()).observe(getViewLifecycleOwner(), teams -> {
            Team activeTeam = teamsModel.getActiveTeam();
            if (activeTeam == null) {
                String username = new ViewModelProvider(requireActivity()).get(UserModel.class).getUsername();
                boolean found = false;
                for (Team team : teams) {
                    if (team.getMembers().contains(username)) {
                        activeTeam = team;
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    Log.e("PlayerTasksFragment", "onViewCreated: Player does't have any team");
                } else {
                    binding.points.setText(String.valueOf(activeTeam.getScore()));
                }
            }else{
                binding.points.setText(String.valueOf(activeTeam.getScore()));
            }
        });

        binding.refreshTasks.setOnClickListener(view1 -> {
            gamesModel.refresh();
            model.refresh(gamesModel.getActiveGame().getId());
            teamsModel.refresh(gamesModel.getActiveGame().getId());
        });
    }

    private void displayTasks(List<Task> tasks) {
        binding.tasksLayout.removeAllViews();

        for (Task task : tasks) {
            TaskView taskView = new TaskView(getContext(), task);
            taskView.setOnClick(view -> {
                TasksModel model = new ViewModelProvider(requireActivity()).get(TasksModel.class);
                model.setActiveTask(task);
                NavHostFragment.findNavController(this).navigate(R.id.action_listTasksFragment_to_taskViewFragment);
            });
            binding.tasksLayout.addView(taskView);
        }
    }
}