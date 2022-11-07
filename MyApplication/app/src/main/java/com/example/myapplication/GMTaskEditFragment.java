package com.example.myapplication;

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

import com.example.myapplication.databinding.FragmentGmTaskEditBinding;


public class GMTaskEditFragment extends Fragment {
    private FragmentGmTaskEditBinding binding;


    public GMTaskEditFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentGmTaskEditBinding.inflate(inflater, container, false);
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
            MainActivity.requestHandler.putTask(
                    task.getId(),
                    binding.titleText.getText().toString(),
                    binding.descriptionText.getText().toString(),
                    1,
                    "TEXT",
                    response -> {
                        model.refreshTasks();
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