/*
 *  UCF COP3330 Summer 2021 Assignment 4 Solution
 *  Copyright 2021 Mikhail Plekunov
 */

package ucf.assignments;

import java.time.LocalDate;
import java.util.Objects;

public class TaskModel implements Comparable {
    private String name;
    private LocalDate dueDate;
    private String description;
    private Boolean isCompleted;
    private Integer id;

    public TaskModel(String name) {
        this.name = name;
        dueDate = null;
        description = "";
        isCompleted = false;
        id = null;
    }

    public TaskModel(String name, LocalDate dueDate, String description, Boolean isCompleted) {
        this(name);

        this.name = name;
        this.dueDate = dueDate;
        this.description = description;
        this.isCompleted = isCompleted;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCompletionState(boolean isCompleted) {
        this.isCompleted = isCompleted;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public boolean getCompletionState() {
        return isCompleted;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

    public Integer getId() {
        return id;
    }

    public void clear() {
        name = "";
        dueDate = null;
        description = null;
        isCompleted = false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TaskModel task = (TaskModel) o;
        return Objects.equals(name, task.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public int compareTo(Object o) {
        TaskModel that = (TaskModel) o;
        return name.compareTo(that.getName());
    }
}
