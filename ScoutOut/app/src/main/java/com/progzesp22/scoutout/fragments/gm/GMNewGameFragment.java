package com.progzesp22.scoutout.fragments.gm;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.progzesp22.scoutout.R;
import com.progzesp22.scoutout.SelectDateTimeFragment;
import com.progzesp22.scoutout.databinding.FragmentGmNewGameBinding;

import java.sql.Timestamp;

public class GMNewGameFragment extends Fragment {
    Timestamp startTime;
    Timestamp endTime;
    long endScore;
    String gameTitle = "";
    String gameDescription;

    FragmentGmNewGameBinding binding;

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
        final EditText gameEndTimeEditText = requireView().findViewById(R.id.editTextEndCondition);
        final TextView gameStartTextView = requireView().findViewById((R.id.gameStartTextView));
        final RadioButton pointsButton = requireView().findViewById(R.id.endGamePointsRadioButton);
        final RadioButton timeButton = requireView().findViewById(R.id.endGameTimeRadioButton);
        final CheckBox autoStartButton = requireView().findViewById(R.id.gameStartTime);
        final EditText gameTitleEditText = requireView().findViewById(R.id.titleText);
        final EditText gameDescriptionEditText = requireView().findViewById(R.id.descriptionText);

        binding.gameStartTime.setOnCheckedChangeListener((group, checkedId) -> {
            if (autoStartButton.isChecked()) {
                DialogFragment newFragment = new SelectDateTimeFragment(gameStartTextView);
                assert getFragmentManager() != null;
                newFragment.show(getFragmentManager(), "DatePicker");
            }
        });

        binding.endGameRadio.setOnCheckedChangeListener((group, checkedId) -> {
            if (pointsButton.isChecked()) {
                gameEndTimeEditText.setVisibility(View.VISIBLE);
                gameEndTimeEditText.getText().clear();
                gameEndTimeEditText.setHint("Wprowadź liczbę");
                gameEndTimeEditText.setEnabled(true);
            } else if (timeButton.isChecked()) {
                gameEndTimeEditText.setVisibility(View.VISIBLE);
                gameEndTimeEditText.getText().clear();
                gameEndTimeEditText.setHint("");
                DialogFragment newFragment = new SelectDateTimeFragment(gameEndTimeEditText);
                assert getFragmentManager() != null;
                newFragment.show(getFragmentManager(), "DatePicker");
                gameEndTimeEditText.setEnabled(false);
            } else {
                gameEndTimeEditText.setVisibility(View.INVISIBLE);
                gameEndTimeEditText.getText().clear();
                gameEndTimeEditText.setHint("");
                gameEndTimeEditText.setEnabled(false);
            }
        });

        binding.addTask.setOnClickListener(view1 -> {
                NavHostFragment.findNavController(this).navigate(R.id.action_addTask);
        });

        binding.saveGameButton.setOnClickListener((group)-> {

            gameTitle = gameTitleEditText.getText().toString();
            gameDescription = gameDescriptionEditText.getText().toString();
            if(autoStartButton.isChecked()) {
                try {
                    startTime = Timestamp.valueOf(gameStartTextView.getText().toString());
                } catch (Exception e) {
                    Toast.makeText(getContext(), "Nieprawidłowy format daty rozpoczęcia!", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            else {
                startTime = new Timestamp(System.currentTimeMillis());
            }


            if(gameTitle.isEmpty()) {
                Toast.makeText(getContext(), "Podaj tytuł gry!", Toast.LENGTH_SHORT).show();
            }
            else {
                if(pointsButton.isChecked()){
                    String temp = gameEndTimeEditText.getText().toString();
                    if (!temp.isEmpty()) {
                        try {
                            endScore = Integer.parseInt(temp);
                        } catch(NumberFormatException e) {
                            Toast.makeText(getContext(), "Nieprawidłowa liczba punktów!", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                    else {
                        Toast.makeText(getContext(), "Nie podałeś liczby punktów!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                else if (timeButton.isChecked()){
                    try {
                        endTime = Timestamp.valueOf(gameEndTimeEditText.getText().toString());
                    } catch(Exception e) {
                        Toast.makeText(getContext(), "Nieprawidłowy format daty zakończenia!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                Toast.makeText(getContext(), "Poprawnie wypełniony formularz.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
