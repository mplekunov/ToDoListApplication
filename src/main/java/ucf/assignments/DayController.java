/*
 *  UCF COP3330 Summer 2021 Assignment 4 Solution
 *  Copyright 2021 Mikhail Plekunov
 */

package ucf.assignments;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;

public class DayController extends ListController {
    //At this point I have a hard time understanding what I will want to do with this class
    //I will need more time to understand the way I will want to fire my controllers and scenes
    private final String labelName;

    @FXML Label centerDayField;
    @FXML ScrollPane centerTaskScrollPane;

    public DayController(String labelName, ToDoListController listController) {
        super(listController);
        this.labelName = labelName;
    }

    public void initialize() {
        centerDayField.setText(labelName);
    }
}
