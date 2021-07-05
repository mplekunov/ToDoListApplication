/*
 *  UCF COP3330 Summer 2021 Assignment 4 Solution
 *  Copyright 2021 Mikhail Plekunov
 */

package ucf.assignments;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.effect.BoxBlur;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import java.io.IOException;

public class ToDoListController {
    private Database database;

    @FXML TextField topSearchBar;

    @FXML Button leftTodayBtn;
    @FXML Button leftTomorrowBtn;
    @FXML Button leftNextSevenDaysBtn;
    @FXML ToggleButton leftShowListsBtn;
    @FXML ScrollPane leftScrollPane;
    @FXML Button leftNewListBtn;

    @FXML TextField rightTaskNameField;
    @FXML Button rightAddDueDateBtn;
    @FXML TextArea rightAddNoteField;
    @FXML Button rightBackBtn;
    @FXML Button rightDeleteBtn;

    @FXML BorderPane mainPane;
    private AnchorPane todayView;
    private AnchorPane tomorrowView;
    private AnchorPane nextSevenDaysView;
    private AnchorPane listView;

    //final implementation will be different, for now it just loads different scenes just to prove that everything works
    @FXML public void initialize() {
        //The line below will create a new database object with specified file path
        //when database object will be created I will be able to use information from database
        //for initialization of fields in scenes (e.g. passing this info to controllers)
        //database = new Database()

        //Code below is there just for the sake of testing transition between scenes
        //It won't be structured in that way in the final version
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("DayView.fxml"));
        fxmlLoader.setControllerFactory(DayController -> new DayController("Today!", this));
        try {
            todayView = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        fxmlLoader = new FXMLLoader(getClass().getResource("DayView.fxml"));
        fxmlLoader.setControllerFactory(DayController -> new DayController("Tomorrow!", this));
        try {
            tomorrowView = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        fxmlLoader = new FXMLLoader(getClass().getResource("DayView.fxml"));
        fxmlLoader.setControllerFactory(DayController -> new DayController("Next 7 Days!", this));
        try {
            nextSevenDaysView = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        fxmlLoader = new FXMLLoader(getClass().getResource("listView.fxml"));
        fxmlLoader.setControllerFactory(ListController -> new ListController("CurrentList", this));
        try {
            listView = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        mainPane.centerProperty().set(todayView);
    }

    @FXML
    public void topSearchBarReleased(MouseEvent event) {
        if (event.getEventType().equals(MouseEvent.MOUSE_RELEASED))
                topSearchBar.setText("");
    }

    @FXML
    public void leftTodayBtnClicked(MouseEvent event) {
        btnStyle(event, "-fx-padding: 0 0 0 40; -fx-font-size: 15");

        if (event.getEventType().equals(MouseEvent.MOUSE_RELEASED))
            if (!mainPane.centerProperty().get().equals(todayView))
                //gets collection of tasks which have today's due date
                //if there are no tasks in a collection, shows an empty screen indicating that there are no tasks left for today
                //else use DayController to initialize scene with the data from tasks collection
                //shows that scene in the mainPane.center
                mainPane.centerProperty().set(todayView);
    }

    @FXML
    public void leftTomorrowBtnClicked(MouseEvent event) {
        btnStyle(event, "-fx-padding: 0 0 0 40; -fx-font-size: 15");

        if (event.getEventType().equals(MouseEvent.MOUSE_RELEASED))
            if (!mainPane.centerProperty().get().equals(tomorrowView))
                //gets collection of tasks which have tomorrow's due date
                //if there are no tasks in a collection, shows an empty screen indicating that there are no tasks left for tomorrow
                //else use DayController to initialize scene with the data from tasks collection
                //shows that scene in the mainPane.center
                mainPane.centerProperty().set(tomorrowView);
    }

    @FXML
    public void leftNextSevenDaysBtnClicked(MouseEvent event) {
        btnStyle(event, "-fx-padding: 0 0 0 40; -fx-font-size: 15");

        if (event.getEventType().equals(MouseEvent.MOUSE_RELEASED))
            if (!mainPane.centerProperty().get().equals(nextSevenDaysView))
                //gets collection of tasks which have due date in the range of [today, next7Days)
                //if there are no tasks in a collection, shows an empty screen indicating that there are no tasks left for that period
                //else use DayController to initialize scene with the data from tasks collection
                //shows that scene in the mainPane.center
                mainPane.centerProperty().set(nextSevenDaysView);
    }

    @FXML
    public void leftShowListsBtnClicked(MouseEvent event) {
        btnStyle(event, "-fx-padding: 0 0 0 40; -fx-font-size: 13; -fx-graphic-text-gap: 160;");

        if (event.getEventType().equals(MouseEvent.MOUSE_RELEASED)) {
            //gets collection of lists
            //fill ScrollPane with elements name
            if (leftScrollPane.visibleProperty().get()) {
                leftScrollPane.setVisible(false);
                leftScrollPane.setManaged(false);
            } else {
              leftScrollPane.setVisible(true);
              leftScrollPane.setManaged(true);
            }
        }
    }

    //Temporary implementation, I will need to do a dynamic button creation
    @FXML
    public void leftListBtnClicked(MouseEvent event) {
        btnStyle(event, "-fx-padding: 0 0 0 30; -fx-font-size: 13;");

        if (event.getEventType().equals(MouseEvent.MOUSE_RELEASED)) {
            //search for a the same name of the list in list collection as the text property of the pressed button
            //use ListController to initialize scene with the data from the list
            //show scene
            mainPane.centerProperty().set(listView);
        }
    }

    //I will probably just change button on the textField
    @FXML
    public void leftNewListBtnClicked(MouseEvent event) {
        btnStyle(event, "-fx-padding: 0 0 0 20; -fx-font-size: 14");
        //hides button
        //creates tempTextField on the place of button
        //let user enter the name of the list
        //checks for the same name in the current list collection
        //if list with the same name is found, asks user to enter different name (tooltip or contextpopup)
        //else initializes new list and adds it to the list collection
        //shows button again
    }

    @FXML
    public void rightAddDueDateBtnClicked(MouseEvent event) {
        btnStyle(event, "-fx-padding: 0 0 0 20; -fx-font-size: 12");
        //open calendar as contextmenu or popup or small window
        //let user pick the date
        //changes the date of the task to the new date
    }

    //Final version. It's a gui element which closes (hides) pane, nothing else.
    @FXML
    public void rightBackBtnClicked(MouseEvent event) {
        btnStyle(event, "-fx-padding: 0 0 0 16;");

        if (event.getEventType().equals(MouseEvent.MOUSE_RELEASED)) {
            var right = mainPane.getRight();
            if (right.visibleProperty().get())
                hideNode(right);
            else
                showNode(right);
        }
    }

    @FXML
    public void rightDeleteBtnClicked(MouseEvent event) {
        btnStyle(event, "-fx-padding: 0 16 0 0;");

        if (event.getEventType().equals(MouseEvent.MOUSE_RELEASED)) {
            //get task collection from the current list
            //looks for the task with the same name as the one currently specified in the textField on right Pane
            //delete that task from list collection
            //closes right pane (Task property pane)
            rightBackBtnClicked(event);
        }
    }

    protected void hideNode(Node node) {
        node.setManaged(false);
        node.setVisible(false);
    }

    protected void showNode(Node node) {
        node.setManaged(true);
        node.setVisible(true);
    }

    //Final Version, puts style on button press/release
    protected void btnStyle(MouseEvent event, String style) {
        Node btn = ((Node)event.getSource());

        if (event.getEventType().equals(MouseEvent.MOUSE_PRESSED)) {
            btn.getParent().setEffect(new BoxBlur(2, 2, 1));
            btn.setStyle(style);
        } else if (event.getEventType().equals(MouseEvent.MOUSE_RELEASED)) {
            btn.setStyle("");
            btn.getParent().setEffect(null);
        }
    }
}
