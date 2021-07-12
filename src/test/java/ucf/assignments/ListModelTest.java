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

class ListModelTest {
    private TreeSet<TaskModel> test_set = new TreeSet<>();
    private TaskModel prince = new TaskModel("Prince");

    @BeforeEach
    void SetUp() {
        test_set.add(prince);
        test_set.add(new TaskModel("Henry"));
        test_set.add(new TaskModel("Did"));
        test_set.add(new TaskModel("Nothing"));
        test_set.add(new TaskModel("Wrong"));
    }

    @Test
    @DisplayName("getTask by String")
    void getTask() {
        ListModel listModel = new ListModel("test_case", null, test_set);

        TaskModel taskModel = listModel.getTask("Henry");

        assertTrue(test_set.contains(taskModel));
    }

    @Test
    @DisplayName("getTask by Object")
    void testGetTask() {
        ListModel listModel = new ListModel("test_case", null, test_set);

        TaskModel taskModel = listModel.getTask(prince);

        assertTrue(test_set.contains(taskModel));
    }

    @Test
    @DisplayName("find task by String")
    void findTask() {
        ListModel listModel = new ListModel("test_case", null, test_set);

        TaskModel taskModel = listModel.findTask("Prince");

        assertTrue(test_set.contains(taskModel));
    }

    @Test
    @DisplayName("add task to ListModel")
    void addTask() {
        ListModel listModel = new ListModel("test_case");

        listModel.addTask(prince);

        assertEquals(prince, listModel.getTask(prince));
    }

    @Test
    @DisplayName("delete Task by String")
    void deleteTask() {
        ListModel listModel = new ListModel("test_case", null, test_set);

        listModel.deleteTask("Prince");

        assertEquals(test_set.size() - 1, listModel.getAllTasks().size());
    }

    @Test
    @DisplayName("delete Task by Object")
    void testDeleteTask() {
        ListModel listModel = new ListModel("test_case", null, test_set);

        listModel.deleteTask(prince);

        assertEquals(test_set.size() -1, listModel.getAllTasks().size());
    }

    @Test
    @DisplayName("delete all tasks")
    void deleteAllTasks() {
        ListModel listModel = new ListModel("test_case", null, test_set);

        listModel.deleteAllTasks();

        assertTrue(listModel.getAllTasks().isEmpty());
    }

    @Test
    @DisplayName("returns collection of all tasks")
    void getAllTasks() {
        ListModel listModel = new ListModel("test_case", null, test_set);

        assertArrayEquals(test_set.toArray(), listModel.getAllTasks().toArray());
    }

    @Test
    @DisplayName("returns collection of completed tasks")
    void getCompletedTasks() {
        ListModel listModel = new ListModel("tes_case", null, test_set);

        listModel.getTask(prince).setCompletionState(true);

        assertTrue(listModel.getCompletedTasks().contains(prince));
    }

    @Test
    @DisplayName("returns collection of in progress task (completion state = false)")
    void getInProgressTasks() {
        ListModel listModel = new ListModel("test_case", null, test_set);

        assertTrue(listModel.getInProgressTasks().contains(prince));
    }
}