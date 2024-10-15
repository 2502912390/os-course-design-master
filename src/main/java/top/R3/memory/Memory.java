package top.R3.memory;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;


@Data
public class Memory {
    private Integer pid;//对应进程
    private Integer memorySize;//大小
    private List<Integer> memoryList;//占用的内存块

    public Memory(Integer pid, Integer memorySize) {
        this.pid = pid;
        this.memorySize = memorySize;
        this.memoryList = new ArrayList<>();
    }
}
