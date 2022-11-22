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
import com.progzesp22.scoutout.databinding.FragmentGmEditTaskBinding;
import com.progzesp22.scoutout.domain.Task;
import com.progzesp22.scoutout.domain.TasksModel;
import com.progzesp22.scoutout.MainActivity;


public class GMEditTaskFragment extends Fragment {
    private FragmentGmEditTaskBinding binding;


    public GMEditTaskFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentGmEditTaskBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TasksModel model = new ViewModelProvider(requireActivity()).get(TasksModel.class);
        Task task = model.getActiveTask();

        if (task == null) return;
        binding.titleText.setText(task.getName());
        binding.descriptionText.setText(task.getDescription());

        binding.button.setOnClickListener(view1 -> {

            Task editedTask = new Task(
                    task.getId(),
                    binding.titleText.getText().toString(),
                    binding.descriptionText.getText().toString(),
                    task.getGameId(),
                    task.getType(),
                    task.getPrerequisiteTasks()
            );

            MainActivity.requestHandler.patchTask(
                    editedTask,
                    response -> {
                        model.refresh();
                        Toast.makeText(getContext(), "Zmieniono", Toast.LENGTH_SHORT).show();
                        NavHostFragment.findNavController(this).navigateUp();
                    },
                    error -> {
                        Toast.makeText(getContext(), R.string.server_error, Toast.LENGTH_SHORT).show();
                        Log.e("GMTaskEditFragment", error.toString());
                    }
            );
        });
    }
}