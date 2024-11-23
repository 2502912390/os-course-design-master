package top.ComFive.process;

/**
 * PCBBuilder 类用于构建进程控制块（PCB）对象
 */
public class PCBBuilder {
    private Integer pid = -1;                // 进程ID
    private Integer totalRunTime = -1;       // 总运行时间
    private String ocupyingDeviceType = "";  // 占用的设备类型
    private String status = "就绪";          // 进程状态，默认为"就绪"
    private Integer committedTime = -1;      // 提交时间
    private Integer continueTime = -1;       // 持续时间
    private Integer finalTime = -1;          // 结束时间

    /**
     * 设置进程ID
     * @param pid 进程ID
     * @return 返回当前PCBBuilder对象，支持链式调用
     */
    public PCBBuilder pid(Integer pid) {
        this.pid = pid;
        return this;
    }

    /**
     * 设置总运行时间
     * @param totalRunTime 总运行时间
     * @return 返回当前PCBBuilder对象，支持链式调用
     */
    public PCBBuilder totalRunTime(Integer totalRunTime) {
        this.totalRunTime = totalRunTime;
        return this;
    }

    /**
     * 设置占用的设备类型
     * @param ocupyingDeviceType 占用的设备类型
     * @return 返回当前PCBBuilder对象，支持链式调用
     */
    public PCBBuilder ocupyingDeviceType(String ocupyingDeviceType) {
        this.ocupyingDeviceType = ocupyingDeviceType;
        return this;
    }

    /**
     * 设置进程状态
     * @param status 进程状态
     * @return 返回当前PCBBuilder对象，支持链式调用
     */
    public PCBBuilder status(String status) {
        this.status = status;
        return this;
    }

    /**
     * 设置提交时间
     * @param committedTime 提交时间
     * @return 返回当前PCBBuilder对象，支持链式调用
     */
    public PCBBuilder committedTime(Integer committedTime) {
        this.committedTime = committedTime;
        return this;
    }

    /**
     * 设置持续时间
     * @param continueTime 持续时间
     * @return 返回当前PCBBuilder对象，支持链式调用
     */
    public PCBBuilder continueTime(Integer continueTime) {
        this.continueTime = continueTime;
        return this;
    }

    /**
     * 设置结束时间
     * @param finalTime 结束时间
     * @return 返回当前PCBBuilder对象，支持链式调用
     */
    public PCBBuilder finalTime(Integer finalTime) {
        this.finalTime = finalTime;
        return this;
    }

    /**
     * 构建PCB对象
     * @return 返回构建好的PCB对象
     */
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