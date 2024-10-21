package top.R3.controller;

// 导入所需的类和包
import de.felixroske.jfxsupport.FXMLController;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import lombok.extern.slf4j.Slf4j;
import top.R3.device.Device;
import top.R3.device.DeviceHandler;
import top.R3.domain.MyTreeItem;
import top.R3.filesys.FileSysHandler;
import top.R3.memory.Memory;
import top.R3.memory.MemoryHandler;
import top.R3.process.PCB;
import top.R3.process.PCBBuilder;
import top.R3.process.ProcessHandler;
import top.R3.view.CreateProcessView;
import top.R3.view.MemoryView;
import top.R3.process.Process;
import javax.annotation.Resource;
import java.util.*;

// 使用FXMLController注解标记这是一个JavaFX控制器类
@FXMLController
// 使用Slf4j注解添加日志功能
@Slf4j
public class IndexController {
    // 注入其他组件和视图
    @Resource
    private MemoryView memoryStage;
    @Resource
    private CreateProcessView createProcessStage;

    // FXML注解标记的UI组件，与FXML文件中的元素对应
    @FXML
    public Button start;//开始
    @FXML
    public Button stop;//暂停
    @FXML
    public Button create;//新建
    @FXML
    public Button restart;//重置

    @FXML
    public Label time;//显示的时间
    @FXML
    private TreeView<String> treeView;

    @FXML
    public ChoiceBox<Integer> cpuTimeChoiceBox;

    //进程区域
    @Resource
    private ProcessHandler processHandler;//CPU处理器，封装了数据表增删改查方法
    // 定义各种进程队列
    private final ObservableList<Process> waitTable = FXCollections.observableArrayList();//等待队列数据表
    private final ObservableList<Process> readyTable = FXCollections.observableArrayList();//就绪队列数据表
    private final ObservableList<Process> exeTable = FXCollections.observableArrayList();//执行队列数据表
    private final ObservableList<Process> finishTable = FXCollections.observableArrayList();//完成队列数据表
    
    // 定义各种TableView和对应的列
    @FXML
    private TableView<Process> waitQueue;//等待队列
    public TableColumn<Process, IntegerProperty> waitPid;
    public TableColumn<Process, IntegerProperty> waitTotalTimeCol;
    public TableColumn<Process, StringProperty>  waitDev;
    public TableColumn<Process, StringProperty>  waitStatus;
    public TableColumn<Process, IntegerProperty> waitCommitTime;
    public TableColumn<Process, IntegerProperty> waitContinueTime;
    public TableColumn<Process, IntegerProperty> waitFinishedTime;
    
    @FXML
    private TableView<Process> readyQueue;//就绪队列
    public TableColumn<Process, IntegerProperty>  readyPid;
    public TableColumn<Process, IntegerProperty> readyTotalTimeCol;
    public TableColumn<Process, StringProperty> readyDev;
    public TableColumn<Process, StringProperty> readyStatus;
    public TableColumn<Process, IntegerProperty> readyCommitTime;
    public TableColumn<Process, IntegerProperty> readyContinueTime;
    public TableColumn<Process, IntegerProperty> readyFinishedTime;
    
    @FXML
    private TableView<Process> executeQueue;//执行队列
    public TableColumn<Process, IntegerProperty> exePid;
    public TableColumn<Process, IntegerProperty> exeTotalTimeCol;
    public TableColumn<Process, StringProperty> exeDev;
    public TableColumn<Process, StringProperty> exeStatus;
    public TableColumn<Process, IntegerProperty> exeCommitTime;
    public TableColumn<Process, IntegerProperty> exeContinueTime;
    public TableColumn<Process, IntegerProperty> exeFinishedTime;
    
    @FXML
    private TableView<Process> finishQueue;//完成队列
    public TableColumn<Process, IntegerProperty> finishPid;
    public TableColumn<Process, IntegerProperty> finishTotalTimeCol;
    public TableColumn<Process, StringProperty> finishDev;
    public TableColumn<Process, StringProperty> finishStatus;
    public TableColumn<Process, IntegerProperty> finishCommitTime;
    public TableColumn<Process, IntegerProperty> finishContinueTime;
    public TableColumn<Process, IntegerProperty> finishFinishedTime;
    
