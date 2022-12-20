package com.progzesp22.scoutout.fragments.gm;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.progzesp22.scoutout.R;
import com.progzesp22.scoutout.databinding.FragmentGmCheckAnswerBinding;
import com.progzesp22.scoutout.domain.Answer;
import com.progzesp22.scoutout.domain.GamesModel;
import com.progzesp22.scoutout.domain.Task;
import com.progzesp22.scoutout.domain.TasksModel;
import com.progzesp22.scoutout.MainActivity;


public class GMCheckAnswerFragment extends Fragment {
    private FragmentGmCheckAnswerBinding binding;


    public GMCheckAnswerFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentGmCheckAnswerBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TasksModel taskModel = new ViewModelProvider(requireActivity()).get(TasksModel.class);
        GamesModel gamesModel = new ViewModelProvider(requireActivity()).get(GamesModel.class);
        long activeGameId = gamesModel.getActiveGame().getId();

        Answer answer = taskModel.getActiveAnswer();

        if (answer == null) return;
        Task task = taskModel.getById(answer.getTaskId());
        if (task == null) return;
        showTask(task);
        showAnswer(answer);

        taskModel.getTasks(activeGameId).observe(getViewLifecycleOwner(), tasks -> {
            showAnswer(answer);
            showTask(task);
        });



        binding.accept.setOnClickListener(view1 -> {
            answer.setApproved(true);
            answer.setChecked(true);
            answer.setScore(task.getMaxScore());

            MainActivity.requestHandler.patchAnswer(answer, response -> {
                Toast.makeText(getContext(), "Zaakceptowano", Toast.LENGTH_SHORT).show();
                taskModel.refresh(activeGameId);
                NavHostFragment.findNavController(this).navigateUp();
            }, error -> {
                Toast.makeText(getContext(), R.string.server_error, Toast.LENGTH_SHORT).show();
            });
        });

        binding.decline.setOnClickListener(view1 -> {
            answer.setApproved(false);
            answer.setChecked(true);
            answer.setScore(0);

            MainActivity.requestHandler.patchAnswer(answer, response -> {
                Toast.makeText(getContext(), "Odrzucono", Toast.LENGTH_SHORT).show();
                taskModel.refresh(activeGameId);
                NavHostFragment.findNavController(this).navigateUp();
            }, error -> {
                Toast.makeText(getContext(), R.string.server_error, Toast.LENGTH_SHORT).show();
            });
        });


        taskModel.downloadFullAnswer(answer.getId());

    }

    private void showTask(Task task){
        binding.titleText.setText(task.getName());
        binding.descriptionText.setText(task.getDescription());
    }

    private void showAnswer(Answer ans){
        binding.answerText.setText(ans.getAnswer());
    }

}