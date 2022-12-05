package com.progzesp22.scoutout.domain;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import com.progzesp22.scoutout.domain.Task;
import com.progzesp22.scoutout.MainActivity;

public class TasksModel extends ViewModel {
    private MutableLiveData<List<Task>> tasks;
    private Task activeTask;
    private Answer activeAnswer;

    private static final String TAG = "TasksModel";


    public LiveData<List<Task>> getTasks() {
        if (tasks == null) {
            tasks = new MutableLiveData<>();
            fetch();
        }

        return tasks;
    }

    public void refresh() {
        if (tasks == null) {
            tasks = new MutableLiveData<>();
        }

        fetch();
    }


    public void setActiveTask(Task task) {
        activeTask = task;
    }

    public Task getActiveTask() {
        return activeTask;
    }

    public Task getById(long id) {
        if (tasks == null || tasks.getValue() == null) {
            return null;
        }

        for (Task task : tasks.getValue()) {
            if (task.getId() == id) {
                return task;
            }
        }

        return null;
    }

    private void fetch() {
        MainActivity.requestHandler.getTasks(response -> {
            List<Task> currentTasks = tasks.getValue();
            if (currentTasks == null) {
                currentTasks = new ArrayList<>();
            }

            try {
                for (int i = 0; i < response.length(); i++) {
                    Task parsedTask = Task.fromJson(response.getJSONObject(i));

                    boolean found = false;
                    for (Task task : currentTasks) {
                        if (task.getId() == parsedTask.getId()) {
                            found = true;
                            task.setName(parsedTask.getName());
                            task.setDescription(parsedTask.getDescription());
                            task.setType(parsedTask.getType());
                        }
                    }

                    if (!found) {
                        currentTasks.add(parsedTask);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            // postValue executes in main thread, setValue executes in this thread
            // I am not sure which one to use here
//            tasks.postValue(currentTasks);
            tasks.setValue(currentTasks);


            fetchAnswers(); // fetch answers after we fetch new tasks

        }, error -> {
            Log.e("TasksModel", "Error fetching tasks: " + error.toString());
        });
    }

    private void fetchAnswers() {
        MainActivity.requestHandler.getAnswers(false, response -> {
            List<Task> currentTasks = tasks.getValue();
            if (currentTasks == null) {
                currentTasks = new ArrayList<>(); // this should never happen but just in case
                Log.e(TAG, "Fetching answers while tasks list doesn't exist");
            }

            try {
                for (int i = 0; i < response.length(); i++) {
                    Answer parsedAnswer = Answer.fromJson(response.getJSONObject(i));

                    boolean taskFound = false;
                    for (Task task : currentTasks) {
                        if (task.getId() == parsedAnswer.getTaskId()) {
                            taskFound = true;
                            List<Answer> answers = task.getAnswers();
                            boolean answerFound = false;
                            for (Answer answer : answers) {
                                if (answer.getId() == parsedAnswer.getId()) {
                                    answerFound = true;
                                    answer.setApproved(parsedAnswer.isApproved());
                                    answer.setChecked(parsedAnswer.isChecked());
                                    break;
                                }
                            }

                            if (!answerFound) {
                                answers.add(parsedAnswer);
                            }
                        }
                    }

                    if (!taskFound) {
                        Log.e("TasksModel", "Task not found for answer: " + parsedAnswer.getId());
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

//            tasks.postValue(currentTasks);
            tasks.setValue(currentTasks);

        }, error -> {
            Log.e("TasksModel", "Error fetching answers: " + error.toString());
        });
    }

    public void setActiveAnswer(Answer answer) {
        activeAnswer = answer;
    }

    public Answer getActiveAnswer() {
        return activeAnswer;
    }

    public List<Answer> getUncheckedAnswers() {
        List<Answer> uncheckedAnswers = new ArrayList<>();

        if (tasks == null || tasks.getValue() == null) {
            return uncheckedAnswers;
        }

        for (Task task : tasks.getValue()) {
            for (Answer answer : task.getAnswers()) {
                if (!answer.isChecked()) {
                    uncheckedAnswers.add(answer);
                }
            }
        }

        return uncheckedAnswers;
    }

    public List<Answer> getAnswers() {
        List<Answer> answers = new ArrayList<>();

        if (tasks == null || tasks.getValue() == null) {
            return answers;
        }

        for (Task task : tasks.getValue()) {
            answers.addAll(task.getAnswers());
        }

        return answers;
    }
}
