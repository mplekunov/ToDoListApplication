/*
 *  UCF COP3330 Summer 2021 Assignment 4 Solution
 *  Copyright 2021 Mikhail Plekunov
 */

package ucf.assignments;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ListStructure {
    private final String cached = "Cached";
    private final String updated = "Updated";
    private final String removed = "Removed";
    private final String created = "Created";

    private final Map<String, Map<Task, String>> listCollection;
    private static final Database db = new Database();

    public ListStructure() {
        db.connect();

        this.listCollection = new HashMap<>();

        db.getAllLists().forEach(entry -> {
            Map<Task, String> taskCollection = db.getList(entry).stream()
                    .collect(Collectors.toMap(Function.identity(), task -> cached));

            listCollection.put(entry, taskCollection);
        });

        db.disconnect();
    }

    public void pushTask(String listName, Task task) throws NoSuchElementException, NullPointerException{
        if (task != null) {
            if (!listCollection.containsKey(listName))
                listCollection.get(listName).put(task, updated);
            else
                throw new NoSuchElementException();
        }

        throw new NullPointerException();
    }
    
    public boolean popTask(String listName, Task task) {
        if (task != null)
            listCollection.get(listName).replace(task, removed);

        throw new NullPointerException();
    }

    public Task findTask(String listName, String taskName) {
        return listCollection.get(listName).keySet().stream().filter(task -> task.getName().equals(taskName)).findFirst().orElseThrow(null);
    }

    public List<Task> getTaskCollection(String listName) {
        return listCollection.get(listName).entrySet().stream()
                .filter(entry -> !entry.getValue().equals(removed))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    public void deleteList(String listName) {
        db.connect();

        db.removeList(listName);

        db.disconnect();
    }

    public void update(String listName) {
        db.connect();

        if (db.findList(listName)) {
            listCollection.get(listName).entrySet().stream()
                    .filter(entry -> !entry.getValue().equals(cached))
                    .forEach(entry -> {
                       if (entry.getValue().equals(removed))
                           db.removeTask(entry.getKey().getName());
                       else if (entry.getValue().equals(updated))
                           db.updateTask(entry.getKey());
                    });
        }

        db.disconnect();
    }
}
