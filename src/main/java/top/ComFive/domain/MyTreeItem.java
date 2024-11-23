package top.ComFive.domain;

import javafx.scene.control.TreeItem;
import lombok.Data;

import java.io.Serializable;

// 使用Lombok的@Data注解自动生成getter、setter、equals、hashCode和toString方法
@Data
// MyTreeItem类继承自JavaFX的TreeItem类，并实现Serializable接口以支持序列化
public class MyTreeItem extends TreeItem<String> implements Serializable {
    // 序列化版本UID，用于确保序列化的兼容性
    private static final long serialVersionUID = -154223199645632L;

    // 标识是否为目录
    private boolean isDir;
    // 文件名
    private String filename;
    // 文件编号数组
    private int[] distNumber;//？？？

    // 构造函数
    public MyTreeItem(boolean isDir, String str, int len){//len用来区分是文件/文件夹
        // 调用父类TreeItem的构造函数，设置显示的文本
        super(str);
        // 设置是否为目录
        this.isDir = isDir;
        // 设置文件名
        this.filename = str;

        // 初始化文件编号数组 存储对应的磁盘下标
        this.distNumber = new int[6]; // 注：这里固定为6，根据实际需求调整
        // 设置数组的第一个元素为len，可能用于记录数组的有效长度
        this.distNumber[0] = len;
    }

    // 获取文件名的方法（虽然@Data注解已经生成了getter，这里可能是为了特殊处理）
    public String getFileName(){
        return this.filename;
    }

    // 获取文件占用的对应的磁盘下标
    public int[] getDistNumber(){
        return this.distNumber;
    }

    // 重写isLeaf方法，用于判断是否为叶子节点（即文件）
    @Override
    public boolean isLeaf() {
        // 如果不是目录，则是叶子节点（文件）
        return !isDir;
    }
}