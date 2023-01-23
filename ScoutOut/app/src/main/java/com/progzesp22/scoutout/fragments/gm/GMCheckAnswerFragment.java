package com.progzesp22.scoutout.fragments.gm;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import java.util.Base64;


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
        show(task, answer);


        taskModel.getTasks(activeGameId).observe(getViewLifecycleOwner(), tasks -> {
            show(task, answer);
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
                Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();
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
                Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();
            });
        });


        taskModel.downloadFullAnswer(answer.getId());

    }

    private void show(Task task, Answer ans){
        binding.titleText.setText(task.getName());
        binding.descriptionText.setText(task.getDescription());

        binding.answerText.setVisibility(View.GONE);
        binding.image.setVisibility(View.GONE);
        switch(task.getType()){
            case TEXT:
            case QR_CODE:
                binding.answerText.setVisibility(View.VISIBLE);
                binding.answerText.setText(ans.getAnswer());
                break;
            case PHOTO:
                binding.image.setVisibility(View.VISIBLE);
                byte[] decodedString = Base64.getDecoder().decode(ans.getAnswer());
                Bitmap bmp = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                binding.image.setImageBitmap(bmp);
                break;
            case NAV_POS:
                break;
            case AUDIO:
                break;
        }

    }
}