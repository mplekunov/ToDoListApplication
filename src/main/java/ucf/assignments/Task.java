/*
 *  UCF COP3330 Summer 2021 Assignment 4 Solution
 *  Copyright 2021 Mikhail Plekunov
 */

package ucf.assignments;

import java.time.LocalDate;
import java.util.Objects;

public class Task {
    private String name;
    private LocalDate dueDate;
    private String description;
    private int state;
    public Task() {
        this.name = "";
        this.dueDate = null;
        this.description = "";
        this.state = 0;
    }

    public Task(String name, LocalDate dueDate, String description) {
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

    public void setState(int state) {
        this.state = state;
    }

    public int getState() {
        return state;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return name.equals(task.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, state);
    }
}