    //设备处理器
    @Resource
    private DeviceHandler deviceHandler;
    @FXML
    public TableView<Device> deviceQueue;
    @FXML
    public TableColumn<Device,StringProperty> deviceName;
    @FXML
    public TableColumn<Device,IntegerProperty> deviceOccupyPid;
    
    private  Integer timeNum = 0;//操作系统运行的（当前）时间

    private final Timeline timeline = new Timeline();
    public Integer auto_add_pid = 1;//自动创建的进程id

    private Integer cpuTime = 0;// cpu运行时间
    private Integer cTime = 4;//时间片
    
    //磁盘区域
    @FXML
    private TextField command;//命令输入窗口
    @FXML
    public TextArea console;//控制台显示
    private MyTreeItem root;
    @Resource
    private FileSysHandler fileSysHandler;
    @Resource
    private FileSysController fileSysController;
    
    //内存模块
    @FXML
    public AnchorPane memoryBox;//子模块为FlowPane

    @FXML
    public ProgressBar memoryBar;
    public ObservableList<FlowPane> memories = FXCollections.observableArrayList();//内存的存储空间的图形化
    public Text memoryText;
    @Resource
    private MemoryHandler memoryHandler;

    // 初始化函数，窗口打开后自动执行
    public void initialize() {
        // 初始化文件系统树结构
        root = new MyTreeItem(true,"root",2);
        treeView.setRoot(root);
        MyTreeItem C = new MyTreeItem(true,"C",2);

        //至少要包含 5 个目录和 15 个文件。
        MyTreeItem mt1 =new MyTreeItem(false,"demo1.e",1);
        MyTreeItem mt2 =new MyTreeItem(false,"demo2.e",1);
        MyTreeItem mt3 =new MyTreeItem(false,"demo3.e",1);
        MyTreeItem mt4 =new MyTreeItem(false,"demo4.e",1);
        MyTreeItem mt5 =new MyTreeItem(false,"demo5.e",1);
        hashMap.put("\\C\\demo1.e","");
        hashMap.put("\\C\\demo2.e","");
        hashMap.put("\\C\\demo3.e","");
        hashMap.put("\\C\\demo4.e","");
        hashMap.put("\\C\\demo5.e","");//没有占用磁盘？？？
        C.getChildren().addAll(mt1,mt2,mt3,mt4,mt5);//作为其子节点

        root.getChildren().addAll(C);
        console.setText("欢迎使用");
        memoryBar.setProgress(0.05);// 设置进度条为 5%

        // 设置CPU时间片选项
        cpuTimeChoiceBox.setItems(FXCollections.observableArrayList(4, 6, 8));
        
        // 绑定进程表格列与进程属性
        processHandler.bindProperties(readyPid, readyTotalTimeCol, readyDev, readyStatus, readyCommitTime, readyContinueTime, readyFinishedTime);
        processHandler.bindProperties(exePid,exeTotalTimeCol,exeDev,exeStatus,exeCommitTime,exeContinueTime,exeFinishedTime);
        processHandler.bindProperties(waitPid, waitTotalTimeCol, waitDev, waitStatus, waitCommitTime, waitContinueTime, waitFinishedTime);
        processHandler.bindProperties(finishPid, finishTotalTimeCol, finishDev, finishStatus, finishCommitTime, finishContinueTime, finishFinishedTime);
        
        // 设置各队列的数据源
        readyQueue.setItems(readyTable);
        executeQueue.setItems(exeTable);
        waitQueue.setItems(waitTable);
        finishQueue.setItems(finishTable);
        
        // 设置设备表格
        deviceHandler.bindProperties(deviceQueue,deviceOccupyPid,deviceName);
        
        // 设置时间线
        timeline.setCycleCount(Integer.MAX_VALUE);
        timeline.getKeyFrames().addAll(clockRun());
        
        // 初始化内存显示
        ObservableList<Node> children = memoryBox.getChildren();
        for(Node node : children){
            if(node instanceof FlowPane)
                memories.add((FlowPane)node);
        }
        
        // 内存分配测试
        Memory memory = new Memory(1, 5);
        memoryHandler.findFreeMemory(memory);
        memoryHandler.apply(memory);
    }

