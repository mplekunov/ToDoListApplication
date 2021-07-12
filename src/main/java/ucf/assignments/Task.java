/*
 *  UCF COP3330 Summer 2021 Assignment 4 Solution
 *  Copyright 2021 Mikhail Plekunov
 */

package ucf.assignments;

import java.time.LocalDate;
import java.util.Objects;

public class Task implements Comparable {
    private String name;
    private LocalDate dueDate;
    private String description;
    private boolean isCompleted;
    private Integer id;

    public Task(String name) {
        this.name = name;
        dueDate = null;
        description = "";
        isCompleted = false;
        id = null;
    }

    public Task(String name, LocalDate dueDate, String description) {
        this(name);

        this.name = name;
        this.dueDate = dueDate;
        this.description = description;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(name, task.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public int compareTo(Object o) {
        Task that = (Task) o;
        return name.compareTo(that.getName());
    }
}
