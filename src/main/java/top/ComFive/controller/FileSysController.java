package top.ComFive.controller;

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
import top.ComFive.disk.Disk;
import top.ComFive.filesys.FileSysHandler;

// 使用Lombok注解添加日志功能
@Slf4j
// 使用Lombok注解自动生成getter、setter等方法
@Data
// 标记为JavaFX控制器
@FXMLController
public class FileSysController {
    // 存放小矩形(代表磁盘块)的FlowPane容器
    @FXML
    public FlowPane rectBox;

    // 观察列表，用于存储磁盘对象
    @FXML
    public ObservableList<Disk> diskList = FXCollections.observableArrayList();

    // 文件分配表
    public TableView<Disk> diskTable;
    public TableColumn<Disk, IntegerProperty> num;  // 盘号列
    public TableColumn<Disk, IntegerProperty> next; // 索引列（指向下一个磁盘块）

    // 初始化方法，在FXML加载后自动调用
    public void initialize(){
        // 给每个矩形块(代表磁盘块)命名id,并创建对应的Disk对象
        ObservableList<Node> rectList = rectBox.getChildren();

        for(int i=0; i<rectList.size(); i++){
            rectList.get(i).setId("rect"+String.valueOf(i));//为每一个可视化的rect设置一个id

            diskList.add(new Disk(i,i));
        }

        // 绑定表格列与Disk对象的属性
        num.setCellValueFactory(new PropertyValueFactory<>("num"));
        next.setCellValueFactory(new PropertyValueFactory<>("next"));

        // 将diskList设置为表格的数据源
        diskTable.setItems(diskList);

        // 设置前三个磁盘块为已占用状态
        diskList.get(0).setNext(1);
        diskList.get(1).setNext(255);
        diskList.get(2).setNext(255);//255代码EOF

        // 设置第2和第3个矩形(索引1和2)的颜色为绿色，表示已占用
        rectList.get(0).setStyle("-fx-fill: #676161;");
        rectList.get(1).setStyle("-fx-fill: #676161;");
        rectList.get(2).setStyle("-fx-fill: #676161;");
    }

    // 查找空闲磁盘块的方法
    public int findFreeDisk(){
        int diskLen = diskList.size();
        // 从第4个磁盘块开始查找（前3个为系统保留）
        for(int i=3; i<diskLen; i++){
            // next为0表示该磁盘块空闲
            if(diskList.get(i).getNext()==0) {
                return i;  // 返回找到的空闲磁盘块索引
            }
        }
        return -1;  // 如果没有找到空闲磁盘块，返回-1
    }
}