/*
 *  UCF COP3330 Summer 2021 Assignment 4 Solution
 *  Copyright 2021 Mikhail Plekunov
 */

package ucf.assignments;

import java.util.*;
import java.util.stream.Collectors;

public class ToDoList {
    private final Map<ListManager, DataState> lists;

    public ToDoList(Database db) {
        lists = new TreeMap<>();

        db.getLists().forEach(list -> lists.put(list, DataState.Uncached));
    }

    public ListManager findList(String listName) throws NullPointerException {
        return lists.keySet().stream()
                .filter(key -> key.getListName().equals(listName))
                .findFirst().orElseThrow(null);
    }

    public void addList(String listName) throws NullPointerException {
        boolean isFound = lists.keySet().stream()
                .anyMatch(key -> key.getListName().equals(listName));

        if (isFound) {
            ListManager listManager = findList(listName);

            if (lists.get(listManager).equals(DataState.Removed)) {
                listManager.setId(null);
                lists.replace(listManager, DataState.Uncached);
            } else
                throw new NullPointerException();
        } else
            lists.put(new ListManager(listName), DataState.Uncached);
    }

    public void changeListName(String oldName, String newName) throws NullPointerException {
        ListManager mappedList = findList(oldName);

        mappedList.setListName(newName);

        lists.replace(mappedList, DataState.Uncached);
    }

    public void deleteList(String listName) throws NullPointerException {
        boolean isFound = lists.keySet().stream()
                .anyMatch(key -> key.getListName().equals(listName));

        if (isFound) {
            ListManager mappedList = findList(listName);

            lists.put(mappedList, DataState.Removed);
        } else
            throw new NullPointerException();
    }

    public void upload(Database db) {
        lists.forEach((key, value) -> {
            if (value == DataState.Removed)
                db.deleteList(key);
            else {
                db.updateList(key);
                key.upload(db);
            }
        });

        lists.values().removeIf(dataState -> dataState.equals(DataState.Removed));
        lists.entrySet().forEach(entry -> entry.setValue(DataState.Uncached));
    }

    public List<ListManager> getLists() {
        return lists.entrySet().stream()
                .filter(list -> !list.getValue().equals(DataState.Removed))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }
}
