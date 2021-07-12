/*
 *  UCF COP3330 Summer 2021 Assignment 4 Solution
 *  Copyright 2021 Mikhail Plekunov
 */

package ucf.assignments;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class ListManager implements Comparable {
    private String listName;
    private Integer id;
    private final Map<Task, DataState> tasks;

    public ListManager(String listName) {
        this.listName = listName;
        tasks = new TreeMap<>();
        id = null;
    }

    public ListManager(String listName, int id, Set<Task> cachedTasks) {
        this(listName);
        this.id = id;
        cachedTasks.forEach(task -> tasks.put(task, DataState.Cached));
    }

    public void setListName(String listName) {
        this.listName = listName;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Task findTask(String taskName) throws NullPointerException {
        return tasks.keySet().stream()
                .filter(key -> key.getName().equals(taskName))
                .findFirst().orElseThrow(null);
    }

    public void addTask(String taskName) throws NullPointerException {
        boolean isFound = tasks.keySet().stream()
                .anyMatch(key -> key.getName().equals(taskName));

        if (isFound) {
            Task task = findTask(taskName);

            if (tasks.get(task).equals(DataState.Removed)) {
                task.setDescription(null);
                task.setDueDate(null);
                task.setCompletionState(false);
                task.setId(null);

                tasks.replace(task, DataState.Uncached);
            } else
                throw new NullPointerException();
        } else
            tasks.put(new Task(taskName), DataState.Uncached);
    }

    public void changeTaskName(String oldName, String newName) throws NullPointerException {
        Task mappedTask = findTask(oldName);
        mappedTask.setName(newName);

        tasks.replace(mappedTask, DataState.Uncached);
    }

    public void changeTaskCompletionState(String taskName, boolean state) throws NullPointerException {
        Task mappedTask = findTask(taskName);
        mappedTask.setCompletionState(state);

        tasks.replace(mappedTask, DataState.Uncached);
    }

    public void changeTaskDueDate(String taskName, LocalDate date) throws NullPointerException {
        Task mappedTask = findTask(taskName);
        mappedTask.setDueDate(date);

        tasks.replace(mappedTask, DataState.Uncached);
    }

    public void changeTaskNote(String taskName, String note) throws NullPointerException {
        Task mappedTask = findTask(taskName);
        mappedTask.setDescription(note);

        tasks.replace(mappedTask, DataState.Uncached);
    }

    public void deleteTask(String taskName) throws NullPointerException {
        boolean isFound = tasks.keySet().stream()
                .anyMatch(key -> key.getName().equals(taskName));

        if (isFound) {
            Task mappedTask = findTask(taskName);

            tasks.replace(mappedTask, DataState.Removed);
        } else
            throw new NullPointerException();
    }

    public void upload(Database db) {
        tasks.entrySet().stream()
                .filter(task -> !task.getValue().equals(DataState.Cached))
                .forEach(task -> {
                    if (task.getValue().equals(DataState.Uncached))
                        db.updateTask(task.getKey(), listName);
                    else
                        db.deleteTask(task.getKey());
                });

        tasks.values().removeIf(dataState -> dataState.equals(DataState.Removed));
        tasks.entrySet().forEach(entry -> entry.setValue(DataState.Cached));
    }

    public Integer getId() {
        return id;
    }

    public String getListName() {
        return listName;
    }

    public List<Task> getAllTasks() {
        return tasks.entrySet().stream()
                .filter(task -> !task.getValue().equals(DataState.Removed))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    public List<Task> getCompletedTasks() {
        return tasks.keySet().stream()
                .filter(Task::getCompletionState)
                .collect(Collectors.toList());
    }

    public List<Task> getInProgressTasks() {
        return tasks.keySet().stream()
                .filter(task -> !task.getCompletionState())
                .collect(Collectors.toList());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ListManager that = (ListManager) o;
        return Objects.equals(listName, that.listName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public int compareTo(Object o) {
        ListManager that = (ListManager) o;
        return listName.compareTo(that.getListName());
    }
}
