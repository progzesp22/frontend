package com.progzesp22.scoutout.fragments.gm;

import android.os.Bundle;
import android.os.CountDownTimer;
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
import com.progzesp22.scoutout.databinding.FragmentGmGameBinding;
import com.progzesp22.scoutout.domain.Game;
import com.progzesp22.scoutout.domain.GamesModel;
import com.progzesp22.scoutout.domain.Task;
import com.progzesp22.scoutout.domain.TasksModel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class GMGameFragment extends Fragment {
    private FragmentGmGameBinding binding;
    private CountDownTimer timer;

    public GMGameFragment() {
        // Required empty public constructor
    }

    public static GMGameFragment newInstance() {
        return new GMGameFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentGmGameBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        GamesModel gamesModel = new ViewModelProvider(requireActivity()).get(GamesModel.class);
        TasksModel tasksModel = new ViewModelProvider(requireActivity()).get(TasksModel.class);


        gamesModel.getGames().observe(getViewLifecycleOwner(), games -> {
            if (games.size() > 0) {
                gamesModel.setActiveGame(games.get(0));
            }

            Game activeGame = gamesModel.getActiveGame();

            if (activeGame != null) {
                binding.gmGameName.setText(activeGame.getName());

                if (activeGame.getEndCondition() == Game.EndCondition.TIME) {
                    binding.gmGameTimerGroup.setVisibility(View.VISIBLE);

                    Date endTime = activeGame.getEndTime();
                    Date startTime = activeGame.getStartTime();

                    long timeWhole = endTime.getTime() - startTime.getTime();
                    long timePassed = new Date().getTime() - startTime.getTime();

                    DateFormat df = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());

                    binding.gmGameTimerValue.setText(df.format(endTime));
                    binding.gmGameTimerCountdownProgressBar.setProgress((int) (100 * ((double)timePassed / (double) timeWhole)));

                    if (timer != null) {
                        timer.cancel();
                    }

                    timer = new CountDownTimer(timeWhole - timePassed, 1000) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                            long timePassed = new Date().getTime() - startTime.getTime();
                            binding.gmGameTimerCountdownProgressBar.setProgress((int) (100 * ((double)timePassed / (double) timeWhole)));
                        }

                        @Override
                        public void onFinish() {
                            // tutaj można by teoretycznie zrobić jakiś komunikat o końcu gry
                            binding.gmGameTimerCountdownProgressBar.setProgress(100);
                        }
                    };

                    timer.start();
                }

                tasksModel.getTasks(activeGame.getId()).observe(getViewLifecycleOwner(), tasks -> {
                    int count = 0;
                    int finished = 0;
                    for (Task task : tasks) {
                        if (task.getGameId() == activeGame.getId()) {
                            count++;
                            if (task.isFinished()) {
                                finished++;
                            }
                        }
                    }

                    binding.gmGameTasksLeftValue.setText(String.format(Locale.getDefault(), "%d/%d", finished, count));

                    binding.gmGameTasksProgressBar.setMax(count);
                    binding.gmGameTasksProgressBar.setProgress(finished);

                    int unchecedAnswersCount = tasksModel.getUncheckedAnswers().size();

                    binding.gmGameAnswersValue.setText(String.format(Locale.getDefault(), "%d", unchecedAnswersCount));
                });


            } else {
                binding.gmGameName.setText("No active game");
            }
        });

        binding.gmGameEndButton.setOnClickListener(v -> {
            // TODO: zakończenie gry
            Toast.makeText(getContext(), "End game", Toast.LENGTH_SHORT).show();
        });

        binding.gmGameAnswersButton.setOnClickListener(v -> {
            NavHostFragment.findNavController(this).navigate(R.id.action_gm_game_fragment_to_GMListToAcceptFragment);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(timer != null) {
            timer.cancel();
        }
        timer = null;
    }
}