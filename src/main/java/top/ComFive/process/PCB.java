package top.ComFive.process;

import lombok.Data;


@Data
public class PCB {
        private Integer pid; //进程id
        private Integer totalRunTime ; //总运行时间
        private String ocupyingDeviceType ;//占用的设备
        private String status ; //状态
        private Integer committedTime ; //提交时间
        private Integer continueTime ;//持续时间
        private Integer finalTime ; //最终时间
        //添加阻塞原因
        public PCB() { }
}
