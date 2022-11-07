package com.example.myapplication;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.myapplication.databinding.FragmentListTasksBinding;
import com.example.myapplication.databinding.FragmentTaskAnswerBinding;

import java.util.Objects;


public class TaskAnswerFragment extends Fragment {
    private FragmentTaskAnswerBinding binding;


    public TaskAnswerFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentTaskAnswerBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TasksModel model = new ViewModelProvider(requireActivity()).get(TasksModel.class);
        Task task = model.getActiveTask();

        if(task == null) return;
        binding.titleText.setText(task.getName());
        binding.descriptionText.setText(task.getDescription());
        binding.answerButton.setOnClickListener(view1 -> {
            int user_id = 7;
            MainActivity.requestHandler.postAnswer(
                    task.getId(),
                    user_id,
                    binding.answerText.getText().toString(),
                    response -> {
                        Toast.makeText(getContext(), "Wysłano", Toast.LENGTH_SHORT).show();
                        NavHostFragment.findNavController(this).navigateUp();
                    },
                    error -> Toast.makeText(getContext(), R.string.server_error, Toast.LENGTH_LONG).show());
        });
    }
}