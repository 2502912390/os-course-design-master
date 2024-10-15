package top.R3.controller;

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

@FXMLController
@Slf4j
public class IndexController {
    @Resource
    private MemoryView memoryStage;
    @Resource
    private CreateProcessView createProcessStage;

    @FXML
    public Button start;//开始
    @FXML
    public Button stop;//暂停
    @FXML
    public Button create;//新建
    @FXML
    public Button restart;//重置

    @FXML
    public Label time;
    @FXML
    private TreeView<String> treeView;

    @FXML
    public ChoiceBox<Integer> cpuTimeChoiceBox;

    //进程区域
    @Resource
    private ProcessHandler processHandler;//CPU处理器，封装了数据表增删改查方法
    private final ObservableList<Process> waitTable = FXCollections.observableArrayList();//等待队列数据表
    private final ObservableList<Process> readyTable = FXCollections.observableArrayList();//
    private final ObservableList<Process> exeTable = FXCollections.observableArrayList();//
    private final ObservableList<Process> finishTable = FXCollections.observableArrayList();//
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
    private  Integer timeNum = 0;
    //运行的东西
    private final Timeline timeline = new Timeline();
    public Integer auto_add_pid = 1;
    // cpu运行时间
    private Integer cpuTime = 0;
    private Integer cTime = 4;
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
    public AnchorPane memoryBox;
    //TODO:进度条
    @FXML
    public ProgressBar memoryBar;
    public ObservableList<FlowPane> memories = FXCollections.observableArrayList();
    public Text memoryText;
    @Resource
    private MemoryHandler memoryHandler;
    //初始化函数，窗口打开后自动执行
    public void initialize() {
        root = new MyTreeItem(true,"root",2);
        treeView.setRoot(root);
        MyTreeItem C = new MyTreeItem(true,"C",2);

        MyTreeItem mt1 =new MyTreeItem(false,"demo1.e",1);
        MyTreeItem mt2 =new MyTreeItem(false,"demo2.e",1);
        MyTreeItem mt3 =new MyTreeItem(false,"demo3.e",1);
        MyTreeItem mt4 =new MyTreeItem(false,"demo4.e",1);
        MyTreeItem mt5 =new MyTreeItem(false,"demo5.e",1);
        hashMap.put("\\C\\demo1.e","");
        hashMap.put("\\C\\demo2.e","");
        hashMap.put("\\C\\demo3.e","");
        hashMap.put("\\C\\demo4.e","");
        hashMap.put("\\C\\demo5.e","");
        C.getChildren().addAll(mt1,mt2,mt3,mt4,mt5);

        root.getChildren().addAll(C);
        console.setText("欢迎使用");
        memoryBar.setProgress(0.05);

        //可选设备
        cpuTimeChoiceBox.setItems(FXCollections.observableArrayList(4, 6, 8));
        //设置进程参数
        processHandler.bindProperties(readyPid, readyTotalTimeCol, readyDev, readyStatus, readyCommitTime, readyContinueTime, readyFinishedTime);
        processHandler.bindProperties(exePid,exeTotalTimeCol,exeDev,exeStatus,exeCommitTime,exeContinueTime,exeFinishedTime);
        processHandler.bindProperties(waitPid, waitTotalTimeCol, waitDev, waitStatus, waitCommitTime, waitContinueTime, waitFinishedTime);
        processHandler.bindProperties(finishPid, finishTotalTimeCol, finishDev, finishStatus, finishCommitTime, finishContinueTime, finishFinishedTime);
        readyQueue.setItems(readyTable);
        executeQueue.setItems(exeTable);
        waitQueue.setItems(waitTable);
        finishQueue.setItems(finishTable);
        //设置设备参数
        deviceHandler.bindProperties(deviceQueue,deviceOccupyPid,deviceName);
        //设置循环次数
        timeline.setCycleCount(Integer.MAX_VALUE);
        //设置CPU,内存，设备，时钟运动
        timeline.getKeyFrames().addAll(clockRun());
        ObservableList<Node> children = memoryBox.getChildren();
        for(Node node : children){
            if(node instanceof FlowPane)
                memories.add((FlowPane) node);
        }
        //内存分配test
        Memory memory = new Memory(1, 5);
        memoryHandler.findFreeMemory(memory);
        memoryHandler.apply(memory);
    }
    /** TODO 时钟帧每秒执行一次
    **/
    private KeyFrame clockRun(){
        return new KeyFrame(Duration.seconds(1),"clock",event->{
            // 设置cpu轮转时间片
            timeNum++;
            cpuTime++;
            time.setText(timeNum+"");
            if(timeNum%2==0){
                Random random = new Random();
                int i = random.nextInt(3);
                createProcess(++auto_add_pid,str.charAt(i)+"",random.nextInt(7)+2);
            }
            readyQueue.refresh();
            executeQueue.refresh();
            waitQueue.refresh();
            finishQueue.refresh();
            deviceQueue.refresh();

            cpu();
        });
    }

