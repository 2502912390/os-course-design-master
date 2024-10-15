package top.R3.process;

/**
 * @Author lemon
 * @Date 2022/10/9 21:04
 * @Version 1.0
 */
public class PCBBuilder {
    private Integer pid = -1;
    private Integer totalRunTime =-1;
    private String ocupyingDeviceType ="";//占用的设备
    private String status ="就绪";
    private Integer committedTime =-1 ;
    private Integer continueTime =-1;//持续时间
    private Integer finalTime =-1;

    public PCBBuilder pid(Integer pid) {
        this.pid = pid;
        return this;
    }

    public PCBBuilder totalRunTime(Integer totalRunTime) {
        this.totalRunTime = totalRunTime;
        return this;
    }

    public PCBBuilder ocupyingDeviceType(String ocupyingDeviceType) {
        this.ocupyingDeviceType = ocupyingDeviceType;
        return this;
    }

    public PCBBuilder status(String status) {
        this.status = status;
        return this;

    }

    public PCBBuilder committedTime(Integer committedTime) {
        this.committedTime = committedTime;
        return this;

    }

    public PCBBuilder continueTime(Integer continueTime) {
        this.continueTime = continueTime;
        return this;
    }

    public PCBBuilder finalTime(Integer finalTime) {
        this.finalTime = finalTime;
        return this;
    }
    public PCB builder(){
        PCB pcb = new PCB();
        pcb.setPid(pid);
        pcb.setTotalRunTime(totalRunTime);
        pcb.setOcupyingDeviceType(ocupyingDeviceType);
        pcb.setStatus(status);
        pcb.setCommittedTime(committedTime);
        pcb.setContinueTime(continueTime);
        pcb.setFinalTime(finalTime);
        return pcb;
    }
}
