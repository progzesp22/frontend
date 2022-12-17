package com.progzesp22.scoutout;


import android.content.Context;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class AnswerView extends LinearLayout {
    public AnswerView(Context context) {
        this(context, (String) null);
    }


    public AnswerView(Context context, String taskTitle) {
        super(context);

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.task_view, this, true);

        if (taskTitle == null) return;

        TextView text = (TextView) getChildAt(0);
        text.setText(taskTitle);
    }

    public void setAnswer(String answer) {
        TextView text = (TextView) getChildAt(0);
        text.setText(answer);
    }

    public void disableButton() {
        Button button = (Button) getChildAt(1);
        button.setVisibility(INVISIBLE);
    }

    public void setOnClick(OnClickListener listener) {
        Button button = (Button) getChildAt(1);
        button.setOnClickListener(listener);
    }
}