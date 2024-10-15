package top.R3.filesys;

import lombok.Data;
@Data
public class File {
    private FCB fcb; //记录文件的存放位置
    private String type;//文件类型，目录或文件
    private String fname;//文件名字
}
