# 操作系统模拟器

## 项目简介

这是一个基于JavaFX和Spring Boot的操作系统模拟器，旨在展示操作系统的核心概念和功能。该项目模拟了进程管理、内存管理、设备管理、文件系统等操作系统的基本组件，并提供了直观的图形用户界面。

## 项目结构

项目主要分为以下几个模块：

1. 核心应用模块 (`Application.java`)
2. 视图模块 (`view` 包)
3. 进程管理模块 (`process` 包)
4. 内存管理模块 (`memory` 包)
5. 设备管理模块 (`device` 包)
6. 文件系统模块 (`filesys` 包)
7. 磁盘管理模块 (`disk` 包)

## 模块详解

### 1. 核心应用模块

- `Application.java`: 应用程序的入口点，负责启动JavaFX应用并设置主窗口。

### 2. 视图模块

- `IndexView.java`: 主视图类，关联主界面的FXML文件。
- `CreateProcessView.java`: 创建进程的视图类。
- `MemoryView.java`: 内存视图类，用于展示内存使用情况。

### 3. 进程管理模块

- `Process.java`: 表示一个进程的类，包含进程的各种属性。
- `PCB.java`: 进程控制块类，存储进程的核心信息。
- `PCBBuilder.java`: 用于构建PCB对象的建造者类。
- `ProcessHandler.java`: 处理进程相关操作的类，如进程状态转换、进程调度等。

### 4. 内存管理模块

- `Memory.java`: 表示内存块的类。
- `MemoryHandler.java`: 处理内存分配和释放的类。
- `MemoryContainer.java`: 存储和管理所有内存对象的容器类。

### 5. 设备管理模块

- `DeviceHandler.java`: 管理设备分配和释放的类。

### 6. 文件系统模块

- `File.java`: 表示文件的类。
- `FCB.java`: 文件控制块类，存储文件的元数据。
- `FileSysHandler.java`: 处理文件系统操作的类，如创建、删除文件等。

### 7. 磁盘管理模块

- `Disk.java`: 表示磁盘块的类。

## 主要功能

1. 进程管理：创建、调度和终止进程。
2. 内存管理：分配和释放内存，可视化内存使用情况。
3. 设备管理：分配和释放设备资源。
4. 文件系统：创建、删除、查找文件和目录。
5. 磁盘管理：模拟磁盘空间的分配和释放。

## 技术栈

- Java
- JavaFX (图形用户界面)
- Spring Boot (依赖注入和管理)
- FXML (界面布局)

## 如何运行

1. 确保您的系统已安装Java开发环境和Maven。
2. 克隆此仓库到本地。
3. 在项目根目录执行 `mvn spring-boot:run`。
4. 应用程序将启动，显示主界面。

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
