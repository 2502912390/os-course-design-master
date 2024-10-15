package top.R3.memory;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author lemon
 * @Date 2022/10/24 11:48
 * @Version 1.0
 */
public class MemoryContainer {
    public static final Map<Integer,Memory> memoryMap = new HashMap<>();

    public static Map<Integer, Memory> getMap() {
        return memoryMap;
    }

    public void putMemory(Integer id,Memory memory) {
        memoryMap.put(id,memory);
    }
}
