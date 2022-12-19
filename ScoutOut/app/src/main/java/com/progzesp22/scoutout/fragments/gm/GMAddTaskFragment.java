package com.progzesp22.scoutout.fragments.gm;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.progzesp22.scoutout.R;
import com.progzesp22.scoutout.RequestHandler;
import com.progzesp22.scoutout.databinding.FragmentGmAddTaskBinding;
import com.progzesp22.scoutout.domain.Task;
import com.progzesp22.scoutout.domain.TasksModel;
import com.progzesp22.scoutout.MainActivity;

import java.util.ArrayList;


public class GMAddTaskFragment extends Fragment {
    private FragmentGmAddTaskBinding binding;
    long maxScore;
    long gameId;
    String taskTitle = "";
    String taskDescription = "";
    String answerType = "TEXT"; //domyślnie odpowiedź tekstowa


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
        final RadioButton answerTypeTextButton = requireView().findViewById(R.id.answerTypeTextRadioButton);
        final RadioButton answerTypeQRButton = requireView().findViewById(R.id.answerTypeQRRadioButton);
        final RadioButton answerTypePictureButton = requireView().findViewById(R.id.answerTypePictureRadioButton);
        final RadioButton answerTypeAudioButton = requireView().findViewById(R.id.answerTypeAudioRadioButton);
//        final RadioButton answerTypeNavButton = requireView().findViewById(R.id.answerTypeNavigationRadioButton);

        TasksModel model = new ViewModelProvider(requireActivity()).get(TasksModel.class);

        binding.answerTypeRadio.setOnCheckedChangeListener((group, checkedId) -> {
            if(answerTypeTextButton.isChecked()) {
                answerType = "TEXT";
            }
            else if(answerTypeQRButton.isChecked()) {
                answerType = "QR_CODE";
            }
            else if(answerTypePictureButton.isChecked()) {
                answerType = "PHOTO";
            }
            else if(answerTypeAudioButton.isChecked()) {
                answerType = "AUDIO";
            }
            else {
                answerType = "NAV_POS";
            }
        });

        binding.button.setOnClickListener(view1 -> {
            taskTitle = binding.titleText.getText().toString();
            taskDescription = binding.descriptionText.getText().toString();
            if(taskTitle.isEmpty()) {
                Toast.makeText(getContext(), "Brak tytułu!", Toast.LENGTH_SHORT).show();
            }
            else {
                try {
                    maxScore = Integer.parseInt(binding.taskScoreText.getText().toString());
                } catch (NumberFormatException ex) {
                    Toast.makeText(getContext(), "Nieprawidłowa liczba punktów!", Toast.LENGTH_SHORT).show();
                    return;
                }
                String toSend = "name: " + taskTitle + ", description: " + taskDescription + ", type: " + answerType +  " prerequisiteTasks: [],  maxScore: " + maxScore;
                Toast.makeText(getContext(),  toSend, Toast.LENGTH_LONG).show();
            }

//            Task newTask = new Task(
//                    Task.UNKNOWN_ID,
//                    binding.titleText.getText().toString(),
//                    binding.descriptionText.getText().toString(),
//                    RequestHandler.GAME_ID, // Hardcoded game ID for now
//                    Task.TaskType.TEXT,
//                    new ArrayList<>()
//            );
//
//            MainActivity.requestHandler.postTask(
//                    newTask,
//                    response -> {
//                        Toast.makeText(getContext(), "Wysłano", Toast.LENGTH_SHORT).show();
//                        model.refresh();
//                        NavHostFragment.findNavController(this).navigateUp();
//                    },
//                    error -> {
//                        Toast.makeText(getContext(), R.string.server_error, Toast.LENGTH_SHORT).show();
//                        Log.e("GMTaskAddFragment", error.toString());
//                    }
//            );
        });
    }

}