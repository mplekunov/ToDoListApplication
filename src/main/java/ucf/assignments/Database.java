/*
 *  UCF COP3330 Summer 2021 Assignment 4 Solution
 *  Copyright 2021 Mikhail Plekunov
 */

package ucf.assignments;

import java.sql.*;
import java.util.*;

public class Database {
    private final String connectionStringPrefix = "jdbc:sqlite:";
    private String filePath;

    public Database() {
        filePath = connectionStringPrefix + "ToDoListDB.sqlite";
    }

    public String getFilePath() {
        return filePath.substring(connectionStringPrefix.length());
    }

    public void setConnection(String connection) {
        this.filePath = connectionStringPrefix + connection;
    }

    public SortedSet<ListModel> getLists() {
        var lists = new TreeSet<ListModel>();
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(filePath);
            Statement statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery("SELECT ROWID, list_name FROM Lists");

            while (resultSet.next()) {
                int id = resultSet.getInt("ROWID");
                String name = resultSet.getString("list_name");

                lists.add(new ListModel(name, id, getTasks(name)));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }

        return lists;
    }

    public SortedSet<TaskModel> getTasks(String listName) {
        var tasks = new TreeSet<TaskModel>();
        Connection connection = null;

        try {
            connection = DriverManager.getConnection(filePath);
            Statement statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery(String.format("SELECT ROWID, task_name, task_state, task_due_date, task_note FROM Tasks WHERE list_name = '%s'", listName));

            while (resultSet.next()) {
                var task = new TaskModel(resultSet.getString("task_name"));

                task.setId(resultSet.getInt("ROWID"));
                task.setCompletionState(resultSet.getInt("task_state") == 1);
                task.setDueDate(DateFormatter.stringToDate(resultSet.getString("task_due_date")));
                task.setDescription(resultSet.getString("task_note"));

                tasks.add(task);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }

        return tasks;
    }

    public void updateList(ListModel listManager) {
        Connection connection = null;

        try {
            connection = DriverManager.getConnection(filePath);
            Statement statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery(String.format("SELECT * FROM Lists WHERE ROWID = %d", listManager.getId()));

            if (resultSet.next())
                statement.execute(String.format("UPDATE Lists SET list_name = '%s' WHERE ROWID = %d", listManager.getListName(), listManager.getId()));
            else
                statement.execute(String.format("INSERT INTO Lists VALUES ('%s')", listManager.getListName()));

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    public void deleteList(ListModel listManager) {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(filePath);
            Statement statement = connection.createStatement();

            statement.execute(String.format("DELETE FROM Lists WHERE ROWID = %d", listManager.getId()));
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }

    }

    public void updateTask(TaskModel task, String listName) {
        Connection connection = null;

        try {
            connection = DriverManager.getConnection(filePath);
            Statement statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery(String.format("SELECT * FROM Tasks WHERE ROWID = %d", task.getId()));

            if (resultSet.next()) {
                statement.execute(String.format("UPDATE Tasks SET task_name = '%s', task_state = %d, task_due_date = '%s', task_note = '%s' WHERE ROWID = %d",
                        task.getName(), task.getCompletionState() ? 1 : 0, DateFormatter.dateToString(task.getDueDate()), task.getDescription(), task.getId()));
            } else {
                statement.execute(String.format("INSERT INTO Tasks VALUES ('%s', %d, '%s', '%s', '%s')",
                        task.getName(), task.getCompletionState() ? 1 : 0, DateFormatter.dateToString(task.getDueDate()), task.getDescription(), listName));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    public void deleteTask(TaskModel task) {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(filePath);
            Statement statement = connection.createStatement();

            statement.execute(String.format("DELETE FROM Tasks WHERE ROWID = %d", task.getId()));
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }
}
