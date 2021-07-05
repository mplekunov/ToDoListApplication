/*
 *  UCF COP3330 Summer 2021 Assignment 4 Solution
 *  Copyright 2021 Mikhail Plekunov
 */

package ucf.assignments;

import java.util.ArrayList;

public class List {
    private java.util.List<Task> taskCollection;

    public List() {
        taskCollection = new ArrayList<>();
    }

    public List(java.util.List<Task> taskCollection) {
        this.taskCollection = taskCollection;
    }

    public void push(Task task) {
        //checks for the task name in the collection
        //if there is no task name found adds name
        throw new UnsupportedOperationException();

    }
    
    public void pop(Task task) {
        //removes task from the collection
        throw new UnsupportedOperationException();

    }

    public Task find(Task task) {
        //looks for the specific task in the task collection
        //if there is a similar task returns it
        //otherwise throws runtime
        throw new UnsupportedOperationException();
    }

    public Task find(String name) {
        //looks for the specific task in the task collection by comparing their names
        //if it finds one, returns it.
        //otherwise throws runtime
        throw new UnsupportedOperationException();
    }

    public java.util.List<Task> getTaskCollection() {
        return taskCollection;
    }

    public void setTaskCollection(java.util.List<Task> taskCollection) {
        this.taskCollection = taskCollection;
    }
}
