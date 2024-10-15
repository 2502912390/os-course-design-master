package top.R3.device;

import javafx.beans.property.*;
import lombok.Data;

@Data
public class Device {
    // 设备类型
    private StringProperty type = new SimpleStringProperty();
    
    // 占用设备的进程ID
    private IntegerProperty ocupyPID = new SimpleIntegerProperty();
    
    // 设备是否被占用
    private BooleanProperty ocupied = new SimpleBooleanProperty();

    // 构造函数
    public Device(String type) {
        this.type.set(type);
    }
    
    // 释放设备
    public void free() {
        this.ocupied.set(false);
        this.ocupyPID.set(0);
    }

    // 检查设备是否被占用
    public boolean isOcupied() {
        return this.ocupied.get();
    }

    // 设置设备占用状态
    public void setOcupied(boolean ocupied) {
        this.ocupied.set(ocupied);
    }

    // 获取设备类型
    public String getType() {
        return type.get();
    }

    // 获取占用设备的进程ID
    public int getOcupyPID(){
        return ocupyPID.get();
    }
    
    // 将设备设置为占用状态
    public void setOcupied() {
        this.ocupied.set(true);
    }

    // 设置占用设备的进程ID并将设备设置为占用状态
    public void setOcupyPID(int pid) {
        ocupyPID.set(pid);
        this.ocupied.set(true);
    }
}