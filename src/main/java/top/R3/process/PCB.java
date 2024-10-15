package top.R3.process;

import javafx.beans.property.*;
import lombok.Data;


@Data
public class PCB {
    private Integer pid; //进程id
    private Integer totalRunTime ; //总运行时间
    private String ocupyingDeviceType ;//占用的设备
    private String status ; //状态
    private Integer committedTime ; //提交事件
    private Integer continueTime ;//持续时间
    private Integer finalTime ; //最终时间
    public PCB() {

    }
}
