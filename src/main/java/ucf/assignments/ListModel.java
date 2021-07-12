/*
 *  UCF COP3330 Summer 2021 Assignment 4 Solution
 *  Copyright 2021 Mikhail Plekunov
 */

package ucf.assignments;

import java.util.*;
import java.util.stream.Collectors;

public class ListModel implements Comparable {
    private String listName;
    private Integer id;
    private final Map<TaskModel, DataState> tasks;

    public ListModel(String listName) {

        this.listName = listName;
        tasks = new HashMap<>();
        id = null;
    }

    public ListModel(String listName, Integer id, SortedSet<TaskModel> cachedTasks) {
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

    public TaskModel getTask(String taskName) {
        TaskModel mapped = tasks.entrySet().stream()
                .filter(entry -> !entry.getValue().equals(DataState.Removed))
                .filter(entry -> entry.getKey().getName().equals(taskName))
                .map(Map.Entry::getKey)
                .findFirst().orElseThrow(null);

        tasks.replace(mapped, DataState.Uncached);

        return mapped;
    }

    public TaskModel getTask(TaskModel task) {
        TaskModel mapped = tasks.keySet().stream()
                .filter(key -> key.equals(task))
                .findFirst().orElseThrow(null);

        tasks.replace(mapped, DataState.Uncached);

        return mapped;
    }

    public TaskModel findTask(String taskName) {
        TaskModel mapped = tasks.entrySet().stream()
                .filter(entry -> !entry.getValue().equals(DataState.Removed))
                .filter(entry -> entry.getKey().getName().equals(taskName))
                .map(Map.Entry::getKey)
                .findFirst().orElseThrow(null);

        return new TaskModel(mapped.getName(), mapped.getDueDate(), mapped.getDescription(), mapped.getCompletionState());
    }

    public void addTask(TaskModel task) {
        boolean isFound = tasks.keySet().stream()
                .anyMatch(key -> key.equals(task));

        if (isFound) {
            TaskModel mapped = tasks.keySet().stream()
                    .filter(key -> key.equals(task))
                    .findFirst().orElseGet(null);

            if (tasks.get(mapped).equals(DataState.Removed)) {
                mapped.setName(task.getName());
                mapped.setDescription(task.getDescription());
                mapped.setDueDate(task.getDueDate());
                mapped.setCompletionState(task.getCompletionState());

                tasks.replace(mapped, DataState.Uncached);
            }
            else
                throw new NullPointerException();
        }
        else
            tasks.put(task, DataState.Uncached);
    }

    public void deleteTask(String taskName) {
        TaskModel mapped = tasks.keySet().stream()
                .filter(key -> key.getName().equals(taskName))
                .findFirst().orElseThrow(null);

        tasks.replace(mapped, DataState.Removed);
        mapped.clear();
    }

    public void deleteTask(TaskModel task) {
        TaskModel mapped = tasks.keySet().stream()
                .filter(key -> key.equals(task))
                .findFirst().orElseThrow(null);

        tasks.replace(mapped, DataState.Removed);
        mapped.clear();
    }

    public void deleteAllTasks() {
        tasks.entrySet().stream()
                .filter(entry -> !entry.getValue().equals(DataState.Removed))
                .forEach(entry -> {
                    entry.setValue(DataState.Removed);
                    entry.getKey().clear();
                });
    }

    public void upload(Database db) {
        tasks.entrySet().stream()
                .filter(entry -> !entry.getValue().equals(DataState.Cached))
                .forEach(entry -> {
                    if (entry.getValue() == DataState.Removed)
                        db.deleteTask(entry.getKey());
                    else
                        db.updateTask(entry.getKey(), listName);
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

    public TreeSet<TaskModel> getAllTasks() {
        return tasks.entrySet().stream()
                .filter(entry -> !entry.getValue().equals(DataState.Removed))
                .map(Map.Entry::getKey)
                .sorted(Comparator.comparing(TaskModel::getCompletionState).reversed()
                    .thenComparing(TaskModel::getName))
                .collect(Collectors.toCollection(TreeSet::new));
    }

    public TreeSet<TaskModel> getCompletedTasks() {
        return tasks.entrySet().stream()
                .filter(entry -> !entry.getValue().equals(DataState.Removed))
                .filter(entry -> entry.getKey().getCompletionState())
                .map(Map.Entry::getKey)
                .sorted(Comparator.comparing(TaskModel::getName)
                    .thenComparing(TaskModel::getDueDate))
                .collect(Collectors.toCollection(TreeSet::new));
    }

    public TreeSet<TaskModel> getInProgressTasks() {
        return tasks.entrySet().stream()
                .filter(entry -> !entry.getValue().equals(DataState.Removed))
                .filter(entry -> !entry.getKey().getCompletionState())
                .map(Map.Entry::getKey)
                .sorted(Comparator.comparing(TaskModel::getName)
                    .thenComparing(TaskModel::getDueDate))
                .collect(Collectors.toCollection(TreeSet::new));
    }

    public void clear() {
        listName = "";
        deleteAllTasks();
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ListModel that = (ListModel) o;
        return Objects.equals(listName, that.listName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public int compareTo(Object o) {
        ListModel that = (ListModel) o;
        return listName.compareTo(that.getListName());
    }
}
