package com.progzesp22.scoutout.fragments.gm;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.progzesp22.scoutout.MainActivity;
import com.progzesp22.scoutout.R;
import com.progzesp22.scoutout.SelectDateTimeFragment;
import com.progzesp22.scoutout.databinding.FragmentGmNewGameBinding;
import com.progzesp22.scoutout.domain.Entity;
import com.progzesp22.scoutout.domain.Game;
import com.progzesp22.scoutout.domain.GamesModel;
import com.progzesp22.scoutout.domain.Task;
import com.progzesp22.scoutout.domain.TasksModel;
import com.progzesp22.scoutout.TasksAdapter;
import com.progzesp22.scoutout.domain.UserModel;

import org.json.JSONException;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class GMNewGameFragment extends Fragment {
    private FragmentGmNewGameBinding binding;
    private Game game;
    private static final String TAG = "GMNewGameFragment";
    static public final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());


    public GMNewGameFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentGmNewGameBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        GamesModel gameModel = new ViewModelProvider(requireActivity()).get(GamesModel.class);

        binding.tasksList.setLayoutManager(new LinearLayoutManager(requireContext()));

        binding.gameStartTime.setOnClickListener(view1 -> {
            if (binding.gameStartTime.isChecked()) {
                DialogFragment newFragment = new SelectDateTimeFragment(calendar -> {
                    binding.gameStartTextView.setText(dateFormat.format(calendar.getTime()));
                });
                newFragment.show(requireFragmentManager(), "DatePicker");
            }
        });

        binding.endGameRadio.setOnCheckedChangeListener((group, checkedId) -> {
            if (binding.endGamePointsRadioButton.isChecked()) {
                binding.editTextEndCondition.setVisibility(View.VISIBLE);
                binding.editTextEndCondition.getText().clear();
                binding.editTextEndCondition.setHint("Wprowadź liczbę");
                binding.editTextEndCondition.setEnabled(true);
            } else if (binding.endGameTimeRadioButton.isChecked()) {
                binding.editTextEndCondition.setVisibility(View.VISIBLE);
                binding.editTextEndCondition.getText().clear();
                binding.editTextEndCondition.setHint("");
                DialogFragment newFragment = new SelectDateTimeFragment(calendar -> {
                    binding.editTextEndCondition.setText(dateFormat.format(calendar));
                });
                newFragment.show(requireFragmentManager(), "DatePicker");
                binding.editTextEndCondition.setEnabled(false);
            } else {
                binding.editTextEndCondition.setVisibility(View.INVISIBLE);
                binding.editTextEndCondition.getText().clear();
                binding.editTextEndCondition.setHint("");
                binding.editTextEndCondition.setEnabled(false);
            }
        });

        binding.addTask.setOnClickListener(view1 -> {
            if(game.getId() == Entity.UNKNOWN_ID){
                Toast.makeText(getContext(), "Należy zapisać grę przed dodaniem zadań", Toast.LENGTH_SHORT).show();
                return;
            }

            if(gameModel.getActiveGame() == null){
                Toast.makeText(getContext(), "Należy zapisać grę przed dodaniem zadań", Toast.LENGTH_SHORT).show();
                return;
            }
            TasksModel tasksModel = new ViewModelProvider(requireActivity()).get(TasksModel.class);
            tasksModel.setActiveTask(null);

            NavHostFragment.findNavController(this).navigate(R.id.action_add_edit_task);
        });

        binding.saveGameButton.setOnClickListener((group)-> {
            Log.d(TAG, "Save game button clicked");
            if (updateGameFromInput(game)) return;
            Log.d(TAG, "Input checks ok");

            if(game.getId() == Entity.UNKNOWN_ID){
                postNewGame();
            } else{
                updateExistingGame();
            }
        });

        binding.startGame.setOnClickListener(view1 -> {
            Log.d(TAG, "Start game button clicked");

            if(game.getId() == Entity.UNKNOWN_ID){
                Toast.makeText(getContext(), "Należy zapisać grę przed wystartowaniem jej", Toast.LENGTH_SHORT).show();
                return;
            }
            game.setState(Game.GameState.PENDING);
            MainActivity.requestHandler.patchGame(game, response -> {
                NavHostFragment.findNavController(this).navigate(R.id.action_open_lobby);
            }, error->{
                Toast.makeText(getContext(), "Błąd otwierania lobby", Toast.LENGTH_SHORT).show();
            });
        });

        loadGameOrCreateNew(gameModel.getActiveGame());

        observeTasks();
    }

    private void observeTasks() {
        TasksModel tasksModel = new ViewModelProvider(requireActivity()).get(TasksModel.class);
        if(game.getId() != Entity.UNKNOWN_ID){
            tasksModel.getTasks(game.getId()).observe(getViewLifecycleOwner(), this::displayTasks);
            tasksModel.refresh(game.getId());
        }
    }

    private void updateExistingGame() {
        Log.d(TAG, "Updating existing game");
        MainActivity.requestHandler.patchGame(game, response -> {
            Toast.makeText(getContext(), "Zapisano grę", Toast.LENGTH_SHORT).show();
        }, error->{
            Toast.makeText(getContext(), "Błąd zapisywania gry", Toast.LENGTH_SHORT).show();
        });
    }

    private void postNewGame() {
        Log.d(TAG, "Posting new game");
        MainActivity.requestHandler.postGame(game, response -> {
            try {
                Game postedGame = Game.fromJson(response);
                game = postedGame;
                GamesModel gameModel = new ViewModelProvider(requireActivity()).get(GamesModel.class);
                gameModel.setActiveGame(game);
                observeTasks();

            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(getContext(), "Błąd zapisywania gry!", Toast.LENGTH_SHORT).show();
                return;
            }
            Toast.makeText(getContext(), "Zapisano grę", Toast.LENGTH_SHORT).show();
        }, error -> {
            Toast.makeText(getContext(), "Błąd zapisywania gry!", Toast.LENGTH_SHORT).show();
        });
    }

    private boolean updateGameFromInput(Game game) {
        String title = binding.titleText.getText().toString();
        if(title.isEmpty()) {
            Toast.makeText(getContext(), "Podaj tytuł gry!", Toast.LENGTH_SHORT).show();
            return true;
        }
        game.setName(title);

        String description = binding.descriptionText.getText().toString();
        game.setDescription(description);


        // autostart
        if(binding.gameStartTime.isChecked()) {
            try {
                Date startTime = dateFormat.parse(binding.gameStartTextView.getText().toString());
                game.setStartTime(startTime);
            } catch (Exception e) {
                Toast.makeText(getContext(), "Nieprawidłowy format daty rozpoczęcia!", Toast.LENGTH_SHORT).show();
                return true;
            }
        }

        // autostop
        if(binding.endGamePointsRadioButton.isChecked()){
            String temp = binding.editTextEndCondition.getText().toString();
            if (!temp.isEmpty()) {
                try {
                    int endScore = Integer.parseInt(temp);
                    game.setEndScore(endScore);
                    game.setEndCondition(Game.EndCondition.SCORE);
                } catch(NumberFormatException e) {
                    Toast.makeText(getContext(), "Nieprawidłowa liczba punktów!", Toast.LENGTH_SHORT).show();
                    return true;
                }
            }
            else {
                Toast.makeText(getContext(), "Nie podałeś liczby punktów!", Toast.LENGTH_SHORT).show();
                return true;
            }

        }
        else if (binding.endGameTimeRadioButton.isChecked()){
            try {
                Date endTime = dateFormat.parse(binding.editTextEndCondition.getText().toString());
                game.setEndTime(endTime);
                game.setEndCondition(Game.EndCondition.TIME);
            } catch(Exception e) {
                Toast.makeText(getContext(), "Nieprawidłowy format daty zakończenia!", Toast.LENGTH_SHORT).show();
                return true;
            }
        }
        return false;
    }

    private void loadGameOrCreateNew(Game game) {
        if (game == null) {
            UserModel userModel = new ViewModelProvider(requireActivity()).get(UserModel.class);
            String gmUsername = userModel.getUsername();

            this.game = new Game(Entity.UNKNOWN_ID, "", gmUsername, Game.GameState.CREATED);
            this.game.setEndCondition(Game.EndCondition.TASKS);
        } else {
            this.game = game;

            binding.titleText.setText(game.getName());
            binding.descriptionText.setText(game.getDescription());

            if (game.getStartTime() != null) {
                binding.gameStartTextView.setText(dateFormat.format(game.getStartTime()));
                binding.gameStartTime.setChecked(true);
            } else {
                binding.gameStartTime.setChecked(false);
            }

            // TODO: connect end game
            switch (game.getEndCondition()) {
                case TIME:
                    break;
                case SCORE:
                    break;
                case MANUAL:
                case TASKS:
                    break;
            }
        }
    }

    public void displayTasks(List<Task> tasks){
        TasksModel model = new ViewModelProvider(requireActivity()).get(TasksModel.class);
        NavController navController = NavHostFragment.findNavController(this);

        binding.tasksList.setAdapter(new TasksAdapter(tasks, navController, model));
    }
}
