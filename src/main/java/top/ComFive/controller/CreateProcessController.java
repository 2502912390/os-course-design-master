package top.ComFive.controller;

// 导入必要的类
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

// 使用Lombok注解简化日志和getter/setter方法的编写
@Slf4j
@Data
// 标记为JavaFX控制器
@FXMLController
public class CreateProcessController {

    // UI组件
    public ChoiceBox<String> deviceType;  // 设备类型选择框
    public TextField totalRunTime;        // 总运行时间输入框
    private Stage stage;                  // 当前窗口
    public Button cancel;                 // 取消按钮

    // 注入IndexController，用于与主界面交互
    @Resource
    private IndexController indexController;
    
    // 设备类型列表
    ObservableList<String> list = FXCollections.observableArrayList("A", "B", "C");

    // 创建进程方法，当用户点击创建按钮时调用
    @FXML
    public void create(MouseEvent mouseEvent) {
        // 获取选择的设备类型
        String devType = deviceType.getValue();
        // 获取输入的总运行时间并转换为整数
        Integer totalTime = Integer.parseInt(totalRunTime.getText());

        // 调用IndexController的createProcess方法创建新进程
        indexController.createProcess(++indexController.auto_add_pid, devType, totalTime);
        // 清空总运行时间输入框
        totalRunTime.setText("");
        // 关闭当前窗口
        stage.close();
    }

    // 取消操作方法，当用户点击取消按钮时调用
    @FXML
    public void cancel(MouseEvent mouseEvent) {
        // 关闭当前窗口
        stage.close();
    }

    // 初始化方法，在FXML加载后自动调用
    public void initialize(){
        // 设置设备类型选择框的选项
        deviceType.setItems(list);
    }
}