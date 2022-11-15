package com.example.myapplication;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.json.JSONException;

import java.util.LinkedList;
import java.util.List;

public class AnswerModel extends ViewModel {
    private MutableLiveData<List<Answer>> uncheckedAnswers;
    private Answer activeAnswer;

    public LiveData<List<Answer>> getUncheckedAnswers() {
        if (uncheckedAnswers == null) {
            uncheckedAnswers = new MutableLiveData<>();
            fetch();
        }

        return uncheckedAnswers;
    }

    public void refreshAnswers() {
        if (uncheckedAnswers == null) {
            uncheckedAnswers = new MutableLiveData<>();
        }

        fetch();
    }

    public void setActiveAnswer(Answer answer) {
        activeAnswer = answer;
    }

    public Answer getActiveAnswer() {
        return activeAnswer;
    }

    private void fetch() {
        MainActivity.requestHandler.getAnswers(true, response -> {
            List<Answer> answers = new LinkedList<>();
            try {
                for (int i = 0; i < response.length(); i++) {
                    Answer ans = Answer.fromJson(response.getJSONObject(i));
                    if (ans != null) {
                        answers.add(ans);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            this.uncheckedAnswers.postValue(answers);
        }, error -> {
            Log.e("AnswersModel", "Error fetching tasks: " + error.toString());
        });
    }
}