    /**TODO:cpu()
    * <p>难度系数:★★★★★</p>
    * <p>读取指令，运行指令，分配内存，分配设备资源</p>
     * Eg：
     * <pre>
     *      processHandler.addItemInList(队列,进程);
     *       processHandler.addItemInList(waitTable,process);
     * </pre>
     * <blockquote><pre>
     * PCB pcb = new PCBBuilder()
     *                 .pid(456)
     *                 .totalRunTime(85)
     *                 .ocupyingDeviceType("A")
     *                 .status("执行")
     *                 .committedTime(timeNum)
     *                 .continueTime(12)
     *                 .builder();//构建者模式
     * Process process = new Process(pcb);
     * //将进程放入等待队列
     * processHandler.addItemInList(waitTable,process);
    * */
    String str = "ABC";
    boolean isRun = true;
    //操作运行队列
    private void cpu() {
        if(executeQueue.getItems().size()==0){
            // 队列为空，说明cpu空闲，此时应该把就绪队列的第一个调到执行队列
            if(readyQueue.getItems().size()==0) return ;
            Process process = this.readyQueue.getItems().get(0);

            processHandler.moveProcessFromReadyToExecute(process,readyTable,exeTable);
        }
        Process process = this.executeQueue.getItems().get(0);
//        if(process!=null){
            process.setContinueTime(process.getContinueTime()+1);
            // 这里将完成的进程加入完成队列
            if(process.getContinueTime() >= process.getTotalRunTime()){
                log.info("进程完成");
                process.setFinalTime(timeNum);
                String type = process.getOcupyingDeviceType();
                log.info("type："+type);
                //将完成的进程移动到完成队列
                if(process.getDevice()==null){
                    log.error("完成进程的设备为空");
                    return;
                }
                deviceHandler.releaseDevice(process.getDevice());
                processHandler.moveProcessFromExecuteToFinish(process,exeTable,finishTable);

                //gl
                memoryHandler.free(process.getId());
                //gl

                // 需求：更新设备之后应该唤醒阻塞的进程
                Process waitProcess = processHandler.getWaitProcess(waitTable,type);

                if(waitProcess !=null){
                    Device device = deviceHandler.ocupyDevice(waitProcess.getOcupyingDeviceType(), waitProcess.getId());
                    // 判断是否有可用设备
                    if(device!=null){
                        // 有可用设备   加入就绪队列
                        waitProcess.setDevice(device);
                        processHandler.moveProcessFromWaitToReady(waitProcess,waitTable,readyTable);
                    }

                }
                cpuTime = 0;
            }
            int size = memoryHandler.memoryTable.size();
            log.info("内存{}",size);
            memoryText.setText(String.valueOf(size)+"%");
            memoryBar.setProgress(size/100.0);
            // 时间片轮转，6秒就让出cpu
            if(cpuTime >= cTime){
                processHandler.moveProcessFromExecuteToReady(process,exeTable,readyTable);
                cpuTime = 0;
            }
//        }
    }

    /**
     * 新建一个进程并添加到等待队列
     * */
    public void createProcess(int pid,String type,int totalTime){
        if(waitTable.size()>=6){

            return;
        }
        PCB pcb = new PCBBuilder()
                .pid(pid)
                .ocupyingDeviceType(type)
                .status("阻塞")
                .committedTime(timeNum)
                .continueTime(0)
                .totalRunTime(totalTime)
                .builder();

        Process process = new Process(pcb);

        //gl
        int memorySize=new Random().nextInt(9);
        Memory memory=new Memory(pid,memorySize+1);
        boolean isSuccess = memoryHandler.findFreeMemory(memory);
        if(!isSuccess){
            log.info("内存不足");
            return;
        }
        memoryHandler.apply(memory);
        //gl

        Device device = deviceHandler.ocupyDevice(type, pid);
        // 判断是否有可用设备
        if(device!=null){
            // 有可用设备   加入就绪队列
            process.setStatus("就绪态");
            processHandler.addItemInList(readyTable,process);
            process.setDevice(device);
        }else{
            // 没有可用设备 加入阻塞队列
            processHandler.addItemInList(waitTable,process);
        }
    }

    /**
     * TODO
     * 难度系数:？
     * 没看懂文档
     * */
    private void memory(){

    }

    /**
     * TODO:跳转帮助
     * <p>难度系数:★☆☆☆☆</p>
     * <p>跳转+画画</p>
     * */
    public void toHelp(MouseEvent mouseEvent) {

    }

