/*
 *  UCF COP3330 Summer 2021 Assignment 4 Solution
 *  Copyright 2021 Mikhail Plekunov
 */

package ucf.assignments;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.effect.BoxBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.InputEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.HashMap;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

public class ToDoListController {
    private final Database database;
    private ToDoListModel toDoListModel;

    private HashMap<String, ListController> listViews;

    @FXML
    TextField topSearchBar;
    @FXML
    AnchorPane topSearchPane;
    @FXML
    MenuItem topMenuImport;
    @FXML
    MenuItem topMenuExport;
    @FXML
    MenuItem topMenuExit;

    @FXML
    Button leftTodayBtn;
    @FXML
    Button leftTomorrowBtn;
    @FXML
    Button leftNextSevenDaysBtn;
    @FXML
    ToggleButton leftShowListsBtn;
    @FXML
    ScrollPane leftScrollPane;
    @FXML
    VBox leftScrollPaneVBox;
    @FXML
    AnchorPane leftNewListPane;
    @FXML
    Button leftNewListBtn;

    @FXML
    BorderPane mainPane;

    public ToDoListController() {
        database = new Database();
        toDoListModel = new ToDoListModel(database);
        listViews = new HashMap<>();
    }

    @FXML
    public void initialize() {
        toDoListModel.getLists().forEach(listManager -> createLeftListBtn(listManager.getListName()));

        mainPane.setOnMouseClicked(this::setFocusResetOnMouseClick);
        mainPane.setOnKeyReleased(this::setFocusResetOnEnterPressed);
        topMenuExit.setOnAction(this::exitApplication);
        topMenuImport.setOnAction(this::importDatabaseFile);
        topMenuExport.setOnAction(this::exportDatabaseFile);


        initDayViewBtn(leftTodayBtn, "Today", LocalDate.now());
        initDayViewBtn(leftTomorrowBtn, "Tomorrow", LocalDate.now().plusDays(1));

        mainPane.centerProperty().set(null);
    }

    private void initDayViewBtn(Button dayBtn, String btnText, LocalDate date) {
        dayBtn.setGraphic(dateToIconConverter(date));

        dayBtn.setGraphicTextGap(20);
        dayBtn.setContentDisplay(ContentDisplay.LEFT);
        dayBtn.setAlignment(Pos.BASELINE_LEFT);
        dayBtn.minHeight(40);
        setAnchorProperty(dayBtn, 0d, 0d, 0d, 0d);
        dayBtn.setId("dayViewBtn");
    }

    private ImageView dateToIconConverter(LocalDate date) {
        String icon_path = "/icons/Calendar/calendar-" + date.getDayOfMonth() + ".png";
        Image img = new Image(getClass().getResource(icon_path).toString());
        ImageView view = new ImageView(img);

        view.setFitWidth(24);
        view.setFitHeight(24);

        return view;
    }

    private void exportDatabaseFile(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();

        String appDir = getClass().getProtectionDomain().getCodeSource().getLocation().getPath();

        if (appDir.endsWith(".jar"))
            appDir = new File(appDir).getParent();

        fileChooser.setInitialDirectory(new File(appDir));

        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("SQLite Database", "*.sqlite"));

        toDoListModel.upload(database);

