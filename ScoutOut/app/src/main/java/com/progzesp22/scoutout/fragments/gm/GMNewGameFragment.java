package com.progzesp22.scoutout.fragments.gm;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;

import com.progzesp22.scoutout.R;
import com.progzesp22.scoutout.SelectDateTimeFragment;
import com.progzesp22.scoutout.databinding.FragmentGmNewGameBinding;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

public class GMNewGameFragment extends Fragment {

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
        final EditText edit = (EditText) getView().findViewById(R.id.editTextEndCondition);

        binding.endGameRadio.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton pointsButton = (RadioButton) group.findViewById(R.id.endGamePointsRadioButton);
            RadioButton timeButton = (RadioButton) group.findViewById(R.id.endGameTimeRadioButton);
            if (pointsButton.isChecked()) {
                edit.setVisibility(View.VISIBLE);
                edit.setText("Wprowadź liczbę");
                edit.setEnabled(true);
            } else if (timeButton.isChecked()) {
                edit.setVisibility(View.VISIBLE);
                DialogFragment newFragment = new SelectDateTimeFragment();
                newFragment.show(getFragmentManager(), "DatePicker");
                edit.setEnabled(false);
            } else {
                edit.setVisibility(View.INVISIBLE);
            }
        });
    }
}
