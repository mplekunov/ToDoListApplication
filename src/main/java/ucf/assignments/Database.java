/*
 *  UCF COP3330 Summer 2021 Assignment 4 Solution
 *  Copyright 2021 Mikhail Plekunov
 */

package ucf.assignments;

import java.nio.channels.NotYetConnectedException;
import java.sql.*;
import java.util.*;

public class Database {
    private Statement statement;

    public Database() {}

    public void connect() {
        if (statement != null) {
            Connection connection;

            try {
                connection = DriverManager.getConnection("jdbc:sqlite:ToDoListDB.sqlite");
                statement = connection.createStatement();
                statement.executeQuery("PRAGMA foreign_keys = on");
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    public void disconnect() {
        try {
            statement.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public boolean findList(String name) throws NoSuchElementException, NotYetConnectedException {
        isConnected();

        ResultSet rs = null;
        try {
            rs = statement.executeQuery(String.format("SELECT name FROM ToDoLists WHERE ToDoLists.name = %s", name));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return rs != null;
    }

    public List<String> getAllLists() throws NoSuchElementException, NotYetConnectedException {
        isConnected();

        var allLists = new LinkedList<String>();

        try {
            ResultSet rs = statement.executeQuery("SELECT name FROM ToDoLists");

            while (rs.next())
                allLists.add(rs.getString("name"));

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return allLists;
    }

    public List<Task> getList(String name) throws NoSuchElementException, NotYetConnectedException {
        isConnected();

        var tasks = new LinkedList<Task>();

        try {
            ResultSet rs = statement.executeQuery(String.format("SELECT Tasks.name, Tasks.state, Tasks.due_date, Tasks.note FROM Tasks WHERE Tasks.list_name = '%s'", name));

            while (rs.next()) {
                Task task = new Task();

                task.setName(rs.getString("name"));
                task.setState(rs.getInt("state"));
                task.setDueDate(rs.getDate("due_date)").toLocalDate());
                task.setDescription(rs.getString("note"));

                tasks.add(task);
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return tasks;
    }

    public ResultSet setList(String name) throws NotYetConnectedException {
        isConnected();

        ResultSet rs = null;
        try {
            rs = statement.executeQuery(String.format("INSERT INTO ToDoLists VALUES (%s)", name));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return rs;
    }

    public ResultSet setTask(String listName, Task task) throws NotYetConnectedException {
        isConnected();

        ResultSet rs = null;
        try {
            rs = statement.executeQuery(String.format("INSERT INTO Tasks VALUES (%s, %d, %s %s %s)",
                    task.getName(), task.getState(), task.getDueDate(), task.getDescription(), listName));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return rs;
    }

    public void removeTask(String name) throws NotYetConnectedException {
        isConnected();

        try {
            statement.executeQuery(String.format("DELETE FROM Tasks WHERE Tasks.name = %S", name));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void removeList(String name) throws NotYetConnectedException {
        isConnected();

        try {
            statement.executeQuery(String.format("DELETE FROM ToDoLists WHERE ToDoLists.name = %s", name));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void updateTask(Task task) throws NotYetConnectedException {
        isConnected();

        try {
            statement.executeQuery(String.format("UPDATE Tasks SET Tasks.name = %s, Tasks.state = %d, Tasks.due_date = %s, Tasks.note = %s",
                    task.getName(), task.getState(), task.getDueDate(), task.getDescription()));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void updateList(String name) throws NotYetConnectedException {
        isConnected();

        try {
            statement.executeQuery(String.format("UPDATE ToDoLists SET ToDoLists.name = %s", name));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private void isConnected() throws NotYetConnectedException {
        if (statement == null)
            throw new NotYetConnectedException();
    }
}
