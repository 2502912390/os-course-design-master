package top.R3.process;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;
import top.R3.device.Device;

/**
 * @Author lemon
 * @Date 2022/10/7 9:24
 * @Version 1.0
 */
/*
 * */
@Data
public class Process {
    private PCB pcb;
    private IntegerProperty id = new SimpleIntegerProperty();
    private IntegerProperty totalRunTime = new SimpleIntegerProperty();
    private StringProperty ocupyingDeviceType = new SimpleStringProperty();//占用的设备
    private StringProperty status = new SimpleStringProperty();
    private IntegerProperty committedTime = new SimpleIntegerProperty();
    private IntegerProperty continueTime = new SimpleIntegerProperty();//持续时间
    private IntegerProperty finalTime = new SimpleIntegerProperty();
    private Device device;

    public Process() {
    }

    public Process(PCB pcb) {
        this.pcb = pcb;
        id.set(pcb.getPid());
        totalRunTime.set(pcb.getTotalRunTime());
        ocupyingDeviceType.set(pcb.getOcupyingDeviceType());
        status.set(pcb.getStatus());
        continueTime.set(pcb.getContinueTime());
        committedTime.set(pcb.getCommittedTime());
        finalTime.set(pcb.getFinalTime());
    }

    public PCB getPcb() {
        return pcb;
    }

    public Integer getId() {
        return id.get();
    }


    public int getTotalRunTime() {
        return totalRunTime.get();
    }

    public String getOcupyingDeviceType() {
        return ocupyingDeviceType.get();
    }

    public String getStatus() {
        return status.get();
    }

    public int getCommittedTime() {
        return committedTime.get();
    }

    public int getContinueTime() {
        return continueTime.get();
    }

    public int getFinalTime() {
        return finalTime.get();
    }

    public void setStatus(String status) {
        this.status.set(status);
    }

    public void setContinueTime(int continueTime) {
        this.continueTime.set(continueTime);
    }

    public void setFinalTime(int finalTime) {
        this.finalTime.set(finalTime);
    }
}
