package com.progzesp22.scoutout.fragments.gm;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.progzesp22.scoutout.R;
import com.progzesp22.scoutout.databinding.FragmentGmAddTaskBinding;
import com.progzesp22.scoutout.domain.Entity;
import com.progzesp22.scoutout.domain.GamesModel;
import com.progzesp22.scoutout.domain.Task;
import com.progzesp22.scoutout.domain.TasksModel;
import com.progzesp22.scoutout.MainActivity;

import java.util.ArrayList;


public class GMAddTaskFragment extends Fragment {
    private FragmentGmAddTaskBinding binding;


    public GMAddTaskFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentGmAddTaskBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TasksModel model = new ViewModelProvider(requireActivity()).get(TasksModel.class);
        GamesModel gamesModel = new ViewModelProvider(requireActivity()).get(GamesModel.class);
        long activeGameId = gamesModel.getActiveGame().getId();

        binding.button.setOnClickListener(view1 -> {
            Task newTask = new Task(
                    Entity.UNKNOWN_ID,
                    binding.titleText.getText().toString(),
                    binding.descriptionText.getText().toString(),
                    Entity.UNKNOWN_ID, // Hardcoded game ID for now
                    Task.TaskType.TEXT,
                    new ArrayList<>()
            );

            MainActivity.requestHandler.postTask(
                    newTask,
                    response -> {
                        Toast.makeText(getContext(), "Wysłano", Toast.LENGTH_SHORT).show();
                        model.refresh(activeGameId);
                        NavHostFragment.findNavController(this).navigateUp();
                    },
                    error -> {
                        Toast.makeText(getContext(), R.string.server_error, Toast.LENGTH_SHORT).show();
                        Log.e("GMTaskAddFragment", error.toString());
                    }
            );
        });
    }

}