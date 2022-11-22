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

        Answer answer = taskModel.getActiveAnswer();

        if (answer == null) return;
        Task task = taskModel.getById(answer.getTaskId());
        if (task == null) return;

        binding.titleText.setText(task.getName());
        binding.descriptionText.setText(task.getDescription());
        binding.answerText.setText(answer.getAnswer());
        binding.accept.setOnClickListener(view1 -> {
            MainActivity.requestHandler.patchAnswer(answer.getId(), true, response -> {
                Toast.makeText(getContext(), "Zaakceptowano", Toast.LENGTH_SHORT).show();
                taskModel.refresh();
                NavHostFragment.findNavController(this).navigateUp();
            }, error -> {
                Toast.makeText(getContext(), R.string.server_error, Toast.LENGTH_SHORT).show();
            });
        });

        binding.decline.setOnClickListener(view1 -> {
            MainActivity.requestHandler.patchAnswer(answer.getId(), false, response -> {
                Toast.makeText(getContext(), "Odrzucono", Toast.LENGTH_SHORT).show();
                taskModel.refresh();
                NavHostFragment.findNavController(this).navigateUp();
            }, error -> {
                Toast.makeText(getContext(), R.string.server_error, Toast.LENGTH_SHORT).show();
            });
        });
    }

}