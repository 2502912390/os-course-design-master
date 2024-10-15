package top.R3.device;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

//全局设备容器
@Slf4j
@Component
public class DeviceHandler {
    private ObservableList<Device> deviceList = FXCollections.observableArrayList();

    public DeviceHandler() {
        deviceList.addAll(//假设A设备2个 B设备3个 C设备3个
                new Device("A"),
                new Device("A"),
                new Device("B"),
                new Device("B"),
                new Device("B"),
                new Device("C"),
                new Device("C"),
                new Device("C"));
    }

    //申请type类型设备
    public Device ocupyDevice(String type, int pid) {
        for (Device device : deviceList) {
            if (!device.isOcupied() && device.getType().equals(type)) {
                device.setOcupyPID(pid);
                device.setOcupied(true);
                log.info("pid:{}", pid);
                return device;
            }
        }
        tv.refresh();
        return null;
    }

    //用于显示表格数据
    TableView<Device> tv = null;
    //配置TableView和它的列
    public void bindProperties(TableView<Device> tv, TableColumn<Device, IntegerProperty> ocupyDev, TableColumn<Device, StringProperty> type) {
        ocupyDev.setCellValueFactory(new PropertyValueFactory<>("ocupyPID"));
        type.setCellValueFactory(new PropertyValueFactory<>("type"));
        tv.setItems(deviceList);
        this.tv = tv;
    }

    //释放设备
    public void releaseDevice(Device device) {
        try {
            device.free();
            tv.refresh();
        } catch (Exception e) {
            log.error("不能释放空设备",e);
        }
    }

    // 返回
}
