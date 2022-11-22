package com.progzesp22.scoutout.fragments.player;

import android.os.Bundle;
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
import com.progzesp22.scoutout.domain.Task;
import com.progzesp22.scoutout.domain.TasksModel;

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

        TasksModel model = new ViewModelProvider(requireActivity()).get(TasksModel.class);
        model.getTasks().observe(getViewLifecycleOwner(), this::displayTasks);
        model.refresh();

        binding.refreshTasks.setOnClickListener(view1 -> model.refresh());
    }

    private void displayTasks(List<Task> tasks) {
        binding.tasksLayout.removeAllViews();

        for (Task task : tasks) {
            TaskView taskView = new TaskView(getContext(), task);
            taskView.setOnClick(view -> {
                TasksModel model = new ViewModelProvider(requireActivity()).get(TasksModel.class);
                model.setActiveTask(task);
                NavHostFragment.findNavController(this).navigate(R.id.action_answerTask);
            });
            binding.tasksLayout.addView(taskView);
        }
    }
}