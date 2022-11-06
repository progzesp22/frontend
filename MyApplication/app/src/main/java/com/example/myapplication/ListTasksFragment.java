package com.example.myapplication;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication.databinding.FragmentListTasksBinding;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;


public class ListTasksFragment extends Fragment {
    FragmentListTasksBinding binding;

    public ListTasksFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentListTasksBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TasksModel model = new ViewModelProvider(requireActivity()).get(TasksModel.class);
        model.getTasks().observe(requireActivity(), this::displayTasks);

        binding.refreshTasks.setOnClickListener(view1 -> model.refreshTasks());
    }

    private void displayTasks(List<Task> tasks){
        binding.tasksLayout.removeAllViews();

        for(Task task : tasks){
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