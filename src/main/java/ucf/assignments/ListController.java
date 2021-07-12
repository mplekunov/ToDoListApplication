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
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.HashMap;

import static ucf.assignments.ToDoListController.*;

public class ListController {
    public final ToDoListController toDoListController;
    private HashMap<String, TaskPropertiesController> taskPropertiesViews;

    private final String listName;
    private AnchorPane listView;


    @FXML
    TextField centerListNameField;
    @FXML
    AnchorPane centerListNameFieldPane;
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
        taskPropertiesViews = new HashMap<>();
    }

    @FXML
    public void initialize() {
        ListModel listModel = toDoListController.getToDoListModel().findList(listName);

        centerListNameField.setText(listModel.getListName());
        listModel.getAllTasks().forEach(this::createTaskButton);
        setTaskProgressBar();
    }

    protected void reloadTaskScrollPane() {
        ListModel listModel = toDoListController.getToDoListModel().findList(listName);

        if (taskScrollPaneVBox != null) {
            taskScrollPaneVBox.getChildren().removeAll(taskScrollPaneVBox.getChildren());
            taskPropertiesViews.clear();
        }

        listModel.getAllTasks().forEach(this::createTaskButton);
    }

    //DOESN'T WORK
    @FXML
    public void setClickOnListNameField(MouseEvent event) {
        TextField textField = (TextField) event.getSource();

        centerListNameFieldPane.setStyle("-fx-background-color: rgb(229, 220, 222);");

        if (event.getEventType().equals(MouseEvent.MOUSE_RELEASED)) {
            ContextMenu errorPopup = initTextFieldValidator(textField, "-fx-background-color: rgb(229, 220, 222); -fx-border-color: rgb(229, 220, 222);");

            centerListNameField.addEventFilter(KeyEvent.KEY_PRESSED, keyEvent -> {
                if (textField.getText().length() >= 20) {
                    event.consume();
                }
            });

            centerListNameField.focusedProperty().addListener((observable, unfocused, focused) -> {
                if (unfocused) {
                    if (!textField.getText().isEmpty() && !textField.getText().equals(listName)) {
                        try {
                            toDoListController.getToDoListModel().findList(textField.getText());

                            centerListNameFieldPane.setStyle("-fx-border-color: red; -fx-background-color: rgb(229, 220, 222);");

                            errorPopup.getItems().clear();
                            errorPopup.getItems().add(new MenuItem("List with such name already exists."));
                            errorPopup.show(centerListNameFieldPane, Side.RIGHT,
                                    -(centerListNameFieldPane.getWidth()) + 5, -(centerListNameFieldPane.getHeight() + 5));

                            textField.requestFocus();
                        } catch (NullPointerException e) {
                            toDoListController.getToDoListModel().getList(listName).setListName(textField.getText());
                            toDoListController.reloadListScrollPane();

                            toDoListController.mainPane.centerProperty()
                                    .set(toDoListController.getListView(textField.getText()));
                        }
                    } else {
                        centerListNameFieldPane.setStyle("");
                        textField.setText(listName);
                    }
                }
            });
        }
    }

    @FXML
    public void setClickOnMenuShowAll(ActionEvent event) {
        ListModel listModel = toDoListController.getToDoListModel().findList(listName);

        taskScrollPaneVBox.getChildren().removeAll(taskScrollPaneVBox.getChildren());
        listModel.getAllTasks().forEach(this::createTaskButton);
    }

    @FXML
    public void setClickOnMenuShowCompleted(ActionEvent event) {
        ListModel listModel = toDoListController.getToDoListModel().findList(listName);

        taskScrollPaneVBox.getChildren().removeAll(taskScrollPaneVBox.getChildren());
        listModel.getCompletedTasks().forEach(this::createTaskButton);
    }

    @FXML
    public void setClickOnMenuShowInProgress(ActionEvent event) {
        ListModel listModel = toDoListController.getToDoListModel().findList(listName);

        taskScrollPaneVBox.getChildren().removeAll(taskScrollPaneVBox.getChildren());
        listModel.getInProgressTasks().forEach(this::createTaskButton);
    }

    @FXML
    public void setClickOnMenuDeleteAllTasks(ActionEvent event) {
        ListModel listModel = toDoListController.getToDoListModel().getList(listName);

        taskScrollPaneVBox.getChildren().removeAll(taskScrollPaneVBox.getChildren());
        listModel.getAllTasks().forEach(listModel::deleteTask);
    }

    @FXML
    public void setClickOnMenuDeleteList(ActionEvent event) {
        toDoListController.getToDoListModel().deleteList(listName);
        toDoListController.setCenterPropertyToDefault();
        toDoListController.reloadListScrollPane();
    }

    @FXML
    public void setClickOnTaskButton(MouseEvent event, String taskName) {
        applyButtonStyle(event, "-fx-padding: 0 0 0 0 50; -fx-font-size: 11");

        if (event.getEventType().equals(MouseEvent.MOUSE_PRESSED)) {
            toDoListController.mainPane.rightProperty().set(taskPropertiesViews.get(taskName).getTaskPropertiesView());

            if (toDoListController.mainPane.getRight().isVisible())
                hideNode(taskPropertiesViews.get(taskName).getTaskPropertiesView());
            else {
                showNode(taskPropertiesViews.get(taskName).getTaskPropertiesView());
            }
        }
    }

    private TaskPropertiesController createTaskPropertiesView(String taskName) {
        AnchorPane taskPropertiesView = null;

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("taskPropertiesView.fxml"));
        TaskPropertiesController taskPropertiesController = new TaskPropertiesController(taskName, this);
        fxmlLoader.setControllerFactory(TaskPropertiesController -> taskPropertiesController);
        try {
            taskPropertiesView = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        taskPropertiesController.setTaskPropertiesView(taskPropertiesView);

        return taskPropertiesController;
    }

    @FXML
    public void setClickOnTaskRadioButton(MouseEvent event, String taskName) {
        applyButtonStyle(event, "-fx-background-size: 20");
        RadioButton radioButton = (RadioButton) event.getSource();

        if (event.getEventType().equals(MouseEvent.MOUSE_RELEASED)) {
            ListModel listModel = toDoListController.getToDoListModel().getList(listName);
            listModel.getTask(taskName).setCompletionState(radioButton.isSelected());

            applyTaskPaneStyle(radioButton);

            setTaskProgressBar();
        }
    }

    @FXML
    public void setClickOnNewTaskButton(MouseEvent event) {
        applyButtonStyle(event, "-fx-padding: 0 0 0 20; -fx-font-size: 14");
        Button eventBtn = (Button) event.getSource();
        centerNewTaskPane.setStyle("-fx-background-color: rgb(123, 132, 146);");

        if (event.getEventType().equals(MouseEvent.MOUSE_RELEASED)) {
            if (toDoListController.mainPane.getRight() != null)
                hideNode(toDoListController.mainPane.getRight());

            hideNode(eventBtn);
            TextField textField = new TextField();
            setAnchorProperty(textField, 0d, 0d, 0d, 0d);
            centerNewTaskPane.getChildren().add(textField);

            ContextMenu errorPopup = initTextFieldValidator(textField, "-fx-border-color: rgb(123, 132, 146); -fx-background-color: rgb(123, 132, 146);");

            textField.focusedProperty().addListener((observable, unfocused, focused) -> {
                if (unfocused) {
                    if (!textField.getText().isEmpty()) {
                        try {
                            ListModel listModel = toDoListController.getToDoListModel().getList(listName);
                            TaskModel taskModel = new TaskModel(textField.getText());
                            listModel.addTask(taskModel);

                            taskPropertiesViews.put(textField.getText(), createTaskPropertiesView(textField.getText()));

                            reloadTaskScrollPane();

                            centerNewTaskPane.getChildren().remove(textField);
                            showNode(eventBtn);
                            centerNewTaskPane.setStyle("");
                        } catch (NullPointerException e) {
                            textField.getParent().setStyle("-fx-border-color: red; -fx-background-color: rgb(123, 132, 146);");
                            errorPopup.getItems().clear();
                            errorPopup.getItems().add(new MenuItem("Task with such name already exists!"));
                            errorPopup.show(textField, Side.RIGHT, -textField.getWidth() + 5, -(textField.getHeight() + 10));

                            textField.requestFocus();
                        }
                    } else {
                        toDoListController.mainPane.requestFocus();

                        centerNewTaskPane.getChildren().remove(textField);
                        showNode(eventBtn);
                        centerNewTaskPane.setStyle("");
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

    protected void createTaskButton(TaskModel task) {
        taskPropertiesViews.put(task.getName(), createTaskPropertiesView(task.getName()));

        Button btn = new Button(task.getName());
        btn.setId("centerTaskBtn");
        setAnchorProperty(btn, 30d, 0d, 0d, 0d);

        btn.setOnMousePressed(event -> setClickOnTaskButton(event, task.getName()));
        btn.setOnMouseReleased(event -> setClickOnTaskButton(event, task.getName()));

        RadioButton radioBtn = new RadioButton();
        radioBtn.setSelected(task.getCompletionState());
        radioBtn.setId("centerTaskRadioBtn");
        setAnchorProperty(radioBtn, 0d, null, 0d, 0d);

        radioBtn.setOnMousePressed(event -> setClickOnTaskRadioButton(event, task.getName()));
        radioBtn.setOnMouseReleased(event -> setClickOnTaskRadioButton(event, task.getName()));

        AnchorPane anchorPane = new AnchorPane(btn, radioBtn);
        anchorPane.setId("centerTaskPane");

        applyTaskPaneStyle(radioBtn);
        centerTaskProgressBar.fireEvent(new ActionEvent());
        taskScrollPaneVBox.getChildren().add(anchorPane);
    }

    private void applyTaskPaneStyle(RadioButton radioButton) {
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

    public void setListView(AnchorPane listView) {
        this.listView = listView;
    }

    public AnchorPane getListView() {
        return listView;
    }

    public HashMap<String, TaskPropertiesController> getTaskPropertiesViews() {
        return taskPropertiesViews;
    }
}