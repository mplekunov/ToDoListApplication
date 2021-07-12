/*
 *  UCF COP3330 Summer 2021 Assignment 4 Solution
 *  Copyright 2021 Mikhail Plekunov
 */

package ucf.assignments;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Side;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.io.IOException;

import static ucf.assignments.ToDoListController.*;

public class ListController {
    private final ToDoListController toDoListController;
    private final ListManager listManager;
    private TaskPropertiesController taskProperties;

    private AnchorPane taskPropertiesView;

    @FXML
    TextField centerListNameField;
    @FXML
    AnchorPane centerNewTaskPane;
    @FXML
    TextField centerProgressBarField;
    @FXML
    ProgressBar centerTaskProgressBar;
    @FXML
    ScrollPane centerTaskScrollPane;
    @FXML
    Button centerNewTaskBtn;
    @FXML
    VBox taskScrollPaneVBox;
    @FXML
    MenuBar centerMenuBar;

    @FXML
    MenuItem menuShowAll;
    @FXML
    MenuItem menuShowCompleted;
    @FXML
    MenuItem menuShowInProgress;
    @FXML
    MenuItem menuDeleteList;
    @FXML
    MenuItem menuDeleteAllTasks;

    public ListController(ListManager listManager, ToDoListController toDoListController) {
        this.listManager = listManager;
        this.toDoListController = toDoListController;
    }

    @FXML
    public void initialize() {
        centerListNameField.setText(listManager.getListName());
        listManager.getAllTasks().forEach(task -> createTaskBtn(task.getName()));
        setTaskProgressBar();
    }

    protected void updateTaskScrollPane() {
        taskScrollPaneVBox.getChildren().removeAll(taskScrollPaneVBox.getChildren());
        listManager.getAllTasks().forEach(task -> createTaskBtn(task.getName()));
    }

    //DOESN'T WORK
    @FXML
    public void listNameFieldClicked(MouseEvent event) {
//        if (event.getClickCount() == 2) {
//            centerListNameField.editableProperty().set(true);
//
//            Button btn = (Button) toDoListController.leftScrollPaneVBox
//                    .lookupAll("#leftListBtn").stream()
//                    .filter(node -> ((Button)node).getText().equals(centerListNameField.getText()))
//                    .findFirst().get();
//
//            ContextMenu contextMenu = toDoListController.initTextFieldValidator(centerListNameField);
//
//            Pane parent = (Pane)centerListNameField.getParent();
//
//            centerListNameField.focusedProperty().addListener((observable, unfocused, focused) -> {
//                if (unfocused) {
//                    if (!centerListNameField.getText().isEmpty()) {
//                        boolean isMatch = toDoListController.getToDoList().getLists().stream().anyMatch(o -> {
//                            if (o.getListName().equals(centerListNameField.getText()))
//                                return !centerListNameField.getText().equals(btn.getText());
//
//                            return false;
//                        });
//
//                        if (isMatch) {
//                            parent.setStyle("-fx-border-color: red");
//                            contextMenu.getItems().clear();
//                            contextMenu.getItems().add(new MenuItem("List with that name already exists!"));
//                            contextMenu.show(parent, Side.RIGHT, -parent.getWidth(), -parent.getHeight());
//
//                            centerListNameField.requestFocus();
//                        }
//                        else {
//                            btn.setText(centerListNameField.getText());
//                            centerListNameField.editableProperty().set(false);
//                            listManager.setListName(centerListNameField.getText());
//                        }
//                    }
//                }
//            });
//        }
    }

    @FXML
    public void menuShowAllClicked(ActionEvent event) {
        taskScrollPaneVBox.getChildren().removeAll(taskScrollPaneVBox.getChildren());
        listManager.getAllTasks().forEach(task -> createTaskBtn(task.getName()));
    }

    @FXML
    public void menuShowCompletedClicked(ActionEvent event) {
        taskScrollPaneVBox.getChildren().removeAll(taskScrollPaneVBox.getChildren());
        listManager.getCompletedTasks().forEach(task -> createTaskBtn(task.getName()));
    }

    @FXML
    public void menuShowInProgressClicked(ActionEvent event) {
        taskScrollPaneVBox.getChildren().removeAll(taskScrollPaneVBox.getChildren());
        listManager.getInProgressTasks().forEach(task -> createTaskBtn(task.getName()));
    }

    @FXML
    public void menuDeleteAllTasksClicked(ActionEvent event) {
        taskScrollPaneVBox.getChildren().removeAll(taskScrollPaneVBox.getChildren());
        listManager.getAllTasks().forEach(task -> listManager.deleteTask(task.getName()));
    }

    @FXML
    public void menuDeleteListClicked(ActionEvent event) {
        toDoListController.getToDoList().deleteList(listManager.getListName());
        toDoListController.setCenterPropertyToDefault();
        toDoListController.updateListScrollPane();
    }

