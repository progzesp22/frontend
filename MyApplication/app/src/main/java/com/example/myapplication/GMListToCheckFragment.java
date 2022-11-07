package com.example.myapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.example.myapplication.databinding.FragmentGmListToAcceptBinding;

import java.util.List;


public class GMListToCheckFragment extends Fragment {
    FragmentGmListToAcceptBinding binding;

    public GMListToCheckFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentGmListToAcceptBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        AnswerModel model = new ViewModelProvider(requireActivity()).get(AnswerModel.class);
        model.getUncheckedAnswers().observe(requireActivity(), this::displayAnswers);

        binding.refreshAnswerActionButton.setOnClickListener(view1 -> model.refreshAnswers()); // TODO: rename
    }

    private void displayAnswers(List<Answer> answers) {
        binding.tasksLayout.removeAllViews(); // TODO: rename
        AnswerModel model = new ViewModelProvider(requireActivity()).get(AnswerModel.class);
        TasksModel taskModel = new ViewModelProvider(requireActivity()).get(TasksModel.class);


        for (Answer answer : answers) {
            Task task = taskModel.getById(answer.getTaskId());
            if (task == null) {
                continue;
            }

            AnswerView answerView = new AnswerView(getContext(), task.getName());
            answerView.setOnClick(view -> {
                model.setActiveAnswer(answer);
                NavHostFragment.findNavController(this).navigate(R.id.action_checkTask); // TODO: rename to check answer
            });
            binding.tasksLayout.addView(answerView);
        }
    }
}