package top.ComFive.memory;

import java.util.HashMap;
import java.util.Map;


public class MemoryContainer {
    // 静态Map，用于存储内存对象，键为Integer类型的ID，值为Memory对象
    public static final Map<Integer,Memory> memoryMap = new HashMap<>();

    /**
     * 获取内存映射表
     * @return 返回存储内存对象的Map
     */
    public static Map<Integer, Memory> getMap() {
        return memoryMap;
    }

    /**
     * 将内存对象添加到映射表中
     * @param id 内存对象的ID
     * @param memory 要添加的Memory对象
     */
    public void putMemory(Integer id,Memory memory) {
        memoryMap.put(id,memory);
    }
}