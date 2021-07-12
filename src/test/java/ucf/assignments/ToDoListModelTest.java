/*
 *  UCF COP3330 Summer 2021 Assignment 4 Solution
 *  Copyright 2021 Mikhail Plekunov
 */

package ucf.assignments;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ToDoListModelTest {
    private TreeSet<ListModel> test_set = new TreeSet<>();
    private Database mockDatabase;
    private ListModel prince = new ListModel("Prince");

    @BeforeEach
    void SetUp() {
        mockDatabase = mock(Database.class);
        when(mockDatabase.getLists()).thenReturn(test_set);

        test_set.add(prince);
        test_set.add(new ListModel("Henry"));
        test_set.add(new ListModel("Did"));
        test_set.add(new ListModel("Nothing"));
        test_set.add(new ListModel("Wrong"));
    }

    @Test
    @DisplayName("get list by String")
    void getList() {
        ToDoListModel listModel = new ToDoListModel(mockDatabase);

        assertEquals(listModel.getList("Prince"), prince);
    }

    @Test
    @DisplayName("get list by Object")
    void testGetList() {
        ToDoListModel listModel = new ToDoListModel(mockDatabase);

        assertEquals(prince, listModel.getList(prince));
    }

    @Test
    @DisplayName("find list by String")
    void findList() {
        ToDoListModel listModel = new ToDoListModel(mockDatabase);

        assertEquals(prince, listModel.findList("Prince"));
    }

    @Test
    @DisplayName("add list by Object")
    void addList() {
        ToDoListModel listModel = new ToDoListModel(mockDatabase);

        ListModel test = new ListModel("Michael");
        listModel.addList(test);

        assertEquals(test, listModel.findList("Michael"));
    }

    @Test
    @DisplayName("delete list by Object")
    void deleteList() {
        ToDoListModel listModel = new ToDoListModel(mockDatabase);

        listModel.deleteList(prince);

        assertEquals(listModel.getLists().size(), test_set.size() - 1);

        Exception exception = null;
        try {
            listModel.getList(prince);
        } catch (NullPointerException e) {
            exception = e;
        }

        assertNull(exception);
    }

    @Test
    @DisplayName("delete list by String")
    void testDeleteList() {
        ToDoListModel listModel = new ToDoListModel(mockDatabase);

        listModel.deleteList("Prince");
        assertEquals(listModel.getLists().size(), test_set.size() - 1);

        Exception exception = null;
        try {
            listModel.getList(prince);
        } catch (NullPointerException e) {
            exception = e;
        }

        assertNull(exception);
    }

    @Test
    @DisplayName("get all lists from ")
    void getLists() {
        ToDoListModel listModel = new ToDoListModel(mockDatabase);

        assertEquals(test_set.toArray(), listModel.getLists().toArray());
    }
}