package top.R3.controller;
// 导入必要的类
import de.felixroske.jfxsupport.FXMLController;
import javafx.beans.property.IntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.FlowPane;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import javax.annotation.Resource;
import top.R3.disk.Disk;
import top.R3.filesys.FileSysHandler;

// 使用Lombok注解简化日志和getter/setter方法的编写
@Slf4j
@Data
// 标记为JavaFX控制器
@FXMLController
public class FileSysController {
    // 存放小矩形的盒子
    @FXML
    public FlowPane rectBox;

    // 盘列表
    @FXML
    public ObservableList<Disk> diskList = FXCollections.observableArrayList();

    // 表格
    public TableView<Disk> diskTable;
    public TableColumn<Disk, IntegerProperty> num;  // 盘号
    public TableColumn<Disk, IntegerProperty> next; // 索引

    @Resource
    private FileSysHandler fileSysHandler;

    // 初始化方法
    public void initialize(){
        // 给矩形块命名id,并绑定表格和盘块
        ObservableList<Node> rectList = rectBox.getChildren();
        for(int i=0; i<rectList.size(); i++){
            rectList.get(i).setId("rect"+String.valueOf(i));
            diskList.add(new Disk(i,i));
        }

        // 绑定参数
        num.setCellValueFactory(new PropertyValueFactory<>("num"));
        next.setCellValueFactory(new PropertyValueFactory<>("next"));

        // 表格绑定磁盘块
        diskTable.setItems(diskList);
        diskList.get(0).setNext(255); // 前三个磁盘
        diskList.get(1).setNext(255);
        diskList.get(2).setNext(255);

        // 设置第2和第3个矩形的颜色为红色
        rectList.get(1).setStyle("-fx-fill: #ff1111;");
        rectList.get(2).setStyle("-fx-fill: #ff1111;");
    }

    // 找空的磁盘块
    public int findFreeDisk(){
        int diskLen = diskList.size();

        for(int i=3; i<diskLen; i++){
            if(diskList.get(i).getNext()==0) {
                return i;
            }
        }
        return -1;
    }
}