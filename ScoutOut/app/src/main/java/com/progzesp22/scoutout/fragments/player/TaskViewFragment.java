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
import com.progzesp22.scoutout.databinding.FragmentTaskViewBinding;
import com.progzesp22.scoutout.domain.Task;
import com.progzesp22.scoutout.domain.TasksModel;

public class TaskViewFragment extends Fragment {

    FragmentTaskViewBinding binding;

    public TaskViewFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentTaskViewBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.goToUpload.setOnClickListener(view1 -> {
            NavHostFragment.findNavController(this).navigate(R.id.action_taskViewFragment_to_taskAnswerFragment);
        });

        binding.goToPreviousAnwsers.setOnClickListener(view1 -> {
            NavHostFragment.findNavController(this).navigate(R.id.action_taskViewFragment_to_previousAnwsersFragment);
        });

        TasksModel tasksModel = new ViewModelProvider(requireActivity()).get(TasksModel.class);
        Task activeTask = tasksModel.getActiveTask();

        if (activeTask == null) {
            NavHostFragment.findNavController(this).navigateUp();
            Log.e("TASK-VIEW", "onViewCreated: activeTask is null");
            return;
        }

        binding.taskName.setText(activeTask.getName());
        binding.taskDescription.setText(activeTask.getDescription());

        //TODO: populate ListView with subtasks.
    }
}