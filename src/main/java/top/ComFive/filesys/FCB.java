//package top.ComFive.filesys;
//
//import lombok.Data;
//import org.assertj.core.util.Lists;
//
//import java.util.List;
//
//// 文件控制块(File Control Block)
//@Data
//public class FCB {
//    private Integer start; // 文件在磁盘存放的起始位置
//    private Integer Long;  // 记录该文件的长度
//
//    // 记录该文件分配到的磁盘块id列表
//    private List<Integer> list = Lists.newArrayList();
//
//    /**
//     * 构造函数
//     * @param start 文件起始位置
//     * @param aLong 文件长度
//     * @param list 分配的磁盘块id列表
//     */
//    public FCB(Integer start, Integer aLong, List<Integer> list) {
//        this.start = start;
//        this.Long = aLong;
//        this.list = list;
//    }
//}