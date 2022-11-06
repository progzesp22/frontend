package com.example.myapplication;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.json.JSONException;

import java.util.LinkedList;
import java.util.List;

public class TasksModel extends ViewModel {
    private MutableLiveData<List<Task>> tasks;
    private Task activeTask;

    public LiveData<List<Task>> getTasks(){
        if(tasks == null){
            tasks = new MutableLiveData<>();
            fetchTasks();
        }

        return tasks;
    }

    public void refreshTasks(){
        if(tasks == null){
            tasks = new MutableLiveData<>();
        }

        fetchTasks();
    }

    public void setActiveTask(Task task){
        activeTask = task;
    }

    public Task getActiveTask(){
        return activeTask;
    }

    private void fetchTasks() {
        MainActivity.requestHandler.getTasks(response -> {
            List<Task> tasks = new LinkedList<>();
            try {
                for (int i = 0; i < response.length(); i++) {
                    tasks.add(Task.fromJson(response.getJSONObject(i)));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            this.tasks.postValue(tasks);
        }, error -> {
            Log.e("TasksModel", "Error fetching tasks: " + error.toString());
        });
    }
}
