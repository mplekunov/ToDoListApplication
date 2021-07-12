/*
 *  UCF COP3330 Summer 2021 Assignment 4 Solution
 *  Copyright 2021 Mikhail Plekunov
 */

package ucf.assignments;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.skin.DatePickerSkin;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;

import static ucf.assignments.ToDoListController.*;

public class TaskPropertiesController {
    private final ListController listController;
    private final Task task;

    @FXML AnchorPane right;
    @FXML TextField rightTaskNameField;
    @FXML Button rightAddDueDateBtn;
    @FXML TextArea rightAddNoteField;
    @FXML Button rightBackBtn;
    @FXML Button rightDeleteBtn;
    @FXML VBox rightDueDateAndNoteVBox;
    @FXML AnchorPane rightAddDueDatePane;

    public TaskPropertiesController(Task task, ListController listController) {
        this.listController = listController;
        this.task = task;
    }

    public void initialize() {
        rightTaskNameField.setText(task.getName());

        rightAddNoteField.setText(task.getDescription());
        rightAddNoteField.addEventFilter(KeyEvent.KEY_RELEASED, event -> {
            TextArea tx = (TextArea) event.getSource();
            if (tx.getText().length() >= 256) {
                event.consume();
            }
            else if (tx.getText().length() >= 1)
                listController.getListManager().changeTaskNote(task.getName(), tx.getText());
        });

        if (task.getDueDate() != null ) {
            rightAddDueDateBtn.setText(DateFormatter.dateToString(task.getDueDate()));
            createDueDateClearBtn();
        }else
            rightAddDueDateBtn.setText("Add Due Date");

        rightAddDueDateBtn.setOnMouseClicked(event -> {
            hideNode(rightAddDueDateBtn.getParent());
            VBox calendar = createCalendar(rightDueDateAndNoteVBox, task);
            rightDueDateAndNoteVBox.getChildren().add(calendar);
        });

        rightDeleteBtn.setOnMouseReleased(event -> {
            listController.getListManager().deleteTask(task.getName());

            rightBackBtnClicked(event);
            listController.taskScrollPaneVBox.getChildren().remove(((Node)event.getSource()).getParent());
        });

    }

    protected void setDueDateClearBtn(MouseEvent event) {
        btnStyle(event,"-fx-padding: 0 16 0 0;");
        Button clear = (Button) event.getSource();

        if (event.getEventType().equals(MouseEvent.MOUSE_RELEASED)) {
            rightAddDueDateBtn.setText("Add Due Date");
            setAnchorProperty(rightAddDueDateBtn, 0d, 0d, 0d, 0d);

            rightAddDueDatePane.getChildren().remove(clear);
            task.setDueDate(null);
        }
    }

    @FXML
    //DOESN'T WORK
    protected void addListenerToRightTextField(MouseEvent event) {
        if (event.getEventType().equals(MouseEvent.MOUSE_RELEASED)) {
            ContextMenu errorPopup = initTextFieldValidator(rightTaskNameField);

            Button eventBtn = (Button) event.getSource();
            rightTaskNameField.focusedProperty().addListener((observable, unfocused, focused) -> {
                if (unfocused) {
                    if (rightTaskNameField.getText().isEmpty())
                        rightTaskNameField.requestFocus();
                    else {
                        try {
                            Task foundTask = listController.getListManager().findTask(rightTaskNameField.getText());

                            if (!foundTask.getName().equals(eventBtn.getText())) {
                                rightTaskNameField.getParent().setStyle("-fx-border-color: red");
                                errorPopup.getItems().clear();
                                errorPopup.getItems().add(new MenuItem("Task with that name already exists!"));
                                errorPopup.show(rightTaskNameField, Side.RIGHT, -rightTaskNameField.getWidth(), -rightTaskNameField.getHeight());

                                rightTaskNameField.requestFocus();
                            }
                        } catch (NullPointerException e) {
                            listController.getListManager().changeTaskName(eventBtn.getText(), rightTaskNameField.getText());
                            eventBtn.setText(rightTaskNameField.getText());

                            rightBackBtnClicked(event);
                            listController.updateTaskScrollPane();
                        }
                    }
                }
            });
        }
    }

    @FXML
    public void rightBackBtnClicked(MouseEvent event) {
        btnStyle(event, "-fx-padding: 0 0 0 16;");

        if (event.getEventType().equals(MouseEvent.MOUSE_RELEASED)) {
            if (right.visibleProperty().get())
                hideNode(right);
            else
                showNode(right);
        }
    }
    @FXML
    public void rightAddDueDateBtnClicked(MouseEvent event) {
        btnStyle(event, "-fx-padding: 0 0 0 20; -fx-font-size: 12");
    }

    @FXML
    public void rightDeleteBtnClicked(MouseEvent event) {
        btnStyle(event, "-fx-padding: 0 16 0 0;");
    }

    protected void createDueDateClearBtn() {
        Button clear = new Button();
        rightAddDueDatePane.getChildren().add(clear);

        clear.setId("rightDueDateClearBtn");
        Image img = new Image(getClass().getResource("/icons/clear.png").toString());
        ImageView view = new ImageView(img);
        view.setFitHeight(24);
        view.setFitWidth(24);
        view.setPreserveRatio(true);
        clear.setGraphic(view);

        setAnchorProperty(clear, null, 0d, 0d, 0d);
        setAnchorProperty(rightAddDueDateBtn, 0d, 38d, 0d, 0d);

        clear.setOnMousePressed(this::setDueDateClearBtn);
        clear.setOnMouseReleased(this::setDueDateClearBtn);
    }

    protected Button createCalendarBtn(String name) {
        Button btn = new Button();
        btn.setText(name);
        btn.setId("calendarBtn");
        btn.setPrefWidth(40);
        btn.setMaxWidth(Double.MAX_VALUE);

        return btn;
    }

    protected VBox createCalendar(Pane parent, Task task) {
        DatePicker datePicker = new DatePicker(task.getDueDate());
        DatePickerSkin datePickerSkin = new DatePickerSkin(datePicker);
        Node popupContent = datePickerSkin.getPopupContent();

        popupContent.setStyle("-fx-background-color: transparent; -fx-background-radius: 8; -fx-border-radius: 8;");

        Button cancel = createCalendarBtn("Cancel");
        Button save = createCalendarBtn("Save");

        HBox hbox = new HBox(cancel, save);
        hbox.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;");
        hbox.setSpacing(2);
        hbox.setAlignment(Pos.CENTER);
        HBox.setHgrow(save, Priority.ALWAYS);
        HBox.setHgrow(cancel, Priority.ALWAYS);

        VBox vbox = new VBox(popupContent, hbox);
        vbox.setSpacing(2);
        vbox.setStyle("-fx-background-color: rgb(245, 229, 201); -fx-border-color: rgb(195, 179, 151); -fx-background-radius: 8; -fx-border-radius: 8;");

        cancel.setOnMouseReleased(keyEvent -> {
            parent.getChildren().remove(vbox);
            showNode(rightAddDueDateBtn.getParent());
        });

        save.setOnMouseReleased(keyEvent -> {
            task.setDueDate(datePicker.getValue());
            rightAddDueDateBtn.setText(DateFormatter.dateToString(task.getDueDate()));
            createDueDateClearBtn();
            cancel.fireEvent(keyEvent);
        });

        return vbox;
    }
}
