package com.progzesp22.scoutout.fragments.gm;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.progzesp22.scoutout.R;
import com.progzesp22.scoutout.databinding.FragmentGmListUncheckedAnswerBinding;
import com.progzesp22.scoutout.domain.Answer;
import com.progzesp22.scoutout.domain.Task;
import com.progzesp22.scoutout.domain.TasksModel;
import com.progzesp22.scoutout.AnswerView;

import java.util.List;


public class GMListUncheckedAnswersFragment extends Fragment {
    FragmentGmListUncheckedAnswerBinding binding;

    public GMListUncheckedAnswersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentGmListUncheckedAnswerBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TasksModel tasksModel = new ViewModelProvider((requireActivity())).get(TasksModel.class);
        tasksModel.getTasks().observe(getViewLifecycleOwner(), tasks -> {
            List<Answer> answers = tasksModel.getUncheckedAnswers();
            if (answers != null) {
                displayAnswers(answers);
            }
        });

        tasksModel.refresh();

        binding.refreshAnswerActionButton.setOnClickListener(view1 -> tasksModel.refresh());
    }

    private void displayAnswers(List<Answer> answers) {
        binding.answersLayout.removeAllViews();
        TasksModel taskModel = new ViewModelProvider(requireActivity()).get(TasksModel.class);


        for (Answer answer : answers) {
            Task task = taskModel.getById(answer.getTaskId());
            if (task == null) {
                continue;
            }

            AnswerView answerView = new AnswerView(getContext(), task.getName());
            answerView.setOnClick(view -> {
                taskModel.setActiveAnswer(answer);
                NavHostFragment.findNavController(this).navigate(R.id.action_checkAnswer);
            });
            binding.answersLayout.addView(answerView);
        }
    }
}