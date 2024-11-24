package top.ComFive.controller;

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
import top.ComFive.device.Device;
import top.ComFive.device.DeviceHandler;
import top.ComFive.domain.MyTreeItem;
import top.ComFive.filesys.FileSysHandler;
import top.ComFive.memory.Memory;
import top.ComFive.memory.MemoryHandler;
import top.ComFive.process.PCB;
import top.ComFive.process.PCBBuilder;
import top.ComFive.process.ProcessHandler;
import top.ComFive.view.CreateProcessView;
import top.ComFive.view.MemoryView;
import top.ComFive.process.Process;
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
    private Integer cTime = 5;//时间片

    private boolean isRun = false;

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
        hashMap.put("\\C\\demo5.e","");
        C.getChildren().addAll(mt1,mt2,mt3,mt4,mt5);//作为其子节点

        MyTreeItem D = new MyTreeItem(true,"D",2);
        //至少要包含 5 个目录和 15 个文件。
        MyTreeItem dmt1 =new MyTreeItem(false,"demo1.e",1);
        MyTreeItem dmt2 =new MyTreeItem(false,"demo2.e",1);
        MyTreeItem dmt3 =new MyTreeItem(false,"demo3.e",1);
        MyTreeItem dmt4 =new MyTreeItem(false,"demo4.e",1);
        MyTreeItem dmt5 =new MyTreeItem(false,"demo5.e",1);
        hashMap.put("\\D\\demo1.e","");
        hashMap.put("\\D\\demo2.e","");
        hashMap.put("\\D\\demo3.e","");
        hashMap.put("\\D\\demo4.e","");
        hashMap.put("\\D\\demo5.e","");

        MyTreeItem E = new MyTreeItem(true,"E",2);
        //至少要包含 5 个目录和 15 个文件。
        MyTreeItem emt1 =new MyTreeItem(false,"demo1.e",1);
        MyTreeItem emt2 =new MyTreeItem(false,"demo2.e",1);
        MyTreeItem emt3 =new MyTreeItem(false,"demo3.e",1);
        MyTreeItem emt4 =new MyTreeItem(false,"demo4.e",1);
        MyTreeItem emt5 =new MyTreeItem(false,"demo5.e",1);
        hashMap.put("\\E\\demo1.e","");
        hashMap.put("\\E\\demo2.e","");
        hashMap.put("\\E\\demo3.e","");
        hashMap.put("\\E\\demo4.e","");
        hashMap.put("\\E\\demo5.e","");

        MyTreeItem F = new MyTreeItem(true,"F",2);
        //至少要包含 5 个目录和 15 个文件。
        MyTreeItem fmt1 =new MyTreeItem(false,"demo1.e",1);
        MyTreeItem fmt2 =new MyTreeItem(false,"demo2.e",1);
        MyTreeItem fmt3 =new MyTreeItem(false,"demo3.e",1);
        MyTreeItem fmt4 =new MyTreeItem(false,"demo4.e",1);
        MyTreeItem fmt5 =new MyTreeItem(false,"demo5.e",1);
        hashMap.put("\\F\\demo1.e","");
        hashMap.put("\\F\\demo2.e","");
        hashMap.put("\\F\\demo3.e","");
        hashMap.put("\\F\\demo4.e","");
        hashMap.put("\\F\\demo5.e","");

        MyTreeItem G = new MyTreeItem(true,"G",2);
        //至少要包含 5 个目录和 15 个文件。
        MyTreeItem gmt1 =new MyTreeItem(false,"demo1.e",1);
        MyTreeItem gmt2 =new MyTreeItem(false,"demo2.e",1);
        MyTreeItem gmt3 =new MyTreeItem(false,"demo3.e",1);
        MyTreeItem gmt4 =new MyTreeItem(false,"demo4.e",1);
        MyTreeItem gmt5 =new MyTreeItem(false,"demo5.e",1);
        hashMap.put("\\G\\demo1.e","");
        hashMap.put("\\G\\demo2.e","");
        hashMap.put("\\G\\demo3.e","");
        hashMap.put("\\G\\demo4.e","");
        hashMap.put("\\G\\demo5.e","");

        D.getChildren().addAll(dmt1,dmt2,dmt3,dmt4,dmt5);
        E.getChildren().addAll(emt1,emt2,emt3,emt4,emt5);
        F.getChildren().addAll(fmt1,fmt2,fmt3,fmt4,fmt5);
        G.getChildren().addAll(gmt1,gmt2,gmt3,gmt4,gmt5);

        root.getChildren().addAll(C);
        root.getChildren().addAll(D);
        root.getChildren().addAll(E);
        root.getChildren().addAll(F);
        root.getChildren().addAll(G);

        //TODO
        //要添加磁盘占用

        console.setText("欢迎使用");
        memoryBar.setProgress(0.05);// 设置内存进度条为 5%

        // 设置CPU时间片选项
        cpuTimeChoiceBox.setItems(FXCollections.observableArrayList(4, 5, 6, 7));//添加几种时间片选择
        
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
//
        
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

    private KeyFrame clockRun() {
        // 调用有参数的方法，传入默认值
        return clockRun("default");
    }

    /** 
     * 时钟帧每秒执行一次
     * 返回一个KeyFrame对象，定义了每秒执行的操作
     */
    private KeyFrame clockRun(String parts){
        return new KeyFrame(Duration.seconds(1),"clock",event->{
            // 更新时间和CPU时间
            timeNum++;
            cpuTime++;
            time.setText(timeNum+"");

            if(parts.equals("default")){
                System.out.println("default");
                // 每两秒创建一个新进程 TODO 在运行文件的时候调用/就绪队列为空的时候调用
                if(timeNum%2==0){
                    Random random = new Random();
                    int i = random.nextInt(3);
                    createProcess( ++auto_add_pid,str.charAt(i)+"",random.nextInt(7)+2 );
                }
            }else{//运行脚本
                System.out.println("NONONE....");
                if(!isRun){
                    createProcess(++auto_add_pid,parts.charAt(1)+"",parts.charAt(2)-'0');
                    isRun=true;
                }
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
    //别的地方是否还有用到？
    String[] command_set={"create","delete","rmdir","copy","mkdir","deldir","type","write","run"};
    //将磁盘占用操作与hashMap绑定？
    Map<String, String> hashMap = new HashMap<>();//文件名 文件内容
    public void cmd(KeyEvent keyEvent) {
        // 检查是否按下Enter键
        if(keyEvent.getCode().getName().equalsIgnoreCase("enter")){
            String original_input=command.getText();
            original_input = original_input.replaceAll("\\n","");//删除换行符

            String[] instruction_patch=original_input.split("\\\\");//分割各个部分
            command.clear();

            int command_idx=-1;//对应命令的下标
            // 识别命令类型
            for(int i=0;i<command_set.length;i++){
                if(instruction_patch[0].trim().equals(command_set[i])){
                    command_idx=i;
                    break;
                }
            }

            int instruction_patch_num= instruction_patch.length;
            if(instruction_patch_num<=1){
                console.setText(console.getText()+"\n请输入正确的路径1");
                return;
            }

            String[] instructionAndpath=original_input.split(" ");
            String file_path=instructionAndpath[instructionAndpath.length-1];
            String file_name=instruction_patch[instruction_patch_num-1];

//            System.out.println(java.util.Arrays.toString(instructionAndpath));//[create, \C\test2.e]
//            System.out.println(java.util.Arrays.toString(instruction_patch));//[create , C, test2.e]

            // 根据命令类型执行相应操作
            switch (command_idx){
                case 0:{
                    // 创建文件
                    hashMap.put(file_path,"");
                    cmdCreate(instruction_patch,1,false);
                    break;
                }
                case 1:{
                    // 删除文件
                    cmdDelete(instruction_patch,1);
                    hashMap.remove(file_path);
                    break;
                }
                case 2:{
                    // 删除空目录
                    MyTreeItem parentmt=fileSysHandler.checkFile(root,instruction_patch,1);
                    int fileSize=parentmt.getChildren().size();
                    for(int i=0;i<fileSize;i++){
                        MyTreeItem mt = (MyTreeItem) parentmt.getChildren().get(i);
                        if(mt.getFileName().equals(file_name)){
                            if(mt.getChildren().size()==0){
                                cmdDelete(instruction_patch,2);
                            }
                            else console.setText(console.getText()+"\n目录不为空！");
                        }
                    }
                    break;   
                }
                case 3:{
                    int num=instructionAndpath.length;
                    // 复制文件
                    if(num==2) {
                        String newstr = new String();
                        String dia_filename ="\\" +file_name;// \filename

                        newstr += instruction_patch[0]; //copy
                        newstr += dia_filename;// copy \filename

                        instruction_patch = newstr.split("\\\\");
                        hashMap.put(dia_filename,hashMap.get(file_path));// 默认为root路径
                        cmdCreate(instruction_patch, 1, false);
                    }else if (num==3) {
                        String newstr = new String();

                        String target_file_path=instructionAndpath[instructionAndpath.length-2];//目标路径
                        newstr += instruction_patch[0];//copy
                        newstr += target_file_path+"\\";// copy \target\
                        newstr += file_name;// copy \target\filename

                        instruction_patch = newstr.split("\\\\");

                        hashMap.put(target_file_path,hashMap.get(file_path));
                        cmdCreate(instruction_patch, 1, false);
                    }
                    break;
                }
                case 4:{
                    // 创建目录
                    cmdCreate(instruction_patch,2,true);
                    break;
                }
                case 5:{
                    // 删除目录（包括非空目录）
                    cmdDelete(instruction_patch,2);
                    Set<String> set=hashMap.keySet();
                    for(String s:set){
                        if(s.contains(file_path)){
                            hashMap.remove(s);
                        }
                    }
                    break;
                }
                case 6:{
                    // 读取文件内容
                    if(hashMap.containsKey(file_path)){//map中读取并且展示
                        String content=hashMap.get(file_path);

                        Stage primaryStage=new Stage();
                        primaryStage.setWidth(400);
                        primaryStage.setHeight(335);
                        primaryStage.setTitle(file_path);

                        TextArea textarea = new TextArea();
                        textarea.setPrefWidth(400);
                        textarea.setPrefHeight(330);
                        textarea.setWrapText(true);
                        textarea.setEditable(false);
                        textarea.setText(content);//显示文字

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
                }
                case 7:{
                    // 写入文件内容
                    if(hashMap.containsKey(file_path)){
                        Stage primaryStage=new Stage();
                        primaryStage.setTitle("请输入...");
                        primaryStage.setWidth(400);
                        primaryStage.setHeight(335);

                        TextArea textArea = new TextArea();
                        textArea.setPrefWidth(400);
                        textArea.setPrefHeight(300);
                        textArea.setWrapText(true);

                        Button saveButton = new Button("保存");
                        saveButton.setOnAction(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent event) {
                                String u = textArea.getText();
                                hashMap.put(file_path,u);
                                primaryStage.close();
                            }
                        });
                        Button cancelButton = new Button("取消");
                        cancelButton.setOnAction(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent event) {
                                String u="";
                                hashMap.put(file_path,u);
                                primaryStage.close();
                            }
                        });

                        VBox vbox1=new VBox(5);

                        VBox layout5 = new VBox();
                        layout5.setPrefWidth(400);
                        layout5.setPrefHeight(250);
                        layout5.getChildren().add(textArea);

                        HBox layout6 = new HBox(25);
                        layout6.setPrefWidth(400);
                        layout6.setPrefHeight(30);
                        layout6.getChildren().addAll(saveButton,cancelButton);

                        vbox1.getChildren().addAll(layout5,layout6);

                        Scene scene1 = new Scene(vbox1);
                        primaryStage.setScene(scene1);
                        primaryStage.show();
                    }else{
                        console.setText(console.getText()+"\n该文件不存在");
                    }
                    break;
                }
                case 8:{
                    //运行文件
                    String content=hashMap.get(file_path);

                    // 去除换行符
                    String processed = content.replace("\n", "").replace("\r", "");
                    // 按分号分隔字符串
                    String[] parts = processed.split(";");

                    Map<String, Integer> variables = new HashMap<>(); // 存储变量及其值
                    for (String part : parts) {
                        part = part.trim(); // 去除多余空格
                        if (part.equals("end")) {
                            break; // 遇到end停止处理
                        }

                        if(part.startsWith("!")){//调用对应的设备
                            timeline.getKeyFrames().addAll(clockRun(part));
                            timeline.play();
                        }

                        if (part.contains("=")) {// 赋值操作
                            String[] assignment = part.split("=");
                            String varName = assignment[0].trim();
                            int value = Integer.parseInt(assignment[1].trim());
                            variables.put(varName, value);
                        } else if (part.endsWith("++")) {// 自增操作
                            String varName = part.substring(0, part.length() - 2).trim();
                            variables.put(varName, variables.getOrDefault(varName, 0) + 1);//读取上一次的值 并加1
                        } else if (part.endsWith("--")) {// 自减操作
                            String varName = part.substring(0, part.length() - 2).trim();
                            variables.put(varName, variables.getOrDefault(varName, 0) - 1);
                        }
                    }

                    String result="result: \n";
                    for (Map.Entry<String, Integer> entry : variables.entrySet()) {
                        result += entry.getKey() + " = " + entry.getValue();
                        result += "\n";
                    }

                    String outFile = file_path.replaceAll("\\.e", "")+".out";//改文件后缀
                    hashMap.put(outFile,result);
                    instruction_patch = outFile.split("\\\\");
                    cmdCreate(instruction_patch,1,false);
                    break;
                }
                default:
                    console.setText(console.getText()+"\n输入错误");
            }
        }
    }

    /**
     * 创建文件或目录
     */
    public void cmdCreate(String[] str,int type,boolean isDir){//type是指 文件和文件夹占用的大小不同 文件为1 文件夹为2
        int instruction_patch_num= str.length;
        MyTreeItem mt =new MyTreeItem(isDir,str[instruction_patch_num-1],type);//创建对应的实例
        MyTreeItem parentmt =fileSysHandler.checkFile(root,str,1);//查找其父实例

        if(parentmt == null){
            console.setText(console.getText()+"\n父路径不正确，请重新输入");
        }
        else fileSysHandler.creatFileOrDir(parentmt, mt);
    }

    /**
     * 删除文件或目录
     */
    public void cmdDelete(String[] str,int type){
        int instruction_patch_num= str.length;
        MyTreeItem parentmt =fileSysHandler.checkFile(root,str,1);
        if(parentmt ==null){
            console.setText(console.getText()+"\n父路径不正确，请重新输入");
        }
        else{
            fileSysHandler.checkDeleteFile(parentmt,str[instruction_patch_num-1],type);
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


    /**
     * 打开创建进程的窗口
     */
    @Resource
    private CreateProcessController createProcessController;
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
        timeline.getKeyFrames().addAll(clockRun());
        timeline.play();//开始运行
    }

    /**
     * 暂停系统运行
     */
    public void onStop(MouseEvent mouseEvent) {
        timeline.pause();
    }
}