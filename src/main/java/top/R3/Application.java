package top.R3;

import de.felixroske.jfxsupport.AbstractJavaFxApplicationSupport;
import javafx.stage.Stage;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import top.R3.splash.MainSplash;
import top.R3.view.IndexView;

/**
 * *使用Spring Boot框架的JavaFX应用程序
 * 这个是主函数，更换第二个参数可以改变主界面
 *
 * */
@SpringBootApplication(scanBasePackages = "top.R3")
public class Application extends AbstractJavaFxApplicationSupport {
    public static void main(String[] args) {
        // 使用Spring Boot支持启动JavaFX应用程序
        /**
         * R3Application.class: 表示要启动的 JavaFX 应用程序的主类，即当前的 R3Application 类。
         * IndexView.class: 表示应用程序的主界面类。在这里，IndexView 类被指定为主界面。
         * new MainSplash(): 创建了一个新的 MainSplash 对象，这可能是应用程序启动时显示的启动画面或者欢迎界面。
         * args: 是命令行参数，这是 main 方法接受的参数，可以通过命令行传递给应用程序。**/
        Application.launch(Application.class, IndexView.class,new MainSplash(),args);
    }
    @Override
    public void beforeInitialView(Stage stage, ConfigurableApplicationContext ctx) {
        // 在显示初始视图之前配置主舞台
        stage.setTitle("菜单"); // 设置舞台标题为"菜单"
        stage.setWidth(1500); // 将舞台宽度设置为1300像素
        stage.setHeight(900); // 将舞台高度设置为900像素
    }
}
