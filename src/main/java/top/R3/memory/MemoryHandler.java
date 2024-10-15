package top.R3.memory;

import javafx.collections.ObservableList;
import javafx.scene.layout.FlowPane;
import org.springframework.stereotype.Component;
import top.R3.controller.IndexController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * MemoryHandler 类负责处理内存相关的操作
 */
@Component
public class MemoryHandler {
    @Resource
    private IndexController indexController;
    
    // 内存表，记录每个内存块的占用状态
    public Map<Integer,Boolean> memoryTable = new HashMap<>(100);

    /**
     * 改变内存块的状态（申请或释放）
     * @param type 操作类型："apply" 或 "free"
     * @param is 要操作的内存块列表
     */
    private void change(String type, List<Integer> is){
        ObservableList<FlowPane> memories = indexController.memories;
        for(Integer i : is){
            int x = i/20;
            int y = i%20;
            if("apply".equals(type)){
                memories.get(x).getChildren().get(y).setStyle("-fx-fill: #f25555");
                memoryTable.put(i,true);
            }
            else if("free".equals(type)){
                memories.get(x).getChildren().get(y).setStyle("-fx-fill: #1e90ff");
                memoryTable.remove(i);
            }
        }
    }

    /**
     * 申请内存
     * @param memory 要申请的内存对象
     */
    public void apply(Memory memory){
        MemoryContainer.getMap().put(memory.getPid(), memory);
        List<Integer> memoryList = memory.getMemoryList();
        change("apply",memoryList);
    }

    /**
     * 释放内存
     * @param pid 要释放内存的进程ID
     */
    public void free(Integer pid){
        Memory memory = MemoryContainer.getMap().remove(pid);
        List<Integer> memoryList = memory.getMemoryList();
        change("free",memoryList);
    }

    /**
     * 查找并预分配空闲内存
     * @param memory 要分配的内存对象
     * @return 是否成功找到并预分配了足够的内存
     */
    public synchronized boolean findFreeMemory(Memory memory){
        ObservableList<FlowPane> memories = indexController.memories;
        for(int i=0;i<100;i++){
            if(memoryTable.get(i)==null||!memoryTable.get(i)){
                int x = i/20;
                int y = i%20;
                memories.get(x).getChildren().get(y);
                memory.getMemoryList().add(i);
                if(memory.getMemoryList().size()==memory.getMemorySize()){
                    break;
                }
            }else{
                memory.getMemoryList().clear();
            }
        }
        return memory.getMemoryList().size() == memory.getMemorySize();
    }
}
