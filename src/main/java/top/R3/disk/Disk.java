package top.R3.disk;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

/**
 * Disk 类表示磁盘的一个块
 * 使用 JavaFX 的属性来支持 UI 绑定
 * 每个disk要有64字节
 */
public class Disk {
    private Integer id;  // 磁盘块的唯一标识符
    private IntegerProperty num;  // 磁盘（矩形块）编号
    private IntegerProperty next;  // 磁盘指向，-1为结束，0表示空磁盘

    /**
     * 构造函数
     * @param id 磁盘块的唯一标识符
     * @param num 磁盘块编号
     */
    public Disk(Integer id, Integer num) {//num会根据排序规则而改变
        this.id = id;
        this.num = new SimpleIntegerProperty(num);
        this.next = new SimpleIntegerProperty();
    }

    /**
     * 设置下一个磁盘块的指向
     * @param next 下一个磁盘块的编号
     */
    public void setNext(int next) {
        this.next.set(next);
    }

    /**
     * 设置磁盘块编号
     * @param num 新的磁盘块编号
     */
    public void setNum(int num) {
        this.num.set(num);
    }

    /**
     * 获取下一个磁盘块的指向
     * @return 下一个磁盘块的编号
     */
    public int getNext() {
        return next.get();
    }

    /**
     * 获取磁盘块编号
     * @return 磁盘块编号
     */
    public int getNum() {
        return num.get();
    }
}
