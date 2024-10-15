package top.R3.filesys;

import lombok.Data;
import org.assertj.core.util.Lists;

import java.util.List;

//磁盘分配表
@Data
public class FCB {
    private Integer start; //文件在磁盘存放的起始位置
    private Integer Long; //记录该文件的长度

    //记录该文件分配到的磁盘id
    private List<Integer> list = Lists.newArrayList();

    public FCB(Integer start, Integer aLong, List<Integer> list) {
        this.start = start;
        this.Long = aLong;
        this.list = list;
    }
}
