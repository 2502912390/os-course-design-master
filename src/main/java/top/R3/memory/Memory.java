package top.R3.memory;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

// 使用Lombok的@Data注解自动生成getter、setter、equals、hashCode和toString方法
@Data
public class Memory {
    private Integer pid;        // 对应进程的ID
    private Integer memorySize; // 内存大小
    private List<Integer> memoryList; // 存储占用的内存块列表

    // 构造函数
    public Memory(Integer pid, Integer memorySize) {
        this.pid = pid;
        this.memorySize = memorySize;
        this.memoryList = new ArrayList<>(); // 初始化内存块列表
    }
}