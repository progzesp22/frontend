package com.progzesp22.scoutout;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.progzesp22.scoutout.domain.Answer;
import com.progzesp22.scoutout.domain.Task;

import java.util.Base64;

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

    public void showAnswer(Answer answer, Task task) {
        TextView text = (TextView) getChildAt(0);
        text.setText(task.getType().toString() + " " + answer.getAnswer());

//        switch(task.getType()){
//            case TEXT:
//            case QR_CODE:
//
//                binding.answerText.setVisibility(View.VISIBLE);
//                binding.answerText.setText(answer.getAnswer());
//                break;
//            case PHOTO:
//                binding.image.setVisibility(View.VISIBLE);
//                byte[] decodedString = Base64.getDecoder().decode(ans.getAnswer());
//                Bitmap bmp = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
//                binding.image.setImageBitmap(bmp);
//                break;
//            case NAV_POS:
//                break;
//            case AUDIO:
//                break;
//        }
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