    /** 
     * 时钟帧每秒执行一次
     * 返回一个KeyFrame对象，定义了每秒执行的操作
     */
    private KeyFrame clockRun(){
        return new KeyFrame(Duration.seconds(1),"clock",event->{
            // 更新时间和CPU时间
            timeNum++;
            cpuTime++;
            time.setText(timeNum+"");
            
            // 每两秒创建一个新进程
            if(timeNum%2==0){
                Random random = new Random();
                int i = random.nextInt(3);
                createProcess( ++auto_add_pid,str.charAt(i)+"",random.nextInt(7)+2 );
            }
            
            // 刷新所有队列显示
            readyQueue.refresh();
            executeQueue.refresh();
            waitQueue.refresh();
            finishQueue.refresh();
            deviceQueue.refresh();

            // 执行CPU调度
            cpu();
        });
    }

    /**
     * CPU调度方法
     * 处理进程的执行、完成和时间片轮转
     */
    String str = "ABC";

    //重写 进程创建 撤销 阻塞 唤醒
    private void cpu() {//时间片要为5
        // 如果执行队列为空，从就绪队列调度进程
        if(executeQueue.getItems().size()==0){
            if(readyQueue.getItems().size()==0) return ;//执行和就绪队列都没有 则结束cpu调度
            Process process = this.readyQueue.getItems().get(0);
            processHandler.moveProcessFromReadyToExecute(process,readyTable,exeTable);//就绪-》执行
        }
        
        // 获取当前执行的进程
        Process process = this.executeQueue.getItems().get(0);
        process.setContinueTime(process.getContinueTime()+1);
        
        // 检查进程是否完成
        if(process.getContinueTime() >= process.getTotalRunTime()){
            log.info("进程完成");
            process.setFinalTime(timeNum);
            String type = process.getOcupyingDeviceType();
            log.info("type："+type);
            
            // 释放设备并移动进程到完成队列
            if(process.getDevice()==null){
                log.error("完成进程的设备为空");
                return;
            }
            deviceHandler.releaseDevice(process.getDevice());
            processHandler.moveProcessFromExecuteToFinish(process,exeTable,finishTable);

            // 释放内存
            memoryHandler.free(process.getId());

            // 尝试唤醒等待队列中的进程
            Process waitProcess = processHandler.getWaitProcess(waitTable,type);//释放设备之后可以唤醒阻塞的进程
            if(waitProcess !=null){
                Device device = deviceHandler.ocupyDevice(waitProcess.getOcupyingDeviceType(), waitProcess.getId());
                if(device!=null){
                    waitProcess.setDevice(device);
                    processHandler.moveProcessFromWaitToReady(waitProcess,waitTable,readyTable);//阻塞-》就绪
                }
            }

            cpuTime = 0;//完成任务之后 运行时间归0
        }
        
        // 更新内存使用情况显示
        int size = memoryHandler.memoryTable.size();//不对吧？ 可能是有编号 但是没被占用？？？
        log.info("内存{}",size);
        memoryText.setText(String.valueOf(size)+"%");
        memoryBar.setProgress(size/100.0);
        

        if(cpuTime >= cTime){// 时间片轮转 大于时间片 当前进程进入就绪状态
            processHandler.moveProcessFromExecuteToReady(process,exeTable,readyTable);
            cpuTime = 0;
        }
    }

    /**
     * 创建新进程并添加到适当的队列
     */
    public void createProcess(int pid,String type,int totalTime){
        if(waitTable.size()>=6){
            return;
        }
        // 创建PCB和进程对象
        PCB pcb = new PCBBuilder()
                .pid(pid)
                .ocupyingDeviceType(type)
                .status("阻塞")//创建初为阻塞态
                .committedTime(timeNum)
                .continueTime(0)
                .totalRunTime(totalTime)
                .builder();

        Process process = new Process(pcb);

        // 分配内存
        int memorySize=new Random().nextInt(9);
        Memory memory=new Memory(pid,memorySize+1);
        boolean isSuccess = memoryHandler.findFreeMemory(memory);
        if(!isSuccess){
            log.info("内存不足");
            return;
        }
        memoryHandler.apply(memory);

        // 尝试分配设备
        Device device = deviceHandler.ocupyDevice(type, pid);
        if(device!=null){//分配设备成功 则是就绪 否则是等待
            process.setStatus("就绪态");
            processHandler.addItemInList(readyTable,process);
            process.setDevice(device);
        }else{
            processHandler.addItemInList(waitTable,process);
        }
    }

