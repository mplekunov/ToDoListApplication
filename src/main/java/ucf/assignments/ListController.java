/*
 *  UCF COP3330 Summer 2021 Assignment 4 Solution
 *  Copyright 2021 Mikhail Plekunov
 */

package ucf.assignments;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

public class ListController {
    private final ToDoListController toDoListController;
    private final String listName;

    @FXML TextField centerListNameField;

    @FXML TextField centerProgressBarField;
    @FXML ProgressBar centerTaskProgressBar;

    @FXML ScrollPane centerTaskScrollPane;

    @FXML Button centerNewTaskBtn;

    @FXML MenuBar centerMenuBar;
    @FXML MenuItem menuShowAll;
    @FXML MenuItem menuShowCompleted;
    @FXML MenuItem menuShowInProgress;
    @FXML MenuItem menuDeleteList;

    public ListController(ToDoListController toDoListController) {
        listName = "";
        this.toDoListController = toDoListController;
    }

    public ListController(String listName, ToDoListController toDoListController) {
        this.listName = listName;
        this.toDoListController = toDoListController;
    }

    public void initialize() {
        if (!listName.isEmpty())
            centerListNameField.setText(listName);
    }

    @FXML
    public void listNameFieldClicked(MouseEvent event) {
        if (event.getClickCount() == 2) {
            centerListNameField.editableProperty().set(true);
            //get current list
            //let user change list name
            //save new list name
            //change editable property to false when user presses enter or "loses focus"
        }
    }

    @FXML
    public void menuShowAllClicked(ActionEvent event) {
        //get sorted collection of items in the current list
        //display all items
        //this method is called by default when user opens list
    }

    @FXML
    public void menuShowCompletedClicked(ActionEvent event) {
        //get sorted collection of items in the current list
        //display only items that are completed
    }

    @FXML
    public void menuShowInProgressClicked(ActionEvent event) {
        //get sorted collection of items in the current list
        //display only items that are in progress
    }

    @FXML
    public void menuDeleteListClicked(ActionEvent event) {
        //get current list shown on the screen from collection of lists
        //deletes this list from collection
        //sets center property of the mainPane to "Today" Screen
    }


    @FXML
    public void centerTaskBtnClicked(MouseEvent event) {
        toDoListController.btnStyle(event, "-fx-padding: 0 0 0 0 50; -fx-font-size: 11");
        //get task from the current list
        //if task's name isn't the same as the one specified in the appropriate variable in the right Pane
        //set every required field from task to the appropriate variables in right Pane
        toDoListController.rightBackBtnClicked(event); //show right pane
        //else ignore
    }

    @FXML
    public void centerTaskRadioBtnClicked(MouseEvent event) {
        toDoListController.btnStyle(event, "-fx-background-size: 20");
        //get task from the current list
        //read its state (completed or in progress)
        //change it's state
        //check the "current sorting" order
        //if user chose show only in progress, remove button from the ScrollPane
        //if user chose show only completed, update sorted collection
        //if user chose show all, update sorted collection
        //apply effect on the button
        //move button to the end
    }

    @FXML
    public void centerNewTaskBtnClicked(MouseEvent event) {
        toDoListController.btnStyle(event, "-fx-padding: 0 0 0 20; -fx-font-size: 14");
        //get current list shown on the screen from collection of lists
        //show new window which will provide user with an ability to enter task name, due date and etc.
        //add all this information into the new item of the current list
        //update current list
        //display current list again (sorted), now including the newly created task
    }
}
