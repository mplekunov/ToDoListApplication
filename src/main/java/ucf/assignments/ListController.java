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
    private final String listName;

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

    public ListController(String listName, ToDoListController toDoListController) {
        this.listName = listName;
        this.toDoListController = toDoListController;
    }

    @FXML
    public void initialize() {
        ListModel listModel = toDoListController.getToDoListModel().findList(listName);

        centerListNameField.setText(listModel.getListName());
        listModel.getAllTasks().forEach(this::createTaskBtn);
        setTaskProgressBar();
    }

    protected void updateTaskScrollPane() {
        ListModel listModel = toDoListController.getToDoListModel().findList(listName);
        if (taskScrollPaneVBox != null)
            taskScrollPaneVBox.getChildren().removeAll(taskScrollPaneVBox.getChildren());
        listModel.getAllTasks().forEach(this::createTaskBtn);
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
        ListModel listModel = toDoListController.getToDoListModel().findList(listName);

        taskScrollPaneVBox.getChildren().removeAll(taskScrollPaneVBox.getChildren());
        listModel.getAllTasks().forEach(this::createTaskBtn);
    }

    @FXML
    public void menuShowCompletedClicked(ActionEvent event) {
        ListModel listModel = toDoListController.getToDoListModel().findList(listName);

        taskScrollPaneVBox.getChildren().removeAll(taskScrollPaneVBox.getChildren());
        listModel.getCompletedTasks().forEach(this::createTaskBtn);
    }

    @FXML
    public void menuShowInProgressClicked(ActionEvent event) {
        ListModel listModel = toDoListController.getToDoListModel().findList(listName);

        taskScrollPaneVBox.getChildren().removeAll(taskScrollPaneVBox.getChildren());
        listModel.getInProgressTasks().forEach(this::createTaskBtn);
    }

    @FXML
    public void menuDeleteAllTasksClicked(ActionEvent event) {
        ListModel listModel = toDoListController.getToDoListModel().getList(listName);

        taskScrollPaneVBox.getChildren().removeAll(taskScrollPaneVBox.getChildren());
        listModel.getAllTasks().forEach(listModel::deleteTask);
    }

    @FXML
    public void menuDeleteListClicked(ActionEvent event) {
        toDoListController.getToDoListModel().deleteList(listName);
        toDoListController.setCenterPropertyToDefault();
        toDoListController.updateListScrollPane();
    }

    @FXML
    public void centerTaskBtnClicked(MouseEvent event, String taskName) {
        btnStyle(event, "-fx-padding: 0 0 0 0 50; -fx-font-size: 11");

        if (event.getEventType().equals(MouseEvent.MOUSE_PRESSED)) {
            ListModel listModel = toDoListController.getToDoListModel().findList(listName);

            if (toDoListController.mainPane.getRight() == null || taskPropertiesView == null || !taskProperties.rightTaskNameField.getText().equals(taskName)) {
                createTaskPropertiesView(listModel.findTask(taskName).getName());
                toDoListController.mainPane.setRight(taskPropertiesView);
            }

            if (taskPropertiesView.isVisible())
                hideNode(taskPropertiesView);
            else
                showNode(taskPropertiesView);

        }
    }

    private void createTaskPropertiesView(String taskName) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("taskPropertiesView.fxml"));
        taskProperties = new TaskPropertiesController(taskName, this);
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
            ListModel listModel = toDoListController.getToDoListModel().getList(listName);
            listModel.getTask(taskName).setCompletionState(radioButton.isSelected());

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
                            ListModel listModel = toDoListController.getToDoListModel().getList(listName);
                            TaskModel taskModel = new TaskModel(textField.getText());
                            listModel.addTask(taskModel);

                            createTaskBtn(taskModel);

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
        ListModel listModel = toDoListController.getToDoListModel().findList(listName);

        if (listModel.getAllTasks().size() != 0) {
            centerTaskProgressBar.setProgress(listModel.getCompletedTasks().size() / (double) listModel.getAllTasks().size());
            centerProgressBarField.setText(String.format("%.0f", centerTaskProgressBar.getProgress() * 100) + "%");
        } else
            centerProgressBarField.setText("0%");
    }

    protected void createTaskBtn(TaskModel task) {
        Button btn = new Button(task.getName());
        btn.setId("centerTaskBtn");
        setAnchorProperty(btn, 30d, 0d, 0d, 0d);

        btn.setOnMousePressed(event -> centerTaskBtnClicked(event, task.getName()));
        btn.setOnMouseReleased(event -> centerTaskBtnClicked(event, task.getName()));

        RadioButton radioBtn = new RadioButton();
        radioBtn.setSelected(task.getCompletionState());
        radioBtn.setId("centerTaskRadioBtn");
        setAnchorProperty(radioBtn, 0d, null, 0d, 0d);

        radioBtn.setOnMousePressed(event -> centerTaskRadioBtnClicked(event, task.getName()));
        radioBtn.setOnMouseReleased(event -> centerTaskRadioBtnClicked(event, task.getName()));

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

    public ListModel findListModel() {
        return toDoListController.getToDoListModel().findList(listName);
    }

    public ListModel getListModel() {
        return toDoListController.getToDoListModel().getList(listName);
    }
}