        if (!toDoListModel.getLists().isEmpty()) {
            File file = fileChooser.showSaveDialog(mainPane.getScene().getWindow());

            if (!file.getName().contains("."))
                file = new File(file.getAbsolutePath() + ".sqlite");

            try {
                Files.copy(Paths.get(database.getFilePath()), Path.of(file.getPath()), REPLACE_EXISTING);
            } catch (java.io.IOException e) {
                e.printStackTrace();
            }

            updateListScrollPane();
            mainPane.centerProperty().set(null);
        } else {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setHeaderText("Action is not valid");
            errorAlert.setContentText("You have no items for export");
            errorAlert.showAndWait();
        }
    }

    private void importDatabaseFile(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();

        String appDir = getClass().getProtectionDomain().getCodeSource().getLocation().getPath();

        if (appDir.endsWith(".jar"))
            appDir = new File(appDir).getParent();

        fileChooser.setInitialDirectory(new File(appDir));

        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("SQLite Database", "*.sqlite"));

        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null)
            database.setConnection(selectedFile.getAbsolutePath());

        //removes all already existing entries
        leftScrollPaneVBox.getChildren().removeAll(leftScrollPaneVBox.getChildren());
        toDoListModel = new ToDoListModel(database);
    }

    @FXML
    public void exitApplication(ActionEvent event) {
        toDoListModel.upload(database);

        Platform.exit();
        System.exit(0);
    }

    private void setFocusResetOnEnterPressed(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER)
            mainPane.requestFocus();
    }

    @FXML
    public void topSearchBarReleased(MouseEvent event) {
        TextField textField = (TextField) event.getSource();
        topSearchPane.setStyle("-fx-background-color: rgb(197, 174, 162)");

        if (event.getEventType().equals(MouseEvent.MOUSE_RELEASED)) {
            ContextMenu errorPopup = initTextFieldValidator(textField, "-fx-background-color: rgb(197, 174, 162); -fx-border-color: rgb(197, 174, 162);");

            topSearchBar.addEventFilter(KeyEvent.KEY_RELEASED, keyEvent -> {
                if (textField.getText().length() >= 20) {
                    event.consume();
                }
            });

            topSearchBar.focusedProperty().addListener((observable, unfocused, focused) -> {
                if (unfocused) {
                    if (!textField.getText().isEmpty()) {
                        try {
                            toDoListModel.findList(textField.getText());
                            //duplicate
                            if (mainPane.getRight() != null)
                                hideNode(mainPane.getRight());

                            mainPane.centerProperty().set(listViews.get(textField.getText()).getListView());

                            textField.setText("");
                            topSearchPane.setStyle("");
                        } catch (NullPointerException e) {
                            topSearchPane.setStyle("-fx-border-color: red; -fx-background-color: rgb(197, 174, 162);");

                            errorPopup.getItems().clear();
                            errorPopup.getItems().add(new MenuItem("There is no List with such name."));
                            errorPopup.show(textField, Side.RIGHT, -(textField.getWidth() + 20), -(textField.getHeight() + 4));

                            textField.requestFocus();
                        }
                    }
                    else
                        topSearchPane.setStyle("");
                }
            });
        }
    }

    @FXML
    public void leftShowListsBtnClicked(MouseEvent event) {
        btnStyle(event, "-fx-padding: 0 0 0 40; -fx-font-size: 13; -fx-graphic-text-gap: 160;");

        if (event.getEventType().equals(MouseEvent.MOUSE_RELEASED)) {
            //duplicate
            if (mainPane.getRight() != null)
                hideNode(mainPane.getRight());

            updateListScrollPane();

            if (leftScrollPane.visibleProperty().get()) {
                leftScrollPane.setVisible(false);
                leftScrollPane.setManaged(false);
            } else {
                leftScrollPane.setVisible(true);
                leftScrollPane.setManaged(true);
            }
        }
    }

    @FXML
    public void leftListBtnClicked(MouseEvent event) {
        btnStyle(event, "-fx-padding: 0 0 0 30; -fx-font-size: 13;");
        Button eventBtn = (Button) event.getSource();

        if (event.getEventType().equals(MouseEvent.MOUSE_RELEASED)) {
            //duplicate
            if (mainPane.getRight() != null)
                hideNode(mainPane.getRight());

            mainPane.centerProperty().set(listViews.get(eventBtn.getText()).getListView());
        }
    }

    @FXML
    public void leftNewListBtnClicked(MouseEvent event) {
        btnStyle(event, "-fx-padding: 0 0 0 20; -fx-font-size: 14");
        Button eventBtn = (Button) event.getSource();
        leftNewListPane.setStyle("-fx-background-color: rgb(123, 132, 146);");

        if (event.getEventType().equals(MouseEvent.MOUSE_RELEASED)) {
            if (mainPane.getRight() != null)
                hideNode(mainPane.getRight());

            hideNode(eventBtn);

            TextField textField = new TextField();
            setAnchorProperty(textField, 0d, 0d, 0d, 0d);
            leftNewListPane.getChildren().add(textField);

            ContextMenu errorPopup = initTextFieldValidator(textField, "-fx-background-color: rgb(123, 132, 146); -fx-border-color: rgb(123, 132, 146);");
            Pane parent = (Pane) leftNewListBtn.getParent();

            textField.focusedProperty().addListener((observable, unfocused, focused) -> {
                if (unfocused) {
                    if (!textField.getText().isEmpty()) {
                        try {
                            toDoListModel.addLst(new ListModel(textField.getText()));

                            createLeftListBtn(textField.getText());

                            updateListScrollPane();

                            parent.getChildren().remove(textField);
                            showNode(eventBtn);
                            leftNewListPane.setStyle("");

                            if (!leftScrollPane.isVisible()) {
                                leftShowListsBtn.fire();
                                leftShowListsBtnClicked(event);
                            }
                        } catch (NullPointerException e) {
                            textField.getParent().setStyle("-fx-border-color: red; -fx-background-color: rgb(123, 132, 146);");
                            errorPopup.getItems().clear();
                            errorPopup.getItems().add(new MenuItem("List with that name already exists!"));
                            errorPopup.show(textField, Side.RIGHT, -textField.getWidth() + 5, -(textField.getHeight() + 10));

                            textField.requestFocus();
                        }
                    } else {
                        mainPane.requestFocus();

                        parent.getChildren().remove(textField);
                        showNode(eventBtn);
                        leftNewListPane.setStyle("");
                    }
                }
            });
        }
    }

    protected static ContextMenu initTextFieldValidator(TextField textField, String style) {
        ContextMenu contextMenu = new ContextMenu();
        contextMenu.setId("contextMenu");
        contextMenu.setAutoHide(false);

        textField.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (contextMenu.getItems().size() > 0) {
                contextMenu.hide();
                contextMenu.getItems().clear();
                textField.getParent().setStyle(style);
            }
        });

        return contextMenu;
    }

    protected void updateListScrollPane() {
        if (leftScrollPaneVBox != null) {
            leftScrollPaneVBox.getChildren().removeAll(leftScrollPaneVBox.getChildren());
            listViews.clear();
        }

        toDoListModel.getLists().forEach(listManager -> createLeftListBtn(listManager.getListName()));
    }

    private ListController createListView(String listName) {
        AnchorPane listView = null;

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("listView.fxml"));
        ListController listController = new ListController(listName, this);
        fxmlLoader.setControllerFactory(ListController -> listController);
        try {
            listView = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        listController.setListView(listView);

        return listController;
    }

    private void createLeftListBtn(String listName) {
        Button listBtn = new Button(listName);
        listBtn.setId("leftListBtn");
        setAnchorProperty(listBtn, 0d, 0d, 0d, 0d);

        listBtn.onMousePressedProperty().set(this::leftListBtnClicked);
        listBtn.onMouseReleasedProperty().set(this::leftListBtnClicked);

        leftScrollPaneVBox.getChildren().add(new AnchorPane(listBtn));
        listViews.put(listName, createListView(listName));
    }

    protected static void setAnchorProperty(Node node, Double left, Double right, Double top, Double bottom) {
        AnchorPane.setLeftAnchor(node, left);
        AnchorPane.setRightAnchor(node, right);
        AnchorPane.setTopAnchor(node, top);
        AnchorPane.setBottomAnchor(node, bottom);
    }

    protected static void hideNode(Node node) {
        node.setVisible(false);
        node.setManaged(false);
    }

    protected static void showNode(Node node) {
        node.setVisible(true);
        node.setManaged(true);
    }

    protected static void btnStyle(MouseEvent event, String style) {
        Node btn = ((Node) event.getSource());

        if (event.getEventType().equals(MouseEvent.MOUSE_PRESSED)) {
            btn.getParent().setEffect(new BoxBlur(2, 2, 1));
            btn.setStyle(style);
        } else if (event.getEventType().equals(MouseEvent.MOUSE_RELEASED)) {
            btn.setStyle("");
            btn.getParent().setEffect(null);
        }
    }

    protected void setCenterPropertyToDefault() {
        mainPane.centerProperty().set(null);
    }

    protected ToDoListModel getToDoListModel() {
        return toDoListModel;
    }

    private void setFocusResetOnMouseClick(InputEvent mouseEvent) {
        if (mouseEvent.getEventType().equals(MouseEvent.MOUSE_CLICKED)) {
            Node node = (Node) mouseEvent.getSource();
            node.requestFocus();
        }
    }

}
