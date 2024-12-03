package top.ComFive.device;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 这个类负责管理设备
 */
@Slf4j // Lombok注解，自动创建日志对象
@Component // Spring注解，标记这个类为一个组件
public class DeviceHandler {
    // 存储设备的可观察列表
    private ObservableList<Device> deviceList = FXCollections.observableArrayList();

    // 用于显示设备信息的TableView
    TableView<Device> tv = null;

    /**
     * 构造函数，初始化设备列表
     */
    public DeviceHandler() {
        // 添加一些模拟的设备
        deviceList.addAll(
                new Device("A"),
                new Device("B"),
                new Device("B"),
                new Device("C"),
                new Device("C"));
    }

    /**
     * 绑定TableView的列与Device对象的属性
     */
    public void bindProperties(TableView<Device> tv, TableColumn<Device, IntegerProperty> ocupyDev, TableColumn<Device, StringProperty> type) {
        ocupyDev.setCellValueFactory(new PropertyValueFactory<>("ocupyPID"));
        type.setCellValueFactory(new PropertyValueFactory<>("type"));

        tv.setItems(deviceList);
        this.tv = tv;
    }

    /**
     * 申请指定类型的设备
     * @param type 设备类型
     * @param pid 进程ID
     * @return 分配的设备，如果没有可用设备则返回null
     */
    public Device ocupyDevice(String type, int pid) {
        for (Device device : deviceList) {//遍历设备列表
            if (!device.isOcupied() && device.getType().equals(type)) {
                device.setOcupyPID(pid);
                device.setOcupied(true);
                log.info("进程 {} 占用了设备 {}", pid, type);
                return device;
            }
        }
        tv.refresh();
        return null;
    }

    /**
     * 释放设备
     * @param device 要释放的设备
     */
    public void releaseDevice(Device device) {
        try {
            device.free();
            log.info("设备 {} 已被释放", device.getType());
            tv.refresh();
        } catch (Exception e) {
            log.error("不能释放空设备", e);
        }
    }
}