    /**
     * 内存管理方法（待实现）
     */
    private void memory(){
        // TODO: 实现内存管理逻辑
    }

    /**
     * 跳转到帮助界面（待实现）
     */
    public void toHelp(MouseEvent mouseEvent) {
        // TODO: 实现帮助界面跳转逻辑
    }

    /**
     * 处理用户输入的命令
     */
    String[] order={"create","delete","rmdir","copy","mkdir","deldir","read","write"};
    Map<String, String> hashMap = new HashMap<>();//存放树形结构的文件
    public void cmd(KeyEvent keyEvent) {
        // 检查是否按下Enter键
        if(keyEvent.getCode().getName().equalsIgnoreCase("enter")){
            String input=command.getText();
            input = input.replaceAll("\\n","");//删除换行符
            long spaceCount = input.chars().filter(Character::isWhitespace).count();//统计空白字符的个数
            String[] str=input.split("\\\\");//分割
            command.clear();

            int index=-1;//对应命令的下标
            // 识别命令类型
            for(int i=0;i<order.length;i++){
                if(str[0].trim().equals(order[i])){
                    index=i;
                    break;
                }
            }
            
            int strLen= str.length;
            if(strLen<=1){
                console.setText(console.getText()+"\n请输入正确的路径1");
                return;
            }
            
            // 根据命令类型执行相应操作
            switch (index){
                case 0:
                    // 创建文件
                    String[] str13=input.split(" ");
                    String str14=str13[str13.length-1];
                    hashMap.put(str14,"");
                    cmdCreate(str,1,false);
                    break;
                case 1:
                    // 删除文件
                    cmdDelete(str,1);
                    String[] str7=input.split(" ");
                    String str8=str7[str7.length-1];
                    hashMap.remove(str8);
                    break;
                case 2:
                    // 删除空目录
                    MyTreeItem parentmt=fileSysHandler.checkFile(root,str,1);
                    int fileSize=parentmt.getChildren().size();
                    for(int i=0;i<fileSize;i++){
                        MyTreeItem mt = (MyTreeItem) parentmt.getChildren().get(i);
                        if(mt.getFileName().equals(str[strLen-1])){
                            if(mt.getChildren().size()==0){
                                cmdDelete(str,2);
                            }
                            else console.setText(console.getText()+"\n目录不为空！");
                        }
                    }
                    break;
                case 3:
                    // 复制文件
                    if(spaceCount==1) {
                        String newstr = new String();
                        String[] str50=input.split(" ");
                        String str51=str50[str50.length-1]; //旧
                        newstr += str[0] + "\\";
                        newstr += str[strLen-1];
                        String str40="\\"+str[strLen-1];
                        System.out.println(str40);
                        str = newstr.split("\\\\");
                        hashMap.put(str40,hashMap.get(str51));
                        cmdCreate(str, 1, false);
                    }
                    if (spaceCount==2) {
                        String newstr = new String();
                        String[] str30=input.split(" ");
                        String str31=str30[str30.length-1]; //旧
                        String str32=str30[str30.length-2]; //新
                        newstr += str[0];
                        newstr+=str32;
                        newstr+="\\";
                        newstr+=str[strLen-1];
                        str = newstr.split("\\\\");
                        hashMap.put(str32,hashMap.get(str31));
                        cmdCreate(str, 1, false);
                    }
                    break;
                case 4:
                    // 创建目录
                    cmdCreate(str,2,true);
                    break;
                case 5:
                    // 删除目录（包括非空目录）
                    String[] str11=input.split(" ");
                    String str12=str11[str11.length-1];
                    cmdDelete(str,2);
                    Set<String> set=hashMap.keySet();
                    for(String s:set){
                        if(s.contains(str12)){
                            hashMap.remove(s);
                        }
                    }
                    break;
                case 6://不用
                    // 读取文件内容
                    String[] str4=input.split(" ");
                    String str5=str4[str4.length-1];
                    if(hashMap.containsKey(str5)){
                        String readstr=hashMap.get(str5);
                        Stage primaryStage=new Stage();
                        primaryStage.setWidth(400);
                        primaryStage.setHeight(335);
                        primaryStage.setTitle("写字板");

                        TextArea textarea = new TextArea();
                        textarea.setPrefWidth(400);
                        textarea.setPrefHeight(330);
                        textarea.setWrapText(true);
                        textarea.setEditable(false);
                        textarea.setText(readstr);

                        VBox layout = new VBox();
                        layout.setPrefWidth(400);
                        layout.setPrefHeight(330);
                        layout.getChildren().add(textarea);

                        Scene scene = new Scene(layout);
                        primaryStage.setScene(scene);
                        primaryStage.show();
                    }else{
                        console.setText(console.getText()+"\n该文件不存在");
                    }
                    break;
                case 7://不用
                    // 写入文件内容
                    String[] str3=input.split(" ");
                    String str2=str3[str3.length-1];
                    if(hashMap.containsKey(str2)){
                        Stage primaryStage1=new Stage();
                        primaryStage1.setTitle("写字板");
                        primaryStage1.setWidth(400);
                        primaryStage1.setHeight(335);

                        TextArea textArea1 = new TextArea();
                        textArea1.setPrefWidth(400);
                        textArea1.setPrefHeight(300);
                        textArea1.setWrapText(true);
                        String userInput="";
                        Button saveButton1 = new Button("保存");
                        saveButton1.setOnAction(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent event) {
                                String u=textArea1.getText();
                                hashMap.put(str2,u);
                                primaryStage1.close();
                            }
                        });
                        Button saveButton3 = new Button("取消");
                        saveButton3.setOnAction(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent event) {
                                String u="";
                                hashMap.put(str2,u);
                                primaryStage1.close();
                            }
                        });

