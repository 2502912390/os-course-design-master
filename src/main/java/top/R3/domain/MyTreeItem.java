package top.R3.domain;

import javafx.scene.control.TreeItem;
import lombok.Data;
import top.R3.filesys.File;

import java.io.Serializable;

@Data
public class MyTreeItem extends TreeItem<String> implements Serializable {
    private static final long serialVersionUID = -154223199645632L;
    //是否是目录
    private boolean isDir;
    private String filename;//文件名
    private int[] distNumber;//文件编号

    public MyTreeItem(boolean isDir, String str,int len){
        super(str);
        this.isDir = isDir;
        this.filename=str;
        this.distNumber=new int[6];//？？？
//        this.distNumber=new int[len];
        //数组第一个元素是数组的长度
        this.distNumber[0]=len;
    }

    public String getFileName(){
        return this.filename;
    }

    public int[] getDistNumber(){
        return this.distNumber;
    }

    @Override
    //是否是文件
    public boolean isLeaf() {
        return !isDir;
    }
}