    @FXML
    public void centerTaskBtnClicked(MouseEvent event, String taskName) {
        btnStyle(event, "-fx-padding: 0 0 0 0 50; -fx-font-size: 11");

        if (event.getEventType().equals(MouseEvent.MOUSE_PRESSED)) {
            if (toDoListController.mainPane.getRight() == null || taskPropertiesView == null || !taskProperties.rightTaskNameField.getText().equals(taskName)) {
                createTaskPropertiesView(listManager.findTask(taskName));
                toDoListController.mainPane.setRight(taskPropertiesView);
            }

            if (taskPropertiesView.isVisible())
                hideNode(taskPropertiesView);
            else
                showNode(taskPropertiesView);

        }
    }

    private void createTaskPropertiesView(Task task) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("taskPropertiesView.fxml"));
        taskProperties = new TaskPropertiesController(task, this);
        fxmlLoader.setControllerFactory(taskPropertiesController -> taskProperties);
        try {
            taskPropertiesView = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void centerTaskRadioBtnClicked(MouseEvent event, String taskName) {
        btnStyle(event, "-fx-background-size: 20");
        RadioButton radioButton = (RadioButton) event.getSource();

        if (event.getEventType().equals(MouseEvent.MOUSE_RELEASED)) {
            listManager.changeTaskCompletionState(taskName, radioButton.isSelected());

            taskPaneStyle(radioButton);

            setTaskProgressBar();
        }
    }

    @FXML
    public void centerNewTaskBtnClicked(MouseEvent event) {
        btnStyle(event, "-fx-padding: 0 0 0 20; -fx-font-size: 14");
        Button eventBtn = (Button) event.getSource();

        if (event.getEventType().equals(MouseEvent.MOUSE_RELEASED)) {
            if (taskPropertiesView != null)
                hideNode(taskPropertiesView);

            hideNode(eventBtn);
            TextField textField = new TextField();
            setAnchorProperty(textField, 0d, 0d, 0d, 0d);
            centerNewTaskPane.getChildren().add(textField);

            ContextMenu errorPopup = initTextFieldValidator(textField);

            textField.focusedProperty().addListener((observable, unfocused, focused) -> {
                if (unfocused) {
                    if (!textField.getText().isEmpty()) {
                        try {
                            listManager.addTask(textField.getText());

                            createTaskBtn(textField.getText());

                            updateTaskScrollPane();

                            centerNewTaskPane.getChildren().remove(textField);
                            showNode(eventBtn);
                        } catch (NullPointerException e) {
                            textField.getParent().setStyle("-fx-border-color: red");
                            errorPopup.getItems().clear();
                            errorPopup.getItems().add(new MenuItem("List with that name already exists!"));
                            errorPopup.show(textField, Side.RIGHT, -textField.getWidth(), -textField.getHeight());

                            textField.requestFocus();
                        }
                    } else {
                        toDoListController.mainPane.requestFocus();

                        centerNewTaskPane.getChildren().remove(textField);
                        showNode(eventBtn);
                    }
                }
            });
        }
    }

    private void setTaskProgressBar() {
        if (listManager.getAllTasks().size() != 0) {
            centerTaskProgressBar.setProgress(listManager.getCompletedTasks().size() / (double) listManager.getAllTasks().size());
            centerProgressBarField.setText(String.format("%.0f", centerTaskProgressBar.getProgress() * 100) + "%");
        } else
            centerProgressBarField.setText("0%");
    }

    protected void createTaskBtn(String taskName) {
        Button btn = new Button(taskName);
        btn.setId("centerTaskBtn");
        setAnchorProperty(btn, 30d, 0d, 0d, 0d);

        btn.setOnMousePressed(event -> centerTaskBtnClicked(event, taskName));
        btn.setOnMouseReleased(event -> centerTaskBtnClicked(event, taskName));

        RadioButton radioBtn = new RadioButton();
        radioBtn.setSelected(listManager.findTask(taskName).getCompletionState());
        radioBtn.setId("centerTaskRadioBtn");
        setAnchorProperty(radioBtn, 0d, null, 0d, 0d);

        radioBtn.setOnMousePressed(event -> centerTaskRadioBtnClicked(event, taskName));
        radioBtn.setOnMouseReleased(event -> centerTaskRadioBtnClicked(event, taskName));

        AnchorPane anchorPane = new AnchorPane(btn, radioBtn);
        anchorPane.setId("centerTaskPane");

        taskPaneStyle(radioBtn);
        centerTaskProgressBar.fireEvent(new ActionEvent());
        taskScrollPaneVBox.getChildren().add(anchorPane);
    }

    private void taskPaneStyle(RadioButton radioButton) {
        if (radioButton.isSelected())
            radioButton.getParent().setStyle("-fx-background-color: grey; -fx-border-color: grey;");
        else
            radioButton.getParent().setStyle("");
    }

    public ListManager getListManager() {
        return listManager;
    }
}
