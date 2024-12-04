package top.ComFive.filesys;


import javafx.scene.Node;
import javafx.scene.shape.Rectangle;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.ComFive.controller.FileSysController;
import top.ComFive.controller.IndexController;
import top.ComFive.domain.MyTreeItem;
import javax.annotation.Resource;

@Slf4j
@Component
public class FileSysHandler {
    private FileSysController fileSysController;

    @Autowired
    public void setFileSysController(FileSysController fileSysController){
        this.fileSysController=fileSysController;
    }

    // 查找对应的node强制转换为 Rectangle 并返回
    private Rectangle lookupById(Integer fid){
        Node lookup = fileSysController.getRectBox().lookup("#rect" + fid);//在可视化的rect中找对应的
        if(lookup==null){
            log.error("索引不在范围内");
            return null;
        }
        return (Rectangle) lookup;
    }

    @Resource
    private IndexController indexController;
    //根据磁盘下标 改变颜色
    public void changeColor(Integer fid,String appOrfree){
        if (fid==0||fid==1||fid==2){//0 1 2不改变颜色
            return;
        }
        if("apply".equals(appOrfree)){
            lookupById(fid).setStyle("-fx-fill: #676161;");
        }else if("free".equals(appOrfree)){
            lookupById(fid).setStyle("-fx-fill: #1eff31;");
        }
    }

    //设置Disk_id磁盘的下一块指向
    public void updateDiskTable(Integer Disk_id,Integer next){
        fileSysController.getDiskList().get(Disk_id).setNext(next);
        fileSysController.getDiskTable().refresh();
    }

    //创建一个新的文件或文件夹，并将其添加到父节点的子节点列表中
    public void creatFileOrDir(MyTreeItem parent,MyTreeItem children){//要看哪里调用了
        int fileSize=parent.getChildren().size();

        for(int i=0;i<fileSize;i++){
            MyTreeItem mt=(MyTreeItem) parent.getChildren().get(i);
            if(mt.getFileName().equals(children.getFileName())){
                indexController.console.setText(indexController.console.getText()+"\n已存在同名的文件或文件夹");
                return;
            }
        }

        parent.getChildren().add(children);
        indexController.console.setText(indexController.console.getText()+"\n创建完成");

        //为文件的各个块分配磁盘空间，并在磁盘表中标记这些块为使用中
        int[] t=children.getDistNumber(); //获取文件的编号
        t[t[0]+1]=255;//最后一块设置为255

        int diskLen=fileSysController.diskList.size();//总的磁盘数
        int diskIndex=3;

        for(int i=1;i<=t[0];i++){
            while (fileSysController.diskList.get(diskIndex).getNext()!=0&&diskIndex<diskLen){//从第三块开始遍历 找到空磁盘的下标
                diskIndex++;
            }
            t[i]=diskIndex++;
        }

        //遍历所有分配的块 更新磁盘表和文件的颜色标识
        for(int i=1;i<=t[0];i++){
            updateDiskTable(t[i],t[i+1]);
            changeColor(t[i],"apply");
        }
    }

    //查找target的父Item
    public MyTreeItem checkFile(MyTreeItem file_parent, String[] filepath, int now_layer){
        if((now_layer+1)==filepath.length){//表示路径已经完全匹配了 返回
            return file_parent;
        }
        int fileSize=file_parent.getChildren().size();
        for(int i=0;i<fileSize;i++){//深度优先搜索递归查找
            MyTreeItem mt = (MyTreeItem) file_parent.getChildren().get(i);
            if(mt.getFileName().equals(filepath[now_layer])){
                if(mt.isLeaf()){
                    return null;
                }
                //递归查找
                return checkFile(mt,filepath,now_layer+1);
            }
        }
        return null;
    }

    //检查并删除root下指定的文件或文件夹
    public void checkDeleteFile(MyTreeItem root,String str,int type){//父节点 文件名 类型
        int mtlen=root.getChildren().size();

        for(int i=0;i<mtlen;i++){
            MyTreeItem mt=(MyTreeItem) root.getChildren().get(i);
            if(mt.getFileName().equals(str)){
                if(mt.getDistNumber()[0]==type){//检查文件名和磁盘类型
                    root.getChildren().remove(i); //在树中删除

                    int[] t=mt.getDistNumber();//改文件占用的磁盘下标
                    for(int j=1;j<t[0]+1;j++){//更新磁盘表
                        updateDiskTable(t[j],0);
                        changeColor(t[j],"free");
                    }

                    //删除该目录下的所有文件
                    deleteFile(mt);
                    indexController.console.setText(indexController.console.getText()+"\n删除成功");
                    break;
                }
                else indexController.console.setText(indexController.console.getText()+"\n请输入正确的路径");
            }
        }
    }

    //递归删除目录及其包含的文件的（主要是释放磁盘块）
    public void deleteFile(MyTreeItem root){
        int mtlen=root.getChildren().size();
        if(mtlen==0){
            return;
        }
        else{
            for(int i=0;i<mtlen;i++){
                MyTreeItem mt=(MyTreeItem) root.getChildren().get(i);
                //释放磁盘块
                int[] t=mt.getDistNumber();
                for(int j=1;j<t[0]+1;j++){
                    updateDiskTable(t[j],0);
                    changeColor(t[j],"free");
                }
                //递归查找删除
                deleteFile(mt);
            }
        }
        return;
    }

}
