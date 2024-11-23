package top.ComFive.process;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 进程处理类，包含绑定属性，CRUD，移动等操作
 */
@Slf4j
@Component
public class ProcessHandler {
    /**
     * 绑定表格列与Process对象的属性
     */
    public void bindProperties(TableColumn<Process, IntegerProperty> pid,
                               TableColumn<Process, IntegerProperty> totalRunTime,
                               TableColumn<Process, StringProperty> ocupyingDeviceType,
                               TableColumn<Process, StringProperty> status,
                               TableColumn<Process, IntegerProperty> committedTime,
                               TableColumn<Process, IntegerProperty> continueTime,
                               TableColumn<Process, IntegerProperty> finalTime
                               ){
        pid.setCellValueFactory(new PropertyValueFactory<>("id"));
        totalRunTime.setCellValueFactory(new PropertyValueFactory<>("totalRunTime"));
        ocupyingDeviceType.setCellValueFactory(new PropertyValueFactory<>("ocupyingDeviceType"));
        status.setCellValueFactory(new PropertyValueFactory<>("status"));
        committedTime.setCellValueFactory(new PropertyValueFactory<>("committedTime"));
        continueTime.setCellValueFactory(new PropertyValueFactory<>("continueTime"));
        finalTime.setCellValueFactory(new PropertyValueFactory<>("finalTime"));
    }

    /**
     * 绑定TableView与ObservableList
     */
    public void bindTableViewAndList(TableView<Process> tv, ObservableList<Process> list){
        tv.setItems(list);
    }

    /**
     * 根据进程ID从表格中移除进程
     */
    public void removeByPid(ObservableList<Process> table, Integer pid){
        int len = table.size();
        for(int i=0; i<len; i++){
            Process process = table.get(i);
            if(process.getId() == pid){
                table.remove(process);
                log.info("删除成功");
                return;
            }
        }
    }

    /**
     * 从表格中移除指定进程
     */
    public void remove(ObservableList<Process> table, Process process){
        table.remove(process);
        log.info("删除成功");
    }

    /**
     * 向列表中添加进程
     */
    public void addItemInList(ObservableList<Process> list, Process process){
        list.add(process);
    }

    /**
     * 将进程从等待态移动到就绪态
     */
    public void moveProcessFromWaitToReady(Process process, ObservableList<Process> waitTable, ObservableList<Process> readyTable){
        waitTable.remove(process);
        process.setStatus("就绪态");
        readyTable.add(process);
    }

    /**
     * 将进程从就绪态移动到运行态
     */
    public void moveProcessFromReadyToExecute(Process process, ObservableList<Process> readyTable, ObservableList<Process> executeTable){
        boolean remove = readyTable.remove(process);
        process.setStatus("运行态");
        executeTable.add(process);
    }

    /**
     * 将进程从运行态移动到阻塞态
     */
    public void moveProcessFromExecuteToWait(Process process, ObservableList<Process> executeTable, ObservableList<Process> waitTable){
        executeTable.remove(process);
        process.setStatus("阻塞态");
        waitTable.add(process);
    }

    /**
     * 将进程从运行态移动到就绪态
     */
    public void moveProcessFromExecuteToReady(Process process, ObservableList<Process> executeTable, ObservableList<Process> readyTable){
        executeTable.remove(process);
        process.setStatus("就绪态");
        readyTable.add(process);
    }

    /**
     * 将进程从运行态移动到完成态
     */
    public void moveProcessFromExecuteToFinish(Process process, ObservableList<Process> executeTable, ObservableList<Process> finishTable){
        executeTable.remove(process);
        process.setStatus("完成态");
        finishTable.add(process);
    }

    /**
     * 获取一个指定类型设备的阻塞进程
     */
    public Process getWaitProcess(ObservableList<Process> waitTable, String type) {
        for (Process process : waitTable) {
            if(type.equals(process.getOcupyingDeviceType())){
                log.info("找到了想要的进程");
                return process;
            }
        }
        return null;
    }
}