                        VBox vbox1=new VBox(5);

                        VBox layout5 = new VBox();
                        layout5.setPrefWidth(400);
                        layout5.setPrefHeight(300);
                        layout5.getChildren().add(textArea1);

                        HBox layout6 = new HBox(304);
                        layout6.setPrefWidth(400);
                        layout6.setPrefHeight(30);
                        layout6.getChildren().addAll(saveButton1,saveButton3);
                        vbox1.getChildren().addAll(layout5,layout6);

                        Scene scene1 = new Scene(vbox1);
                        primaryStage1.setScene(scene1);
                        primaryStage1.show();
                    }else{
                        console.setText(console.getText()+"\n该文件不存在");
                    }
                    break;
                default:
                    console.setText(console.getText()+"\n输入错误");
            }
        }
    }

    /**
     * 创建文件或目录
     */
    public void cmdCreate(String[] str,int type,boolean isDir){
        int strLen= str.length;
        MyTreeItem mt =new MyTreeItem(isDir,str[strLen-1],type);
        MyTreeItem parentmt =fileSysHandler.checkFile(root,str,1);
        if(parentmt ==null){
            console.setText(console.getText()+"\n请输入正确的路径2");
        }
        else fileSysHandler.creatFileOrDir(parentmt, mt);
    }

    /**
     * 删除文件或目录
     */
    public void cmdDelete(String[] str,int type){
        int strLen= str.length;
        MyTreeItem parentmt =fileSysHandler.checkFile(root,str,1);
        if(parentmt ==null){
            console.setText(console.getText()+"\n请输入正确的路径");
        }
        else{
            fileSysHandler.checkDeleteFile(parentmt,str[strLen-1],type);
        }
    }

    /**
     * 跳转到内存视图
     */
    public void toMemory(MouseEvent mouseEvent) {
        Stage stage = new Stage();
        stage.setScene(memoryStage.getScene());
        stage.show();
    }

    @Resource
    private CreateProcessController createProcessController;

    /**
     * 打开创建进程的窗口
     */
    public void onCreateProcess(MouseEvent mouseEvent) {
        Stage stage = new Stage();
        stage.setScene(createProcessStage.getScene());
        createProcessController.setStage(stage);
        stage.show();
    }

    /**
     * 重置系统（待实现）
     */
    public void onReset(MouseEvent mouseEvent) {
        // TODO: 实现重置逻辑
    }

    /**
     * 开始系统运行
     */
    public void onStart(MouseEvent mouseEvent) {
        if(cpuTimeChoiceBox.getValue()!=null){
            cTime = cpuTimeChoiceBox.getValue();
            log.info("当前时间片为：{}",cTime);
        }
        timeline.play();//开始运行
    }

    /**
     * 暂停系统运行
     */
    public void onStop(MouseEvent mouseEvent) {
        timeline.pause();
    }
}