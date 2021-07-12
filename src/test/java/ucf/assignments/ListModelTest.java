package ucf.assignments;

import javafx.concurrent.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;
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
    }

    @Test
    void getAllTasks() {
    }

    @Test
    void getCompletedTasks() {
    }

    @Test
    void getInProgressTasks() {
    }
}