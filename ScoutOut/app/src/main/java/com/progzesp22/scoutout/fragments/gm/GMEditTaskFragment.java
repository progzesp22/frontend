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

import com.android.volley.VolleyError;
import com.progzesp22.scoutout.R;
import com.progzesp22.scoutout.databinding.FragmentGmEditTaskBinding;
import com.progzesp22.scoutout.domain.Entity;
import com.progzesp22.scoutout.domain.GamesModel;
import com.progzesp22.scoutout.domain.Task;
import com.progzesp22.scoutout.domain.TasksModel;
import com.progzesp22.scoutout.MainActivity;

import org.json.JSONObject;

import java.util.LinkedList;


public class GMEditTaskFragment extends Fragment {
    private FragmentGmEditTaskBinding binding;
    private Task task;
    TasksModel tasksModel;

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

    private void loadOrCreateTask(Task incomingTask){
        if(incomingTask == null){
            GamesModel gamesModel = new ViewModelProvider(requireActivity()).get(GamesModel.class);
            long gameId = gamesModel.getActiveGame().getId();
            task = new Task(Entity.UNKNOWN_ID, "", "", gameId, Task.TaskType.TEXT, new LinkedList<>());
        } else{
            task = new Task(incomingTask);
        }

        binding.titleText.setText(task.getName());
        binding.descriptionText.setText(task.getDescription());
    }

    private void saveToServer(){
        if(task.getId() == Entity.UNKNOWN_ID){
            // posting as new task
            MainActivity.requestHandler.postTask(task, this::serverCommunicationSuccess, this::serverCommunicationError);
        } else{
            // patching existing task
            MainActivity.requestHandler.patchTask(task, this::serverCommunicationSuccess, this::serverCommunicationError);
        }
    }

    private void serverCommunicationSuccess(JSONObject response){
        tasksModel.refresh();
        Toast.makeText(getContext(), "Sukces", Toast.LENGTH_SHORT).show();
        NavHostFragment.findNavController(this).navigateUp();
    }

    private void serverCommunicationError(VolleyError error){
        Toast.makeText(getContext(), R.string.server_error, Toast.LENGTH_SHORT).show();
        Log.e("GMTaskEditFragment", error.toString());
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tasksModel = new ViewModelProvider(requireActivity()).get(TasksModel.class);
        loadOrCreateTask(tasksModel.getActiveTask());

        binding.button.setOnClickListener(view1 -> {
            task.setName(binding.titleText.getText().toString());
            task.setDescription(binding.descriptionText.getText().toString());
            saveToServer();
        });
    }
}