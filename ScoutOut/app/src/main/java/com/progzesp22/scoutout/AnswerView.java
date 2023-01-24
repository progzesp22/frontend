package com.progzesp22.scoutout;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.progzesp22.scoutout.domain.Answer;
import com.progzesp22.scoutout.domain.Task;

import java.util.Base64;

public class AnswerView extends LinearLayout {
    final private TextView text;
    final private ImageView imageView;
    final private Button button;

    public AnswerView(Context context) {
        this(context, (String) null);
    }


    public AnswerView(Context context, String taskTitle) {
        super(context);

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.answer_view, this, true);

        text = (TextView) getChildAt(0);
        imageView = (ImageView) getChildAt(1);
        button = (Button) getChildAt(2);
        imageView.setVisibility(View.GONE);


        if (taskTitle == null) return;
        text.setText(taskTitle);
    }

    public void showAnswer(Answer answer, Task task) {


        imageView.setVisibility(View.GONE);

        switch(task.getType()){
            case TEXT:
            case QR_CODE:
                text.setText(answer.getAnswer());
                break;

            case PHOTO:
            case NAV_POS:
            case AUDIO:
                text.setText(task.getType().toString());
                break;

        }
    }

    public void disableButton() {
        button.setVisibility(INVISIBLE);
    }

    public void setOnClick(OnClickListener listener) {
        button.setOnClickListener(listener);
    }
}