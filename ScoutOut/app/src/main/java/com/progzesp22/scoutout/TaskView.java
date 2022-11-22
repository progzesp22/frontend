package com.progzesp22.scoutout;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.progzesp22.scoutout.domain.Task;

public class TaskView extends LinearLayout {
    public TaskView(Context context) {
        this(context, (Task) null);
    }


    public TaskView(Context context, Task task) {
        super(context);

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.task_view, this, true);

        if (task == null) return;

        TextView text = (TextView) getChildAt(0);
        text.setText(task.getName());
    }

    public void setOnClick(OnClickListener listener) {
        Button button = (Button) getChildAt(1);
        button.setOnClickListener(listener);
    }
}