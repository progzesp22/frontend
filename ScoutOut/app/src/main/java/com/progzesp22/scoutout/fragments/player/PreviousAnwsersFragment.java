package com.progzesp22.scoutout.fragments.player;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.progzesp22.scoutout.AnswerView;
import com.progzesp22.scoutout.MainActivity;
import com.progzesp22.scoutout.R;
import com.progzesp22.scoutout.databinding.FragmentPreviousAnwsersBinding;
import com.progzesp22.scoutout.domain.Answer;
import com.progzesp22.scoutout.domain.Task;
import com.progzesp22.scoutout.domain.TasksModel;

import java.util.List;

public class PreviousAnwsersFragment extends Fragment {
    FragmentPreviousAnwsersBinding binding;
    Task activeTask;

    public PreviousAnwsersFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentPreviousAnwsersBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.goToUpload.setOnClickListener(view1 -> {
            NavHostFragment.findNavController(this).navigate(R.id.action_previousAnwsersFragment_to_taskAnswerFragment);
        });

        binding.goToTaskDescription.setOnClickListener(view1 -> {
            NavHostFragment.findNavController(this).navigate(R.id.action_previousAnwsersFragment_to_taskViewFragment);
        });

        TasksModel tasksModel = new ViewModelProvider(requireActivity()).get(TasksModel.class);
        activeTask = tasksModel.getActiveTask();

        if (activeTask == null) {
            NavHostFragment.findNavController(this).navigateUp();
            return;
        }

        binding.taskName.setText(activeTask.getName());

        binding.previousAnswersList.setAdapter(new AnswerListAdapter(activeTask.getAnswers()));
    }


    class AnswerListAdapter extends BaseAdapter {

        List<Answer> answers;

        AnswerListAdapter(List<Answer> answers) {
            this.answers = answers;
        }

        @Override
        public int getCount() {
            return answers.size();
        }

        @Override
        public Object getItem(int i) {
            return answers.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            if (view == null) {
                AnswerView answerView = new AnswerView(getContext());
                answerView.disableButton();
                // TODO: maybe optimize how we get answer from the backend
                MainActivity.requestHandler.getAnswer(answers.get(i).getId(), jsonObject -> {
                    answerView.setAnswer(jsonObject.optString("response"));

            }, error -> {
                answerView.setAnswer("Błąd pobierania odpowiedzi");
                });
                view = answerView;
            }

            return view;
        }
    }
}