package top.R3.process;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import lombok.Data;
import top.R3.device.Device;

/**
 * Process 类表示一个进程
 */
@Data
public class Process {
    private PCB pcb;                                                    // 进程控制块
    private IntegerProperty id = new SimpleIntegerProperty();           // 进程ID
    private IntegerProperty totalRunTime = new SimpleIntegerProperty(); // 总运行时间
    private StringProperty ocupyingDeviceType = new SimpleStringProperty(); // 占用的设备类型
    private StringProperty status = new SimpleStringProperty();         // 进程状态
    private IntegerProperty committedTime = new SimpleIntegerProperty();// 提交时间
    private IntegerProperty continueTime = new SimpleIntegerProperty(); // 持续时间
    private IntegerProperty finalTime = new SimpleIntegerProperty();    // 结束时间
    private Device device;                                              // 关联的设备

    /**
     * 默认构造函数
     */
    public Process() {
    }

    /**
     * 使用PCB构造Process对象
     * @param pcb 进程控制块
     */
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

    // 以下是各个属性的getter方法

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

    // 以下是一些属性的setter方法

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