    /**
     * TODO:用户接口(★★☆☆☆)
     * 需要实现的命令包括：
     * <p>创建文件： create 例 如 $ create \aa\bb.e</p>
     * <p>删除文件： delete 例 如 $ delete \aa\yy</p>
     * <p>显示文件： type 例 如 $ type \zz</p>
     * <p>拷贝文件： copy 例 如 $ copy \xx \aa\yy</p>
     * <p>建立目录： mkdir 例 如 $ mkdir \dd</p>
     * <p>删除空目录： rmdir 目录非空时，要报错。</p>
     * <u>可选实现的命令包括：</u>
     * <p>改变目录路径： chdir</p>
     * <p>删除目录： deldir（既可删除空目录又可删除非空目录）</p>
     * <p>移动文件： move</p>
     * <p>改变文件属性： change</p>
     * <p>磁盘格式化： format</p>
     * <p>磁盘分区命令： fdisk</p>
     */
    String[] order={"create","delete","rmdir","copy","mkdir","deldir","read","write"};
    Map<String, String> hashMap = new HashMap<>();
    public void cmd(KeyEvent keyEvent) {
        //判断是否按下Enter
        if(keyEvent.getCode().getName().equalsIgnoreCase("enter")){
            //将输入内容以符号“\”分割为string数组
            String input=command.getText();
            input=input.replaceAll("\\n","");
            long spaceCount = input.chars().filter(Character::isWhitespace).count();
            String[] str=input.split("\\\\");
            command.clear();
            int index=-1;
            int count=0;
            int newIndex=0;
            for(int i=0;i<order.length;i++){
                //将数组第一个元素和命令数组比较,记下是那条命令
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
            int strIndex=0;
            //根据命令输出相应功能
            switch (index){
                case 0:
                    //创建文件"create"
                    String[] str13=input.split(" ");
                    String str14=str13[str13.length-1];
                    hashMap.put(str14,"");
                    cmdCreate(str,1,false);
                    break;
                case 1:
                    //删除文件"delete"
                    cmdDelete(str,1);
                    String[] str7=input.split(" ");
                    String str8=str7[str7.length-1];
                    hashMap.remove(str8);
                    break;
                case 2:
                    //删除空目录"rmdir"
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
                    //拷贝文件“copy”
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
                    //建立目录"mkdir"
                    cmdCreate(str,2,true);
                    break;
                case 5:
                    //删除目录"deldir"
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
                case 6:
                    //"reader"
                    String[] str4=input.split(" ");
                    String str5=str4[str4.length-1];
                    if(hashMap.containsKey(str5)){
                        String readstr=hashMap.get(str5);
                        Stage primaryStage=new Stage();
                        primaryStage.setWidth(400);
                        primaryStage.setHeight(335);
                        primaryStage.setTitle("写字板");

                        // Create a TextArea
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

                case 7:
                    //"write"
                    String[] str3=input.split(" ");
                    String str2=str3[str3.length-1];
                    if(hashMap.containsKey(str2)){
                        Stage primaryStage1=new Stage();
                        primaryStage1.setTitle("写字板");
                        primaryStage1.setWidth(400);
                        primaryStage1.setHeight(335);

                        // Create a TextArea
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
    //创建方法
    public void cmdCreate(String[] str,int type,boolean isDir){
        int strLen= str.length;
        MyTreeItem mt =new MyTreeItem(isDir,str[strLen-1],type);
        MyTreeItem parentmt =fileSysHandler.checkFile(root,str,1);
        if(parentmt ==null){
            console.setText(console.getText()+"\n请输入正确的路径2");
        }
        else fileSysHandler.creatFileOrDir(parentmt, mt);
    }
    //删除方法
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

    //FINISHED:跳转到内存
    public void toMemory(MouseEvent mouseEvent) {
        Stage stage = new Stage();
        stage.setScene(memoryStage.getScene());
        stage.show();
    }

    @Resource
    private CreateProcessController createProcessController;
    /**
     * TODO:
     * <p>难度系数:★★☆☆☆</p>
     * <p>新页面+新建进程+</p>
     * */
    public void onCreateProcess(MouseEvent mouseEvent) {
        Stage stage = new Stage();
        stage.setScene(createProcessStage.getScene());
        createProcessController.setStage(stage);
        stage.show();
    }
    //TODO:重置
    public void onReset(MouseEvent mouseEvent) {
    }
    //FINISHED
    public void onStart(MouseEvent mouseEvent) {
        if(cpuTimeChoiceBox.getValue()!=null){
            cTime = cpuTimeChoiceBox.getValue();
            log.info("当前时间片为：{}",cTime);
        }
        timeline.play();//开始运行
    }
    //FINISHED
    public void onStop(MouseEvent mouseEvent) {
        timeline.pause();
    }
}
