/*
 *  UCF COP3330 Summer 2021 Assignment 4 Solution
 *  Copyright 2021 Mikhail Plekunov
 */

package ucf.assignments;

import java.util.*;
import java.util.stream.Collectors;

public class ToDoListModel {
    private final Map<ListModel, DataState> lists;

    public ToDoListModel(Database db) {
        lists = new HashMap<>();

        db.getLists().forEach(list -> lists.put(list, DataState.Cached));
    }

    public ListModel getList(String listName) throws NullPointerException {
        ListModel mapped = lists.entrySet().stream()
                .filter(entry -> !entry.getValue().equals(DataState.Removed))
                .filter(entry -> entry.getKey().getListName().equals(listName))
                .map(Map.Entry::getKey)
                .findFirst().orElseThrow(null);

        lists.replace(mapped, DataState.Uncached);

        return mapped;
    }

    public ListModel getList(ListModel listManager) throws NullPointerException {
        ListModel mapped = lists.keySet().stream()
                .filter(key -> key.equals(listManager))
                .findFirst().orElseThrow(null);

        lists.replace(mapped, DataState.Uncached);

        return mapped;
    }

    public ListModel findList(String listName) throws NullPointerException {
        ListModel mapped = lists.entrySet().stream()
                .filter(entry -> !entry.getValue().equals(DataState.Removed))
                .filter(entry -> entry.getKey().getListName().equals(listName))
                .map(Map.Entry::getKey)
                .findFirst().orElseThrow(null);

        return new ListModel(mapped.getListName(), mapped.getId(), new TreeSet<>(mapped.getAllTasks()));
    }

    public void addLst(ListModel listManager) throws NullPointerException {
        boolean isFound = lists.keySet().stream()
                .anyMatch(key -> key.equals(listManager));

        if (isFound) {
            ListModel mapped = lists.keySet().stream()
                    .filter(key -> key.equals(listManager))
                    .findFirst().get();

            if (lists.get(mapped).equals(DataState.Removed)) {
                mapped.setListName(listManager.getListName());

                lists.replace(mapped, DataState.Uncached);
            } else throw new NullPointerException();
        } else
            lists.put(listManager, DataState.Uncached);
    }

    public void deleteList(String listName) throws NullPointerException {
        ListModel mapped = lists.keySet().stream()
                .filter(key -> key.getListName().equals(listName))
                .findFirst().orElseThrow(null);

        lists.replace(mapped, DataState.Removed);
        mapped.deleteAllTasks();
        mapped.clear();
    }

    public void deleteList(ListModel listManager) throws NullPointerException {
        ListModel mapped = lists.keySet().stream()
                .filter(key -> key.equals(listManager))
                .findFirst().orElseThrow(null);

        lists.replace(mapped, DataState.Removed);
        mapped.clear();
    }

    public void upload(Database db) {
        lists.entrySet().stream()
                .filter(entry -> !entry.getValue().equals(DataState.Cached))
                .forEach(entry -> {
                    if (entry.getValue() == DataState.Removed)
                        db.deleteList(entry.getKey());
                    else {
                        db.updateList(entry.getKey());
                        entry.getKey().upload(db);
                    }
                });

        lists.values().removeIf(dataState -> dataState.equals(DataState.Removed));
        lists.entrySet().forEach(entry -> entry.setValue(DataState.Cached));
    }

    public TreeSet<ListModel> getLists() {
        return lists.entrySet().stream()
                .filter(entry -> !entry.getValue().equals(DataState.Removed))
                .map(Map.Entry::getKey)
                .sorted(Comparator.comparing(ListModel::getListName))
                .collect(Collectors.toCollection(TreeSet::new));
    }
}
