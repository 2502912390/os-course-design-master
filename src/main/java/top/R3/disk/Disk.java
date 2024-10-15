package top.R3.disk;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class Disk {
    private Integer id;
    private IntegerProperty num;//磁盘（矩形块）编号
    private IntegerProperty next;//磁盘指向，-1为结束 为0表示空磁盘

    public Disk(Integer id,Integer num) {
        this.id = id;
        this.num = new SimpleIntegerProperty(num);
        this.next = new SimpleIntegerProperty();
    }

    public void setNext(int next) {
        this.next.set(next);
    }

    public void setNum(int num) {
        this.num.set(num);
    }

    public int getNext() {
        return next.get();
    }

    public int getNum() {
        return num.get();
    }
}
