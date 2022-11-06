package com.example.myapplication;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import com.example.myapplication.databinding.FragmentSelectUserTypeBinding;
import android.widget.RadioButton;

import com.example.myapplication.databinding.FragmentSelectUserTypeBinding;

public class SelectUserTypeFragment extends Fragment {
    private FragmentSelectUserTypeBinding binding;


    public SelectUserTypeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSelectUserTypeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.usertypeRadio.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton checkedRadioButton = group.findViewById(checkedId);
            boolean gm = checkedRadioButton == binding.gmRadioButton;

            if(gm){
                binding.gmModeRadio.setVisibility(View.VISIBLE);
            } else{
                binding.gmModeRadio.setVisibility(View.INVISIBLE);
            }
        });

        binding.startButton.setOnClickListener(view1 -> {
            UserModel model = new ViewModelProvider(requireActivity()).get(UserModel.class);
            updateModel(model);

            if(model.getUserType() == UserModel.UserType.PLAYER){
                NavHostFragment.findNavController(this).navigate(R.id.action_playerSelected);
            }
        });
    }

    private void updateModel(UserModel model){
        model.setUsername(binding.editTextTextPersonName.getText().toString());

        int checkedRadioButtonId = binding.usertypeRadio.getCheckedRadioButtonId();
        if(checkedRadioButtonId == R.id.playerRadioButton){
            model.setUserType(UserModel.UserType.PLAYER);
        } else if(checkedRadioButtonId == R.id.gmRadioButton){
            model.setUserType(UserModel.UserType.GM);
        }

        checkedRadioButtonId = binding.gmModeRadio.getCheckedRadioButtonId();
        if(checkedRadioButtonId == R.id.gmAcceptRadioButton){
            model.setMasterMode(UserModel.MasterMode.ACCEPT);
        } else if(checkedRadioButtonId == R.id.gmEditRadioButton){
            model.setMasterMode(UserModel.MasterMode.EDIT);
        }
    }
}