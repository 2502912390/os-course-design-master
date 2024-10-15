# 操作系统模拟器

## 项目简介

这是一个基于JavaFX和Spring Boot的操作系统模拟器，旨在展示操作系统的核心概念和功能。该项目模拟了进程管理、内存管理、设备管理、文件系统等操作系统的基本组件，并提供了直观的图形用户界面。

## 主要功能

1. 进程管理
   - 进程创建、调度和状态管理
   - 进程队列管理（就绪、执行、等待、完成队列）

2. 内存管理
   - 内存分配和释放
   - 内存使用可视化

3. 设备管理
   - 设备分配和释放
   - 支持多种设备类型（A、B、C）

4. 文件系统
   - 文件和目录的创建、删除、查找
   - 树形结构展示文件系统

5. 磁盘管理
   - 磁盘空间分配和释放
   - 磁盘使用可视化

6. 用户界面
   - 主界面、进程创建界面、内存查看界面
   - 进程和设备信息表格展示
   - 文件系统树形视图

7. 系统控制
   - 开始、暂停、重置功能
   - 命令行操作支持

## 技术栈

- Java
- JavaFX (图形用户界面)
- Spring Boot (依赖注入和管理)
- FXML (界面布局)

## 项目结构

- `src/main/java/top/R3/`: 主要源代码目录
  - `Application.java`: 应用程序入口
  - `controller/`: 控制器类
  - `device/`: 设备管理相关类
  - `filesys/`: 文件系统相关类
  - `memory/`: 内存管理相关类
  - `process/`: 进程管理相关类
  - `view/`: 视图相关类
- `src/main/resources/`: 资源文件目录
  - `view/`: FXML布局文件
  - `static/`: 静态资源文件
  - `image/`: 图片资源

## 如何运行

1. 确保您的系统已安装Java开发环境和Maven。
2. 克隆此仓库到本地。
3. 在项目根目录执行 `mvn spring-boot:run`。
4. 应用程序将启动，显示主界面。

## 主要类说明

- `Application.java`: 应用程序的主入口，继承自`AbstractJavaFxApplicationSupport`。
- `IndexView.java`: 主界面视图类。
- `CreateProcessView.java`: 创建进程的视图类。
- `MemoryView.java`: 内存视图类，用于展示内存使用情况。
- `MainSplash.java`: 应用程序启动时的闪屏类。
- `DeviceHandler.java`: 设备管理处理类。
- `ProcessHandler.java`: 进程管理处理类。
- `FileSysHandler.java`: 文件系统处理类。
- `MemoryHandler.java`: 内存管理处理类。

## 贡献

欢迎提交问题和改进建议。如果您想为这个项目做出贡献，请遵循以下步骤：

1. Fork 这个仓库
2. 创建您的特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交您的更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 开启一个 Pull Request

## 许可证

[MIT License](LICENSE)

## 联系方式

项目维护者：[您的姓名] - [您的邮箱]

项目链接：[https://github.com/yourusername/your-repo-name](https://github.com/yourusername/your-repo-name)
