package top.ComFive.memory;

import javafx.collections.ObservableList;
import javafx.scene.layout.FlowPane;
import org.springframework.stereotype.Component;
import top.ComFive.controller.IndexController;

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
    
    // 内存表，记录每个内存块的占用状态，键为内存块编号，值为是否被占用
    public Map<Integer,Boolean> memoryTable = new HashMap<>(100);

    /**
     * 改变内存块的状态（申请或释放）
     * @param type 操作类型："apply" 表示申请，"free" 表示释放
     * @param mems 要操作的内存块列表
     */
    private void change(String type, List<Integer> mems){
        ObservableList<FlowPane> memories = indexController.memories;
        for(Integer i : mems){
            int x = i/20;  // 计算行索引
            int y = i%20;  // 计算列索引
            if("apply".equals(type)){
                // 申请内存时，将对应的UI元素设置为红色，并在内存表中标记为已占用
                memories.get(x).getChildren().get(y).setStyle("-fx-fill: #cbff11");//FlowPane对应地方设置为红色
                memoryTable.put(i,true);
            }
            else if("free".equals(type)){
                // 释放内存时，将对应的UI元素设置为蓝色，并从内存表中移除
                memories.get(x).getChildren().get(y).setStyle("-fx-fill: #00a4fd");
                memoryTable.remove(i);
            }
        }
    }

    /**
     * 申请内存
     * @param memory 要申请的内存对象
     */
    public void apply(Memory memory){
        // 将内存对象添加到MemoryContainer中
        MemoryContainer.getMap().put(memory.getPid(), memory);
        List<Integer> memoryList = memory.getMemoryList();
        // 更新内存状态为已申请
        change("apply",memoryList);
    }

    /**
     * 释放内存
     * @param pid 要释放内存的进程ID
     */
    public void free(Integer pid){
        // 从MemoryContainer中移除对应的内存对象
        Memory memory = MemoryContainer.getMap().remove(pid);
        List<Integer> memoryList = memory.getMemoryList();
        // 更新内存状态为已释放
        change("free",memoryList);
    }

    /**
     * 查找并预分配空闲内存
     * @param memory 要分配的内存对象
     * @return 是否成功找到并预分配了足够的内存
     */
    public synchronized boolean findFreeMemory(Memory memory){
        ObservableList<FlowPane> memories = indexController.memories;
        boolean Findfree = false;
        for(int i=0;i<100;i++){//遍历所有内存块
            if(memoryTable.get(i)==null||!memoryTable.get(i)){
                // 如果内存块未被占用
                int x = i/20;//行索引
                int y = i%20;//列索引
                memories.get(x).getChildren().get(y);//不进行设置？？？

                // 将该内存块添加到内存对象的列表中
                memory.getMemoryList().add(i);
                if(memory.getMemoryList().size()==memory.getMemorySize()){
                    // 如果已找到足够的内存块，结束搜索
                    Findfree=true;
                    break;
                }
            }else{//保证内存块的连续性？
                // 如果遇到已占用的内存块，清空已收集的内存块列表，重新开始搜索
                memory.getMemoryList().clear();
            }
        }
        // 返回是否成功找到足够的内存块
        return Findfree;
    }
}