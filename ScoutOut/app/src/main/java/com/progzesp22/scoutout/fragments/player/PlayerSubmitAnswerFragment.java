package com.progzesp22.scoutout.fragments.player;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.progzesp22.scoutout.R;
import com.progzesp22.scoutout.databinding.FragmentPlayerSubmitAnswerBinding;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;
import com.progzesp22.scoutout.databinding.FragmentPlayerTasksBinding;
import com.progzesp22.scoutout.domain.TasksModel;
import com.progzesp22.scoutout.domain.Task;
import com.progzesp22.scoutout.MainActivity;
import com.progzesp22.scoutout.MyCaptureActivity;

public class PlayerSubmitAnswerFragment extends Fragment {
    private FragmentPlayerSubmitAnswerBinding binding;


    public PlayerSubmitAnswerFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPlayerSubmitAnswerBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TasksModel model = new ViewModelProvider(requireActivity()).get(TasksModel.class);
        Task task = model.getActiveTask();

        if (task == null) return;
        binding.titleText.setText(task.getName());
        binding.descriptionText.setText(task.getDescription());
        binding.answerButton.setOnClickListener(view1 -> {
            MainActivity.requestHandler.postAnswer(
                    task.getId(),
                    binding.answerText.getText().toString(),
                    response -> {
                        Toast.makeText(getContext(), "Wys??ano", Toast.LENGTH_SHORT).show();
                        NavHostFragment.findNavController(this).navigateUp();
                    },
                    error -> Toast.makeText(getContext(), R.string.server_error, Toast.LENGTH_LONG).show());
        });
        binding.scanQRButton.setOnClickListener(view2 -> scanCode());
    }

    private void scanCode() {
        ScanOptions options = new ScanOptions();
        options.setPrompt("W????cz lamp?? b??yskow??, u??ywaj??c przycisku VolumeUp.");
        options.setBeepEnabled(true);
        options.setOrientationLocked(true);
        options.setCaptureActivity(MyCaptureActivity.class);
        barLauncher.launch(options);
    }

    ActivityResultLauncher<ScanOptions> barLauncher = registerForActivityResult(new ScanContract(), result -> {
        if (result.getContents() != null) {
            Toast.makeText(getContext(), result.getContents(), Toast.LENGTH_SHORT).show();
        }
    });
}