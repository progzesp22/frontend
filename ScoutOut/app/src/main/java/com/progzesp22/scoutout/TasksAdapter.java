package com.progzesp22.scoutout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.RecyclerView;

import com.progzesp22.scoutout.domain.Task;
import com.progzesp22.scoutout.domain.TasksModel;

import java.util.List;

public class TasksAdapter extends RecyclerView.Adapter<TasksAdapter.ViewHolder> {

    private final List<Task> localDataSet;
    private final TasksModel model;
    private final NavController navController;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView taskName;
        private final TextView comment;
        private final Button button;


        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View

            taskName = (TextView) view.findViewById(R.id.taskName);
            comment = (TextView) view.findViewById(R.id.status);
            button = (Button) view.findViewById(R.id.gameButton);
        }

        public TextView getNameTextView() {
            return taskName;
        }
        public TextView getCommentTextView() {
            return comment;
        }
        public Button getButton(){return button;}
    }

    public TasksAdapter(List<Task> dataSet, NavController navController, TasksModel model) {
        localDataSet = dataSet;
        this.navController = navController;
        this.model = model;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.task_row, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        Task task = localDataSet.get(position);
        viewHolder.getNameTextView().setText(task.getName());

        TextView comment = viewHolder.getCommentTextView();
        TextView button = viewHolder.getButton();

        comment.setText("");
        button.setText("edit");
        button.setOnClickListener(view -> navController.navigate(R.id.action_add_edit_task));
    }

    @Override
    public int getItemCount() {
        return localDataSet.size();
    }
}