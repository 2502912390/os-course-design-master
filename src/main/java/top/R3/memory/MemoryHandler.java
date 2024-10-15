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
 * @Author lemon
 * @Date 2022/10/24 10:11
 * @Version 1.0
 */
@Component
public class MemoryHandler {
    @Resource
    private IndexController indexController;
    public Map<Integer,Boolean> memoryTable = new HashMap<>(100);//内存表，占用的内存id ----> 是否被占用

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
    public void apply(Memory memory){
        MemoryContainer.getMap().put(memory.getPid(), memory);
        List<Integer> memoryList = memory.getMemoryList();
        change("apply",memoryList);
    }
    public void free(Integer pid){
        Memory memory = MemoryContainer.getMap().remove(pid);
        List<Integer> memoryList = memory.getMemoryList();

        change("free",memoryList);
    }
    //查找并预分配
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
