package com.example.myapplication;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

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
        List<Task> tasks_l = new LinkedList<>();
        tasks_l.add(new Task(0, "Szyszunie", "Zbierz 20 świeżych szyszek"));
        tasks_l.add(new Task(1, "Problem Collatza", "Rozstrzygnij problem Collatza"));

        tasks.setValue(tasks_l);
    }
}
