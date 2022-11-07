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

import com.example.myapplication.databinding.FragmentGmTaskAddBinding;


public class GMTaskAddFragment extends Fragment {
    private FragmentGmTaskAddBinding binding;


    public GMTaskAddFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentGmTaskAddBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TasksModel model = new ViewModelProvider(requireActivity()).get(TasksModel.class);

        binding.button.setOnClickListener(view1 -> {
            MainActivity.requestHandler.postTask(
                    binding.titleText.getText().toString(),
                    binding.descriptionText.getText().toString(),
                    "TEXT",
                    1,
                    response -> {
                        Toast.makeText(getContext(), "Wysłano", Toast.LENGTH_SHORT).show();
                        model.refreshTasks();
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