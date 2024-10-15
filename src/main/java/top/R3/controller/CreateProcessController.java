package top.R3.controller;
import de.felixroske.jfxsupport.FXMLController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;

@Slf4j
@Data
@FXMLController
public class CreateProcessController {

    public ChoiceBox<String> deviceType;
    public TextField totalRunTime;
    private Stage stage;
    public Button cancel;


    @Resource
    private IndexController indexController;
    ObservableList<String> list = FXCollections.observableArrayList("A", "B", "C");

    @FXML
    public void create(MouseEvent mouseEvent) {
        String devType = deviceType.getValue();
        Integer totalTime = Integer.parseInt(totalRunTime.getText());

        indexController.createProcess(++indexController.auto_add_pid,devType,totalTime);
        totalRunTime.setText("");
        stage.close();
    }

    @FXML
    public void cancel(MouseEvent mouseEvent) {
        stage.close();
    }

    public void initialize(){
        deviceType.setItems(list);
    }

}
