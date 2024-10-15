package top.R3.device;

import javafx.beans.property.*;
import lombok.Data;

public class Device {
    //设备类型
    private StringProperty type = new SimpleStringProperty();
    //占用id
    private IntegerProperty ocupyPID = new SimpleIntegerProperty();
    //是否占用
    private BooleanProperty ocupied = new SimpleBooleanProperty();

    public Device(String type) {
        this.type.set(type);
    }
    //释放
    public void free() {
        this.ocupied.set(false);
        this.ocupyPID.set(0);
    }

    public boolean isOcupied() {
        return this.ocupied.get();
    }

    public void setOcupied(boolean ocupied) {
        this.ocupied.set(ocupied);
    }

    public String getType() {
        return type.get();
    }

    public int getOcupyPID(){
        return ocupyPID.get();
    }
    public void setOcupied() {
        this.ocupied.set(true);
    }

    public void setOcupyPID(int pid) {
        ocupyPID.set(pid);
        this.ocupied.set(true);
    }
}
