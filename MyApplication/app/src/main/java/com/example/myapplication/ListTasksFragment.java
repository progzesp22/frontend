package com.example.myapplication;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.myapplication.databinding.FragmentListTasksBinding;
import com.example.myapplication.databinding.FragmentSelectUserTypeBinding;

import java.util.LinkedList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ListTasksFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListTasksFragment extends Fragment {
    FragmentListTasksBinding binding;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ListTasksFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ListTasksFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ListTasksFragment newInstance(String param1, String param2) {
        ListTasksFragment fragment = new ListTasksFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentListTasksBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        List<Task> tasks = new LinkedList<>();
        tasks.add(new Task(0, "Szyszunie", "Zbierz 20 świeżych szyszek"));
        tasks.add(new Task(1, "Problem Collatza", "Rozstrzygnij problem Collatza"));

        displayTasks(tasks);
    }

    private void displayTasks(List<Task> tasks){
        binding.tasksLayout.removeAllViews();

        for(Task task : tasks){
//            Button test = new Button(this.getContext());
            TextView test = new TextView(getContext());
            test.setText(task.getName());
            binding.tasksLayout.addView(test);
        }
    }
}